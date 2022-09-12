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
package net.sf.l2j.gameserver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.communitybbs.BB.Forum;
import net.sf.l2j.gameserver.communitybbs.Manager.ForumsBBSManager;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.ItemContainer;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListAll;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListDeleteAll;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;
import net.sf.l2j.gameserver.util.Util;

/**
 * This class ...
 * 
 * @version $Revision: 1.7.2.4.2.7 $ $Date: 2005/04/06 16:13:41 $
 */
public class L2Clan
{
	private static final Logger _log = Logger.getLogger(L2Clan.class.getName());

	private String _name;
	private int _clanId;
	private L2ClanMember _leader;
	private Map<Integer, L2ClanMember> _members = new HashMap<>();

	private String _allyName;
	private int _allyId;
	private int _level;
	private int _hasCastle;
	private int _hasHideout;
    private boolean _hasCrest;
    private int _hiredGuards;
    private int _crestId;
    private int _crestLargeId;
    private int _allyCrestId;
    private int _auctionBiddedAt = 0;
    private long _charPenaltyExpiryTime;
    private long _recoverPenaltyExpiryTime;
    private long _dissolvingExpiryTime;
    private long _allyJoinExpiryTime;
    private long _allyPenaltyExpiryTime;
    private int _allyPenaltyType;

    // Ally Penalty Types

    /** Leader clan dismiss clan from ally */
    public static final int PENALTY_TYPE_DISMISS_CLAN = 1;
    /** Leader clan dissolve ally */
    public static final int PENALTY_TYPE_DISSOLVE_ALLY = 2;

    private ItemContainer _warehouse = new ClanWarehouse(this);
    private List<Integer> _atWarWith = new ArrayList<>();

	private boolean _hasCrestLarge;
	private Forum _forum;

    //  Clan Privileges
	public static final int CP_NOTHING = 0;           // No privileges
	public static final int CP_CL_JOIN_CLAN = 1;      // Join clan
	public static final int CP_CL_GIVE_TITLE = 2;     // Give a title
	public static final int CP_CL_VIEW_WAREHOUSE = 4; // View warehouse content
	public static final int CP_CL_MANAGE_CREST = 8; // Manage clan crest
    public static final int CP_CH_OPEN_DOOR = 16;     // Open clan hall doors
	public static final int CP_CH_OTHER_RIGHTS = 32;  // Function adding/restoration
    public static final int CP_CH_DISMISS = 64;      // Expel outsiders
	public static final int CP_CS_OPEN_DOOR = 128;    // Open castle doors
	public static final int CP_CS_OTHER_RIGHTS = 256; // (not fully implemented yet)Function adding/restoration, related to manors, mercenary placement
	public static final int CP_CS_DISMISS = 512;     // Expel outsiders
	public static final int CP_CL_CLAN_WAR = 1024;     // Clan war
	public static final int CP_ALL = 2047;            // All privileges

    /**
     * called if a clan is referenced only by id.
     * in this case all other data needs to be fetched from db
     * @param clanId
     */
    public L2Clan(int clanId)
    {
        _clanId = clanId;
        restore();
        getWarehouse().restore();
    }
    
    /**
     * this is only called if a new clan is created
     * @param clanId
     * @param clanName
     * @param leader
     */
    public L2Clan(int clanId, String clanName, L2ClanMember leader)
    {
        _clanId = clanId;
        _name = clanName;
        setLeader(leader);
    }
    
	/**
	 * @return Returns the clanId.
	 */
	public int getClanId()
	{
		return _clanId;
	}

	/**
	 * @param clanId The clanId to set.
	 */
	public void setClanId(int clanId)
	{
		_clanId = clanId;
	}

	/**
	 * @return Returns the leaderId.
	 */
	public int getLeaderId()
	{
		return (_leader != null ? _leader.getObjectId() : 0);
	}

