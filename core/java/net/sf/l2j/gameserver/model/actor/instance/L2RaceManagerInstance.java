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

import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.instancemanager.games.MonsterRace;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.knownlist.RaceManagerKnownList;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

public class L2RaceManagerInstance extends L2NpcInstance
{
    private static final int COSTS[] =
    {
        100,
        500,
        1000,
        5000,
        10000,
        20000,
        50000,
        100000
    };
    
    public L2RaceManagerInstance(int objectId, L2NpcTemplate template)
    {
        super(objectId, template);
        getKnownList();
        
        MonsterRace.getInstance().addManager(this);
    }
    
    @Override
    public final RaceManagerKnownList getKnownList()
    {
        if (super.getKnownList() == null || !(super.getKnownList() instanceof RaceManagerKnownList))
            this.setKnownList(new RaceManagerKnownList(this));
        return (RaceManagerKnownList) super.getKnownList();
    }

    @Override
	public void deleteMe()
	{
		MonsterRace.getInstance().removeManager(this);
		super.deleteMe();
	}
    
    @Override
    public void onBypassFeedback(L2PcInstance player, String command)
    {
        if (command.startsWith("BuyTicket"))
        {
            if (MonsterRace.getInstance().isAcceptingBets())
            {
                int val = Integer.parseInt(command.substring(10));
                if (val == 0)
                {
                    player.setRace(0, 0);
                    player.setRace(1, 0);
                }
                if ((val == 10 && player.getRace(0) == 0) || (val == 20 && player.getRace(0) == 0 && player.getRace(1) == 0))
                    val = 0;
                showBuyTicket(player, val);
            }
            else
            {
                player.sendPacket(new SystemMessage(SystemMessage.MONSRACE_TICKETS_NOT_AVAILABLE));
                command = "Chat 0";
            }
        }
        else if (command.startsWith("ShowOdds"))
        {
            if (MonsterRace.getInstance().isAcceptingBets())
            {
                player.sendPacket(new SystemMessage(SystemMessage.MONSRACE_NO_PAYOUT_INFO));
                command = "Chat 0";
            }
            else
            {
                showOdds(player);
            }
        }
        else if (command.equals("ShowInfo"))
        {
            showMonsterInfo(player);
        }
        else if (command.equals("calculateWin"))
        {
            // displayCalculateWinnings(player);
        }
        else if (command.equals("viewHistory"))
        {
            // displayHistory(player);
        }
        else
        {
            super.onBypassFeedback(player, command);
        }
    }
    
    public void showOdds(L2PcInstance player)
    {
        if (MonsterRace.getInstance().isAcceptingBets())
            return;
        
        int npcId = getNpcId();
        String filename, search;
        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        filename = getHtmlPath(npcId, 5);
        html.setFile(filename);
        for (int i = 0; i < 8; i++)
        {
            int n = i + 1;
            search = "Mob" + n;
            html.replace(search, MonsterRace.getInstance().getMonsters()[i].getTemplate().name);
        }
        html.replace("1race", String.valueOf(MonsterRace.getInstance().getRaceNumber()));
        html.replace("%objectId%", String.valueOf(getObjectId()));
        player.sendPacket(html);
        player.sendPacket(new ActionFailed());
    }
    
    public void showMonsterInfo(L2PcInstance player)
    {
        int npcId = getNpcId();
        String filename, search;
        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        filename = getHtmlPath(npcId, 6);
        html.setFile(filename);
        for (int i = 0; i < 8; i++)
        {
            int n = i + 1;
            search = "Mob" + n;
            html.replace(search, MonsterRace.getInstance().getMonsters()[i].getTemplate().name);
        }
        html.replace("%objectId%", String.valueOf(getObjectId()));
        player.sendPacket(html);
        player.sendPacket(new ActionFailed());
    }
    
