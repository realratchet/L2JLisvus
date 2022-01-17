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
package net.sf.l2j.gameserver.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2DropCategory;
import net.sf.l2j.gameserver.model.L2DropData;
import net.sf.l2j.gameserver.model.L2MinionData;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.skills.Stats;

/**
 * This class contains all generic data of a template for an L2Spawn object.<BR>
 * <BR>
 * <B><U> Data</U> :</B><BR>
 * <BR>
 * <li>npcId, type, name, sex</li> 
 * <li>rewardExp, rewardSp</li> 
 * <li>aggroRange, factionId, factionRange</li> 
 * <li>rhand, lhand, armor</li> 
 * <li>isUndead</li> 
 * <li>_drops</li> 
 * <li>_minions</li> 
 * <li>_teachInfo</li> 
 * <li>_skills</li> 
 * <li>_questsStart</li><BR>
 * <BR>
 * @version $Revision: 1.1.2.4 $ $Date: 2005/04/02 15:57:51 $
 */
public final class L2NpcTemplate extends L2CharTemplate
{
	protected static Logger _log = Logger.getLogger(L2NpcTemplate.class.getName());
	
	public final int npcId;
	public final int idTemplate;
	public final String type;
	public final String name;
	public final boolean serverSideName;
	public final String title;
	public final boolean serverSideTitle;
	public final String sex;
	public final byte level;
	public final int rewardExp;
	public final int rewardSp;
	public final int aggroRange;
	public final int rhand;
	public final int lhand;
	public final int armor;
	public final String factionId;
	public final int factionRange;
	public final int absorbLevel;
	public final AbsorbCrystalType absorbType;
	public final short ss;
	public final short bss;
	public final short ssRate;
	
	public boolean isQuestMonster = false;
	
	public int race;
	public final String jClass;
	public final AIType AI;
	
	public static enum AbsorbCrystalType
	{
		LAST_HIT,
		FULL_PARTY,
		PARTY_ONE_RANDOM
	}
	
	public static enum AIType
	{
		FIGHTER,
		ARCHER,
		BALANCED,
		MAGE,
		HEALER,
		CORPSE
	}
	
	/** The table containing all Item that can be dropped by L2NpcInstance using this L2NpcTemplate */
	private LinkedList<L2DropCategory> _categories = null;

	/** The table containing all Minions that must be spawn with the L2NpcInstance using this L2NpcTemplate */
	private List<L2MinionData> _minions = null;
	
	private List<ClassId> _teachInfo;
	private Map<Integer, L2Skill> _skills;
	private Map<Stats, Double> _vulnerabilities;
	// contains a list of quests for each event type (questStart, questAttack, questKill, etc)
	public Map<Quest.QuestEventType, Quest[]> questEvents;
	
	/**
	 * Constructor of L2Character.<BR>
	 * <BR>
	 * @param set The StatsSet object to transfer data to the method
	 */
	public L2NpcTemplate(StatsSet set)
	{
		super(set);
		npcId = set.getInteger("npcId");
		idTemplate = set.getInteger("idTemplate");
		type = set.getString("type");
		name = set.getString("name");
		serverSideName = set.getBool("serverSideName");
		title = set.getString("title");
		isQuestMonster = title.equalsIgnoreCase("Quest Monster");
		serverSideTitle = set.getBool("serverSideTitle");
		sex = set.getString("sex");
		level = set.getByte("level");
		rewardExp = set.getInteger("rewardExp");
		rewardSp = set.getInteger("rewardSp");
		aggroRange = set.getInteger("aggroRange");
		rhand = set.getInteger("rhand");
		lhand = set.getInteger("lhand");
		armor = set.getInteger("armor");
		
		String f = set.getString("factionId", null);
		factionId = f != null ? f.intern() : null;
		
		factionRange = set.getInteger("factionRange");
		absorbLevel = set.getInteger("absorbLevel", 0);
		absorbType = AbsorbCrystalType.valueOf(set.getString("absorbType"));
		ss = (short)set.getInteger("ss", 0);
		bss  = (short)set.getInteger("bss", 0);
		ssRate  = (short)set.getInteger("ssRate", 0);
		race = 0;
		jClass = set.getString("jClass");
		AI = AIType.valueOf(set.getString("AI"));
		_teachInfo = null;
	}

	public void addTeachInfo(ClassId classId)
	{
		if (_teachInfo == null)
		{
			_teachInfo = new ArrayList<>();
		}
		_teachInfo.add(classId);
	}

	public ClassId[] getTeachInfo()
	{
		if (_teachInfo == null)
		{
			return null;
		}
		return _teachInfo.toArray(new ClassId[_teachInfo.size()]);
	}

	public boolean canTeach(ClassId classId)
	{
		if (_teachInfo == null)
		{
			return false;
		}

		// If the player is on a third class, fetch the class teacher
		// information for its parent class.
		if (classId.level() == 3)
		{
			return _teachInfo.contains(classId.getParent());
		}

		return _teachInfo.contains(classId);
	}

