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
package net.sf.l2j.gameserver.model.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * @author godson
 */
public class Hero 
{
    private static Logger _log = Logger.getLogger(Hero.class.getName());

    private static final String GET_HEROES = "SELECT heroes.char_id, " + "characters.char_name, heroes.class_id, heroes.count, heroes.played " + "FROM heroes, characters WHERE characters.obj_Id = heroes.char_id " + "AND heroes.played = 1";
    private static final String GET_ALL_HEROES = "SELECT heroes.char_id, " + "characters.char_name, heroes.class_id, heroes.count, heroes.played " + "FROM heroes, characters WHERE characters.obj_Id = heroes.char_id";
    private static final String UPDATE_ALL = "UPDATE heroes SET played = 0";
    private static final String INSERT_HERO = "INSERT INTO heroes (char_id, class_id, count, played) VALUES (?,?,?,?)";
    private static final String UPDATE_HERO = "UPDATE heroes SET count = ?, " + "played = ?" + " WHERE char_id = ?";
    private static final String GET_CLAN_ALLY = "SELECT characters.clanid AS clanid, coalesce(clan_data.ally_Id, 0) AS allyId FROM characters LEFT JOIN clan_data ON clan_data.clan_id = characters.clanid" +
            " WHERE characters.obj_Id = ?";
    private static final String DELETE_ITEMS = "DELETE FROM items WHERE item_id IN " +
            "(6842, 6611, 6612, 6613, 6614, 6615, 6616, 6617, 6618, 6619, 6620, 6621) " +
            "AND owner_id NOT IN (SELECT obj_id FROM characters WHERE accesslevel > 0)";

    private static Map<Integer, StatsSet> _heroes = new ConcurrentHashMap<>();
    private static Map<Integer, StatsSet> _completeHeroes = new ConcurrentHashMap<>();
    
    public static final String COUNT = "count";
    public static final String PLAYED = "played";
    public static final String CLAN_NAME = "clan_name";
    public static final String CLAN_CREST = "clan_crest";
    public static final String ALLY_NAME = "ally_name";
    public static final String ALLY_CREST = "ally_crest";
    
    public static Hero getInstance()
    {
        return SingletonHolder._instance;
    }
    
    public Hero()
    {
        init();
    }
    