    public void showBuyTicket(L2PcInstance player, int val)
    {
        if (!MonsterRace.getInstance().isAcceptingBets())
            return;
        
        int npcId = getNpcId();
        int raceNumber = MonsterRace.getInstance().getRaceNumber();
        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        
        SystemMessage sm;
        String filename, search, replace;
        
        if (val < 10)
        {
            filename = getHtmlPath(npcId, 2);
            html.setFile(filename);
            for (int i = 0; i < 8; i++)
            {
                int n = i + 1;
                search = "Mob" + n;
                html.replace(search, MonsterRace.getInstance().getMonsters()[i].getTemplate().name);
            }
            search = "No1";
            if (val == 0)
                html.replace(search, "");
            else
            {
                html.replace(search, "" + val);
                player.setRace(0, val);
            }
        }
        else if (val < 20)
        {
            if (player.getRace(0) == 0)
                return;
            filename = getHtmlPath(npcId, 3);
            html.setFile(filename);
            html.replace("0place", "" + player.getRace(0));
            search = "Mob1";
            replace = MonsterRace.getInstance().getMonsters()[player.getRace(0) - 1].getTemplate().name;
            html.replace(search, replace);
            search = "0adena";
            if (val == 10)
                html.replace(search, "");
            else
            {
                html.replace(search, "" + COSTS[val - 11]);
                player.setRace(1, val - 10);
            }
        }
        else if (val == 20)
        {
            if (player.getRace(0) == 0 || player.getRace(1) == 0)
                return;
            
            filename = getHtmlPath(npcId, 4);
            html.setFile(filename);
            html.replace("0place", "" + player.getRace(0));
            search = "Mob1";
            replace = MonsterRace.getInstance().getMonsters()[player.getRace(0) - 1].getTemplate().name;
            html.replace(search, replace);
            search = "0adena";
            int price = COSTS[player.getRace(1) - 1];
            html.replace(search, "" + price);
            search = "0tax";
            int tax = 0;
            html.replace(search, "" + tax);
            search = "0total";
            int total = price + tax;
            html.replace(search, "" + total);
        }
        else
        {
            if (player.getRace(0) == 0 || player.getRace(1) == 0)
                return;
            
            int ticket = player.getRace(0);
            int priceId = player.getRace(1);
            
            if (!player.reduceAdena("Race", COSTS[priceId - 1], this, true))
                return;
            
            player.setRace(0, 0);
            player.setRace(1, 0);
            sm = new SystemMessage(SystemMessage.ACQUIRED);
            sm.addNumber(raceNumber);
            sm.addItemName(4443);
            player.sendPacket(sm);
            L2ItemInstance item = new L2ItemInstance(IdFactory.getInstance().getNextId(), 4443);
            item.setCount(1);
            item.setEnchantLevel(raceNumber);
            item.setCustomType1(ticket);
            item.setCustomType2(COSTS[priceId - 1] / 100);
            
            L2ItemInstance addedItem = player.getInventory().addItem("Race", item, player, this);
            InventoryUpdate iu = new InventoryUpdate();
            iu.addItem(addedItem);
            L2ItemInstance adena = player.getInventory().getItemByItemId(Inventory.ADENA_ID);
            iu.addModifiedItem(adena);
            player.sendPacket(iu);
            return;
        }
        html.replace("1race", String.valueOf(raceNumber));
        html.replace("%objectId%", String.valueOf(getObjectId()));
        player.sendPacket(html);
        player.sendPacket(new ActionFailed());
    }
    
    public class Race
    {
        private Info[] info;
        
        public Race(Info[] pInfo)
        {
            this.info = pInfo;
        }
        
        public Info getLaneInfo(int lane)
        {
            return info[lane];
        }
        
        public class Info
        {
            private int id;
            private int place;
            private int odds;
            private int payout;
            
            public Info(int pId, int pPlace, int pOdds, int pPayout)
            {
                this.id = pId;
                this.place = pPlace;
                this.odds = pOdds;
                this.payout = pPayout;
            }
            
            public int getId()
            {
                return id;
            }
            
            public int getOdds()
            {
                return odds;
            }
            
            public int getPayout()
            {
                return payout;
            }
            
            public int getPlace()
            {
                return place;
            }
        }
    }
}
