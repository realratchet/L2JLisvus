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
package net.sf.l2j.gameserver.skills;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.BufferTable;
import net.sf.l2j.gameserver.datatables.BufferTable.BuffInfo;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.skills.conditions.Condition;
import net.sf.l2j.gameserver.skills.conditions.ConditionElementSeed;
import net.sf.l2j.gameserver.skills.conditions.ConditionGameChance;
import net.sf.l2j.gameserver.skills.conditions.ConditionGameTime;
import net.sf.l2j.gameserver.skills.conditions.ConditionGameTime.CheckGameTime;
import net.sf.l2j.gameserver.skills.conditions.ConditionLogicAnd;
import net.sf.l2j.gameserver.skills.conditions.ConditionLogicNot;
import net.sf.l2j.gameserver.skills.conditions.ConditionLogicOr;
import net.sf.l2j.gameserver.skills.conditions.ConditionPlayerClassIdRestriction;
import net.sf.l2j.gameserver.skills.conditions.ConditionPlayerHp;
import net.sf.l2j.gameserver.skills.conditions.ConditionPlayerHpPercentage;
import net.sf.l2j.gameserver.skills.conditions.ConditionPlayerLevel;
import net.sf.l2j.gameserver.skills.conditions.ConditionPlayerPKCount;
import net.sf.l2j.gameserver.skills.conditions.ConditionPlayerRace;
import net.sf.l2j.gameserver.skills.conditions.ConditionPlayerState;
import net.sf.l2j.gameserver.skills.conditions.ConditionPlayerState.CheckPlayerState;
import net.sf.l2j.gameserver.skills.conditions.ConditionSkillStats;
import net.sf.l2j.gameserver.skills.conditions.ConditionTargetAggro;
import net.sf.l2j.gameserver.skills.conditions.ConditionTargetClassIdRestriction;
import net.sf.l2j.gameserver.skills.conditions.ConditionTargetLevel;
import net.sf.l2j.gameserver.skills.conditions.ConditionTargetRace;
import net.sf.l2j.gameserver.skills.conditions.ConditionTargetUsesWeaponKind;
import net.sf.l2j.gameserver.skills.conditions.ConditionUsingItemType;
import net.sf.l2j.gameserver.skills.conditions.ConditionUsingMinEnchantLevel;
import net.sf.l2j.gameserver.skills.conditions.ConditionUsingSkill;
import net.sf.l2j.gameserver.skills.conditions.ConditionWithSkill;
import net.sf.l2j.gameserver.skills.effects.EffectTemplate;
import net.sf.l2j.gameserver.skills.funcs.FuncTemplate;
import net.sf.l2j.gameserver.skills.funcs.Lambda;
import net.sf.l2j.gameserver.skills.funcs.LambdaCalc;
import net.sf.l2j.gameserver.skills.funcs.LambdaConst;
import net.sf.l2j.gameserver.skills.funcs.LambdaStats;
import net.sf.l2j.gameserver.templates.L2ArmorType;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2Weapon;
import net.sf.l2j.gameserver.templates.L2WeaponType;
import net.sf.l2j.gameserver.templates.StatsSet;

/**
 * @author mkizub
 */
abstract class DocumentBase
{
	protected static final Logger _log = Logger.getLogger(DocumentBase.class.getName());

	private final File _file;
	protected Map<String, String[]> _tables;

	protected DocumentBase(File pFile)
	{
		_file = pFile;
		_tables = new HashMap<>();
	}