    /**
     * @return L2ClanMember of clan leader.
     */
    public L2ClanMember getLeader()
    {
        return _leader;
    }

	/**
	 * @param leader The leader to set.
	 */
	public void setLeader(L2ClanMember leader)
	{
		_leader = leader;
		_members.put(leader.getObjectId(), leader);
	}

	/**
	 * @return Returns the leaderName.
	 */
	public String getLeaderName()
	{
		return _members.get(Integer.valueOf(_leader.getObjectId())).getName();
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return _name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		_name = name;
	}
	
	private void addClanMember(L2ClanMember member)
	{
		_members.put(member.getObjectId(), member);
	}
	
	public void addClanMember(L2PcInstance player)
	{
		L2ClanMember member = new L2ClanMember(player);

		addClanMember(member);
	}

	public L2ClanMember getClanMember(String name)
	{
		for (L2ClanMember temp : _members.values())
        {
            if (temp.getName().equals(name))
                return temp;
        }
        return null;
	}

    public L2ClanMember getClanMember(int objectID)
    {
        return _members.get(objectID);
    }

    public void removeClanMember(int objectId, long clanJoinExpiryTime)
	{
		L2ClanMember exMember = _members.remove(objectId);
		if (exMember == null)
		{
			_log.warning("Member with Object ID: " + objectId + " was not found in clan during removal.");
			return;
		}

		// Remove circlets
		CastleManager.getInstance().removeCirclet(exMember, getHasCastle(), getLeaderId() == objectId);
		
        if (!exMember.isOnline())
        {
            removeMemberFromDB(exMember, clanJoinExpiryTime, getLeaderId() == objectId ? System.currentTimeMillis() + Config.ALT_CLAN_CREATE_DAYS * 86400000L : 0);
            return;
        }

        L2PcInstance player = exMember.getPlayerInstance();
		if (player.isClanLeader())
		{
			SiegeManager.getInstance().removeSiegeSkills(player);
	        player.setClanCreateExpiryTime(System.currentTimeMillis() + Config.ALT_CLAN_CREATE_DAYS * 86400000L); // 24*60*60*1000 = 86400000
		}

        if (!player.isNoble())
        {
            player.setTitle("");
        }
        
        // Reset siege status if any
        player.setSiegeState((byte)0);
        player.setSiegeSide(0);

		player.setClan(null);
		player.setClanJoinExpiryTime(clanJoinExpiryTime);
		player.broadcastUserInfo();
		player.sendPacket(new PledgeShowMemberListDeleteAll());
	}

	public L2ClanMember[] getMembers()
	{
		return _members.values().toArray(new L2ClanMember[_members.size()]);
	}
	
	public int getMembersCount()
	{
		return _members.size();
	}
	
	public L2PcInstance[] getOnlineMembers(int exclude)
	{
		List<L2PcInstance> result = new ArrayList<>();
		for (L2ClanMember temp : _members.values())
		{
			L2PcInstance playerInstance = temp.getPlayerInstance();
			if (temp.isOnline() && playerInstance != null && temp.getObjectId() != exclude)
			{
				result.add(playerInstance);
			}
		}
		
		return result.toArray(new L2PcInstance[result.size()]);
	}
	
	/**
	 * @return
	 */
	public int getAllyId()
	{
		return _allyId;
	}

	/**
	 * @return
	 */
	public String getAllyName()
	{
		return _allyName;
	}

    public void setAllyCrestId(int allyCrestId)
    {
        _allyCrestId = allyCrestId;
    }

	/**
	 * @return
	 */
	public int getAllyCrestId()
	{
		return _allyCrestId;
	}

	/**
	 * @return
	 */
	public int getLevel()
	{
		return _level;
	}

	/**
	 * @return
	 */
	public int getHasCastle()
	{
		return _hasCastle;
	}

	/**
	 * @return
	 */
	public int getHasHideout()
	{
		return _hasHideout;
	}
    
