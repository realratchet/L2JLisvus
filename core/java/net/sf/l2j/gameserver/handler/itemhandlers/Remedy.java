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

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.1.2.4 $ $Date: 2005/04/06 16:13:51 $
 */
public class Remedy implements IItemHandler
{
	private static int[] _itemIds =
	{
		1831,
		1832,
		1833,
		1834,
		3889,
		6654
	};

	@Override
	public void useItem(L2PlayableInstance playable, L2ItemInstance item)
	{
		L2PcInstance activeChar;
		if (playable instanceof L2PcInstance)
		{
			activeChar = (L2PcInstance) playable;
		}
		else if (playable instanceof L2PetInstance)
		{
			activeChar = ((L2PetInstance) playable).getOwner();
		}
		else
		{
			return;
		}

		if (activeChar.isInOlympiadMode())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT));
			return;
		}

		L2Event event = activeChar.getEvent();
		if (event != null && event.isStarted())
		{
			return;
		}

		int itemId = item.getItemId();
		if (itemId == 1831) // antidote
		{
			activeChar.negateEffects(L2Skill.SkillType.POISON, 3, 0);
			MagicSkillUse MSU = new MagicSkillUse(playable, playable, 2042, 1, 0, 0);
			activeChar.sendPacket(MSU);
			activeChar.broadcastPacket(MSU);
			playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
		}
		else if (itemId == 1832) // advanced antidote
		{
			activeChar.negateEffects(L2Skill.SkillType.POISON, 7, 0);
			MagicSkillUse MSU = new MagicSkillUse(playable, playable, 2043, 1, 0, 0);
			activeChar.sendPacket(MSU);
			activeChar.broadcastPacket(MSU);
			playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
		}
		else if (itemId == 1833) // bandage
		{
			activeChar.negateEffects(L2Skill.SkillType.BLEED, 3, 0);
			MagicSkillUse MSU = new MagicSkillUse(playable, playable, 34, 1, 0, 0);
			activeChar.sendPacket(MSU);
			activeChar.broadcastPacket(MSU);
			playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
		}
		else if (itemId == 1834) // emergency dressing
		{
			activeChar.negateEffects(L2Skill.SkillType.BLEED, 7, 0);
			MagicSkillUse MSU = new MagicSkillUse(playable, playable, 2045, 1, 0, 0);
			activeChar.sendPacket(MSU);
			activeChar.broadcastPacket(MSU);
			playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
		}
		else if (itemId == 3889) // potion of recovery
		{
			activeChar.stopSkillEffects(4082);
			MagicSkillUse MSU = new MagicSkillUse(playable, playable, 2042, 1, 0, 0);
			activeChar.sendPacket(MSU);
			activeChar.broadcastPacket(MSU);
			playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
		}
		else if (itemId == 6654) // Amulet: Flames of Valakas
		{
			activeChar.stopSkillEffects(4683);
			activeChar.stopSkillEffects(4684);
			MagicSkillUse MSU = new MagicSkillUse(playable, playable, 2233, 1, 0, 0);
			activeChar.sendPacket(MSU);
			activeChar.broadcastPacket(MSU);
			playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
		}
	}

	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
}