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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * @author godson
 */
public class Hero
{
    private static Logger _log = Logger.getLogger(Hero.class.getName());
    
    private static final String GET_HEROES = "SELECT heroes.char_id, characters.char_name, heroes.class_id, heroes.count, heroes.played, heroes.claimed FROM heroes, characters WHERE characters.obj_Id = heroes.char_id AND heroes.played = 1";
    private static final String GET_ALL_HEROES = "SELECT heroes.char_id, characters.char_name, heroes.class_id, heroes.count, heroes.played, heroes.claimed FROM heroes, characters WHERE characters.obj_Id = heroes.char_id";
    private static final String UPDATE_ALL = "UPDATE heroes SET played = 0";
    private static final String INSERT_HERO = "INSERT INTO heroes (char_id, class_id, count, played, claimed) VALUES (?,?,?,?,?)";
    private static final String UPDATE_HERO = "UPDATE heroes SET count = ?, played = ?, claimed = ? WHERE char_id = ?";
    private static final String GET_CLAN_ALLY = "SELECT characters.clanid AS clanid, coalesce(clan_data.ally_Id, 0) AS allyId FROM characters LEFT JOIN clan_data ON clan_data.clan_id = characters.clanid WHERE characters.obj_Id = ?";
    private static final String DELETE_ITEMS = "DELETE FROM items WHERE item_id IN (6842, 6611, 6612, 6613, 6614, 6615, 6616, 6617, 6618, 6619, 6620, 6621) AND owner_id NOT IN (SELECT obj_id FROM characters WHERE accesslevel > 0)";
    
    private static Map<Integer, StatsSet> _heroes = new ConcurrentHashMap<>();
    private static Map<Integer, StatsSet> _completeHeroes = new ConcurrentHashMap<>();
    
    public static final String COUNT = "count";
    public static final String PLAYED = "played";
    public static final String CLAIMED = "claimed";
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
                    hero.set(CLAIMED, rset.getBoolean(CLAIMED));
                    
                    try (PreparedStatement statement2 = con.prepareStatement(GET_CLAN_ALLY))
                    {
                        statement2.setInt(1, charId);
                        try (ResultSet rset2 = statement2.executeQuery())
                        {
                            if (rset2.next())
                            {
                                updateHeroClanData(rset2.getInt("clanid"), rset2.getInt("allyId"), hero);
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
                    hero.set(CLAIMED, rset.getBoolean(CLAIMED));
                    
                    try (PreparedStatement statement2 = con.prepareStatement(GET_CLAN_ALLY))
                    {
                        statement2.setInt(1, charId);
                        try (ResultSet rset2 = statement2.executeQuery())
                        {
                            if (rset2.next())
                            {
                                updateHeroClanData(rset2.getInt("clanid"), rset2.getInt("allyId"), hero);
                            }
                        }
                    }
                    _completeHeroes.put(charId, hero);
                }
            }
        }
        catch (SQLException e)
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
    
    public synchronized void computeNewHeroes(List<StatsSet> newHeroes)
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
                    
                    InventoryUpdate iu = new InventoryUpdate();
                    
                    // Item slots that could be occupied by hero items
                    int[] slots = { Inventory.PAPERDOLL_RHAND, Inventory.PAPERDOLL_HAIR };
                    for (int slot : slots)
                    {
                        L2ItemInstance equippedItem = player.getInventory().getPaperdollItem(slot);
                        if (equippedItem != null && equippedItem.isHeroItem())
                        {
                            player.getInventory().unEquipItemInSlot(slot);
                        }
                    }
                    
                    for (L2ItemInstance item : player.getInventory().getItems())
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
        
        deleteItemsInDb();
        _heroes.clear();
        
        if (newHeroes.isEmpty())
        {
            return;
        }
        