    private void init()
    {
        _heroes.clear();
        _completeHeroes.clear();
        
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            try (PreparedStatement statement = con.prepareStatement(GET_HEROES);
                ResultSet rset = statement.executeQuery())
            {
                while (rset.next())
                {
                    StatsSet hero = new StatsSet();
                    int charId = rset.getInt(Olympiad.CHAR_ID);
                    hero.set(Olympiad.CHAR_NAME, rset.getString(Olympiad.CHAR_NAME));
                    hero.set(Olympiad.CLASS_ID, rset.getInt(Olympiad.CLASS_ID));
                    hero.set(COUNT, rset.getInt(COUNT));
                    hero.set(PLAYED, rset.getInt(PLAYED));

                    try (PreparedStatement statement2 = con.prepareStatement(GET_CLAN_ALLY))
                    {
                        statement2.setInt(1, charId);
                        try (ResultSet rset2 = statement2.executeQuery())
                        {
                            if (rset2.next())
                            {
                                int clanId = rset2.getInt("clanid");
                                int allyId = rset2.getInt("allyId");

                                String clanName = "";
                                String allyName = "";
                                int clanCrest = 0;
                                int allyCrest = 0;

                                if (clanId > 0)
                                {
                                    clanName = ClanTable.getInstance().getClan(clanId).getName();
                                    clanCrest = ClanTable.getInstance().getClan(clanId).getCrestId();

                                    if (allyId > 0)
                                    {
                                        allyName = ClanTable.getInstance().getClan(clanId).getAllyName();
                                        allyCrest = ClanTable.getInstance().getClan(clanId).getAllyCrestId();
                                    }
                                }
                    
                                hero.set(CLAN_CREST, clanCrest);
                                hero.set(CLAN_NAME, clanName);
                                hero.set(ALLY_CREST, allyCrest);
                                hero.set(ALLY_NAME, allyName);
                            }
                        }
                    }

                    _heroes.put(charId, hero);
                }
            }

            try (PreparedStatement statement = con.prepareStatement(GET_ALL_HEROES);
                ResultSet rset = statement.executeQuery())
            {
                while (rset.next())
                {
                    StatsSet hero = new StatsSet();
                    int charId = rset.getInt(Olympiad.CHAR_ID);
                    hero.set(Olympiad.CHAR_NAME, rset.getString(Olympiad.CHAR_NAME));
                    hero.set(Olympiad.CLASS_ID, rset.getInt(Olympiad.CLASS_ID));
                    hero.set(COUNT, rset.getInt(COUNT));
                    hero.set(PLAYED, rset.getInt(PLAYED));

                    try (PreparedStatement statement2 = con.prepareStatement(GET_CLAN_ALLY))
                    {
                        statement2.setInt(1, charId);
                        try (ResultSet rset2 = statement2.executeQuery())
                        {
                            if (rset2.next())
                            {
                                int clanId = rset2.getInt("clanid");
                                int allyId = rset2.getInt("allyId");

                                String clanName = "";
                                String allyName = "";
                                int clanCrest = 0;
                                int allyCrest = 0;

                                if (clanId > 0)
                                {
                                    clanName = ClanTable.getInstance().getClan(clanId).getName();
                                    clanCrest = ClanTable.getInstance().getClan(clanId).getCrestId();

                                    if (allyId > 0)
                                    {
                                        allyName = ClanTable.getInstance().getClan(clanId).getAllyName();
                                        allyCrest = ClanTable.getInstance().getClan(clanId).getAllyCrestId();
                                    }
                                }

                                hero.set(CLAN_CREST, clanCrest);
                                hero.set(CLAN_NAME, clanName);
                                hero.set(ALLY_CREST, allyCrest);
                                hero.set(ALLY_NAME, allyName);
                            }
                        }
                    }
                    _completeHeroes.put(charId, hero);
                }
            }
        }
        catch(SQLException e)
        {
            _log.warning("Hero System: Couldn't load Heroes");
            if (Config.DEBUG)
                e.printStackTrace();
        }