    /**
     * @param crestId The id of pledge crest.
     */
    public void setCrestId(int crestId)
    {
        _crestId = crestId;
    }

	/**
	 * @return Returns the clanCrestId.
	 */
	public int getCrestId()
	{
		return _crestId;
	}

    /**
     * @param crestLargeId The id of pledge LargeCrest.
     */
    public void setCrestLargeId(int crestLargeId)
    {
        _crestLargeId = crestLargeId;
    }

	/**
	 * @return Returns the clan CrestLargeId
	 */
	public int getCrestLargeId()
	{
		return _crestLargeId;
	}
	
	/**
	 * @param allyId The allyId to set.
	 */
	public void setAllyId(int allyId)
	{
		_allyId = allyId;
	}

	/**
	 * @param allyName The allyName to set.
	 */
	public void setAllyName(String allyName)
	{
		_allyName = allyName;
	}

	/**
	 * @param hasCastle The hasCastle to set.
	 */
	public void setHasCastle(int hasCastle)
	{
		_hasCastle = hasCastle;
	}

	/**
	 * @param hasHideout The hasHideout to set.
	 */
	public void setHasHideout(int hasHideout)
	{
		_hasHideout = hasHideout;
	}

	/**
	 * @param level The level to set.
	 */
	public void setLevel(int level)
	{
	    _level = level;
	    if (_level >= 2 && _forum == null && Config.COMMUNITY_TYPE > 0)
	    {
            Forum forum = ForumsBBSManager.getInstance().getForumByName("ClanRoot");
            if (forum != null)
            {
                _forum = forum.GetChildByName(_name);
                if (_forum == null)
                    _forum = ForumsBBSManager.getInstance().CreateNewForum(_name, ForumsBBSManager.getInstance().getForumByName("ClanRoot"), Forum.CLAN, Forum.CLANMEMBERONLY, getClanId());
            }
	    }
	}

	/**
	 * @param id
	 * @return
	 */
	public boolean isMember(int id)
	{
		return (id == 0 ? false :_members.containsKey(id));
	}
	