        for (StatsSet hero : newHeroes)
        {
            int charId = hero.getInteger(Olympiad.CHAR_ID);
            
            if (_completeHeroes.containsKey(charId))
            {
                StatsSet oldHero = _completeHeroes.get(charId);
                int count = oldHero.getInteger(COUNT);
                oldHero.set(COUNT, count + 1);
                oldHero.set(PLAYED, 1);
                oldHero.set(CLAIMED, false);
                _heroes.put(charId, oldHero);
            }
            else
            {
                StatsSet newHero = new StatsSet();
                newHero.set(Olympiad.CHAR_NAME, hero.getString(Olympiad.CHAR_NAME));
                newHero.set(Olympiad.CLASS_ID, hero.getInteger(Olympiad.CLASS_ID));
                newHero.set(COUNT, 1);
                newHero.set(PLAYED, 1);
                newHero.set(CLAIMED, false);
                _heroes.put(charId, newHero);
            }
        }
        
        updateHeroes(false);
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
                            statement.setBoolean(5, hero.getBool(CLAIMED));
                            statement.execute();
                        }
                        
                        try (PreparedStatement statement2 = con.prepareStatement(GET_CLAN_ALLY))
                        {
                            statement2.setInt(1, heroId);
                            try (ResultSet rset2 = statement2.executeQuery())
                            {
                                if (rset2.next())
                                {
                                    updateHeroClanData(rset2.getInt("clanid"), rset2.getInt("allyId"), hero);
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
                            statement.setBoolean(3, hero.getBool(CLAIMED));
                            statement.setInt(4, heroId);
                            statement.execute();
                        }
                    }
                }
            }
        }
        catch (SQLException e)
        {
            _log.warning("Hero System: Couldn't update Heroes");
            if (Config.DEBUG)
                e.printStackTrace();
        }
    }
    
    private void updateHeroClanData(int clanId, int allyId, StatsSet heroData)
    {
        String clanName = "";
        String allyName = "";
        int clanCrest = 0;
        int allyCrest = 0;
        
        if (clanId > 0)
        {
            L2Clan clan = ClanTable.getInstance().getClan(clanId);
            if (clan != null)
            {
                clanName = clan.getName();
                clanCrest = clan.getCrestId();
                
                if (allyId > 0)
                {
                    allyName = clan.getAllyName();
                    allyCrest = clan.getAllyCrestId();
                }
            }
        }
        
        heroData.set(CLAN_CREST, clanCrest);
        heroData.set(CLAN_NAME, clanName);
        heroData.set(ALLY_CREST, allyCrest);
        heroData.set(ALLY_NAME, allyName);
    }
    
    private void deleteItemsInDb()
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement(DELETE_ITEMS))
        {
            statement.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Verifies if the given object ID belongs to a claimed hero.
     * @param objectId the player's object ID to verify
     * @return True if there are heroes and the player is in the list, false otherwise
     */
    public boolean isHero(int objectId)
    {
        return _heroes.containsKey(objectId) && _heroes.get(objectId).getBool(CLAIMED);
    }
    
    /**
     * Verifies if the given object ID belongs to an unclaimed hero.
     * @param objectId the player's object ID to verify
     * @return True if player is unclaimed hero
     */
    public boolean isUnclaimedHero(int objectId)
    {
        return _heroes.containsKey(objectId) && !_heroes.get(objectId).getBool(CLAIMED);
    }
    
    /**
     * Claims the hero status for the given player.
     * @param player the player to become hero
     */
    public void claimHero(L2PcInstance player)
    {
        StatsSet hero = _heroes.get(player.getObjectId());
        if (hero == null)
        {
            hero = new StatsSet();
            _heroes.put(player.getObjectId(), hero);
        }
        
        hero.set(CLAIMED, true);
        
        player.setHero(true);
        player.broadcastPacket(new SocialAction(player.getObjectId(), 16));
        player.sendPacket(new UserInfo(player));
        player.broadcastUserInfo();
        
        updateHeroes(false);
    }
    
    private static class SingletonHolder
    {
        protected static final Hero _instance = new Hero();
    }
}