        _log.info("Hero System: Loaded " + _heroes.size() + " Heroes.");
        _log.info("Hero System: Loaded " + _completeHeroes.size() + " all time Heroes.");
    }
    
    public Map<Integer, StatsSet> getHeroes()
    {
        return _heroes;
    }
    
    public void computeNewHeroes(List<StatsSet> newHeroes)
    {
    	updateHeroes(true);
        
        if (!_heroes.isEmpty())
        {
        	for (int objectId : _heroes.keySet())
            {
            	L2PcInstance player = L2World.getInstance().getPlayer(objectId);
                if (player != null)
                {
                    player.setHero(false);
                    
                    L2ItemInstance[] items;
                    InventoryUpdate iu = new InventoryUpdate();

                    // Item slots that could be occupied by hero items
                    int[] slots = {L2Item.SLOT_LR_HAND, L2Item.SLOT_R_HAND, L2Item.SLOT_HAIR};
                    for (int slot : slots)
                    {
	                    L2ItemInstance equippedItem = player.getInventory().getPaperdollItem(slot);
	                    if (equippedItem != null && equippedItem.isHeroItem())
	                    {
	                    	items = player.getInventory().unEquipItemInBodySlotAndRecord(slot);
		                    for (L2ItemInstance item : items)
		                    {
		                        iu.addModifiedItem(item);
		                    }
	                    }
                    }
                    
                    for (L2ItemInstance item : player.getInventory().getAvailableItems(false))
                    {
                        if (item != null && item.isHeroItem())
                        {
	                        player.destroyItem("Hero", item, null, true);
	                        iu.addRemovedItem(item);
                        }
                    }
                    
                    player.sendPacket(iu);
                    player.broadcastUserInfo();
                }
            }
        }
        
        if (newHeroes.isEmpty())
        {
        	_heroes.clear();
            return;
        }
        
        Map<Integer, StatsSet> heroes = new HashMap<>();
        
        for (StatsSet hero : newHeroes)
        {
        	int charId = hero.getInteger(Olympiad.CHAR_ID);
            
            if (_completeHeroes.containsKey(charId))
            {
                StatsSet oldHero = _completeHeroes.get(charId);
                int count = oldHero.getInteger(COUNT);
                oldHero.set(COUNT, count + 1);
                oldHero.set(PLAYED, 1);
                
                heroes.put(charId, oldHero);
            }
            else
            {
                StatsSet newHero = new StatsSet();
                newHero.set(Olympiad.CHAR_NAME, hero.getString(Olympiad.CHAR_NAME));
                newHero.set(Olympiad.CLASS_ID, hero.getInteger(Olympiad.CLASS_ID));
                newHero.set(COUNT, 1);
                newHero.set(PLAYED, 1);
                
                heroes.put(charId, newHero);
            }
        }
        
        deleteItemsInDb();
        
        _heroes.clear();
        _heroes.putAll(heroes);
        heroes.clear();
        
        updateHeroes(false);
        
        for (int objectId : _heroes.keySet())
        {
        	L2PcInstance player = L2World.getInstance().getPlayer(objectId);
            if (player != null)
            {
                player.setHero(true);
                player.sendPacket(new UserInfo(player));
                player.broadcastUserInfo();
            }
        }
    }
    
    public void updateHeroes(boolean setDefault)
    {
    	try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
        	if (setDefault)
            {
            	try (PreparedStatement statement = con.prepareStatement(UPDATE_ALL))
                {
                    statement.execute();
                }
            }
            else
            {
            	for (Entry<Integer, StatsSet> entry : _heroes.entrySet())
                {
            		Integer heroId = entry.getKey();
            		StatsSet hero = entry.getValue();
                    
                    if (!_completeHeroes.containsKey(heroId))
                    {
                    	try (PreparedStatement statement = con.prepareStatement(INSERT_HERO))
                        {
                            statement.setInt(1, heroId);
                            statement.setInt(2, hero.getInteger(Olympiad.CLASS_ID));
                            statement.setInt(3, hero.getInteger(COUNT));
                            statement.setInt(4, hero.getInteger(PLAYED));
                            statement.execute();
                        }
                        
                        try (PreparedStatement statement2 = con.prepareStatement(GET_CLAN_ALLY))
                        {
                        	statement2.setInt(1, heroId);
                            try (ResultSet rset2 = statement2.executeQuery())
                            {
                                if (rset2.next())
                                {
                                    int clanId = rset2.getInt("clanid");
                                    int allyId = rset2.getInt("allyId");

                                    String clanName = "";
                                    String allyName = "";
                                    int clanCrest = 0;
                                    int allyCrest = 0;

                                    if (clanId > 0)
                                    {
                                        clanName = ClanTable.getInstance().getClan(clanId).getName();
                                        clanCrest = ClanTable.getInstance().getClan(clanId).getCrestId();

                                        if (allyId > 0)
                                        {
                                            allyName = ClanTable.getInstance().getClan(clanId).getAllyName();
                                            allyCrest = ClanTable.getInstance().getClan(clanId).getAllyCrestId();
                                        }
                                    }

                                    hero.set(CLAN_CREST, clanCrest);
                                    hero.set(CLAN_NAME, clanName);
                                    hero.set(ALLY_CREST, allyCrest);
                                    hero.set(ALLY_NAME, allyName);
                                }
                            }
                        }

                        _heroes.put(heroId, hero);
                        _completeHeroes.put(heroId, hero);
                    }
                    else
                    {
                        try (PreparedStatement statement = con.prepareStatement(UPDATE_HERO))
                        {
                            statement.setInt(1, hero.getInteger(COUNT));
                            statement.setInt(2, hero.getInteger(PLAYED));
                            statement.setInt(3, heroId);
                            statement.execute();
                        }
                    }
                }
            }
        }
        catch(SQLException e)
        {
            _log.warning("Hero System: Couldn't update Heroes");
            if (Config.DEBUG)
                e.printStackTrace();
        }
    }

    private void deleteItemsInDb()
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement(DELETE_ITEMS))
        {
            statement.execute();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    private static class SingletonHolder
	{
		protected static final Hero _instance = new Hero();
	}
}