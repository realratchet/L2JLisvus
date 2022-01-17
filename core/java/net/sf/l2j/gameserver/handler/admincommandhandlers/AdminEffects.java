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
package net.sf.l2j.gameserver.handler.admincommandhandlers;

import java.util.Collection;
import java.util.StringTokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2ChestInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.CharInfo;
import net.sf.l2j.gameserver.network.serverpackets.Earthquake;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;

public class AdminEffects implements IAdminCommandHandler
{
	private static String[] _adminCommands =
	{
		"admin_invis",
		"admin_invisible",
		"admin_vis",
		"admin_visible",
		"admin_earthquake",
		"admin_bighead",
		"admin_shrinkhead",
		"admin_gmspeed",
		"admin_unpara_all",
		"admin_para_all",
		"admin_unpara",
		"admin_para",
		"admin_polyself",
		"admin_unpolyself",
		"admin_clearteams",
		"admin_setteam",
		"admin_social",
		"admin_effect"
	};

	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();

		if (command.equals("admin_invis") || command.equals("admin_invisible"))
		{
			activeChar.getAppearance().setInvisible();
			activeChar.broadcastUserInfo();

			activeChar.decayMe();
			activeChar.spawnMe();
			
			// For sitting characters
			if (activeChar.isSitting())
			{
				activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_REST);
			}
		}
		else if (command.equals("admin_vis") || command.equals("admin_visible"))
		{
			activeChar.getAppearance().setVisible();
			activeChar.broadcastUserInfo();
		}
		else if (command.startsWith("admin_earthquake"))
		{
			try
			{
				String val1 = st.nextToken();
				int intensity = Integer.parseInt(val1);
				String val2 = st.nextToken();
				int duration = Integer.parseInt(val2);
				Earthquake eq = new Earthquake(activeChar.getX(), activeChar.getY(), activeChar.getZ(), intensity, duration);
				activeChar.broadcastPacket(eq);
			}
			catch (Exception e)
			{
			}
		}
		else if (command.equals("admin_para_all"))
		{
			try
			{
				for (L2Character character : activeChar.getKnownList().getKnownCharacters())
				{
					if (!character.isGM())
					{
						character.startAbnormalEffect(0x0400);
						character.startParalyze();
					}
				}
			}
			catch (Exception e)
			{
			}
		}
		else if (command.equals("admin_unpara_all"))
		{
			try
			{
				for (L2Character character : activeChar.getKnownList().getKnownCharacters())
				{
					character.stopAbnormalEffect(0x0400);
					character.stopParalyze(true);
				}
			}
			catch (Exception e)
			{
			}
		}
		else if (command.startsWith("admin_para"))
		{
			String type = "1";
			try
			{
				type = st.nextToken();
			}
			catch (Exception e)
			{
			}

			try
			{
				L2Object target = activeChar.getTarget();
				L2Character character = null;
				if (target instanceof L2Character)
				{
					character = (L2Character) target;

					if (type.equals("1"))
					{
						character.startAbnormalEffect(0x0400);
					}
					else
					{
						character.startAbnormalEffect(0x0800);
					}
					character.startParalyze();
				}
			}
			catch (Exception e)
			{
			}
		}
		else if (command.startsWith("admin_unpara"))
		{
			String type = "1";
			try
			{
				type = st.nextToken();
			}
			catch (Exception e)
			{
			}

			try
			{
				L2Object target = activeChar.getTarget();
				L2Character character = null;
				if (target instanceof L2Character)
				{
					character = (L2Character) target;

					if (type.equals("1"))
					{
						character.stopAbnormalEffect(0x0400);
					}
					else
					{
						character.stopAbnormalEffect(0x0800);
					}
					character.stopParalyze(true);
				}
			}
			catch (Exception e)
			{
			}
		}
		else if (command.startsWith("admin_bighead"))
		{
			try
			{
				L2Object target = activeChar.getTarget();
				L2Character character = null;
				if (target instanceof L2Character)
				{
					character = (L2Character) target;
					character.startAbnormalEffect(0x2000);
				}
			}
			catch (Exception e)
			{
			}
		}
		else if (command.startsWith("admin_shrinkhead"))
		{
			try
			{
				L2Object target = activeChar.getTarget();
				L2Character character = null;
				if (target instanceof L2Character)
				{
					character = (L2Character) target;
					character.stopAbnormalEffect(0x2000);
				}
			}
			catch (Exception e)
			{
			}
		}
		else if (command.startsWith("admin_gmspeed"))
		{
			try
			{
				int val = Integer.parseInt(st.nextToken());
				L2Effect effect = activeChar.getFirstEffect(7029);

				if (val == 0 && effect != null)
				{
					activeChar.stopEffect(7029);
					SystemMessage sm = new SystemMessage(SystemMessage.EFFECT_S1_DISAPPEARED);
					sm.addSkillName(effect.getSkill());
					activeChar.sendPacket(sm);
				}
				else if ((val >= 1) && (val <= 4))
				{
					L2Skill gmSpeedSkill = SkillTable.getInstance().getInfo(7029, val);
					gmSpeedSkill.getEffects(activeChar, activeChar);
					activeChar.sendMessage("Use Super Haste Lv." + val + ".");
				}
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Use //gmspeed value = [0...4].");
			}
			finally
			{
				activeChar.updateEffectIcons();
			}
		}
		else if (command.startsWith("admin_polyself"))
		{
			try
			{
				String id = st.nextToken();
				activeChar.getPoly().setPolyInfo("npc", id);
				activeChar.teleToLocation(activeChar.getX(), activeChar.getY(), activeChar.getZ(), false);
				CharInfo info1 = new CharInfo(activeChar);
				activeChar.broadcastPacket(info1);
				UserInfo info2 = new UserInfo(activeChar);
				activeChar.sendPacket(info2);
			}
			catch (Exception e)
			{
			}
		}
		else if (command.startsWith("admin_unpolyself"))
		{
			activeChar.getPoly().setPolyInfo(null, "1");
			activeChar.decayMe();
			activeChar.spawnMe(activeChar.getX(), activeChar.getY(), activeChar.getZ());
			CharInfo info1 = new CharInfo(activeChar);
			activeChar.broadcastPacket(info1);
			UserInfo info2 = new UserInfo(activeChar);
			activeChar.sendPacket(info2);
		}
		else if (command.equals("admin_clear_teams"))
		{
			try
			{
				for (L2PcInstance player : activeChar.getKnownList().getKnownPlayers().values())
				{
					player.setAuraColor(0);
					player.broadcastUserInfo();
				}
			}
			catch (Exception e)
			{
			}
		}
		else if (command.startsWith("admin_setteam"))
		{
			String val = st.nextToken();
			int teamVal = Integer.parseInt(val);
			L2Object target = activeChar.getTarget();
			L2PcInstance player;
			if (target instanceof L2PcInstance)
			{
				player = (L2PcInstance) target;
			}
			else
			{
				return false;
			}

			player.setAuraColor(teamVal);
			player.broadcastUserInfo();
		}
		else if (command.startsWith("admin_social"))
		{
			try
			{
				String target = null;
				L2Object obj = activeChar.getTarget();
				if (st.countTokens() == 2)
				{
					int social = Integer.parseInt(st.nextToken());
					target = st.nextToken();

					if (target != null)
					{
						L2PcInstance player = L2World.getInstance().getPlayer(target);
						if (player != null)
						{
							if (performSocial(social, player, activeChar))
							{
								activeChar.sendMessage(player.getName() + " was affected by your request.");
							}
						}
						else
						{
							try
							{
								int radius = Integer.parseInt(target);
								Collection<L2Object> objs = activeChar.getKnownList().getKnownObjects().values();
								{
									for (L2Object object : objs)
									{
										if (activeChar.isInsideRadius(object, radius, false, false))
										{
											performSocial(social, object, activeChar);
										}
									}
								}
								activeChar.sendMessage(radius + " units radius affected by your request.");
							}
							catch (NumberFormatException nbe)
							{
								activeChar.sendMessage("Incorrect parameter.");
							}
						}
					}
				}
				else if (st.countTokens() == 1)
				{
					int social = Integer.parseInt(st.nextToken());
					if (obj == null)
					{
						obj = activeChar;
					}

					if (performSocial(social, obj, activeChar))
					{
						activeChar.sendMessage(obj.getName() + " was affected by your request.");
					}
					else
					{
						activeChar.sendMessage("Nothing happened.");
					}
				}
				else
				{
					activeChar.sendMessage("Usage: //social <social_id> [player_name|radius]");
				}
			}
			catch (Exception e)
			{
				if (Config.DEBUG)
				{
					e.printStackTrace();
				}
			}
		}
		else if (command.startsWith("admin_effect"))
		{
			try
			{
				L2Object obj = activeChar.getTarget();
				int level = 1, hittime = 1;
				int skill = Integer.parseInt(st.nextToken());

				if (st.hasMoreTokens())
				{
					level = Integer.parseInt(st.nextToken());
				}
				if (st.hasMoreTokens())
				{
					hittime = Integer.parseInt(st.nextToken());
				}

				if (obj == null)
				{
					obj = activeChar;
				}

				if (!(obj instanceof L2Character))
				{
					activeChar.sendMessage("Incorrect target.");
				}
				else
				{
					L2Character target = (L2Character) obj;
					target.broadcastPacket(new MagicSkillUse(target, activeChar, skill, level, hittime, 0));
					activeChar.sendMessage(obj.getName() + " performs MSU " + skill + "/" + level + " by your request.");
				}
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Usage: //effect skill [level | level hittime]");
			}
		}
		return true;
	}

	private boolean performSocial(int action, L2Object target, L2PcInstance activeChar)
	{
		try
		{
			if (target instanceof L2Character)
			{
				if ((target instanceof L2Summon) || (target instanceof L2ChestInstance))
				{
					activeChar.sendMessage("Nothing happened.");
					return false;
				}

				if ((target instanceof L2NpcInstance) && ((action < 1) || (action > 3)))
				{
					activeChar.sendMessage("Nothing happened.");
					return false;
				}

				if ((target instanceof L2PcInstance) && ((action < 2) || (action > 16)))
				{
					activeChar.sendMessage("Nothing happened.");
					return false;
				}

				L2Character character = (L2Character) target;
				character.broadcastPacket(new SocialAction(target.getObjectId(), action));
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
		}

		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return _adminCommands;
	}
}