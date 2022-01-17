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
package net.sf.l2j.gameserver.model.actor.instance;

import java.util.Collection;
import java.util.concurrent.Future;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.instancemanager.FourSepulchersManager;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MoveToPawn;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * 
 * @author sandman
 */
public class L2SepulcherNpcInstance extends L2NpcInstance
{
    protected Future<?> _closeTask = null;
    protected Future<?> _spawnNextMysteriousBoxTask = null;
    protected Future<?> _spawnMonsterTask = null;

    private final static String HTML_FILE_PATH = "data/html/SepulcherNpc/";
    private final static int HALLS_KEY = 7260;

    public L2SepulcherNpcInstance(int objectID, L2NpcTemplate template)
    {
        super(objectID, template);

        if (_closeTask != null)
            _closeTask.cancel(true);
        if (_spawnNextMysteriousBoxTask != null)
            _spawnNextMysteriousBoxTask.cancel(true);
        if (_spawnMonsterTask != null)
            _spawnMonsterTask.cancel(true);

        _closeTask = null;
        _spawnNextMysteriousBoxTask = null;
        _spawnMonsterTask = null;
    }

    @Override
    public void onSpawn()
    {
        super.onSpawn();
    }

    @Override
    public void deleteMe()
    {
        if (_closeTask != null)
        {
            _closeTask.cancel(true);
            _closeTask = null;
        }

        if (_spawnNextMysteriousBoxTask != null)
        {
            _spawnNextMysteriousBoxTask.cancel(true);
            _spawnNextMysteriousBoxTask = null;
        }

        if (_spawnMonsterTask != null)
        {
            _spawnMonsterTask.cancel(true);
            _spawnMonsterTask = null;
        }
        super.deleteMe();
    }
    
    @Override
    public void onInteract(L2PcInstance player)
    {
    	// Rotate to talk to npc face to face
		player.sendPacket(new MoveToPawn(player, this, INTERACTION_DISTANCE));
		
        // Send a Server->Client packet SocialAction to the all L2PcInstance on the _knownPlayer of the L2NpcInstance
        // to display a social action of the L2NpcInstance on their client
        onRandomAnimation(Rnd.get(8));
        doAction(player);
        
        if (Config.PLAYER_MOVEMENT_BLOCK_TIME > 0 && !player.isGM())
		{
			player.updateNotMoveUntil();
		}
    }

    private void doAction(L2PcInstance player)
    {
        if (isDead())
        {
            player.sendPacket(new ActionFailed());
            return;
        }

        switch (getNpcId())
        {
            case 8468:
            case 8469:
            case 8470:
            case 8471:
            case 8472:
            case 8473:
            case 8474:
            case 8475:
            case 8476:
            case 8477:
            case 8478:
            case 8479:
            case 8480:
            case 8481:
            case 8482:
            case 8483:
            case 8484:
            case 8485:
            case 8486:
            case 8487:
                setIsInvul(false);
                reduceCurrentHp(getMaxHp() + 1, player);
                if (_spawnMonsterTask != null)
                    _spawnMonsterTask.cancel(true);
                _spawnMonsterTask = ThreadPoolManager.getInstance().scheduleEffect(new SpawnMonster(getNpcId()), 3500);
                break;
            case 8455:
            case 8456:
            case 8457:
            case 8458:
            case 8459:
            case 8460:
            case 8461:
            case 8462:
            case 8463:
            case 8464:
            case 8465:
            case 8466:
            case 8467:
                setIsInvul(false);
                reduceCurrentHp(getMaxHp() + 1, player);
                final L2Party party = player.getParty();
                if (party != null && !party.isLeader(player))
                {
                    player = party.getPartyMembers().get(0);
                }
                player.addItem("Quest", HALLS_KEY, 1, player, true);
                break;
            default:
            {
                Quest[] qlsa = getTemplate().getEventQuests(Quest.QuestEventType.QUEST_START);
                if (qlsa != null && qlsa.length > 0)
                    player.setLastQuestNpcObject(getObjectId());

                Quest[] qlst = getTemplate().getEventQuests(Quest.QuestEventType.ON_FIRST_TALK);
                if (qlst != null && qlst.length == 1)
                    qlst[0].notifyFirstTalk(this, player);
                else
                    showChatWindow(player, 0);
            }
        }
        player.sendPacket(new ActionFailed());
    }

    @Override
    public String getHtmlPath(int npcId, int val)
    {
        String pom = "";
        if (val == 0)
            pom = "" + npcId;
        else
            pom = npcId + "-" + val;

        return HTML_FILE_PATH + pom + ".htm";
    }

