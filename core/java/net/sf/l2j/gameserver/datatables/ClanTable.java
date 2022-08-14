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
package net.sf.l2j.gameserver.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.instancemanager.AuctionManager;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListAll;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;
import net.sf.l2j.gameserver.util.Util;

/**
 * This class ...
 * 
 * @version $Revision: 1.11.2.5.2.5 $ $Date: 2005/03/27 15:29:18 $
 */
public class ClanTable
{
    private static Logger _log = Logger.getLogger(ClanTable.class.getName());

    private Map<Integer, L2Clan> _clans;

    public static ClanTable getInstance()
    {
        return SingletonHolder._instance;
    }

    public L2Clan[] getClans()
    {
        return _clans.values().toArray(new L2Clan[_clans.size()]);
    }

    private ClanTable()
    {
        _clans = new HashMap<>();
        L2Clan clan;

        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT clan_id FROM clan_data");	           
            ResultSet result = statement.executeQuery())
        {
            // Count the clans
            int clanCount = 0;

            while(result.next())
            {
                _clans.put(Integer.parseInt(result.getString("clan_id")),new L2Clan(Integer.parseInt(result.getString("clan_id"))));
                clan = getClan(Integer.parseInt(result.getString("clan_id")));
                if (clan.getDissolvingExpiryTime() != 0)
                {
                    if (clan.getDissolvingExpiryTime() < System.currentTimeMillis())
                        destroyClan(clan.getClanId());
                    else
                        scheduleRemoveClan(clan.getClanId());
                }
                clanCount++;
            }

            _log.config("Restored "+clanCount+" clans from the database.");
        }
        catch (Exception e)