	public void updateClanInDB()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE clan_data SET leader_id=?,ally_id=?,ally_name=?,ally_crest_id=?,char_penalty_expiry_time=?,recover_penalty_expiry_time=?,dissolving_expiry_time=?,ally_join_expiry_time=?,ally_penalty_expiry_time=?,ally_penalty_type=? WHERE clan_id=?"))
		{
			statement.setInt(1, getLeaderId());
			statement.setInt(2, getAllyId());
			statement.setString(3, getAllyName());
			statement.setInt(4, getAllyCrestId());
			statement.setLong(5, getCharPenaltyExpiryTime());
			statement.setLong(6, getRecoverPenaltyExpiryTime());
			statement.setLong(7, getDissolvingExpiryTime());
			statement.setLong(8, getAllyJoinExpiryTime());
			statement.setLong(9, getAllyPenaltyExpiryTime());
			statement.setInt(10, getAllyPenaltyType());
			statement.setInt(11, getClanId());
			statement.execute();

			if (Config.DEBUG)
                _log.fine("New clan leader saved in db: " + getClanId());
		}
		catch (Exception e)
		{
			_log.warning("error while saving new clan leader to db "+e);
		}
	}	

	public void store()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO clan_data (clan_id,clan_name,clan_level,hasCastle,ally_id,ally_name,leader_id,crest_id,crest_large_id,ally_crest_id) values (?,?,?,?,?,?,?,?,?,?)"))
		{	
			statement.setInt(1, getClanId());
			statement.setString(2, getName());
			statement.setInt(3, getLevel());
			statement.setInt(4, getHasCastle());
			statement.setInt(5, getAllyId());
			statement.setString(6, getAllyName());
			statement.setInt(7, getLeaderId());
			statement.setInt(8, getCrestId());
			statement.setInt(9,getCrestLargeId());
			statement.setInt(10,getAllyCrestId());
			statement.execute();

			if (Config.DEBUG)
                _log.fine("New clan saved in db: "+getClanId());
		}
		catch (Exception e)
		{
			_log.warning("error while saving new clan to db "+e);
		}
	}

    private void removeMemberFromDB(L2ClanMember member, long clanJoinExpiryTime, long clanCreateExpiryTime)
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("UPDATE characters SET clanid=0, title=?, clan_join_expiry_time=?, clan_create_expiry_time=?, clan_privs=0, wantspeace=0 WHERE obj_Id=?"))
        {
            statement.setString(1, "");
            statement.setLong(2, clanJoinExpiryTime);
            statement.setLong(3, clanCreateExpiryTime);
            statement.setInt(4, member.getObjectId());
            statement.execute();

            if (Config.DEBUG)
            	_log.fine("clan member removed in db: "+getClanId());
        }
        catch (Exception e)
        {
            _log.warning("error while removing clan member in db "+e);
        }
    }

    private void restoreWars()
    {
     	try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT clan1, clan2 FROM clan_wars");
            ResultSet rset = statement.executeQuery())
        {
            while(rset.next())
            {
                if (rset.getInt("clan1") == _clanId)
                    setEnemyClan(rset.getInt("clan2"));
            }
        }
        catch (Exception e)
        {
            _log.warning("could not restore clan wars data:"+e);
        }
    }
    
    private void restore()
    {
    	try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT clan_name,clan_level,hasCastle,ally_id,ally_name,leader_id,crest_id,crest_large_id,ally_crest_id,auction_bid_at,char_penalty_expiry_time,recover_penalty_expiry_time,dissolving_expiry_time,ally_join_expiry_time,ally_penalty_expiry_time,ally_penalty_type FROM clan_data where clan_id=?"))
        {
            L2ClanMember member;

            statement.setInt(1, getClanId());
            try (ResultSet clanData = statement.executeQuery())
            {
                if (clanData.next())
                {
                    setName(clanData.getString("clan_name"));
                    setLevel(clanData.getInt("clan_level"));
                    setHasCastle(clanData.getInt("hasCastle"));
                    setAllyId(clanData.getInt("ally_id"));
                    setAllyName(clanData.getString("ally_name"));

                    setCharPenaltyExpiryTime(clanData.getLong("char_penalty_expiry_time"));
                    if (getCharPenaltyExpiryTime() + Config.ALT_CLAN_JOIN_DAYS * 86400000L < System.currentTimeMillis()) // 24*60*60*1000 = 86400000
                    {
                    	setCharPenaltyExpiryTime(0);
                    }
                    setRecoverPenaltyExpiryTime(clanData.getLong("recover_penalty_expiry_time"));
                    if (getRecoverPenaltyExpiryTime() + Config.ALT_RECOVERY_PENALTY * 86400000L < System.currentTimeMillis()) // 24*60*60*1000 = 86400000
                    {
                        setRecoverPenaltyExpiryTime(0);
                    }

                    setDissolvingExpiryTime(clanData.getLong("dissolving_expiry_time"));
                    setAllyJoinExpiryTime(clanData.getLong("ally_join_expiry_time"));

                    setAllyPenaltyExpiryTime(clanData.getLong("ally_penalty_expiry_time"), clanData.getInt("ally_penalty_type"));
                    if (getAllyPenaltyExpiryTime() < System.currentTimeMillis())
                    {
                        setAllyPenaltyExpiryTime(0, 0);
                    }

                    setCrestId(clanData.getInt("crest_id"));
                    if (getCrestId() != 0)
                    {
                        setHasCrest(true);
                    }

                    setCrestLargeId(clanData.getInt("crest_large_id"));
                    if (getCrestLargeId() != 0)
                    {
                    	setHasCrestLarge(true);
                    }

                    setAllyCrestId(clanData.getInt("ally_crest_id"));
                    setAuctionBiddedAt(clanData.getInt("auction_bid_at"), false);

                    int leaderId = (clanData.getInt("leader_id"));          
                
                    try (PreparedStatement statement2 = con.prepareStatement("SELECT char_name,level,classid,obj_Id FROM characters WHERE clanid=?"))
                    {
                        statement2.setInt(1, getClanId());
                        try (ResultSet clanMembers = statement2.executeQuery())
                        {
                            while (clanMembers.next())
                            {
                                member = new L2ClanMember(clanMembers.getString("char_name"), clanMembers.getInt("level"), clanMembers.getInt("classid"), clanMembers.getInt("obj_id"));
                                if (member.getObjectId() == leaderId)
                                    setLeader(member);
                                else
                                    addClanMember(member);
                            }
                        }
                    }
                }
            }

            if (Config.DEBUG && getName() != null)
            	_log.config("Restored clan data for \"" + getName() + "\" from database.");
            restoreWars();
        }
        catch (Exception e)
        {
            _log.warning("error while restoring clan "+e);
        }
    }
    
	public void broadcastToOnlineAllyMembers(L2GameServerPacket packet)
	{
	    if (getAllyId()==0)
	    	return;

	    for (L2Clan clan : ClanTable.getInstance().getClans())
        {
			if (clan.getAllyId() == getAllyId())
			    clan.broadcastToOnlineMembers(packet);
	    }
	}
	
	public void broadcastToOnlineMembers(L2GameServerPacket packet)
	{
		for (L2ClanMember member : _members.values())
		{
			L2PcInstance playerInstance = member.getPlayerInstance();
			if (member.isOnline() && playerInstance != null)
			{
				playerInstance.sendPacket(packet);
			}
		}
	}
	
	public void broadcastToOtherOnlineMembers(L2GameServerPacket packet, L2PcInstance player)
	{
		for (L2ClanMember member : _members.values())
		{
			L2PcInstance playerInstance = member.getPlayerInstance();
			if (member.isOnline() && playerInstance != null && playerInstance != player)
			{
				playerInstance.sendPacket(packet);
			}
		}
	}
	
	@Override
	public String toString()
	{
		return getName();
	}

    /**
     * @return
     */
    public boolean hasCrest()
    {
        return _hasCrest;
    }
    
    public boolean hasCrestLarge()
	{
		return _hasCrestLarge;
	}
    
    public void setHasCrest(boolean flag)
    {
        _hasCrest = flag;
    }
    
    public void setHasCrestLarge(boolean flag)
    {
        _hasCrestLarge = flag;
    }
    
    public ItemContainer getWarehouse()
    {
        return _warehouse;
    }
    public boolean isAtWarWith(Integer id)
    {
    	return _atWarWith != null && _atWarWith.contains(id);
    }
    public void setEnemyClan(L2Clan clan)
    {
    	Integer id = clan.getClanId();
    	_atWarWith.add(id);
    }
    public void setEnemyClan(Integer clan)
    {
    	_atWarWith.add(clan);
    }
    public void deleteEnemyClan(L2Clan clan)
    {
    	Integer id = clan.getClanId();
    	_atWarWith.remove(id);
    }
    public int getHiredGuards()
    {
    	return _hiredGuards;
    }
    
    public void incrementHiredGuards()
    {
    	_hiredGuards++;
    }
    
    public int isAtWar()
    {
       if (_atWarWith != null && !_atWarWith.isEmpty())
           return 1;
       return 0;
    }
    
    public void broadcastClanStatus()
    {
    	for (L2PcInstance member : getOnlineMembers(0))
        {
        	member.sendPacket(new PledgeShowMemberListDeleteAll());
        	member.sendPacket(new PledgeShowMemberListAll(this, member));
        }
    }

    public int getAuctionBiddedAt()
    {
        return _auctionBiddedAt;
    }
    
    public void setAuctionBiddedAt(int id, boolean storeInDb)
    {
        _auctionBiddedAt = id;

        if (storeInDb)
        {
            try (Connection con = L2DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement = con.prepareStatement("UPDATE clan_data SET auction_bid_at=? WHERE clan_id=?"))
            {
                statement.setInt(1, id);
                statement.setInt(2, getClanId());
                statement.execute();
            }
            catch (Exception e)
            {
                _log.warning("Could not store auction for clan: " + e);
            }
        }
    }

    public long getCharPenaltyExpiryTime()
    {
    	return _charPenaltyExpiryTime;
    }

    public void setCharPenaltyExpiryTime(long time)
    {
    	_charPenaltyExpiryTime = time;
    }

    public long getRecoverPenaltyExpiryTime()
    {
    	return _recoverPenaltyExpiryTime;
    }

    public void setRecoverPenaltyExpiryTime(long time)
    {
    	_recoverPenaltyExpiryTime = time;
    }

    public long getDissolvingExpiryTime()
    {
    	return _dissolvingExpiryTime;
    }

    public void setDissolvingExpiryTime(long time)
    {
    	_dissolvingExpiryTime = time;
    }

    public long getAllyJoinExpiryTime()
    {
        return _allyJoinExpiryTime;
    }

    public void setAllyJoinExpiryTime(long time)
    {
    	_allyJoinExpiryTime = time;
    }

    public long getAllyPenaltyExpiryTime()
    {
        return _allyPenaltyExpiryTime;
    }

    public int getAllyPenaltyType()
    {
        return _allyPenaltyType;
    }

    public void setAllyPenaltyExpiryTime(long expiryTime, int penaltyType)
    {
        _allyPenaltyExpiryTime = expiryTime;
        _allyPenaltyType = penaltyType;
    }

    public void createAlly(L2PcInstance player, String allyName)
    {
    	if (player == null)
            return;

        if (Config.DEBUG)
            _log.fine(player.getObjectId() + "(" + player.getName() + ") requested ally creation from ");

        if (!player.isClanLeader())
        {
            player.sendPacket(new SystemMessage(SystemMessage.ONLY_CLAN_LEADER_CREATE_ALLIANCE));
            return;
        }

        if (getAllyId() != 0)
        {
            player.sendPacket(new SystemMessage(SystemMessage.ALREADY_JOINED_ALLIANCE));
            return;
        }

        if (getLevel() < 5)
        {
            player.sendPacket(new SystemMessage(SystemMessage.TO_CREATE_AN_ALLY_YOU_CLAN_MUST_BE_LEVEL_5_OR_HIGHER));
            return;
        }

        if (getAllyPenaltyExpiryTime() > System.currentTimeMillis())
        {
            if (getAllyPenaltyType() == L2Clan.PENALTY_TYPE_DISSOLVE_ALLY)
            {
                player.sendPacket(new SystemMessage(SystemMessage.CANT_CREATE_ALLIANCE_10_DAYS_DISOLUTION));
                return;
            }
        }

        if (getDissolvingExpiryTime() > System.currentTimeMillis())
        {
            player.sendPacket(new SystemMessage(SystemMessage.YOU_MAY_NOT_CREATE_ALLY_WHILE_DISSOLVING));
            return;
        }

        if (!Util.isAlphaNumeric(allyName) || !Util.isValidName(allyName, Config.CLAN_ALLY_NAME_TEMPLATE))
        {
            player.sendPacket(new SystemMessage(SystemMessage.INCORRECT_ALLIANCE_NAME));
            return;
        }

        if (allyName.length() > 16 || allyName.length() < 2)
        {
            player.sendPacket(new SystemMessage(SystemMessage.INCORRECT_ALLIANCE_NAME_LENGTH));
            return;
        }

        if (ClanTable.getInstance().isAllyExists(allyName))
        {
            player.sendPacket(new SystemMessage(SystemMessage.ALLIANCE_ALREADY_EXISTS));
            return;
        }

        setAllyId(getClanId());
        setAllyName(allyName.trim());
        setAllyPenaltyExpiryTime(0, 0);
        updateClanInDB();

        player.sendPacket(new UserInfo(player));
        player.sendMessage("Alliance " + allyName + " has been created.");
    }

    public void dissolveAlly(L2PcInstance player)
    {
        if (getAllyId() == 0)
        {
            player.sendPacket(new SystemMessage(SystemMessage.NO_CURRENT_ALLIANCES));
            return;
        }

        if (!player.isClanLeader() || getClanId() != getAllyId())
        {
            player.sendPacket(new SystemMessage(SystemMessage.FEATURE_ONLY_FOR_ALLIANCE_LEADER));
            return;
        }

        if (player.isInsideZone(L2Character.ZONE_SIEGE))
        {
            player.sendPacket(new SystemMessage(SystemMessage.CANNOT_DISSOLVE_ALLY_WHILE_IN_SIEGE));
            return;
        }

        broadcastToOnlineAllyMembers(new SystemMessage(SystemMessage.ALLIANCE_DISOLVED));

        for (L2Clan clan : ClanTable.getInstance().getClans())
        {
            if (clan.getAllyId() == getAllyId() && clan.getClanId() != getClanId())
            {
                clan.setAllyId(0);
                clan.setAllyName(null);
                clan.setAllyCrestId(0);
                clan.setAllyPenaltyExpiryTime(0, 0);
                clan.updateClanInDB();
            }
        }

        setAllyId(0);
        setAllyName(null);
        setAllyCrestId(0);
        setAllyPenaltyExpiryTime(System.currentTimeMillis() + Config.ALT_CREATE_ALLY_DAYS_WHEN_DISSOLVED * 86400000, L2Clan.PENALTY_TYPE_DISSOLVE_ALLY); //24*60*60*1000 = 86400000
        updateClanInDB();

        // The clan leader should take the XP penalty of a full death.
        player.deathPenalty(false, false, false);
    }

    /**
     * Checks if activeChar and target meet various conditions to join a clan
     *
     * @param activeChar
     * @param target

     * @return
     */
    public boolean CheckClanJoinCondition(L2PcInstance activeChar, L2PcInstance target)
    {
        if (activeChar == null)
            return false;

        int limit = 0;
        switch (getLevel())
        {
            case 0:
                limit = 10;
                break;
            case 1:
                limit = 15;
                break;
            case 2:
                limit = 20;
                break;
            case 3:
                limit = 30;
                break;
            default:
                limit = 40;
                break;
        }

        if ((activeChar.getClanPrivileges() & CP_CL_JOIN_CLAN) != CP_CL_JOIN_CLAN)
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_NOT_AUTHORIZED));
            return false;
        }

        if (target == null)
            return false;


        if (activeChar.getObjectId() == target.getObjectId())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_INVITE_YOURSELF));
            return false;
        }

        if (getCharPenaltyExpiryTime() > System.currentTimeMillis())
        {
            SystemMessage sm = new SystemMessage(SystemMessage.YOU_MUST_WAIT_BEFORE_INVITE);
            sm.addString(target.getName());
            activeChar.sendPacket(sm);
            sm = null;
            return false;
        }

        if (target.getClanId() != 0)
        {
            SystemMessage sm = new SystemMessage(SystemMessage.S1_WORKING_WITH_ANOTHER_CLAN);
            sm.addString(target.getName());
            activeChar.sendPacket(sm);
            sm = null;
            return false;
        }

        if (target.getClanJoinExpiryTime() > System.currentTimeMillis())
        {
            SystemMessage sm = new SystemMessage(SystemMessage.S1_MUST_WAIT_BEFORE_JOINING_ANOTHER_CLAN);
            sm.addString(target.getName());
            activeChar.sendPacket(sm);
            sm = null;
            return false;
        }

        if (getMembers().length >= limit)
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.CLAN_IS_FULL));
            return false;
        }

    	return true;
    }

    /**
     * Checks if activeChar and target meet various conditions to join a clan
     *
     * @param activeChar
     * @param target
     * @return
     */
    public boolean CheckAllyJoinCondition(L2PcInstance activeChar, L2PcInstance target)
    {
        if (activeChar == null)
            return false;

        if (activeChar.getAllyId() == 0 || !activeChar.isClanLeader() || activeChar.getClanId() != activeChar.getAllyId())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.FEATURE_ONLY_FOR_ALLIANCE_LEADER));
            return false;
        }

        L2Clan leaderClan = activeChar.getClan();
        if (leaderClan.getAllyPenaltyExpiryTime() > System.currentTimeMillis())
        {
            if (leaderClan.getAllyPenaltyType() == PENALTY_TYPE_DISMISS_CLAN)
            {
                activeChar.sendPacket(new SystemMessage(SystemMessage.CANT_INVITE_CLAN_WITHIN_1_DAY));
                return false;
            }

        }

        if (target == null)
            return false;

        if (activeChar.getObjectId() == target.getObjectId())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.CANNOT_INVITE_YOURSELF));
            return false;
        }

        if (target.getClan() == null)
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.TARGET_MUST_BE_IN_CLAN));
            return false;
        }

        if (!target.isClanLeader())
        {
            SystemMessage sm = new SystemMessage(SystemMessage.S1_IS_NOT_A_CLAN_LEADER);
            sm.addString(target.getName());
            activeChar.sendPacket(sm);
            sm = null;
            return false;
        }

        L2Clan targetClan = target.getClan();
        if (target.getAllyId() != 0)
        {
            SystemMessage sm = new SystemMessage(SystemMessage.S1_CLAN_ALREADY_MEMBER_OF_S2_ALLIANCE);
            sm.addString(targetClan.getName());
            sm.addString(targetClan.getAllyName());
            activeChar.sendPacket(sm);
            sm = null;
            return false;
        }

        if (targetClan.getAllyJoinExpiryTime() > System.currentTimeMillis())
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.CANT_ENTER_ALLIANCE_WITHIN_1_DAY));
            return false;
        }

        if (activeChar.isInsideZone(L2Character.ZONE_SIEGE) && target.isInsideZone(L2Character.ZONE_SIEGE))
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.OPPOSING_CLAN_IS_PARTICIPATING_IN_SIEGE));
            return false;
        }

        if (leaderClan.isAtWarWith(targetClan.getClanId()))
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.MAY_NOT_ALLY_CLAN_BATTLE));
            return false;
        }

        int numOfClansInAlly = 0;
        for (L2Clan clan : ClanTable.getInstance().getClans())
        {
            if (clan.getAllyId() == activeChar.getAllyId())
            {
                ++numOfClansInAlly;
            }
        }

        if (numOfClansInAlly >= Config.ALT_MAX_NUM_OF_CLANS_IN_ALLY)
        {
            activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EXCEEDED_THE_LIMIT));
            return false;
        }

        return true;
    }

    public void changeLevel(int level)
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("UPDATE clan_data SET clan_level = ? WHERE clan_id = ?"))
        {
            statement.setInt(1, level);
            statement.setInt(2, getClanId());
            statement.execute();
        }
        catch (Exception e)
        {
            _log.warning("could not increase clan level:" + e);
        }

        setLevel(level);

        if (getLeader().isOnline())
        {
            L2PcInstance leader = getLeader().getPlayerInstance();
            if (leader == null)
                return;

            if (level > 3)
                SiegeManager.getInstance().addSiegeSkills(leader);
            else
                SiegeManager.getInstance().removeSiegeSkills(leader);
        }

        // Notify all the members about it
        broadcastToOnlineMembers(new SystemMessage(SystemMessage.CLAN_LEVEL_INCREASED));
        broadcastToOnlineMembers(new PledgeShowInfoUpdate(this));
    }
}