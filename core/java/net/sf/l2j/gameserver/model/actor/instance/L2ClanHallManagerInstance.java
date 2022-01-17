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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.datatables.TeleportLocationTable;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.L2TeleportLocation;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ClanHallDecoration;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.WareHouseDepositList;
import net.sf.l2j.gameserver.network.serverpackets.WareHouseWithdrawalList;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

public class L2ClanHallManagerInstance extends L2FolkInstance
{
	private int _clanHallId = -1;

	/**
	 * @param objectId
	 * @param template
	 */
	public L2ClanHallManagerInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if (getClanHall() == null)
		{
			return;
		}

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		int condition = validateCondition(player);
		if (condition <= COND_ALL_FALSE)
		{
			return;
		}
		else if (condition == COND_HALL_OWNER)
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			String actualCommand = st.nextToken(); // Get actual command
			String val = "";
			if (st.countTokens() >= 1)
			{
				val = st.nextToken();
			}

			if (actualCommand.equalsIgnoreCase("banish_foreigner"))
			{
				if ((player.getClanPrivileges() & L2Clan.CP_CH_DISMISS) == L2Clan.CP_CH_DISMISS)
				{
					getClanHall().banishForeigners();
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("manage_vault"))
			{
				if ((player.getClanPrivileges() & L2Clan.CP_CL_VIEW_WAREHOUSE) == L2Clan.CP_CL_VIEW_WAREHOUSE)
				{
					if (val.equalsIgnoreCase("deposit"))
					{
						showVaultWindowDeposit(player);
					}
					else if (val.equalsIgnoreCase("withdraw"))
					{
						showVaultWindowWithdraw(player);
					}
					else
					{
						NpcHtmlMessage html = new NpcHtmlMessage(1);
						html.setFile("data/html/clanHallManager/vault.htm");
						this.sendHtmlMessage(player, html);
					}
				}
				else
				{
					player.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_NOT_AUTHORIZED));
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("door"))
			{
				if ((player.getClanPrivileges() & L2Clan.CP_CH_OPEN_DOOR) == L2Clan.CP_CH_OPEN_DOOR)
				{
					if (val.equalsIgnoreCase("open"))
					{
						getClanHall().openCloseDoors(true);
					}
					else if (val.equalsIgnoreCase("close"))
					{
						getClanHall().openCloseDoors(false);
					}
					else
					{
						NpcHtmlMessage html = new NpcHtmlMessage(1);
						html.setFile("data/html/clanHallManager/door.htm");
						sendHtmlMessage(player, html);
					}
				}
				else
				{
					player.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_NOT_AUTHORIZED));
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("functions"))
			{
				if (val.equalsIgnoreCase("tele"))
				{
					NpcHtmlMessage html = new NpcHtmlMessage(1);
					if (getClanHall().getFunction(ClanHall.FUNC_TELEPORT) == null)
					{
						html.setFile("data/html/clanHallManager/chamberlain-nac.htm");
					}
					else
					{
						html.setFile("data/html/clanHallManager/tele" + getClanHall().getLocation() + getClanHall().getFunction(ClanHall.FUNC_TELEPORT).getLvl() + ".htm");
					}
					sendHtmlMessage(player, html);
				}
				else if (val.equalsIgnoreCase("item_creation"))
				{
					if (getClanHall().getFunction(ClanHall.FUNC_ITEM_CREATE) == null)
					{
						NpcHtmlMessage html = new NpcHtmlMessage(1);
						html.setFile("data/html/clanHallManager/chamberlain-nac.htm");
						sendHtmlMessage(player, html);
						return;
					}

					if (st.countTokens() < 1)
					{
						return;
					}

					int valbuy = Integer.parseInt(st.nextToken()) + (getClanHall().getFunction(ClanHall.FUNC_ITEM_CREATE).getLvl() * 100000);
					showBuyWindow(player, valbuy);
				}
				else if (val.equalsIgnoreCase("support"))
				{
					NpcHtmlMessage html = new NpcHtmlMessage(1);
					if (getClanHall().getFunction(ClanHall.FUNC_SUPPORT) == null)
					{
						html.setFile("data/html/clanHallManager/chamberlain-nac.htm");
					}
					else
					{
						html.setFile("data/html/clanHallManager/support" + getClanHall().getFunction(ClanHall.FUNC_SUPPORT).getLvl() + ".htm");
						html.replace("%mp%", String.valueOf(getCurrentMp()));
					}
					sendHtmlMessage(player, html);
				}
				else if (val.equalsIgnoreCase("back"))
				{
					showChatWindow(player);
				}
				else
				{
					NpcHtmlMessage html = new NpcHtmlMessage(1);
					html.setFile("data/html/clanHallManager/functions.htm");
					if (getClanHall().getFunction(ClanHall.FUNC_RESTORE_EXP) != null)
					{
						html.replace("%xp_regen%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_RESTORE_EXP).getLvl()) + "%");
					}
					else
					{
						html.replace("%xp_regen%", "0");
					}
					if (getClanHall().getFunction(ClanHall.FUNC_RESTORE_HP) != null)
					{
						html.replace("%hp_regen%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_RESTORE_HP).getLvl()) + "%");
					}
					else
					{
						html.replace("%hp_regen%", "0");
					}
					if (getClanHall().getFunction(ClanHall.FUNC_RESTORE_MP) != null)
					{
						html.replace("%mp_regen%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_RESTORE_MP).getLvl()) + "%");
					}
					else
					{
						html.replace("%mp_regen", "0");
					}
					sendHtmlMessage(player, html);
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("manage"))
			{
				if ((player.getClanPrivileges() & L2Clan.CP_CH_OTHER_RIGHTS) == L2Clan.CP_CH_OTHER_RIGHTS)
				{
					if (val.equalsIgnoreCase("recovery"))
					{
						if (st.countTokens() >= 1)
						{
							val = st.nextToken();
							if (val.equalsIgnoreCase("hp"))
							{
								if (st.countTokens() >= 1)
								{
									int fee = 0;

									if (Config.DEBUG)
									{
										_log.warning("Mp editing invoked");
									}

									val = st.nextToken();
									int percent = Integer.valueOf(val);
									switch (percent)
									{
										case 0:
											break;
										case 20:
											fee = Config.CH_HPREG1_FEE;
											break;
										case 40:
											fee = Config.CH_HPREG2_FEE;
											break;
										case 80:
											fee = Config.CH_HPREG3_FEE;
											break;
										case 100:
											fee = Config.CH_HPREG4_FEE;
											break;
										case 120:
											fee = Config.CH_HPREG5_FEE;
											break;
										case 140:
											fee = Config.CH_HPREG6_FEE;
											break;
										case 160:
											fee = Config.CH_HPREG7_FEE;
											break;
										case 180:
											fee = Config.CH_HPREG8_FEE;
											break;
										case 200:
											fee = Config.CH_HPREG9_FEE;
											break;
										case 220:
											fee = Config.CH_HPREG10_FEE;
											break;
										case 240:
											fee = Config.CH_HPREG11_FEE;
											break;
										case 260:
											fee = Config.CH_HPREG12_FEE;
											break;
										default:
											fee = Config.CH_HPREG13_FEE;
											break;
									}

									if (!getClanHall().updateFunctions(ClanHall.FUNC_RESTORE_HP, percent, fee, Config.CH_HPREG_FEE_RATIO, Calendar.getInstance().getTimeInMillis() + Config.CH_HPREG_FEE_RATIO, (getClanHall().getFunction(ClanHall.FUNC_RESTORE_HP) == null)))
									{
										player.sendMessage("Not enough adena in clan warehouse.");
									}
									else
									{
										revalidateDeco(player);
									}
								}
							}
							else if (val.equalsIgnoreCase("mp"))
							{
								if (st.countTokens() >= 1)
								{
									int fee = 0;

									if (Config.DEBUG)
									{
										_log.warning("Mp editing invoked");
									}

									val = st.nextToken();
									int percent = Integer.valueOf(val);
									switch (percent)
									{
										case 0:
											break;
										case 5:
											fee = Config.CH_MPREG1_FEE;
											break;
										case 10:
											fee = Config.CH_MPREG2_FEE;
											break;
										case 15:
											fee = Config.CH_MPREG3_FEE;
											break;
										case 30:
											fee = Config.CH_MPREG4_FEE;
											break;
										default:
											fee = Config.CH_MPREG5_FEE;
											break;
									}

									if (!getClanHall().updateFunctions(ClanHall.FUNC_RESTORE_MP, percent, fee, Config.CH_MPREG_FEE_RATIO, Calendar.getInstance().getTimeInMillis() + Config.CH_MPREG_FEE_RATIO, (getClanHall().getFunction(ClanHall.FUNC_RESTORE_MP) == null)))
									{
										player.sendMessage("Not enough adena in clan warehouse.");
									}
									else
									{
										revalidateDeco(player);
									}
								}
							}
							else if (val.equalsIgnoreCase("exp"))
							{
								if (st.countTokens() >= 1)
								{
									int fee = 0;

									if (Config.DEBUG)
									{
										_log.warning("Exp editing invoked");
									}

									val = st.nextToken();
									int percent = Integer.valueOf(val);
									switch (percent)
									{
										case 0:
											break;
										case 5:
											fee = Config.CH_EXPREG1_FEE;
											break;
										case 10:
											fee = Config.CH_EXPREG2_FEE;
											break;
										case 15:
											fee = Config.CH_EXPREG3_FEE;
											break;
										case 25:
											fee = Config.CH_EXPREG4_FEE;
											break;
										case 35:
											fee = Config.CH_EXPREG5_FEE;
											break;
										case 40:
											fee = Config.CH_EXPREG6_FEE;
											break;
										default:
											fee = Config.CH_EXPREG7_FEE;
											break;
									}

									if (!getClanHall().updateFunctions(ClanHall.FUNC_RESTORE_EXP, percent, fee, Config.CH_EXPREG_FEE_RATIO, Calendar.getInstance().getTimeInMillis() + Config.CH_EXPREG_FEE_RATIO, (getClanHall().getFunction(ClanHall.FUNC_RESTORE_EXP) == null)))
									{
										player.sendMessage("Not enough adena in clan warehouse.");
									}
									else
									{
										revalidateDeco(player);
									}
								}
							}

						}

						NpcHtmlMessage html = new NpcHtmlMessage(1);
						html.setFile("data/html/clanHallManager/edit_recovery" + getClanHall().getGrade() + ".htm");
						if ((getClanHall().getFunction(ClanHall.FUNC_RESTORE_HP) != null) && (getClanHall().getFunction(ClanHall.FUNC_RESTORE_HP).getLvl() != 0))
						{
							html.replace("%hp%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_RESTORE_HP).getLvl()) + "%");
							html.replace("%hpPrice%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_RESTORE_HP).getLease()));
							html.replace("%hpDate%", format.format(getClanHall().getFunction(ClanHall.FUNC_RESTORE_HP).getEndTime()));
						}
						else
						{
							html.replace("%hp%", "0");
							html.replace("%hpPrice%", "0");
							html.replace("%hpDate%", "0");
						}

						if ((getClanHall().getFunction(ClanHall.FUNC_RESTORE_EXP) != null) && (getClanHall().getFunction(ClanHall.FUNC_RESTORE_EXP).getLvl() != 0))
						{
							html.replace("%exp%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_RESTORE_EXP).getLvl()) + "%");
							html.replace("%expPrice%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_RESTORE_EXP).getLease()));
							html.replace("%expDate%", format.format(getClanHall().getFunction(ClanHall.FUNC_RESTORE_EXP).getEndTime()));
						}
						else
						{
							html.replace("%exp%", "0");
							html.replace("%expPrice%", "0");
							html.replace("%expDate%", "0");
						}

						if ((getClanHall().getFunction(ClanHall.FUNC_RESTORE_MP) != null) && (getClanHall().getFunction(ClanHall.FUNC_RESTORE_MP).getLvl() != 0))
						{
							html.replace("%mp%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_RESTORE_MP).getLvl()) + "%");
							html.replace("%mpPrice%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_RESTORE_MP).getLease()));
							html.replace("%mpDate%", format.format(getClanHall().getFunction(ClanHall.FUNC_RESTORE_MP).getEndTime()));
						}
						else
						{
							html.replace("%mp%", "0");
							html.replace("%mpPrice%", "0");
							html.replace("%mpDate%", "0");
						}
						sendHtmlMessage(player, html);
					}
					else if (val.equalsIgnoreCase("other"))
					{
						if (st.countTokens() >= 1)
						{
							val = st.nextToken();
							if (val.equalsIgnoreCase("item"))
							{
								if (st.countTokens() >= 1)
								{
									int fee = 0;

									if (Config.DEBUG)
									{
										_log.warning("Item editing invoked");
									}

									val = st.nextToken();
									int lvl = Integer.valueOf(val);
									switch (lvl)
									{
										case 0:
											break;
										case 1:
											fee = Config.CH_ITEM1_FEE;
											break;
										case 2:
											fee = Config.CH_ITEM2_FEE;
											break;
										default:
											fee = Config.CH_ITEM3_FEE;
											break;
									}

									if (!getClanHall().updateFunctions(ClanHall.FUNC_ITEM_CREATE, lvl, fee, Config.CH_ITEM_FEE_RATIO, Calendar.getInstance().getTimeInMillis() + Config.CH_ITEM_FEE_RATIO, (getClanHall().getFunction(ClanHall.FUNC_ITEM_CREATE) == null)))
									{
										player.sendMessage("Not enough adena in clan warehouse.");
									}
									else
									{
										revalidateDeco(player);
									}
								}
							}
							else if (val.equalsIgnoreCase("tele"))
							{
								if (st.countTokens() >= 1)
								{
									int fee = 0;

									if (Config.DEBUG)
									{
										_log.warning("Tele editing invoked");
									}

									val = st.nextToken();
									int lvl = Integer.valueOf(val);
									switch (lvl)
									{
										case 0:
											break;
										case 1:
											fee = Config.CH_TELE1_FEE;
											break;
										default:
											fee = Config.CH_TELE2_FEE;
											break;
									}

									if (!getClanHall().updateFunctions(ClanHall.FUNC_TELEPORT, lvl, fee, Config.CH_TELE_FEE_RATIO, Calendar.getInstance().getTimeInMillis() + Config.CH_TELE_FEE_RATIO, (getClanHall().getFunction(ClanHall.FUNC_TELEPORT) == null)))
									{
										player.sendMessage("Not enough adena in clan warehouse.");
									}
									else
									{
										revalidateDeco(player);
									}
								}
							}
							else if (val.equalsIgnoreCase("support"))
							{
								if (st.countTokens() >= 1)
								{
									int fee = 0;

									if (Config.DEBUG)
									{
										_log.warning("Support editing invoked");
									}

									val = st.nextToken();
									int lvl = Integer.valueOf(val);
									switch (lvl)
									{
										case 0:
											break;
										case 1:
											fee = Config.CH_SUPPORT1_FEE;
											break;
										case 2:
											fee = Config.CH_SUPPORT2_FEE;
											break;
										case 3:
											fee = Config.CH_SUPPORT3_FEE;
											break;
										case 4:
											fee = Config.CH_SUPPORT4_FEE;
											break;
										case 5:
											fee = Config.CH_SUPPORT5_FEE;
											break;
										case 6:
											fee = Config.CH_SUPPORT6_FEE;
											break;
										case 7:
											fee = Config.CH_SUPPORT7_FEE;
											break;
										default:
											fee = Config.CH_SUPPORT8_FEE;
											break;
									}

									if (!getClanHall().updateFunctions(ClanHall.FUNC_SUPPORT, lvl, fee, Config.CH_SUPPORT_FEE_RATIO, Calendar.getInstance().getTimeInMillis() + Config.CH_SUPPORT_FEE_RATIO, (getClanHall().getFunction(ClanHall.FUNC_SUPPORT) == null)))
									{
										player.sendMessage("Not enough adena in clan warehouse.");
									}
									else
									{
										revalidateDeco(player);
									}
								}
							}

						}

						NpcHtmlMessage html = new NpcHtmlMessage(1);
						html.setFile("data/html/clanHallManager/edit_other" + getClanHall().getGrade() + ".htm");
						if ((getClanHall().getFunction(ClanHall.FUNC_TELEPORT) != null) && (getClanHall().getFunction(ClanHall.FUNC_TELEPORT).getLvl() != 0))
						{
							html.replace("%tele%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_TELEPORT).getLvl()));
							html.replace("%telePrice%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_TELEPORT).getLease()));
							html.replace("%teleDate%", format.format(getClanHall().getFunction(ClanHall.FUNC_TELEPORT).getEndTime()));
						}
						else
						{
							html.replace("%tele%", "0");
							html.replace("%telePrice%", "0");
							html.replace("%teleDate%", "0");
						}

						if ((getClanHall().getFunction(ClanHall.FUNC_SUPPORT) != null) && (getClanHall().getFunction(ClanHall.FUNC_SUPPORT).getLvl() != 0))
						{
							html.replace("%support%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_SUPPORT).getLvl()));
							html.replace("%supportPrice%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_SUPPORT).getLease()));
							html.replace("%supportDate%", format.format(getClanHall().getFunction(ClanHall.FUNC_SUPPORT).getEndTime()));
						}
						else
						{
							html.replace("%support%", "0");
							html.replace("%supportPrice%", "0");
							html.replace("%supportDate%", "0");
						}

						if ((getClanHall().getFunction(ClanHall.FUNC_ITEM_CREATE) != null) && (getClanHall().getFunction(ClanHall.FUNC_ITEM_CREATE).getLvl() != 0))
						{
							html.replace("%item%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_ITEM_CREATE).getLvl()));
							html.replace("%itemPrice%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_ITEM_CREATE).getLease()));
							html.replace("%itemDate%", format.format(getClanHall().getFunction(ClanHall.FUNC_ITEM_CREATE).getEndTime()));
						}
						else
						{
							html.replace("%item%", "0");
							html.replace("%itemPrice%", "0");
							html.replace("%itemDate%", "0");
						}
						sendHtmlMessage(player, html);
					}
					else if (val.equalsIgnoreCase("deco"))
					{
						if (st.countTokens() >= 1)
						{
							val = st.nextToken();
							if (val.equalsIgnoreCase("curtains"))
							{
								if (st.countTokens() >= 1)
								{
									int fee = 0;
									if (Config.DEBUG)
									{
										_log.warning("Deco curtains editing invoked");
									}
									val = st.nextToken();
									int lvl = Integer.valueOf(val);
									switch (lvl)
									{
										case 0:
											break;
										case 1:
											fee = Config.CH_CURTAIN1_FEE;
											break;
										default:
											fee = Config.CH_CURTAIN2_FEE;
											break;
									}

									if (!getClanHall().updateFunctions(ClanHall.FUNC_DECO_CURTAINS, lvl, fee, Config.CH_CURTAIN_FEE_RATIO, Calendar.getInstance().getTimeInMillis() + Config.CH_CURTAIN_FEE_RATIO, (getClanHall().getFunction(ClanHall.FUNC_DECO_CURTAINS) == null)))
									{
										player.sendMessage("Not enough adena in Clan Warehouse.");
									}
									else
									{
										revalidateDeco(player);
									}
								}
							}
							else if (val.equalsIgnoreCase("porch"))
							{
								if (st.countTokens() >= 1)
								{
									int fee = 0;
									if (Config.DEBUG)
									{
										_log.warning("Deco curtains editing invoked");
									}
									val = st.nextToken();
									int lvl = Integer.valueOf(val);
									switch (lvl)
									{
										case 0:
											break;
										case 1:
											fee = Config.CH_FRONT1_FEE;
											break;
										default:
											fee = Config.CH_FRONT2_FEE;
											break;
									}

									if (!getClanHall().updateFunctions(ClanHall.FUNC_DECO_FRONTPLATEFORM, lvl, fee, Config.CH_FRONT_FEE_RATIO, Calendar.getInstance().getTimeInMillis() + Config.CH_FRONT_FEE_RATIO, (getClanHall().getFunction(ClanHall.FUNC_DECO_FRONTPLATEFORM) == null)))
									{
										player.sendMessage("Not enough adena in Clan Warehouse.");
									}
									else
									{
										revalidateDeco(player);
									}
								}
							}
						}
						NpcHtmlMessage html = new NpcHtmlMessage(1);
						html.setFile("data/html/clanHallManager/deco.htm");

						if ((getClanHall().getFunction(ClanHall.FUNC_DECO_CURTAINS) != null) && (getClanHall().getFunction(ClanHall.FUNC_DECO_CURTAINS).getLvl() != 0))
						{
							html.replace("%curtain%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_DECO_CURTAINS).getLvl()));
							html.replace("%curtainPrice%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_DECO_CURTAINS).getLease()));
							html.replace("%curtainDate%", format.format(getClanHall().getFunction(ClanHall.FUNC_DECO_CURTAINS).getEndTime()));
						}
						else
						{
							html.replace("%curtain%", "0");
							html.replace("%curtainPrice%", "0");
							html.replace("%curtainDate%", "0");
						}

						if ((getClanHall().getFunction(ClanHall.FUNC_DECO_FRONTPLATEFORM) != null) && (getClanHall().getFunction(ClanHall.FUNC_DECO_FRONTPLATEFORM).getLvl() != 0))
						{
							html.replace("%porch%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_DECO_FRONTPLATEFORM).getLvl()));
							html.replace("%porchPrice%", String.valueOf(getClanHall().getFunction(ClanHall.FUNC_DECO_FRONTPLATEFORM).getLease()));
							html.replace("%porchDate%", format.format(getClanHall().getFunction(ClanHall.FUNC_DECO_FRONTPLATEFORM).getEndTime()));
						}
						else
						{
							html.replace("%porch%", "0");
							html.replace("%porchPrice%", "0");
							html.replace("%porchDate%", "0");
						}
						sendHtmlMessage(player, html);
					}
					else if (val.equalsIgnoreCase("back"))
					{
						showChatWindow(player);
					}
					else
					{
						NpcHtmlMessage html = new NpcHtmlMessage(1);
						html.setFile("data/html/clanHallManager/manage.htm");
						sendHtmlMessage(player, html);
					}
				}
				else
				{
					player.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_NOT_AUTHORIZED));
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("support"))
			{
				this.setTarget(player);
				L2Skill skill;
				if (val.isEmpty())
				{
					return;
				}

				try
				{
					int skill_id = Integer.parseInt(val);
					try
					{
						int skill_lvl = 0;
						if (st.countTokens() >= 1)
						{
							skill_lvl = Integer.parseInt(st.nextToken());
						}
						skill = SkillTable.getInstance().getInfo(skill_id, skill_lvl);
						if (skill.getSkillType() == SkillType.SUMMON)
						{
							player.doCast(skill);
						}
						else
						{
							doCast(skill);
						}

						if (getClanHall().getFunction(ClanHall.FUNC_SUPPORT) == null)
						{
							return;
						}
						NpcHtmlMessage html = new NpcHtmlMessage(1);
						if (getClanHall().getFunction(ClanHall.FUNC_SUPPORT).getLvl() == 0)
						{
							return;
						}
						html.setFile("data/html/clanHallManager/support" + getClanHall().getFunction(ClanHall.FUNC_SUPPORT).getLvl() + ".htm");
						html.replace("%mp%", String.valueOf(getCurrentMp()));
						sendHtmlMessage(player, html);
					}
					catch (Exception e)
					{
						player.sendMessage("Invalid skill level!");
					}
				}
				catch (Exception e)
				{
					player.sendMessage("Invalid skill!");
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("goto"))
			{
				int whereTo = Integer.parseInt(val);
				doTeleport(player, whereTo);
				return;
			}
		}
		super.onBypassFeedback(player, command);
	}

	@Override
	public void showChatWindow(L2PcInstance player)
	{
		String filename = "data/html/clanHallManager/chamberlain-no.htm";

		int condition = validateCondition(player);
		if (condition > COND_ALL_FALSE)
		{
			if (condition == COND_HALL_OWNER)
			{
				filename = "data/html/clanHallManager/chamberlain.htm"; // Owner message window
			}
		}

		NpcHtmlMessage html = new NpcHtmlMessage(1);
		html.setFile(filename);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%npcId%", String.valueOf(getNpcId()));
		html.replace("%npcname%", getName());
		player.sendPacket(html);
		
		player.sendPacket(new ActionFailed());
	}
	
	private void sendHtmlMessage(L2PcInstance player, NpcHtmlMessage html)
	{
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%npcname%", getName());
		html.replace("%npcId%", String.valueOf(getNpcId()));
		player.sendPacket(html);
	}

	@Override
	protected int validateCondition(L2PcInstance player)
	{
		if (getClanHall() == null)
		{
			return COND_ALL_FALSE;
		}

		if (player.isGM())
		{
			return COND_HALL_OWNER;
		}

		if (player.getClan() != null)
		{
			if (getClanHall().getOwnerId() == player.getClanId())
			{
				return COND_HALL_OWNER;
			}
		}

		return super.validateCondition(player);
	}

	/**
	 * Return the L2ClanHall this L2NpcInstance belongs to.
	 * @return
	 */
	public final ClanHall getClanHall()
	{
		if (_clanHallId < 0)
		{
			_clanHallId = ClanHallManager.getInstance().getNearbyClanHall(getX(), getY(), 500).getId();
			if (_clanHallId < 0)
			{
				return null;
			}
		}
		return ClanHallManager.getInstance().getClanHallById(_clanHallId);
	}

	private void showVaultWindowDeposit(L2PcInstance player)
	{
		player.sendPacket(new ActionFailed());
		player.setActiveWarehouse(player.getClan().getWarehouse());
		player.sendPacket(new WareHouseDepositList(player, WareHouseDepositList.Clan)); // Or Clan Hall??
	}

	private void showVaultWindowWithdraw(L2PcInstance player)
	{
		player.sendPacket(new ActionFailed());
		player.setActiveWarehouse(player.getClan().getWarehouse());
		player.sendPacket(new WareHouseWithdrawalList(player, WareHouseWithdrawalList.Clan)); // Or Clan Hall ??
	}

	private void doTeleport(L2PcInstance player, int val)
	{
		if (Config.DEBUG)
		{
			_log.warning("doTeleport(L2PcInstance player, int val) is called.");
		}

		L2TeleportLocation list = TeleportLocationTable.getInstance().getTemplate(val);
		if (list != null)
		{
			if (SiegeManager.getInstance().getSiege(list.getLocX(), list.getLocY(), list.getLocZ()) != null)
			{
				// you cannot teleport to village that is in siege Not sure about this one though

				player.sendPacket(new SystemMessage(707));
				return;

			}
			else if (player.reduceAdena("Teleport", list.getPrice(), this, true))
			{
				if (Config.DEBUG)
				{
					_log.warning("Teleporting player " + player.getName() + " for CH to new location: " + list.getLocX() + ":" + list.getLocY() + ":" + list.getLocZ());
				}

				player.teleToLocation(list.getLocX(), list.getLocY(), list.getLocZ(), true);
			}
		}
		else
		{
			_log.warning("No teleport destination with id:" + val);
		}

		player.sendPacket(new ActionFailed());
	}

	private void revalidateDeco(L2PcInstance player)
	{
		ClanHall hall = ClanHallManager.getInstance().getClanHallByOwner(player.getClan());
		if (hall != null)
		{
			player.sendPacket(new ClanHallDecoration(hall));
		}
	}
	
	@Override
	public final boolean isWarehouse()
	{
		return true;
	}
}