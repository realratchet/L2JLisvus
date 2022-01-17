/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.datatables.SummonItemsData;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2SummonItem;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillLaunched;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.PetItemList;
import net.sf.l2j.gameserver.network.serverpackets.SetupGauge;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * 
 * @author FBIagent
 * 
 */
public class SummonItems implements IItemHandler
{
    @Override
	public void useItem(L2PlayableInstance playable, L2ItemInstance item)
    {
        if (!(playable instanceof L2PcInstance))
            return;

        L2PcInstance activeChar = (L2PcInstance)playable;

        if (!activeChar.getFloodProtectors().getItemPetSummon().tryPerformAction("summon items"))
            return;

        if (activeChar.isSitting())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.CANT_MOVE_SITTING));
            return;
        }

        if (activeChar.inObserverMode())
            return;

        if (activeChar.isInOlympiadMode())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT));
            return;
        }

        L2Event event = activeChar.getEvent();
		if (event != null && event.isStarted())
        {
            activeChar.sendMessage("You may not summon a pet in events.");
            return;
        }

        if (activeChar.isAllSkillsDisabled())
            return;

        L2SummonItem summonItem = SummonItemsData.getInstance().getSummonItem(item.getItemId());
        if ((activeChar.getPet() != null || activeChar.isMounted()) && summonItem.isPetSummon())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_ALREADY_HAVE_A_PET));
            return;
        }

        if (activeChar.isAttackingNow() || activeChar.isRooted())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_SUMMON_IN_COMBAT));
            return;
        }

        int npcID = summonItem.getNpcId();
        if (npcID == 0)
            return;

        L2NpcTemplate npcTemplate = NpcTable.getInstance().getTemplate(npcID);
        if (npcTemplate == null)
            return;
        activeChar.stopMove(null);

        switch (summonItem.getType())
        {
            case 0: // Static summons (like Christmas tree)
                try
                {
                    L2Spawn spawn = new L2Spawn(npcTemplate);
                    spawn.setId(IdFactory.getInstance().getNextId());
                    spawn.setLocX(activeChar.getX());
                    spawn.setLocY(activeChar.getY());
                    spawn.setLocZ(activeChar.getZ());
                    L2World.getInstance().storeObject(spawn.spawnOne());
                    activeChar.destroyItem("Summon", item.getObjectId(), 1, null, false);
                    activeChar.sendMessage("Created " + npcTemplate.name + " at x: " + spawn.getLocX() + " y: " + spawn.getLocY() + " z: " + spawn.getLocZ());
                }
                catch (Exception e)
                {
                    activeChar.sendMessage("Target is not in game.");
                }
                break;
            case 1: // Pet summons
                L2Skill skill = SkillTable.getInstance().getInfo(2046, 1);
                if (skill == null)
                    return;

                activeChar.broadcastPacket(new MagicSkillUse(activeChar, skill.getId(), 1, skill.getHitTime(), 0));
                activeChar.sendPacket(new SetupGauge(SetupGauge.BLUE, 5000));
                activeChar.sendPacket(new SystemMessage(SystemMessage.SUMMON_A_PET));

                activeChar.disableAllSkills();

                PetSummonFinalizer psf = new PetSummonFinalizer(activeChar, npcTemplate, item);
                activeChar.setSkillCast(ThreadPoolManager.getInstance().scheduleEffect(psf, skill.getHitTime()));
                activeChar.setSkillCastEndTime(10+GameTimeController.getInstance().getGameTicks()+skill.getHitTime()/GameTimeController.MILLIS_IN_TICK);
                break;
            case 2: // Wyvern
                activeChar.mount(summonItem.getNpcId(), item.getObjectId(), true);
                break;
        }
    }

    static class PetSummonFeedWait implements Runnable
    {
        private L2PcInstance _activeChar;
        private L2PetInstance _petSummon;

        PetSummonFeedWait(L2PcInstance activeChar, L2PetInstance petSummon)
        {
            _activeChar = activeChar;
            _petSummon = petSummon;
        }

        @Override
		public void run()
        {
            try
            {
                if (_petSummon.getCurrentFed() <= 0)
                    _petSummon.unSummon(_activeChar);
                else
                    _petSummon.startFeed();
            }
            catch (Throwable e) {}
        }
    }

    static class PetSummonFinalizer implements Runnable
    {
        private L2PcInstance _activeChar;
        private L2ItemInstance _item;
        private L2NpcTemplate _npcTemplate;

        PetSummonFinalizer(L2PcInstance activeChar, L2NpcTemplate npcTemplate, L2ItemInstance item)
        {
            _activeChar = activeChar;
            _npcTemplate = npcTemplate;
            _item = item;
        }

        @Override
		public void run()
        {
            try
            {
                _activeChar.sendPacket(new MagicSkillLaunched(_activeChar, 2046, 1));

                _activeChar.enableAllSkills();

                L2PetInstance petSummon = L2PetInstance.spawnPet(_npcTemplate, _activeChar, _item);
                if (petSummon == null)
                    return;

                petSummon.setTitle(_activeChar.getName());

                if (!petSummon.isRespawned())
                {
                    petSummon.setCurrentHp(petSummon.getMaxHp());
                    petSummon.setCurrentMp(petSummon.getMaxMp());
                    petSummon.getStat().setExp(petSummon.getExpForThisLevel());
                    petSummon.setCurrentFed(petSummon.getMaxFed());
                }

                petSummon.setRunning();

                if (!petSummon.isRespawned())
                    petSummon.store();

                _activeChar.setPet(petSummon);

                L2World.getInstance().storeObject(petSummon);
                // Use the position that was already set in L2Summon constructor
                petSummon.spawnMe(petSummon.getX(), petSummon.getY(), petSummon.getZ());
                petSummon.startFeed();
                _item.setEnchantLevel(petSummon.getLevel());

                if (petSummon.getCurrentFed() <= 0)
                    ThreadPoolManager.getInstance().scheduleGeneral(new PetSummonFeedWait(_activeChar, petSummon), 60000);
                else
                    petSummon.startFeed();

                petSummon.setFollowStatus(true);
                petSummon.setShowSummonAnimation(false);
                int weaponId = petSummon.getWeapon();
                int armorId = petSummon.getArmor();
                int jewelId = petSummon.getJewel();
                if (weaponId > 0 && petSummon.getOwner().getInventory().getItemByItemId(weaponId) != null)
                {
                    L2ItemInstance item = petSummon.getOwner().getInventory().getItemByItemId(weaponId);
                    L2ItemInstance newItem = petSummon.getOwner().transferItem("Transfer", item.getObjectId(), 1, petSummon.getInventory(), petSummon);

                    if (newItem == null)
                        petSummon.setWeapon(0);
                    else
                        petSummon.getInventory().equipItem(newItem);
                }
                else
                    petSummon.setWeapon(0);

                if (armorId > 0 && petSummon.getOwner().getInventory().getItemByItemId(armorId) != null)
                {
                    L2ItemInstance item = petSummon.getOwner().getInventory().getItemByItemId(armorId);
                    L2ItemInstance newItem = petSummon.getOwner().transferItem("Transfer", item.getObjectId(), 1, petSummon.getInventory(), petSummon);

                    if (newItem == null)
                        petSummon.setArmor(0);
                    else
                        petSummon.getInventory().equipItem(newItem);
                }
                else
                    petSummon.setArmor(0);

                if (jewelId > 0 && petSummon.getOwner().getInventory().getItemByItemId(jewelId) != null)
                {
                    L2ItemInstance item = petSummon.getOwner().getInventory().getItemByItemId(jewelId);
                    L2ItemInstance newItem = petSummon.getOwner().transferItem("Transfer", item.getObjectId(), 1, petSummon.getInventory(), petSummon);

                    if (newItem == null)
                        petSummon.setJewel(0);
                    else
                        petSummon.getInventory().equipItem(newItem);
                }
                else
                    petSummon.setJewel(0);

                petSummon.getOwner().sendPacket(new PetItemList(petSummon));
                petSummon.broadcastStatusUpdate();
            }
            catch (Throwable e) {}
        }
    }

    @Override
	public int[] getItemIds()
    {
    	return SummonItemsData.getInstance().itemIDs();
    }
}