        {
            _log.warning("data error on ClanTable: " + e);
            e.printStackTrace();
        }
    }

    /**
     * @param clanId
     * @return
     */
    public L2Clan getClan(int clanId)
    {
        L2Clan clan = _clans.get(Integer.valueOf(clanId));

        return clan; 
    }

    public L2Clan getClanByName(String clanName)
    {
        for (L2Clan clan : getClans())
        {
            if (clan.getName().equalsIgnoreCase(clanName))
                return clan;
        }

        return null;
    }

    public L2Clan createClan(L2PcInstance player, String clanName)
    {
    	if (player == null)
            return null;

        if (Config.DEBUG)
            _log.fine(player.getObjectId() + "(" + player.getName() + ") requested a clan creation.");

        if (player.getLevel() < 10)
        {
            player.sendPacket(new SystemMessage(SystemMessage.FAILED_TO_CREATE_CLAN));
            return null;
        }

        if (player.getClanId() != 0)
        {
            player.sendPacket(new SystemMessage(SystemMessage.FAILED_TO_CREATE_CLAN));
            return null;
        }

        if (player.getClanCreateExpiryTime() > System.currentTimeMillis())
        {
            player.sendPacket(new SystemMessage(SystemMessage.YOU_MUST_WAIT_XX_DAYS_BEFORE_CREATING_A_NEW_CLAN));
            return null;
        }

        if (!Util.isAlphaNumeric(clanName) || !Util.isValidName(clanName, Config.CLAN_ALLY_NAME_TEMPLATE) || clanName.length() < 2)
        {
            player.sendPacket(new SystemMessage(SystemMessage.CLAN_NAME_INCORRECT));
            return null;
        }

        if (clanName.length() > 16)
        {
            player.sendPacket(new SystemMessage(SystemMessage.CLAN_NAME_TOO_LONG));
            return null;
        }

        if (getClanByName(clanName) != null)
        {
            player.sendMessage("Clan name already exists.");
            return null;
        }

        L2ClanMember leader = new L2ClanMember(player.getName(), player.getLevel(), player.getClassId().getId(), player.getObjectId());

        L2Clan clan = new L2Clan(IdFactory.getInstance().getNextId(), clanName, leader);

        leader.setPlayerInstance(player);
        clan.store();

        player.setClan(clan);

        player.setClanPrivileges(L2Clan.CP_ALL);

        if (Config.DEBUG)
            _log.fine("New clan created: "+clan.getClanId() + " " +clan.getName());

        _clans.put(Integer.valueOf(clan.getClanId()), clan);

        // should be update packet only
        player.sendPacket(new PledgeShowInfoUpdate(clan));
        player.sendPacket(new PledgeShowMemberListAll(clan, player));
        player.sendPacket(new UserInfo(player));
        player.sendPacket(new SystemMessage(SystemMessage.CLAN_CREATED));

        return clan;
    }

    public void destroyClan(int clanId)
    {
        L2Clan clan = getClan(clanId);
        if (clan == null)
            return;

        clan.broadcastToOnlineMembers(new SystemMessage(193));

        if (AuctionManager.getInstance().getAuction(clan.getAuctionBiddedAt()) != null)
            AuctionManager.getInstance().getAuction(clan.getAuctionBiddedAt()).cancelBid(clan.getClanId());

        if (clan.getHasHideout() != 0)
            ClanHallManager.getInstance().getClanHallByOwner(clan).setOwner(null);

        int castleId = clan.getHasCastle();
        if (castleId == 0)
        {
            for (Siege siege : SiegeManager.getInstance().getSieges())
            {
                siege.removeSiegeClan(clanId, true);
            }
        }

        L2ClanMember leaderMember = clan.getLeader();
        if (leaderMember == null)
            clan.getWarehouse().destroyAllItems("ClanRemove", null, null);
        else
            clan.getWarehouse().destroyAllItems("ClanRemove", clan.getLeader().getPlayerInstance(), null);

        for (L2ClanMember member : clan.getMembers())
        {
            clan.removeClanMember(member.getObjectId(), 0);
        }

        _clans.remove(clanId);
        IdFactory.getInstance().releaseId(clanId);

        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            try (PreparedStatement statement = con.prepareStatement("UPDATE characters SET clanid = 0, clan_privs = 0 WHERE clanid=?"))
            {
                statement.setInt(1, clanId);
                statement.execute();
            }

            try (PreparedStatement statement = con.prepareStatement("DELETE FROM clan_data WHERE clan_id=?"))
            {
                statement.setInt(1, clanId);
                statement.execute();
            }


            try (PreparedStatement statement = con.prepareStatement("DELETE FROM clan_wars WHERE clan1=? OR clan2=?"))
            {
                statement.setInt(1, clanId);
                statement.setInt(2, clanId);
                statement.execute();
            }

            if (castleId != 0)
            {
                try (PreparedStatement statement = con.prepareStatement("UPDATE castle SET taxPercent = 0 WHERE id = ?"))
                {
                    statement.setInt(1, castleId);
                    statement.execute();
                }
            }
        }
        catch (Exception e)
        {
            _log.warning("could not dissolve clan:" + e);
        }
    }

    public boolean isAllyExists(String allyName)
    {
        for (L2Clan clan : getClans())
        {
            if (clan.getAllyName() != null && clan.getAllyName().equalsIgnoreCase(allyName))
                return true;
        }
        return false;
    }
    
    public void storeClanWars(int clanId1, int clanId2)
    {
        L2Clan clan1 = ClanTable.getInstance().getClan(clanId1);
        L2Clan clan2 = ClanTable.getInstance().getClan(clanId2);
        clan1.setEnemyClan(clan2);

        clan1.broadcastClanStatus();

        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("REPLACE INTO clan_wars (clan1, clan2) VALUES(?,?)"))
        {
            statement.setInt(1, clanId1);
            statement.setInt(2, clanId2);
            statement.execute();
        }
        catch (Exception e)
        {
            _log.warning("could not store clans wars data:"+e);
        }

        SystemMessage msg = new SystemMessage(1562);
        msg.addString(clan2.getName());
        clan1.broadcastToOnlineMembers(msg);

        // clan1 declared clan war.
        msg = new SystemMessage(1561);
        msg.addString(clan1.getName());
        clan2.broadcastToOnlineMembers(msg);
    }

    public void deleteClanWars(int clanId1, int clanId2)
    {
        L2Clan clan1 = ClanTable.getInstance().getClan(clanId1);
        L2Clan clan2 = ClanTable.getInstance().getClan(clanId2);
        clan1.deleteEnemyClan(clan2);

        clan1.broadcastClanStatus();

        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("DELETE FROM clan_wars WHERE clan1=? AND clan2=?"))
        {
            statement.setInt(1,clanId1);
            statement.setInt(2,clanId2);
            statement.execute();

        }
        catch (Exception e)
        {
            _log.warning("could not restore clans wars data:"+e);
        }


        SystemMessage msg = new SystemMessage(1567);
        msg.addString(clan2.getName());
        clan1.broadcastToOnlineMembers(msg);

        msg = new SystemMessage(1566);
        msg.addString(clan1.getName());
        clan2.broadcastToOnlineMembers(msg);

    }
    
    public void checkSurrender(L2Clan clan1, L2Clan clan2)
    {
        int count = 0;
        for(L2ClanMember player: clan1.getMembers())
        {
            if(player != null && player.getPlayerInstance().getWantsPeace() == 1)
                count++;
        }

        if (count == clan1.getMembers().length-1)
        {
            clan1.deleteEnemyClan(clan2);
            clan2.deleteEnemyClan(clan1);
            deleteClanWars(clan1.getClanId(),clan2.getClanId());
        }
    }

    public void scheduleRemoveClan(final int clanId)
    {
        ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
        {
            @Override
			public void run()
            {
                if (getClan(clanId) == null)
                    return;

                if (getClan(clanId).getDissolvingExpiryTime() != 0)
                    destroyClan(clanId);
            }
        }, getClan(clanId).getDissolvingExpiryTime() - System.currentTimeMillis());
    }
    
    private static class SingletonHolder
	{
		protected static final ClanTable _instance = new ClanTable();
	}
}