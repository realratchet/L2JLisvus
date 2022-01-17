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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.L2DropCategory;
import net.sf.l2j.gameserver.model.L2DropData;
import net.sf.l2j.gameserver.model.L2MinionData;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * This class ...
 * @version $Revision: 1.8.2.6.2.9 $ $Date: 2005/04/06 16:13:25 $
 */
public class NpcTable
{
	private static Logger _log = Logger.getLogger(NpcTable.class.getName());

	private final Map<Integer, L2NpcTemplate> _npcs;

	private static final String RESTORE_NPC = "SELECT " + L2DatabaseFactory.safetyString(new String[]
	{
		"id",
		"idTemplate",
		"name",
		"serverSideName",
		"title",
		"serverSideTitle",
		"class",
		"collision_radius",
		"collision_height",
		"level",
		"sex",
		"type",
		"attackrange",
		"hp",
		"mp",
		"hpreg",
		"mpreg",
		"str",
		"con",
		"dex",
		"int",
		"wit",
		"men",
		"exp",
		"sp",
		"patk",
		"pdef",
		"matk",
		"mdef",
		"atkspd",
		"aggro",
		"matkspd",
		"rhand",
		"lhand",
		"armor",
		"walkspd",
		"runspd",
		"faction_id",
		"faction_range",
		"isUndead",
		"absorb_level",
		"absorb_type",
		"ss",
		"bss",
		"ss_rate",
		"AI"
	});
	
	public static NpcTable getInstance()
	{
		return SingletonHolder._instance;
	}

	private NpcTable()
	{
		_npcs = new HashMap<>();
		restoreNpcData();
	}