    @Override
    public void showChatWindow(L2PcInstance player, int val)
    {
        String filename = getHtmlPath(getNpcId(), val);
        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile(filename);
        html.replace("%objectId%", String.valueOf(getObjectId()));
        player.sendPacket(html);
        player.sendPacket(new ActionFailed());
    }

    @Override
    public void onBypassFeedback(L2PcInstance player, String command)
    {
        if (isBusy() && getBusyMessage().length() > 0)
        {
            NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
            html.setFile("data/html/npcbusy.htm");
            html.replace("%busymessage%", getBusyMessage());
            html.replace("%npcname%", getName());
            html.replace("%playername%", player.getName());
            player.sendPacket(html);
        }
        else if (command.startsWith("Chat"))
        {
            int val = 0;
            try
            {
                val = Integer.parseInt(command.substring(5));
            }
            catch (IndexOutOfBoundsException ioobe)
            {
            }
            catch (NumberFormatException nfe)
            {
            }
            showChatWindow(player, val);
        }
        else if (command.startsWith("open_gate"))
        {
            L2ItemInstance hallsKey = player.getInventory().getItemByItemId(HALLS_KEY);
            if (hallsKey == null)
                showHtmlFile(player, "Gatekeeper-no.htm");
            else if (FourSepulchersManager.getInstance().isAttackTime())
            {
                if (player.getParty() != null)
                {
                    for (L2PcInstance mem : player.getParty().getPartyMembers())
                    {
                        if (mem.getInventory().getItemByItemId(HALLS_KEY) != null)
                            mem.destroyItemByItemId("Quest", HALLS_KEY, mem.getInventory().getItemByItemId(HALLS_KEY).getCount(), mem, true);
                    }
                }
                else
                    player.destroyItemByItemId("Quest", HALLS_KEY, hallsKey.getCount(), player, true);

                openNextDoor(getNpcId());
            }
        }
        else
            super.onBypassFeedback(player, command);
    }

    public void openNextDoor(int npcId)
    {
        int doorId = FourSepulchersManager.getInstance().getHallGateKeepers().get(npcId).intValue();
        final L2DoorInstance door = DoorTable.getInstance().getDoor(doorId);
        if (door != null)
        {
        	door.openMe();
        }

        switch (getNpcId())
        {
            case 13197:
            case 13202:
            case 13207:
            case 13212:
                FourSepulchersManager.getInstance().spawnShadow(getNpcId());
                break;
            default:
                if (_spawnNextMysteriousBoxTask != null)
                    _spawnNextMysteriousBoxTask.cancel(true);
                _spawnNextMysteriousBoxTask = ThreadPoolManager.getInstance().scheduleEffect(new SpawnNextMysteriousBox(npcId), 0);
        }

        if (_closeTask != null)
            _closeTask.cancel(true);
        _closeTask = ThreadPoolManager.getInstance().scheduleEffect(new CloseNextDoor(doorId), 15000);
    }

    private class CloseNextDoor implements Runnable
    {
        private final int _doorId;

        public CloseNextDoor(int doorId)
        {
            _doorId = doorId;
        }

        @Override
		public void run()
        {
        	final L2DoorInstance door = DoorTable.getInstance().getDoor(_doorId);
        	if (door != null)
        	{
        		door.closeMe();
        	}
        }
    }

    private class SpawnNextMysteriousBox implements Runnable
    {
        private int _npcId;

        public SpawnNextMysteriousBox(int npcId)
        {
            _npcId = npcId;
        }

        @Override
		public void run()
        {
            FourSepulchersManager.getInstance().spawnMysteriousBox(_npcId);
        }
    }

    private class SpawnMonster implements Runnable
    {
        private int _npcId;

        public SpawnMonster(int npcId)
        {
            _npcId = npcId;
        }

        @Override
		public void run()
        {
            FourSepulchersManager.getInstance().spawnMonster(_npcId);
        }
    }

    public void sayInShout(String msg)
    {
        if (msg == null || msg.isEmpty())
            return; // Wrong usage

        Collection<L2PcInstance> knownPlayers = L2World.getInstance().getAllPlayers();
        if (knownPlayers == null || knownPlayers.isEmpty())
            return;

        NpcSay sm = new NpcSay(0, 1, this.getNpcId(), msg);
        for (L2PcInstance player : knownPlayers)
        {
            if (player == null)
                continue;

            if (Util.checkIfInRange(15000, player, this, true))
            {
            	player.sendPacket(sm);
            }
        }
    }

    public void showHtmlFile(L2PcInstance player, String file)
    {
        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile("data/html/SepulcherNpc/" + file);
        html.replace("%npcname%", getName());
        player.sendPacket(html);
    }
}