	// add a drop to a given category. If the category does not exist, create it.
	public void addDropData(L2DropData drop, int categoryType)
	{
		if (!drop.isQuestDrop())
		{
			// if the category doesn't already exist, create it first
			if (_categories == null)
			{
				_categories = new LinkedList<>();
			}
			
			synchronized (_categories)
			{
				boolean catExists = false;
				for (L2DropCategory cat : _categories)
				{
					// if the category exists, add the drop to this category.
					if (cat.getCategoryType() == categoryType)
					{
						cat.addDropData(drop, type.equalsIgnoreCase("L2RaidBoss") || type.equalsIgnoreCase("L2GrandBoss"));
						catExists = true;
						break;
					}
				}
				// if the category doesn't exit, create it and add the drop
				if (!catExists)
				{
					L2DropCategory cat = new L2DropCategory(categoryType);
					cat.addDropData(drop, type.equalsIgnoreCase("L2RaidBoss") || type.equalsIgnoreCase("L2GrandBoss"));
					_categories.add(cat);
				}
			}
		}
	}

	public void addRaidData(L2MinionData minion)
	{
		if (_minions == null)
		{
			_minions = new ArrayList<>();
		}
		_minions.add(minion);
	}

	public void addSkill(L2Skill skill)
	{
		if (_skills == null)
		{
			_skills = new ConcurrentHashMap<>();
		}
		_skills.put(skill.getId(), skill);
	}

	public void addVulnerability(Stats id, double vuln)
	{
		if (_vulnerabilities == null)
		{
			_vulnerabilities = new HashMap<>();
		}
		_vulnerabilities.put(id, Double.valueOf(vuln));
	}

	public double getVulnerability(Stats id)
	{
		if (_vulnerabilities == null || !_vulnerabilities.containsKey(id))
		{
			return 1;
		}
		return _vulnerabilities.get(id);
	}

	public double removeVulnerability(Stats id)
	{
		return _vulnerabilities.remove(id);
	}

	public List<L2DropCategory> getDropData()
	{
		return _categories;
	}

	/**
	 * Return the list of all possible item drops of this L2NpcTemplate.<BR>
	 * (ie full drops and part drops, mats, miscellaneous & UNCATEGORIZED)<BR>
	 * <BR>
	 * @return
	 */
	public List<L2DropData> getAllDropData()
	{
		if (_categories == null)
		{
			return null;
		}
		
		List<L2DropData> lst = new ArrayList<>();
		for (L2DropCategory tmp : _categories)
		{
			lst.addAll(tmp.getAllDrops());
		}
		return lst;
	}

	/**
	 * Empty all possible drops of this L2NpcTemplate.<BR>
	 * <BR>
	 */
	public synchronized void clearAllDropData()
	{
		if (_categories == null)
		{
			return;
		}
		
		while (!_categories.isEmpty())
		{
			_categories.getFirst().clearAllDrops();
			_categories.removeFirst();
		}
		_categories.clear();
	}

	/**
	 * Return the list of all Minions that must be spawn with the L2NpcInstance using this L2NpcTemplate.<BR>
	 * <BR>
	 * @return
	 */
	public List<L2MinionData> getMinionData()
	{
		return _minions;
	}

	@Override
	public Map<Integer, L2Skill> getSkills()
	{
		return _skills;
	}

	public void addQuestEvent(Quest.QuestEventType EventType, Quest q)
	{
		if (questEvents == null)
		{
			questEvents = new HashMap<>();
		}

		if (questEvents.get(EventType) == null)
		{
			questEvents.put(EventType, new Quest[]{q});
		}
		else
		{
			Quest[] _quests = questEvents.get(EventType);
			int len = _quests.length;

			// if only one registration per npc is allowed for this event type
			// then only register this NPC if not already registered for the specified event.
			// if a quest allows multiple registrations, then register regardless of count
			// In all cases, check if this new registration is replacing an older copy of the SAME quest
			if (!EventType.isMultipleRegistrationAllowed())
			{
				if (_quests[0].getName().equals(q.getName()))
				{
					_quests[0] = q;
				}
				else
				{
					_log.warning("Quest event not allowed in multiple quests. Skipped addition of Event Type '" + EventType + "' for NPC '" + name + "' and quest '" + q.getName() + "'.");
				}
			}
			else
			{
				// be ready to add a new quest to a new copy of the list, with larger size than previously.
				Quest[] tmp = new Quest[len + 1];
				// loop through the existing quests and copy them to the new list.  While doing so, also
				// check if this new quest happens to be just a replacement for a previously loaded quest.
				// If so, just save the updated reference and do NOT use the new list. Else, add the new
				// quest to the end of the new list
				for (int i = 0; i < len; i++)
				{
					if (_quests[i].getName().equals(q.getName()))
					{
						_quests[i] = q;
						return;
					}
					tmp[i] = _quests[i];
				}
				tmp[len] = q;
				questEvents.put(EventType, tmp);
			}
		}
	}

	public Quest[] getEventQuests(Quest.QuestEventType EventType)
	{
		if (questEvents == null)
		{
			return null;
		}

		return questEvents.get(EventType);
	}
	
	public void setRace(int newrace)
	{
		race = newrace;
	}
}