	private void restoreNpcData()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(RESTORE_NPC + " FROM npc");
			ResultSet npcdata = statement.executeQuery())
		{
			fillNpcTable(npcdata, false);
		}
		catch (Exception e)
		{
			_log.severe("NPCTable: Error creating NPC table: " + e);
		}

		if (Config.CUSTOM_NPC_TABLE)
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(RESTORE_NPC + " FROM custom_npc");
				ResultSet npcdata = statement.executeQuery())
			{
				fillNpcTable(npcdata, true);
			}
			catch (Exception e)
			{
				_log.severe("NPCTable: Error creating Custom NPC table: " + e);
			}
		}

		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT npcid, skillid, level FROM npcskills");
			ResultSet npcskills = statement.executeQuery())
		{
			L2NpcTemplate npcDat = null;
			L2Skill npcSkill = null;

			while (npcskills.next())
			{
				int mobId = npcskills.getInt("npcid");
				npcDat = _npcs.get(mobId);

				if (npcDat == null)
				{
					continue;
				}

				int skillId = npcskills.getInt("skillid");
				int level = npcskills.getInt("level");

				if (npcDat.race == 0)
				{
					if ((skillId >= 4290) && (skillId <= 4302))
					{
						npcDat.setRace(skillId);
						continue;
					}
				}

				npcSkill = SkillTable.getInstance().getInfo(skillId, level);
				if (npcSkill == null)
				{
					continue;
				}
				
				npcDat.addSkill(npcSkill);
			}
		}
		catch (Exception e)
		{
			_log.severe("NPCTable: Error reading NPC skills table: " + e);
		}

		if (Config.CUSTOM_NPC_SKILLS_TABLE)
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT npcid, skillid, level FROM custom_npcskills");
				ResultSet npcskills = statement.executeQuery())
			{
				L2NpcTemplate npcDat = null;
				L2Skill npcSkill = null;

				while (npcskills.next())
				{
					int mobId = npcskills.getInt("npcid");
					npcDat = _npcs.get(mobId);

					if (npcDat == null)
					{
						continue;
					}

					int skillId = npcskills.getInt("skillid");
					int level = npcskills.getInt("level");

					if (npcDat.race == 0)
					{
						if ((skillId >= 4290) && (skillId <= 4302))
						{
							npcDat.setRace(skillId);
							continue;
						}
					}

					npcSkill = SkillTable.getInstance().getInfo(skillId, level);
					if (npcSkill == null)
					{
						continue;
					}

					npcDat.addSkill(npcSkill);
				}
			}
			catch (Exception e)
			{
				_log.severe("NPCTable: Error reading Custom NPC skills table: " + e);
			}
		}

		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT mobId, itemId, min_drop, max_drop, category, chance FROM droplist ORDER BY mobId, chance DESC");
			ResultSet dropData = statement.executeQuery())
		{
			L2DropData dropDat = null;
			L2NpcTemplate npcDat = null;

			while (dropData.next())
			{
				int mobId = dropData.getInt("mobId");
				npcDat = _npcs.get(mobId);
				if (npcDat == null)
				{
					_log.severe("NPCTable: No npc correlating with id : " + mobId);
					continue;
				}
				dropDat = new L2DropData();

				dropDat.setItemId(dropData.getInt("itemId"));
				dropDat.setMinDrop(dropData.getInt("min_drop"));
				dropDat.setMaxDrop(dropData.getInt("max_drop"));
				dropDat.setChance(dropData.getInt("chance"));

				int category = dropData.getInt("category");
				npcDat.addDropData(dropDat, category);
			}
		}
		catch (Exception e)
		{
			_log.severe("NPCTable: Error reading NPC drop data: " + e);
		}

		if (Config.CUSTOM_DROPLIST_TABLE)
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT mobId, itemId, min_drop, max_drop, category, chance FROM custom_droplist ORDER BY mobId, chance DESC");
				ResultSet dropData = statement.executeQuery())
			{
				L2DropData dropDat = null;
				L2NpcTemplate npcDat = null;
				int cCount = 0;

				while (dropData.next())
				{
					int mobId = dropData.getInt("mobId");
					npcDat = _npcs.get(mobId);
					if (npcDat == null)
					{
						_log.severe("NPCTable: No custom npc correlating with id : " + mobId);
						continue;
					}
					dropDat = new L2DropData();

					dropDat.setItemId(dropData.getInt("itemId"));
					dropDat.setMinDrop(dropData.getInt("min_drop"));
					dropDat.setMaxDrop(dropData.getInt("max_drop"));
					dropDat.setChance(dropData.getInt("chance"));

					int category = dropData.getInt("category");
					npcDat.addDropData(dropDat, category);
					cCount++;
				}

				_log.info("CustomDropList : Added " + cCount + " drops to custom droplist");
			}
			catch (Exception e)
			{
				_log.severe("NPCTable: Error reading Custom NPC drop data: " + e);
			}
		}

		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT npc_id, class_id FROM skill_learn");
			ResultSet learndata = statement.executeQuery())
		{
			while (learndata.next())
			{
				int npcId = learndata.getInt("npc_id");
				int classId = learndata.getInt("class_id");

				L2NpcTemplate npc = getTemplate(npcId);
				if (npc == null)
				{
					_log.warning("NPCTable: Error getting NPC template ID " + npcId + " while trying to load skill trainer data.");
					continue;
				}

				npc.addTeachInfo(ClassId.values()[classId]);
			}
		}
		catch (Exception e)
		{
			_log.severe("NPCTable: Error reading NPC trainer data: " + e);
		}

		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT boss_id, minion_id, amount_min, amount_max FROM minions");
			ResultSet minionData = statement.executeQuery())
		{
			L2MinionData minionDat = null;
			L2NpcTemplate npcDat = null;
			int cnt = 0;

			while (minionData.next())
			{
				int raidId = minionData.getInt("boss_id");
				npcDat = _npcs.get(raidId);
				minionDat = new L2MinionData();
				minionDat.setMinionId(minionData.getInt("minion_id"));
				minionDat.setAmountMin(minionData.getInt("amount_min"));
				minionDat.setAmountMax(minionData.getInt("amount_max"));
				npcDat.addRaidData(minionDat);
				cnt++;
			}

			_log.config("NpcTable: Loaded " + cnt + " minions.");
		}
		catch (Exception e)
		{
			_log.severe("Error loading minion data: " + e);
		}
		
		if (Config.CUSTOM_MINIONS_TABLE)
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT boss_id, minion_id, amount_min, amount_max FROM custom_minions");
				ResultSet minionData = statement.executeQuery())
			{
				L2MinionData minionDat = null;
				L2NpcTemplate npcDat = null;
				int cnt = 0;

				while (minionData.next())
				{
					int raidId = minionData.getInt("boss_id");
					npcDat = _npcs.get(raidId);
					minionDat = new L2MinionData();
					minionDat.setMinionId(minionData.getInt("minion_id"));
					minionDat.setAmountMin(minionData.getInt("amount_min"));
					minionDat.setAmountMax(minionData.getInt("amount_max"));
					npcDat.addRaidData(minionDat);
					cnt++;
				}

				_log.config("NpcTable: Loaded " + cnt + " custom minions.");
			}
			catch (Exception e)
			{
				_log.severe("Error loading custom minion data: " + e);
			}
		}
	}

	private void fillNpcTable(ResultSet NpcData, boolean customData) throws Exception
	{
		int count = 0;
		while (NpcData.next())
		{
			StatsSet npcDat = new StatsSet();
			int id = NpcData.getInt("id");

			if (Config.ASSERT)
			{
				assert id < 1000000;
			}

			npcDat.set("npcId", id);
			npcDat.set("idTemplate", NpcData.getInt("idTemplate"));
			int level = NpcData.getInt("level");
			npcDat.set("level", level);
			npcDat.set("jClass", NpcData.getString("class"));

			npcDat.set("baseShldDef", 0);
			npcDat.set("baseShldRate", 0);
			npcDat.set("baseCritRate", 38);

			npcDat.set("name", NpcData.getString("name"));
			npcDat.set("serverSideName", NpcData.getBoolean("serverSideName"));
			// npcDat.set("name", "");
			npcDat.set("title", NpcData.getString("title"));
			npcDat.set("serverSideTitle", NpcData.getBoolean("serverSideTitle"));
			npcDat.set("collisionRadius", NpcData.getDouble("collision_radius"));
			npcDat.set("collisionHeight", NpcData.getDouble("collision_height"));
			npcDat.set("sex", NpcData.getString("sex"));
			npcDat.set("type", NpcData.getString("type"));
			npcDat.set("baseAtkRange", NpcData.getInt("attackrange"));
			npcDat.set("rewardExp", NpcData.getInt("exp"));
			npcDat.set("rewardSp", NpcData.getInt("sp"));
			npcDat.set("basePAtkSpd", NpcData.getInt("atkspd"));
			npcDat.set("baseMAtkSpd", NpcData.getInt("matkspd"));
			npcDat.set("aggroRange", NpcData.getInt("aggro"));
			npcDat.set("rhand", NpcData.getInt("rhand"));
			npcDat.set("lhand", NpcData.getInt("lhand"));
			npcDat.set("armor", NpcData.getInt("armor"));
			npcDat.set("baseWalkSpd", NpcData.getInt("walkspd"));
			npcDat.set("baseRunSpd", NpcData.getInt("runspd"));

			// constants, until we have stats in DB
			npcDat.set("baseSTR", NpcData.getInt("str"));
			npcDat.set("baseCON", NpcData.getInt("con"));
			npcDat.set("baseDEX", NpcData.getInt("dex"));
			npcDat.set("baseINT", NpcData.getInt("int"));
			npcDat.set("baseWIT", NpcData.getInt("wit"));
			npcDat.set("baseMEN", NpcData.getInt("men"));

			npcDat.set("baseHpMax", NpcData.getInt("hp"));
			npcDat.set("baseCpMax", 0);
			npcDat.set("baseMpMax", NpcData.getInt("mp"));
			npcDat.set("baseHpReg", NpcData.getFloat("hpreg") > 0 ? NpcData.getFloat("hpreg") : 1.5 + ((level - 1) / 10));
			npcDat.set("baseMpReg", NpcData.getFloat("mpreg") > 0 ? NpcData.getFloat("mpreg") : 0.9 + (0.3 * ((level - 1) / 10)));
			npcDat.set("basePAtk", NpcData.getInt("patk"));
			npcDat.set("basePDef", NpcData.getInt("pdef"));
			npcDat.set("baseMAtk", NpcData.getInt("matk"));
			npcDat.set("baseMDef", NpcData.getInt("mdef"));

			npcDat.set("factionId", NpcData.getString("faction_id"));
			npcDat.set("factionRange", NpcData.getInt("faction_range"));

			npcDat.set("isUndead", NpcData.getString("isUndead"));

			npcDat.set("absorbLevel", NpcData.getString("absorb_level"));
			npcDat.set("absorbType", NpcData.getString("absorb_type"));

			npcDat.set("ss", NpcData.getInt("ss"));
			npcDat.set("bss", NpcData.getInt("bss"));
			npcDat.set("ssRate", NpcData.getInt("ss_rate"));

			npcDat.set("AI", NpcData.getString("AI"));

			L2NpcTemplate template = new L2NpcTemplate(npcDat);
			template.addVulnerability(Stats.BOW_WPN_VULN, 1);
			template.addVulnerability(Stats.BLUNT_WPN_VULN, 1);
			template.addVulnerability(Stats.DAGGER_WPN_VULN, 1);

			L2NpcTemplate oldTemplate = getTemplate(id);
			if (oldTemplate != null)
			{
				// add quest events to the new template
				if (oldTemplate.questEvents != null)
				{
					template.questEvents = oldTemplate.questEvents;
				}
			}

			_npcs.put(id, template);
			count++;
		}

		if (count > 0)
		{
			if (!customData)
			{
				_log.config("NpcTable: (Re)Loaded " + count + " NPC template(s).");
			}
			else
			{
				_log.config("NpcTable: (Re)Loaded " + count + " custom NPC template(s).");
			}
		}
	}

	public void reloadNpc(int id)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			// save a copy of the old data
			L2NpcTemplate old = getTemplate(id);
			Map<Integer, L2Skill> skills = new HashMap<>();
			if (old.getSkills() != null)
			{
				skills.putAll(old.getSkills());
			}

			List<L2DropCategory> categories = new ArrayList<>();
			if (old.getDropData() != null)
			{
				categories.addAll(old.getDropData());
			}

			ClassId[] classIds = null;
			if (old.getTeachInfo() != null)
			{
				classIds = old.getTeachInfo().clone();
			}

			List<L2MinionData> minions = new ArrayList<>();
			if (old.getMinionData() != null)
			{
				minions.addAll(old.getMinionData());
			}

			// reload the NPC base data
			try (PreparedStatement st = con.prepareStatement(RESTORE_NPC + " FROM npc WHERE id=?"))
			{
				st.setInt(1, id);
				try (ResultSet rs = st.executeQuery())
				{
					fillNpcTable(rs, false);
				}
			}

			if (Config.CUSTOM_NPC_TABLE) // reload certain NPCs
			{
				try (PreparedStatement st = con.prepareStatement(RESTORE_NPC + " FROM custom_npc WHERE id=?"))
				{
					st.setInt(1, id);
					try (ResultSet rs = st.executeQuery())
					{
						fillNpcTable(rs, true);
					}
				}
			}

			// restore additional data from saved copy
			L2NpcTemplate created = getTemplate(id);

			// set race
			created.setRace(old.race);

			for (L2Skill skill : skills.values())
			{
				created.addSkill(skill);
			}

			if (classIds != null)
			{
				for (ClassId classId : classIds)
				{
					created.addTeachInfo(classId);
				}
			}

			for (L2MinionData minion : minions)
			{
				created.addRaidData(minion);
			}
		}
		catch (Exception e)
		{
			_log.warning("NPCTable: Could not reload data for NPC " + id + ": " + e);
		}
	}

	// just wrapper
	public void reloadAllNpc()
	{
		restoreNpcData();
	}

	public void saveNpc(StatsSet npc)
	{
		String query = "";

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			Map<String, Object> set = npc.getSet();

			String name = "";
			String values = "";

			for (Object obj : set.keySet())
			{
				name = (String) obj;

				if (name.equalsIgnoreCase("npcId"))
				{
					continue;
				}

				if (!values.isEmpty())
				{
					values += ", ";
				}

				values += name + " = '" + set.get(name) + "'";

			}

			int updated = 0;
			if (Config.CUSTOM_NPC_TABLE)
			{
				query = "UPDATE custom_npc SET " + values + " WHERE id = ?";
				try (PreparedStatement statement = con.prepareStatement(query))
				{
					statement.setInt(1, npc.getInteger("npcId"));
					updated = statement.executeUpdate();
				}
			}

			if (updated == 0)
			{
				query = "UPDATE npc SET " + values + " WHERE id = ?";
				try (PreparedStatement statement = con.prepareStatement(query))
				{
					statement.setInt(1, npc.getInteger("npcId"));
					statement.executeUpdate();
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("NPCTable: Could not store new NPC data in database: " + e);
		}
	}

	public void replaceTemplate(L2NpcTemplate npc)
	{
		_npcs.put(npc.npcId, npc);
	}

	public L2NpcTemplate getTemplate(int id)
	{
		return _npcs.get(id);
	}

	public L2NpcTemplate getTemplateByName(String name)
	{
		for (L2NpcTemplate npcTemplate : _npcs.values())
		{
			if (npcTemplate.name.equalsIgnoreCase(name))
			{
				return npcTemplate;
			}
		}

		return null;
	}

	public L2NpcTemplate[] getAllOfLevel(int lvl)
	{
		List<L2NpcTemplate> list = new ArrayList<>();

		for (L2NpcTemplate t : _npcs.values())
		{
			if (t.level == lvl)
			{
				list.add(t);
			}
		}

		return list.toArray(new L2NpcTemplate[list.size()]);
	}

	public L2NpcTemplate[] getAllMonstersOfLevel(int lvl)
	{
		List<L2NpcTemplate> list = new ArrayList<>();

		for (L2NpcTemplate t : _npcs.values())
		{
			if ((t.level == lvl) && "L2Monster".equals(t.type))
			{
				list.add(t);
			}
		}

		return list.toArray(new L2NpcTemplate[list.size()]);
	}

	public L2NpcTemplate[] getAllNpcStartingWith(String letter)
	{
		List<L2NpcTemplate> list = new ArrayList<>();

		for (L2NpcTemplate t : _npcs.values())
		{
			if (t.name.startsWith(letter) && "L2Npc".equals(t.type))
			{
				list.add(t);
			}
		}

		return list.toArray(new L2NpcTemplate[list.size()]);
	}
	
	private static class SingletonHolder
	{
		protected static final NpcTable _instance = new NpcTable();
	}
}