	protected Document parse()
	{
		Document doc;
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			doc = factory.newDocumentBuilder().parse(_file);
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Error loading file " + _file, e);
			return null;
		}
		try
		{
			parseDocument(doc);
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Error in file " + _file, e);
			return null;
		}
		return doc;
	}

	protected abstract void parseDocument(Document doc);

	protected abstract StatsSet getStatsSet();

	protected abstract String getTableValue(String name);

	protected abstract String getTableValue(String name, int idx);

	protected void resetTable()
	{
		_tables = new HashMap<>();
	}

	protected void setTable(String name, String[] table)
	{
		_tables.put(name, table);
	}

	protected void parseTemplate(Node n, Object template)
	{
		Condition condition = null;
		n = n.getFirstChild();
		if (n == null)
		{
			return;
		}
		if ("cond".equalsIgnoreCase(n.getNodeName()))
		{
			condition = parseCondition(n.getFirstChild(), template);
			Node msg = n.getAttributes().getNamedItem("msg");
			Node msgId = n.getAttributes().getNamedItem("msgId");
			if (condition != null && msg != null)
			{
				condition.setMessage(msg.getNodeValue());
			}
			else if (condition != null && msgId != null)
			{
				condition.setMessageId(Integer.decode(getValue(msgId.getNodeValue(), null)));
				Node addName = n.getAttributes().getNamedItem("addName");
				if (addName != null && Integer.decode(getValue(msgId.getNodeValue(), null)) > 0)
				{
					condition.addName();
				}
			}

			n = n.getNextSibling();
		}
		for (; n != null; n = n.getNextSibling())
		{
			if ("add".equalsIgnoreCase(n.getNodeName()))
			{
				attachFunc(n, template, "Add", condition);
			}
			else if ("sub".equalsIgnoreCase(n.getNodeName()))
			{
				attachFunc(n, template, "Sub", condition);
			}
			else if ("mul".equalsIgnoreCase(n.getNodeName()))
			{
				attachFunc(n, template, "Mul", condition);
			}
			else if ("basemul".equalsIgnoreCase(n.getNodeName()))
			{
				attachFunc(n, template, "BaseMul", condition);
			}
			else if ("div".equalsIgnoreCase(n.getNodeName()))
			{
				attachFunc(n, template, "Div", condition);
			}
			else if ("set".equalsIgnoreCase(n.getNodeName()))
			{
				attachFunc(n, template, "Set", condition);
			}
			else if ("enchant".equalsIgnoreCase(n.getNodeName()))
			{
				attachFunc(n, template, "Enchant", condition);
			}
			else if ("skill".equalsIgnoreCase(n.getNodeName()))
			{
				attachSkill(n, template, condition);
			}
			else if ("effect".equalsIgnoreCase(n.getNodeName()))
			{
				if (template instanceof EffectTemplate)
				{
					throw new RuntimeException("Nested effects");
				}
				attachEffect(n, template, condition);
			}
		}
	}

	protected void attachFunc(Node n, Object template, String name, Condition attachCond)
	{
		Stats stat = Stats.valueOfXml(n.getAttributes().getNamedItem("stat").getNodeValue());
		String order = n.getAttributes().getNamedItem("order").getNodeValue();
		Lambda lambda = getLambda(n, template);
		int ord = Integer.decode(getValue(order, template));

		Condition applyCond = parseCondition(n.getFirstChild(), template);
		FuncTemplate ft = new FuncTemplate(attachCond, applyCond, name, stat, ord, lambda);
		if (template instanceof L2Item)
		{
			((L2Item) template).attach(ft);
		}
		else if (template instanceof L2Skill)
		{
			((L2Skill) template).attach(ft);
		}
		else if (template instanceof EffectTemplate)
		{
			((EffectTemplate) template).attach(ft);
		}
	}

	protected void attachLambdaFunc(Node n, Object template, LambdaCalc calc)
	{
		String name = n.getNodeName();
		StringBuilder sb = new StringBuilder(name);
		sb.setCharAt(0, Character.toUpperCase(name.charAt(0)));
		name = sb.toString();
		Lambda lambda = getLambda(n, template);
		FuncTemplate ft = new FuncTemplate(null, null, name, null, calc._funcs.length, lambda);
		calc.addFunc(ft.getFunc(new Env(), calc));
	}

	protected void attachEffect(Node n, Object template, Condition attachCond)
	{
		NamedNodeMap attrs = n.getAttributes();
		String name = attrs.getNamedItem("name").getNodeValue();
		
		int time = 1;
		int altTime1 = 0;
		int altTime2 = 0;
		int count = 1;
		
		if (attrs.getNamedItem("count") != null)
		{
			count = Integer.decode(getValue(attrs.getNamedItem("count").getNodeValue(), template));
			if (count < 1)
			{
				count = 1;
			}
		}

		if (attrs.getNamedItem("time") != null)
		{
			time = Integer.decode(getValue(attrs.getNamedItem("time").getNodeValue(), template));
			if (time < 1)
			{
				time = 1;
			}

			if (Config.ENABLE_MODIFY_SKILL_DURATION)
			{
				if (Config.SKILL_DURATION_LIST.containsKey(((L2Skill) template).getId()))
				{
					if ((((L2Skill) template).getLevel() >= 100) && (((L2Skill) template).getLevel() < 140))
					{
						time += Config.SKILL_DURATION_LIST.get(((L2Skill) template).getId());
					}
					else
					{
						time = Config.SKILL_DURATION_LIST.get(((L2Skill) template).getId());
					}

					if (Config.DEBUG)
					{
						_log.info("*** Skill " + ((L2Skill) template).getName() + " (" + ((L2Skill) template).getLevel() + ") changed duration to " + time + " seconds.");
					}
				}
			}
			
			// Set custom time if AIO or NPC buffer is enabled
			if (Config.AIO_BUFFER_ENABLED || Config.NPC_BUFFER_ENABLED)
			{
				Map<Integer, BuffInfo> aioBuffs = BufferTable.getInstance().getAIOBuffs();
				Map<Integer, BuffInfo> npcBuffs = BufferTable.getInstance().getNPCBuffs();
				
				if (aioBuffs.containsKey(((L2Skill) template).getId()))
				{
					altTime1 = aioBuffs.get(((L2Skill) template).getId()).getDuration();
				}
				
				if (npcBuffs.containsKey(((L2Skill) template).getId()))
				{
					altTime2 = npcBuffs.get(((L2Skill) template).getId()).getDuration();
				}
			}
		}

		boolean self = false;
		if (attrs.getNamedItem("self") != null)
		{
			if (Integer.decode(getValue(attrs.getNamedItem("self").getNodeValue(), template)) == 1)
			{
				self = true;
			}
		}

		boolean icon = true;
		if (attrs.getNamedItem("noicon") != null)
		{
			if (Integer.decode(getValue(attrs.getNamedItem("noicon").getNodeValue(), template)) == 1)
			{
				icon = false;
			}
		}

		Lambda lambda = getLambda(n, template);
		Condition applayCond = parseCondition(n.getFirstChild(), template);
		short abnormal = 0;
		if (attrs.getNamedItem("abnormal") != null)
		{
			String abn = attrs.getNamedItem("abnormal").getNodeValue();
			if (abn.equals("poison"))
			{
				abnormal = L2Character.ABNORMAL_EFFECT_POISON;
			}
			if (abn.equals("bleeding"))
			{
				abnormal = L2Character.ABNORMAL_EFFECT_BLEEDING;
			}
			if (abn.equals("flame"))
			{
				abnormal = L2Character.ABNORMAL_EFFECT_FLAME;
			}
			if (abn.equals("bighead"))
			{
				abnormal = L2Character.ABNORMAL_EFFECT_BIG_HEAD;
			}
		}
		float stackOrder = 0;
		String stackType = "none";
		if (attrs.getNamedItem("stackType") != null)
		{
			stackType = attrs.getNamedItem("stackType").getNodeValue();
		}
		if (attrs.getNamedItem("stackOrder") != null)
		{
			stackOrder = Float.parseFloat(getValue(attrs.getNamedItem("stackOrder").getNodeValue(), template));
		}
		EffectTemplate lt = new EffectTemplate(attachCond, applayCond, name, lambda, count, time, altTime1, altTime2, abnormal, stackType, stackOrder, icon);
		parseTemplate(n, lt);
		if (template instanceof L2Item)
		{
			((L2Item) template).attach(lt);
		}
		else if ((template instanceof L2Skill) && !self)
		{
			((L2Skill) template).attach(lt);
		}
		else if ((template instanceof L2Skill) && self)
		{
			((L2Skill) template).attachSelf(lt);
		}
	}

	protected void attachSkill(Node n, Object template, Condition attachCond)
	{
		NamedNodeMap attrs = n.getAttributes();
		int id = 0, lvl = 1;
		if (attrs.getNamedItem("id") != null)
		{
			id = Integer.decode(getValue(attrs.getNamedItem("id").getNodeValue(), template));
		}
		if (attrs.getNamedItem("lvl") != null)
		{
			lvl = Integer.decode(getValue(attrs.getNamedItem("lvl").getNodeValue(), template));
		}

		L2Skill skill = SkillTable.getInstance().getInfo(id, lvl);
		if (attrs.getNamedItem("chance") != null)
		{
			if ((template instanceof L2Weapon) || (template instanceof L2Item))
			{
				skill.attach(new ConditionGameChance(Integer.decode(getValue(attrs.getNamedItem("chance").getNodeValue(), template))), true);
			}
			else
			{
				skill.attach(new ConditionGameChance(Integer.decode(getValue(attrs.getNamedItem("chance").getNodeValue(), template))), false);
			}
		}
		if (template instanceof L2Weapon)
		{
			if ((attrs.getNamedItem("onUse") != null) || ((attrs.getNamedItem("onCrit") == null) && (attrs.getNamedItem("onCast") == null)))
			{
				((L2Weapon) template).attach(skill); // Attach as skill triggered on use
			}
			if (attrs.getNamedItem("onCrit") != null)
			{
				((L2Weapon) template).attachOnCrit(skill); // Attach as skill triggered on critical hit
			}
			if (attrs.getNamedItem("onCast") != null)
			{
				((L2Weapon) template).attachOnCast(skill); // Attach as skill triggered on cast
			}
		}
		else if (template instanceof L2Item)
		{
			((L2Item) template).attach(skill); // Attach as skill triggered on use
		}
	}

	protected Condition parseCondition(Node n, Object template)
	{
		while ((n != null) && (n.getNodeType() != Node.ELEMENT_NODE))
		{
			n = n.getNextSibling();
		}
		if (n == null)
		{
			return null;
		}
		if ("and".equalsIgnoreCase(n.getNodeName()))
		{
			return parseLogicAnd(n, template);
		}
		if ("or".equalsIgnoreCase(n.getNodeName()))
		{
			return parseLogicOr(n, template);
		}
		if ("not".equalsIgnoreCase(n.getNodeName()))
		{
			return parseLogicNot(n, template);
		}
		if ("player".equalsIgnoreCase(n.getNodeName()))
		{
			return parsePlayerCondition(n);
		}
		if ("target".equalsIgnoreCase(n.getNodeName()))
		{
			return parseTargetCondition(n, template);
		}
		if ("skill".equalsIgnoreCase(n.getNodeName()))
		{
			return parseSkillCondition(n);
		}
		if ("using".equalsIgnoreCase(n.getNodeName()))
		{
			return parseUsingCondition(n);
		}
		if ("game".equalsIgnoreCase(n.getNodeName()))
		{
			return parseGameCondition(n);
		}
		return null;
	}

	protected Condition parseLogicAnd(Node n, Object template)
	{
		ConditionLogicAnd cond = new ConditionLogicAnd();
		for (n = n.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if (n.getNodeType() == Node.ELEMENT_NODE)
			{
				cond.add(parseCondition(n, template));
			}
		}
		if ((cond._conditions == null) || (cond._conditions.length == 0))
		{
			_log.severe("Empty <and> condition in " + _file);
		}
		return cond;
	}

	protected Condition parseLogicOr(Node n, Object template)
	{
		ConditionLogicOr cond = new ConditionLogicOr();
		for (n = n.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if (n.getNodeType() == Node.ELEMENT_NODE)
			{
				cond.add(parseCondition(n, template));
			}
		}
		if ((cond._conditions == null) || (cond._conditions.length == 0))
		{
			_log.severe("Empty <or> condition in " + _file);
		}
		return cond;
	}

	protected Condition parseLogicNot(Node n, Object template)
	{
		for (n = n.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if (n.getNodeType() == Node.ELEMENT_NODE)
			{
				return new ConditionLogicNot(parseCondition(n, template));
			}
		}
		_log.severe("Empty <not> condition in " + _file);
		return null;
	}

	protected Condition parsePlayerCondition(Node n)
	{
		Condition cond = null;
		int[] ElementSeeds = new int[5];
		NamedNodeMap attrs = n.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++)
		{
			Node a = attrs.item(i);
			if ("races".equalsIgnoreCase(a.getNodeName()))
			{
				final String[] raceValues = a.getNodeValue().split(",");
				final Race[] races = new Race[raceValues.length];
				for (int r = 0; r < raceValues.length; r++)
				{
					if (raceValues[i] != null)
					{
						races[i] = Race.valueOf(raceValues[i]);
					}
				}
				cond = joinAnd(cond, new ConditionPlayerRace(races));
			}
			else if ("level".equalsIgnoreCase(a.getNodeName()))
			{
				int lvl = Integer.decode(getValue(a.getNodeValue(), null));
				cond = joinAnd(cond, new ConditionPlayerLevel(lvl));
			}
			else if ("class_id_restriction".equalsIgnoreCase(a.getNodeName()))
			{
				StringTokenizer st = new StringTokenizer(a.getNodeValue(), ",");
				List<Integer> list = new ArrayList<>(st.countTokens());
				while (st.hasMoreTokens())
				{
					String item = st.nextToken().trim();
					list.add(Integer.decode(getValue(item, null)));
				}
				cond = joinAnd(cond, new ConditionPlayerClassIdRestriction(list));
			}
			else if ("resting".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.valueOf(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerState(CheckPlayerState.RESTING, val));
			}
			else if ("flying".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.valueOf(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerState(CheckPlayerState.FLYING, val));
			}
			else if ("moving".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.valueOf(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerState(CheckPlayerState.MOVING, val));
			}
			else if ("running".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.valueOf(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerState(CheckPlayerState.RUNNING, val));
			}
			else if ("behind".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.valueOf(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerState(CheckPlayerState.BEHIND, val));
			}
			else if ("front".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.valueOf(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerState(CheckPlayerState.FRONT, val));
			}
			else if ("chaotic".equalsIgnoreCase(a.getNodeName()))
            {
                boolean val = Boolean.valueOf(a.getNodeValue());
                cond = joinAnd(cond, new ConditionPlayerState(CheckPlayerState.CHAOTIC, val));
            }
            else if ("olympiad".equalsIgnoreCase(a.getNodeName()))
            {
                boolean val = Boolean.valueOf(a.getNodeValue());
                cond = joinAnd(cond, new ConditionPlayerState(CheckPlayerState.OLYMPIAD, val));
            }
            else if ("clanLeader".equalsIgnoreCase(a.getNodeName()))
            {
                boolean val = Boolean.valueOf(a.getNodeValue());
                cond = joinAnd(cond, new ConditionPlayerState(CheckPlayerState.CLAN_LEADER, val));
            }
            else if ("siegeAttacker".equalsIgnoreCase(a.getNodeName()))
            {
                boolean val = Boolean.valueOf(a.getNodeValue());
                cond = joinAnd(cond, new ConditionPlayerState(CheckPlayerState.SIEGE_ATTACKER, val));
            }
            else if ("siegeDefender".equalsIgnoreCase(a.getNodeName()))
            {
                boolean val = Boolean.valueOf(a.getNodeValue());
                cond = joinAnd(cond, new ConditionPlayerState(CheckPlayerState.SIEGE_DEFENDER, val));
            }
			else if ("hp".equalsIgnoreCase(a.getNodeName()))
			{
				int hp = Integer.decode(getValue(a.getNodeValue(), null));
				cond = joinAnd(cond, new ConditionPlayerHp(hp));
			}
			else if ("hprate".equalsIgnoreCase(a.getNodeName()))
			{
				double rate = Double.parseDouble(getValue(a.getNodeValue(), null));
				cond = joinAnd(cond, new ConditionPlayerHpPercentage(rate));
			}
			else if ("pkCount".equalsIgnoreCase(a.getNodeName()))
            {
            	int pkCount = Integer.decode(getValue(a.getNodeValue(), null));
            	cond = joinAnd(cond, new ConditionPlayerPKCount(pkCount));
            }
			else if ("seed_fire".equalsIgnoreCase(a.getNodeName()))
			{
				ElementSeeds[0] = Integer.decode(getValue(a.getNodeValue(), null));
			}
			else if ("seed_water".equalsIgnoreCase(a.getNodeName()))
			{
				ElementSeeds[1] = Integer.decode(getValue(a.getNodeValue(), null));
			}
			else if ("seed_wind".equalsIgnoreCase(a.getNodeName()))
			{
				ElementSeeds[2] = Integer.decode(getValue(a.getNodeValue(), null));
			}
			else if ("seed_various".equalsIgnoreCase(a.getNodeName()))
			{
				ElementSeeds[3] = Integer.decode(getValue(a.getNodeValue(), null));
			}
			else if ("seed_any".equalsIgnoreCase(a.getNodeName()))
			{
				ElementSeeds[4] = Integer.decode(getValue(a.getNodeValue(), null));
			}
		}

		// Elemental seed condition processing
		for (int elementSeed : ElementSeeds)
		{
			if (elementSeed > 0)
			{
				cond = joinAnd(cond, new ConditionElementSeed(ElementSeeds));
				break;
			}
		}

		if (cond == null)
		{
			_log.severe("Unrecognized <player> condition in " + _file);
		}
		return cond;
	}

	protected Condition parseTargetCondition(Node n, Object template)
	{
		Condition cond = null;
		NamedNodeMap attrs = n.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++)
		{
			Node a = attrs.item(i);
			if ("aggro".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.valueOf(a.getNodeValue());
				cond = joinAnd(cond, new ConditionTargetAggro(val));
			}
			else if ("level".equalsIgnoreCase(a.getNodeName()))
			{
				int lvl = Integer.decode(getValue(a.getNodeValue(), template));
				cond = joinAnd(cond, new ConditionTargetLevel(lvl));
			}
			else if ("class_id_restriction".equalsIgnoreCase(a.getNodeName()))
			{
				StringTokenizer st = new StringTokenizer(a.getNodeValue(), ",");
				List<Integer> list = new ArrayList<>(st.countTokens());
				while (st.hasMoreTokens())
				{
					String item = st.nextToken().trim();
					list.add(Integer.decode(getValue(item, null)));
				}
				cond = joinAnd(cond, new ConditionTargetClassIdRestriction(list));
			}
			else if ("races".equalsIgnoreCase(a.getNodeName()))
			{
				final String[] raceValues = a.getNodeValue().split(",");
				final Race[] races = new Race[raceValues.length];
				for (int r = 0; r < raceValues.length; r++)
				{
					if (raceValues[r] != null)
					{
						races[r] = Race.valueOf(raceValues[r]);
					}
				}
				cond = joinAnd(cond, new ConditionTargetRace(races));
			}
			else if ("using".equalsIgnoreCase(a.getNodeName()))
			{
				int mask = 0;
				StringTokenizer st = new StringTokenizer(a.getNodeValue(), ",");
				while (st.hasMoreTokens())
				{
					String item = st.nextToken().trim();
					for (L2WeaponType wt : L2WeaponType.values())
					{
						if (wt.toString().equals(item))
						{
							mask |= wt.mask();
							break;
						}
					}
					for (L2ArmorType at : L2ArmorType.values())
					{
						if (at.toString().equals(item))
						{
							mask |= at.mask();
							break;
						}
					}
				}
				cond = joinAnd(cond, new ConditionTargetUsesWeaponKind(mask));
			}
		}
		if (cond == null)
		{
			_log.severe("Unrecognized <target> condition in " + _file);
		}
		return cond;
	}

	protected Condition parseSkillCondition(Node n)
	{
		NamedNodeMap attrs = n.getAttributes();
		Stats stat = Stats.valueOfXml(attrs.getNamedItem("stat").getNodeValue());
		return new ConditionSkillStats(stat);
	}

	protected Condition parseUsingCondition(Node n)
	{
		Condition cond = null;
		NamedNodeMap attrs = n.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++)
		{
			Node a = attrs.item(i);
			if ("kind".equalsIgnoreCase(a.getNodeName()))
			{
				int mask = 0;
				StringTokenizer st = new StringTokenizer(a.getNodeValue(), ",");
				while (st.hasMoreTokens())
				{
					String item = st.nextToken().trim();
					for (L2WeaponType wt : L2WeaponType.values())
					{
						if (wt.toString().equals(item))
						{
							mask |= wt.mask();
							break;
						}
					}
					for (L2ArmorType at : L2ArmorType.values())
					{
						if (at.toString().equals(item))
						{
							mask |= at.mask();
							break;
						}
					}
				}
				cond = joinAnd(cond, new ConditionUsingItemType(mask));
			}
			else if ("skill".equalsIgnoreCase(a.getNodeName()))
			{
				int id = Integer.parseInt(a.getNodeValue());
				cond = joinAnd(cond, new ConditionUsingSkill(id));
			}
			else if ("minEnchantLevel".equalsIgnoreCase(a.getNodeName()))
			{
				int enchantLevel = Integer.parseInt(a.getNodeValue());
				cond = joinAnd(cond, new ConditionUsingMinEnchantLevel(enchantLevel));
			}
		}
		if (cond == null)
		{
			_log.severe("Unrecognized <using> condition in " + _file);
		}
		return cond;
	}

	protected Condition parseGameCondition(Node n)
	{
		Condition cond = null;
		NamedNodeMap attrs = n.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++)
		{
			Node a = attrs.item(i);
			if ("skill".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.valueOf(a.getNodeValue());
				cond = joinAnd(cond, new ConditionWithSkill(val));
			}
			if ("night".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.valueOf(a.getNodeValue());
				cond = joinAnd(cond, new ConditionGameTime(CheckGameTime.NIGHT, val));
			}
			if ("chance".equalsIgnoreCase(a.getNodeName()))
			{
				int val = Integer.decode(getValue(a.getNodeValue(), null));
				cond = joinAnd(cond, new ConditionGameChance(val));
			}
		}
		if (cond == null)
		{
			_log.severe("Unrecognized <game> condition in " + _file);
		}
		return cond;
	}

	protected void parseTable(Node n)
	{
		NamedNodeMap attrs = n.getAttributes();
		String name = attrs.getNamedItem("name").getNodeValue();
		if (name.charAt(0) != '#')
		{
			throw new IllegalArgumentException("Table name must start with #");
		}
		StringTokenizer data = new StringTokenizer(n.getFirstChild().getNodeValue());
		List<String> array = new ArrayList<>();
		while (data.hasMoreTokens())
		{
			array.add(data.nextToken());
		}
		String[] res = new String[array.size()];
		int i = 0;
		for (String str : array)
		{
			res[i++] = str;
		}
		setTable(name, res);
	}

	protected void parseBeanSet(Node n, StatsSet set, Integer level)
	{
		String name = n.getAttributes().getNamedItem("name").getNodeValue().trim();
		String value = n.getAttributes().getNamedItem("val").getNodeValue().trim();
		char ch = value.length() == 0 ? ' ' : value.charAt(0);
		if ((ch == '#') || (ch == '-') || Character.isDigit(ch))
		{
			set.set(name, String.valueOf(getValue(value, level)));
		}
		else
		{
			set.set(name, value);
		}
	}

	protected Lambda getLambda(Node n, Object template)
	{
		Node nval = n.getAttributes().getNamedItem("val");
		if (nval != null)
		{
			String val = nval.getNodeValue();
			if (val.charAt(0) == '#')
			{ // table by level
				return new LambdaConst(Double.parseDouble(getTableValue(val)));
			}
			else if (val.charAt(0) == '$')
			{
				if (val.equalsIgnoreCase("$player_level"))
				{
					return new LambdaStats(LambdaStats.StatsType.PLAYER_LEVEL);
				}
				if (val.equalsIgnoreCase("$target_level"))
				{
					return new LambdaStats(LambdaStats.StatsType.TARGET_LEVEL);
				}
				if (val.equalsIgnoreCase("$player_max_hp"))
				{
					return new LambdaStats(LambdaStats.StatsType.PLAYER_MAX_HP);
				}
				if (val.equalsIgnoreCase("$player_max_mp"))
				{
					return new LambdaStats(LambdaStats.StatsType.PLAYER_MAX_MP);
				}
				// try to find value out of item fields
				StatsSet set = getStatsSet();
				String field = set.getString(val.substring(1));
				if (field != null)
				{
					return new LambdaConst(Double.parseDouble(getValue(field, template)));
				}
				// failed
				throw new IllegalArgumentException("Unknown value " + val);
			}
			else
			{
				return new LambdaConst(Double.parseDouble(val));
			}
		}
		LambdaCalc calc = new LambdaCalc();
		n = n.getFirstChild();
		while ((n != null) && (n.getNodeType() != Node.ELEMENT_NODE))
		{
			n = n.getNextSibling();
		}
		if ((n == null) || !"val".equals(n.getNodeName()))
		{
			throw new IllegalArgumentException("Value not specified");
		}

		for (n = n.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if (n.getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}
			attachLambdaFunc(n, template, calc);
		}
		return calc;
	}

	protected String getValue(String value, Object template)
	{
		// is it a table?
		if (value.charAt(0) == '#')
		{
			if (template instanceof L2Skill)
			{
				return getTableValue(value);
			}
			else if (template instanceof Integer)
			{
				return getTableValue(value, ((Integer) template).intValue());
			}
			else
			{
				throw new IllegalStateException();
			}
		}
		return value;
	}

	protected Condition joinAnd(Condition cond, Condition c)
	{
		if (cond == null)
		{
			return c;
		}
		if (cond instanceof ConditionLogicAnd)
		{
			((ConditionLogicAnd) cond).add(c);
			return cond;
		}
		ConditionLogicAnd and = new ConditionLogicAnd();
		and.add(cond);
		and.add(c);
		return and;
	}
}