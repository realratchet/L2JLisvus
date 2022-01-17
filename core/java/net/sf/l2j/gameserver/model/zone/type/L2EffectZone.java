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
package net.sf.l2j.gameserver.model.zone.type;

import java.util.concurrent.Future;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.network.serverpackets.EtcStatusUpdate;
import net.sf.l2j.util.Rnd;
import net.sf.l2j.util.StringUtil;

/**
 * another type of damage zone with skills
 * @author kerberos
 */
public class L2EffectZone extends L2ZoneType
{
	private int _chance;
	private int _initialDelay;
	private int _reuse;
	private boolean _enabled;
	private String _target;
	private Future<?> _task;

	boolean _bypassConditions;
	int[][] _skills;

	public L2EffectZone(int id)
	{
		super(id);
		_chance = 100;
		_initialDelay = 0;
		_reuse = 30000;
		_enabled = true;
		_target = "pc";
		_bypassConditions = false;
	}

	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("chance"))
		{
			_chance = Integer.parseInt(value);
		}
		else if (name.equals("initialDelay"))
		{
			_initialDelay = Integer.parseInt(value);
		}
		else if (name.equals("default_enabled"))
		{
			_enabled = Boolean.parseBoolean(value);
		}
		else if (name.equals("target"))
		{
			_target = String.valueOf(value);
		}
		else if (name.equals("reuse"))
		{
			_reuse = Integer.parseInt(value);
		}
		else if (name.equals("bypassSkillConditions"))
		{
			_bypassConditions = Boolean.parseBoolean(value);
		}
		else if (name.equals("skillIdLvl"))
		{
			String[] propertySplit = value.split(";");
			_skills = new int[propertySplit.length][2];
			
			for (int i = 0; i < propertySplit.length; i++)
			{
				String skill = propertySplit[i];
				String[] skillSplit = skill.split("-");
				if (skillSplit.length != 2)
				{
					_log.warning(StringUtil.concat("[L2EffectZone]: invalid config property -> skillsIdLvl \"", skill, "\""));
				}
				else
				{
					try
					{
						_skills[i][0] = Integer.parseInt(skillSplit[0]);
						_skills[i][1] = Integer.parseInt(skillSplit[1]);
					}
					catch (NumberFormatException nfe)
					{
						if (!skill.isEmpty())
						{
							_log.warning(StringUtil.concat("[L2EffectZone]: invalid config property -> skillsIdLvl \"", skillSplit[0], "\"", skillSplit[1]));
						}
					}
				}
			}
		}
		else
		{
			super.setParameter(name, value);
		}
	}

	@Override
	protected void onEnter(L2Character character)
	{
		if (((character instanceof L2PlayableInstance) && _target.equalsIgnoreCase("pc")) || ((character instanceof L2PcInstance) && _target.equalsIgnoreCase("pc_only")) || ((character instanceof L2MonsterInstance) && _target.equalsIgnoreCase("npc")))
		{
			if (_task == null)
			{
				synchronized (this)
				{
					if ((_task == null) && (_skills != null))
					{
						_task = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new ApplySkill(this), _initialDelay, _reuse);
					}
				}
			}
		}
		if (character instanceof L2PcInstance)
		{
			character.setInsideZone(L2Character.ZONE_DANGER_AREA, true);
			character.sendPacket(new EtcStatusUpdate((L2PcInstance) character));
		}
	}

	@Override
	protected void onExit(L2Character character)
	{
		if (_characterList.isEmpty() && (_task != null))
		{
			_task.cancel(true);
			_task = null;
		}
		if (character instanceof L2PcInstance)
		{
			character.setInsideZone(L2Character.ZONE_DANGER_AREA, false);
			if (!character.isInsideZone(L2Character.ZONE_DANGER_AREA))
			{
				character.sendPacket(new EtcStatusUpdate((L2PcInstance) character));
			}
		}
	}
	
	public String getTargetType()
	{
		return _target;
	}

	public boolean isEnabled()
	{
		return _enabled;
	}

	public int getChance()
	{
		return _chance;
	}

	public void setZoneEnabled(boolean val)
	{
		_enabled = val;
	}

	private class ApplySkill implements Runnable
	{
		private final L2EffectZone _poisonZone;

		ApplySkill(L2EffectZone zone)
		{
			_poisonZone = zone;
		}

		@Override
		public void run()
		{
			if (isEnabled())
			{
				for (L2Character temp : _poisonZone.getCharacterList())
				{
					if ((temp != null) && !temp.isDead())
					{
						if (Rnd.get(100) < getChance() && (temp instanceof L2PcInstance && getTargetType().equalsIgnoreCase("pc_only") 
							|| temp instanceof L2PlayableInstance && getTargetType().equalsIgnoreCase("pc") 
							|| temp instanceof L2MonsterInstance && temp.hasAI() && temp.getAI().getIntention() != CtrlIntention.AI_INTENTION_IDLE 
							&& getTargetType().equalsIgnoreCase("npc")))
						{
							for (int[] skillData : _skills)
							{
								int skillId = skillData[0];
								int skillLvl = skillData[1];

								L2Skill skill = SkillTable.getInstance().getInfo(skillId, skillLvl);

								if (_bypassConditions || skill.checkCondition(temp, null, false))
								{
									// If target hasn't got the skill effect yet, cast it on him
									if (temp.getFirstEffect(skillId) == null)
									{
										// If skill has no effects, use its handler
										if (skill.getEffects(temp, temp).length == 0)
										{
											// Get the skill handler corresponding to the skill type (PDAM, MDAM, SWEEP...)
											ISkillHandler handler = SkillHandler.getInstance().getSkillHandler(skill.getSkillType());
											
											// Launch the magic skill and calculate its effects
											try
											{
												L2Character[] targets = new L2Character[]
												{
													temp
												};
												
												if (handler != null)
												{
													handler.useSkill(temp, skill, targets);
												}
												else
												{
													skill.useSkill(temp, targets);
												}
											}
											catch (Exception e)
											{
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}