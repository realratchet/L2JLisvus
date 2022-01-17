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
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.SevenSigns;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.datatables.TeleportLocationTable;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2TeleportLocation;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ExShowCropInfo;
import net.sf.l2j.gameserver.network.serverpackets.ExShowCropSetting;
import net.sf.l2j.gameserver.network.serverpackets.ExShowManorDefaultInfo;
import net.sf.l2j.gameserver.network.serverpackets.ExShowSeedInfo;
import net.sf.l2j.gameserver.network.serverpackets.ExShowSeedSetting;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.StringUtil;

/**
 * Castle Chamberlains implementation used for: - tax rate control - regional manor system control - castle treasure control - ...
 */
public class L2CastleChamberlainInstance extends L2FolkInstance
{
	public L2CastleChamberlainInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}
	
	private void sendHtmlMessage(L2PcInstance player, NpcHtmlMessage html)
	{
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%npcId%", String.valueOf(getNpcId()));
		player.sendPacket(html);
	}
	
	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		if ((player.getLastFolkNPC() == null) || (player.getLastFolkNPC().getObjectId() != getObjectId()))
		{
			return;
		}
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		int condition = validateCondition(player);
		if (condition <= COND_ALL_FALSE)
		{
			return;
		}
		
		if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
		{
			return;
		}
		else if (condition == COND_CASTLE_OWNER)
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
				if ((player.getClanPrivileges() & L2Clan.CP_CS_DISMISS) == L2Clan.CP_CS_DISMISS)
				{
					getCastle().banishForeigners(); // Move non-clan members off castle area
				}
				else
				{
					NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/chamberlain/chamberlain-noprivs.htm");
					html.replace("%objectId%", String.valueOf(getObjectId()));
					player.sendPacket(html);
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("list_siege_clans"))
			{
				if (player.isClanLeader())
				{
					getCastle().getSiege().listRegisterClan(player); // List current register clan
				}
				else
				{
					NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/chamberlain/chamberlain-noprivs.htm");
					html.replace("%objectId%", String.valueOf(getObjectId()));
					player.sendPacket(html);
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("crown"))
			{
				if (!player.isClanLeader())
				{
					player.sendMessage("Only the castle lord may possess the Lord's Crown.");
					return;
				}
				
				if (player.getInventory().getItemByItemId(CastleManager.CASTLE_LORD_CROWN) != null)
				{
					player.sendMessage("You have already received a Lord's Crown.");
					return;
				}
				
				player.addItem("Circlet", CastleManager.CASTLE_LORD_CROWN, 1, null, true);
				
				return;
			}
			else if (actualCommand.equalsIgnoreCase("receive_report"))
			{
				if (player.isClanLeader())
				{
					L2Clan clan = ClanTable.getInstance().getClan(getCastle().getOwnerId());
					if (clan == null)
					{
						return;
					}
					
					NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/chamberlain/chamberlain-report.htm");
					html.replace("%objectId%", String.valueOf(getObjectId()));
					html.replace("%clanname%", clan.getName());
					html.replace("%clanleadername%", clan.getLeaderName());
					html.replace("%castlename%", getCastle().getName());
					
					int currentPeriod = SevenSigns.getInstance().getCurrentPeriod();
					switch (currentPeriod)
					{
						case SevenSigns.PERIOD_COMP_RECRUITING:
							html.replace("%ss_event%", "Quest Event Initialization");
							break;
						case SevenSigns.PERIOD_COMPETITION:
							html.replace("%ss_event%", "Competition (Quest Event)");
							break;
						case SevenSigns.PERIOD_COMP_RESULTS:
							html.replace("%ss_event%", "Quest Event Results");
							break;
						case SevenSigns.PERIOD_SEAL_VALIDATION:
							html.replace("%ss_event%", "Seal Validation");
							break;
					}
					
					int sealOwner1 = SevenSigns.getInstance().getSealOwner(1);
					switch (sealOwner1)
					{
						case SevenSigns.CABAL_NULL:
							html.replace("%ss_avarice%", "Not in Possession");
							break;
						case SevenSigns.CABAL_DAWN:
							html.replace("%ss_avarice%", "Lords of Dawn");
							break;
						case SevenSigns.CABAL_DUSK:
							html.replace("%ss_avarice%", "Revolutionaries of Dusk");
							break;
					}
					
					int sealOwner2 = SevenSigns.getInstance().getSealOwner(2);
					switch (sealOwner2)
					{
						case SevenSigns.CABAL_NULL:
							html.replace("%ss_gnosis%", "Not in Possession");
							break;
						case SevenSigns.CABAL_DAWN:
							html.replace("%ss_gnosis%", "Lords of Dawn");
							break;
						case SevenSigns.CABAL_DUSK:
							html.replace("%ss_gnosis%", "Revolutionaries of Dusk");
							break;
					}
					
					int sealOwner3 = SevenSigns.getInstance().getSealOwner(3);
					switch (sealOwner3)
					{
						case SevenSigns.CABAL_NULL:
							html.replace("%ss_strife%", "Not in Possession");
							break;
						case SevenSigns.CABAL_DAWN:
							html.replace("%ss_strife%", "Lords of Dawn");
							break;
						case SevenSigns.CABAL_DUSK:
							html.replace("%ss_strife%", "Revolutionaries of Dusk");
							break;
					}
					player.sendPacket(html);
					return;
				}
				NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/chamberlain/chamberlain-noprivs.htm");
				html.replace("%objectId%", String.valueOf(getObjectId()));
				player.sendPacket(html);
				return;
			}
			else if (actualCommand.equalsIgnoreCase("items"))
			{
				if ((player.getClanPrivileges() & L2Clan.CP_CS_OTHER_RIGHTS) == L2Clan.CP_CS_OTHER_RIGHTS)
				
				{
					if (val.isEmpty())
					{
						return;
					}
					
					player.tempInventoryDisable();
					
					if (Config.DEBUG)
					{
						_log.fine("Showing chamberlain buylist");
					}
					
					showBuyWindow(player, Integer.parseInt(val + "1"));
					player.sendPacket(new ActionFailed());
					
				}
				else
				{
					NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/chamberlain/chamberlain-noprivs.htm");
					html.replace("%objectId%", String.valueOf(getObjectId()));
					player.sendPacket(html);
					return;
				}
				
			}
			else if (actualCommand.equalsIgnoreCase("manage_siege_defender"))
			{
				if (player.isClanLeader())
				{
					getCastle().getSiege().listRegisterClan(player);
				}
				else
				{
					NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/chamberlain/chamberlain-noprivs.htm");
					html.replace("%objectId%", String.valueOf(getObjectId()));
					player.sendPacket(html);
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("manage_vault"))
			{
				if (!player.isClanLeader())
				{
					NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/chamberlain/chamberlain-noprivs.htm");
					html.replace("%objectId%", String.valueOf(getObjectId()));
					player.sendPacket(html);
					return;
				}
				
				String filename = "data/html/chamberlain/chamberlain-vault.htm";
				int amount = 0;
				if (val.equalsIgnoreCase("deposit"))
				{
					try
					{
						amount = Integer.parseInt(st.nextToken());
					}
					catch (NoSuchElementException e)
					{
					}
					
					if ((amount > 0) && (((long) getCastle().getTreasury() + amount) <= L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID)))
					{
						if (player.reduceAdena("Castle", amount, this, true))
						{
							getCastle().addToTreasuryNoTax(amount);
						}
						else
						{
							sendPacket(new SystemMessage(SystemMessage.YOU_NOT_ENOUGH_ADENA));
						}
					}
				}
				else if (val.equalsIgnoreCase("withdraw"))
				{
					try
					{
						amount = Integer.parseInt(st.nextToken());
					}
					catch (NoSuchElementException e)
					{
					}
					
					if (amount > 0)
					{
						if (getCastle().getTreasury() < amount)
						{
							filename = "data/html/chamberlain/chamberlain-vault-no.htm";
						}
						else
						{
							if (getCastle().addToTreasuryNoTax((-1) * amount))
							{
								player.addAdena("Castle", amount, this, true);
							}
						}
					}
				}
				
				NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile(filename);
				html.replace("%objectId%", String.valueOf(getObjectId()));
				html.replace("%npcname%", getName());
				html.replace("%tax_income%", formatAdena(getCastle().getTreasury()));
				html.replace("%withdraw_amount%", formatAdena(amount));
				player.sendPacket(html);
				return;
			}
			else if (actualCommand.equalsIgnoreCase("manor"))
			{
				String filename = "";
				if (CastleManorManager.getInstance().isDisabled())
				{
					filename = "data/html/npcdefault.htm";
				}
				else
				{
					int cmd = Integer.parseInt(val);
					switch (cmd)
					{
						case 0:
							filename = "data/html/chamberlain/manor/manor.htm";
							break;
						case 1:
							filename = "data/html/chamberlain/manor/manor_help00" + st.nextToken() + ".htm";
							break;
						default:
							filename = "data/html/chamberlain/chamberlain-no.htm";
							break;
					}
				}
				
				if (filename.length() != 0)
				{
					NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile(filename);
					html.replace("%objectId%", String.valueOf(getObjectId()));
					html.replace("%npcname%", getName());
					player.sendPacket(html);
				}
				return;
			}
			else if (command.startsWith("manor_menu_select"))
			{
				if ((player.getClanPrivileges() & L2Clan.CP_CS_OTHER_RIGHTS) != L2Clan.CP_CS_OTHER_RIGHTS)
				
				{
					NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/chamberlain/chamberlain-noprivs.htm");
					html.replace("%objectId%", String.valueOf(getObjectId()));
					player.sendPacket(html);
					return;
				}
				
				if (CastleManorManager.getInstance().isUnderMaintenance())
				{
					player.sendPacket(new SystemMessage(SystemMessage.THE_MANOR_SYSTEM_IS_CURRENTLY_UNDER_MAINTENANCE));
					player.sendPacket(new ActionFailed());
					return;
				}
				
				String params = command.substring(command.indexOf("?") + 1);
				StringTokenizer str = new StringTokenizer(params, "&");
				int ask = Integer.parseInt(str.nextToken().split("=")[1]);
				int state = Integer.parseInt(str.nextToken().split("=")[1]);
				int time = Integer.parseInt(str.nextToken().split("=")[1]);
				
				int castleId;
				if (state == -1)
				{
					castleId = getCastle().getCastleId();
				}
				else
				{
					castleId = state;
				}
				
				switch (ask)
				{
					case 3:
					{
						if ((time == 1) && !CastleManager.getInstance().getCastleById(castleId).isNextPeriodApproved())
						{
							player.sendPacket(new ExShowSeedInfo(castleId, null));
						}
						else
						{
							player.sendPacket(new ExShowSeedInfo(castleId, CastleManager.getInstance().getCastleById(castleId).getSeedProduction(time)));
						}
						break;
					}
					case 4:
					{
						if ((time == 1) && !CastleManager.getInstance().getCastleById(castleId).isNextPeriodApproved())
						{
							player.sendPacket(new ExShowCropInfo(castleId, null));
						}
						else
						{
							player.sendPacket(new ExShowCropInfo(castleId, CastleManager.getInstance().getCastleById(castleId).getCropProcure(time)));
						}
						break;
					}
					case 5:
					{
						player.sendPacket(new ExShowManorDefaultInfo());
						break;
					}
					case 7:
					{
						if (getCastle().isNextPeriodApproved())
						{
							player.sendPacket(new SystemMessage(SystemMessage.A_MANOR_CANNOT_BE_SET_UP_BETWEEN_6_AM_AND_8_PM));
						}
						else
						{
							player.sendPacket(new ExShowSeedSetting(getCastle().getCastleId()));
						}
						break;
					}
					case 8:
					{
						if (getCastle().isNextPeriodApproved())
						{
							player.sendPacket(new SystemMessage(SystemMessage.A_MANOR_CANNOT_BE_SET_UP_BETWEEN_6_AM_AND_8_PM));
						}
						else
						{
							player.sendPacket(new ExShowCropSetting(getCastle().getCastleId()));
						}
						break;
					}
				}
			}
			else if (actualCommand.equalsIgnoreCase("operate_door")) // door control
			{
				if ((player.getClanPrivileges() & L2Clan.CP_CS_OPEN_DOOR) != L2Clan.CP_CS_OPEN_DOOR)
				{
					NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/chamberlain/chamberlain-noprivs.htm");
					html.replace("%objectId%", String.valueOf(getObjectId()));
					player.sendPacket(html);
					return;
				}
				
				if (!val.isEmpty())
				{
					boolean open = (Integer.parseInt(val) == 1);
					while (st.hasMoreTokens())
					{
						getCastle().openCloseDoor(player, Integer.parseInt(st.nextToken()), open);
					}
				}
				
				NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/chamberlain/" + getTemplate().npcId + "-d.htm");
				html.replace("%objectId%", String.valueOf(getObjectId()));
				html.replace("%npcname%", getName());
				player.sendPacket(html);
				
				return;
			}
			else if (actualCommand.equalsIgnoreCase("tax_set")) // tax rates control
			{
				if (!player.isClanLeader())
				{
					NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/chamberlain/chamberlain-tax.htm");
					html.replace("%objectId%", String.valueOf(getObjectId()));
					html.replace("%tax%", String.valueOf(getCastle().getTaxPercent()));
					player.sendPacket(html);
					return;
				}
				
				if (!val.isEmpty())
				{
					getCastle().setTaxPercent(player, Integer.parseInt(val));
				}
				
				String msg = StringUtil.concat(
					"<html><body>",
					getName() + ":<br>",
					"Current tax rate: " + getCastle().getTaxPercent() + "%<br>",
					"<table>",
					"<tr>",
					"<td>Set tax rate to:</td>",
					"<td><edit var=\"value\" width=40><br>",
					"<button value=\"Adjust\" action=\"bypass -h npc_%objectId%_tax_set $value\" width=80 height=15></td>",
					"</tr>",
					"</table>",
					"</center>",
					"</body></html>");
				
				sendHtmlMessage(player, msg);
				return;
			}
			else if (actualCommand.equalsIgnoreCase("manage_functions"))
			{
				NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/chamberlain/chamberlain-manage.htm");
				html.replace("%objectId%", String.valueOf(getObjectId()));
				player.sendPacket(html);
				return;
			}
			else if (actualCommand.equalsIgnoreCase("products"))
			{
				NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/chamberlain/chamberlain-products.htm");
				html.replace("%objectId%", String.valueOf(getObjectId()));
				html.replace("%npcId%", String.valueOf(getNpcId()));
				player.sendPacket(html);
				return;
			}
			else if (actualCommand.equalsIgnoreCase("functions"))
			{
				if (val.equalsIgnoreCase("tele"))
				{
					NpcHtmlMessage html = new NpcHtmlMessage(1);
					if (getCastle().getFunction(Castle.FUNC_TELEPORT) == null)
					{
						html.setFile("data/html/chamberlain/chamberlain-nac.htm");
					}
					else
					{
						html.setFile("data/html/chamberlain/" + getNpcId() + "-t" + getCastle().getFunction(Castle.FUNC_TELEPORT).getLvl() + ".htm");
					}
					sendHtmlMessage(player, html);
				}
				else if (val.equalsIgnoreCase("support"))
				{
					NpcHtmlMessage html = new NpcHtmlMessage(1);
					if (getCastle().getFunction(Castle.FUNC_SUPPORT) == null)
					{
						html.setFile("data/html/chamberlain/chamberlain-nac.htm");
					}
					else
					{
						html.setFile("data/html/chamberlain/support" + getCastle().getFunction(Castle.FUNC_SUPPORT).getLvl() + ".htm");
						html.replace("%mp%", String.valueOf((int) getCurrentMp()));
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
					html.setFile("data/html/chamberlain/chamberlain-functions.htm");
					if (getCastle().getFunction(Castle.FUNC_RESTORE_EXP) != null)
					{
						html.replace("%xp_regen%", String.valueOf(getCastle().getFunction(Castle.FUNC_RESTORE_EXP).getLvl()));
					}
					else
					{
						html.replace("%xp_regen%", "0");
					}
					
					if (getCastle().getFunction(Castle.FUNC_RESTORE_HP) != null)
					{
						html.replace("%hp_regen%", String.valueOf(getCastle().getFunction(Castle.FUNC_RESTORE_HP).getLvl()));
					}
					else
					{
						html.replace("%hp_regen%", "0");
					}
					
					if (getCastle().getFunction(Castle.FUNC_RESTORE_MP) != null)
					{
						html.replace("%mp_regen%", String.valueOf(getCastle().getFunction(Castle.FUNC_RESTORE_MP).getLvl()));
					}
					else
					{
						html.replace("%mp_regen%", "0");
					}
					
					sendHtmlMessage(player, html);
				}
			}
			else if (actualCommand.equalsIgnoreCase("manage"))
			{
				if ((player.getClanPrivileges() & L2Clan.CP_CS_OTHER_RIGHTS) == L2Clan.CP_CS_OTHER_RIGHTS)
				{
					if (val.equalsIgnoreCase("recovery"))
					{
						if (st.countTokens() >= 1)
						{
							if (getCastle().getOwnerId() == 0)
							{
								player.sendMessage("This castle has no owner, so there are not functions to manage.");
								return;
							}
							
							val = st.nextToken();
							if (val.equalsIgnoreCase("hp_cancel"))
							{
								NpcHtmlMessage html = new NpcHtmlMessage(1);
								html.setFile("data/html/chamberlain/functions-cancel.htm");
								html.replace("%apply%", "recovery hp 0");
								sendHtmlMessage(player, html);
								return;
							}
							else if (val.equalsIgnoreCase("mp_cancel"))
							{
								NpcHtmlMessage html = new NpcHtmlMessage(1);
								html.setFile("data/html/chamberlain/functions-cancel.htm");
								html.replace("%apply%", "recovery mp 0");
								sendHtmlMessage(player, html);
								return;
							}
							else if (val.equalsIgnoreCase("exp_cancel"))
							{
								NpcHtmlMessage html = new NpcHtmlMessage(1);
								html.setFile("data/html/chamberlain/functions-cancel.htm");
								html.replace("%apply%", "recovery exp 0");
								sendHtmlMessage(player, html);
								return;
							}
							else if (val.equalsIgnoreCase("edit_hp"))
							{
								val = st.nextToken();
								NpcHtmlMessage html = new NpcHtmlMessage(1);
								html.setFile("data/html/chamberlain/functions-apply.htm");
								html.replace("%name%", "Fireplace (HP Recovery Device)");
								int percent = Integer.valueOf(val);
								int cost;
								
								switch (percent)
								{
									case 80:
										cost = Config.CS_HPREG1_FEE;
										break;
									case 120:
										cost = Config.CS_HPREG2_FEE;
										break;
									case 180:
										cost = Config.CS_HPREG3_FEE;
										break;
									case 240:
										cost = Config.CS_HPREG4_FEE;
										break;
									default: // 300
										cost = Config.CS_HPREG5_FEE;
										break;
								}
								
								html.replace("%cost%", String.valueOf(cost) + "</font>Adena /" + String.valueOf(Config.CS_HPREG_FEE_RATIO / 1000 / 60 / 60 / 24) + " Day</font>)");
								html.replace("%use%", "Provides additional HP recovery for clan members in the castle.<font color=\"00FFFF\">" + String.valueOf(percent) + "%</font>");
								html.replace("%apply%", "recovery hp " + String.valueOf(percent));
								sendHtmlMessage(player, html);
								return;
							}
							else if (val.equalsIgnoreCase("edit_mp"))
							{
								val = st.nextToken();
								NpcHtmlMessage html = new NpcHtmlMessage(1);
								html.setFile("data/html/chamberlain/functions-apply.htm");
								html.replace("%name%", "Carpet (MP Recovery)");
								int percent = Integer.valueOf(val);
								int cost;
								
								switch (percent)
								{
									case 5:
										cost = Config.CS_MPREG1_FEE;
										break;
									case 15:
										cost = Config.CS_MPREG2_FEE;
										break;
									case 30:
										cost = Config.CS_MPREG3_FEE;
										break;
									default: // 40
										cost = Config.CS_MPREG4_FEE;
										break;
								}
								
								html.replace("%cost%", String.valueOf(cost) + "</font>Adena /" + String.valueOf(Config.CS_MPREG_FEE_RATIO / 1000 / 60 / 60 / 24) + " Day</font>)");
								html.replace("%use%", "Provides additional MP recovery for clan members in the castle.<font color=\"00FFFF\">" + String.valueOf(percent) + "%</font>");
								html.replace("%apply%", "recovery mp " + String.valueOf(percent));
								sendHtmlMessage(player, html);
								return;
							}
							else if (val.equalsIgnoreCase("edit_exp"))
							{
								val = st.nextToken();
								NpcHtmlMessage html = new NpcHtmlMessage(1);
								html.setFile("data/html/chamberlain/functions-apply.htm");
								html.replace("%name%", "Chandelier (EXP Recovery Device)");
								int percent = Integer.valueOf(val);
								int cost;
								
								switch (percent)
								{
									case 15:
										cost = Config.CS_EXPREG1_FEE;
										break;
									case 25:
										cost = Config.CS_EXPREG2_FEE;
										break;
									case 35:
										cost = Config.CS_EXPREG3_FEE;
										break;
									default: // 50
										cost = Config.CS_EXPREG4_FEE;
										break;
								}
								
								html.replace("%cost%", String.valueOf(cost) + "</font>Adena /" + String.valueOf(Config.CS_EXPREG_FEE_RATIO / 1000 / 60 / 60 / 24) + " Day</font>)");
								html.replace("%use%", "Restores the Exp of any clan member who is resurrected in the castle.<font color=\"00FFFF\">" + String.valueOf(percent) + "%</font>");
								html.replace("%apply%", "recovery exp " + String.valueOf(percent));
								sendHtmlMessage(player, html);
								return;
							}
							else if (val.equalsIgnoreCase("hp"))
							{
								if (st.countTokens() >= 1)
								{
									if (Config.DEBUG)
									{
										_log.warning("Mp editing invoked");
									}
									
									val = st.nextToken();
									NpcHtmlMessage html = new NpcHtmlMessage(1);
									html.setFile("data/html/chamberlain/functions-apply_confirmed.htm");
									if (getCastle().getFunction(Castle.FUNC_RESTORE_HP) != null)
									{
										if (getCastle().getFunction(Castle.FUNC_RESTORE_HP).getLvl() == Integer.valueOf(val))
										{
											html.setFile("data/html/chamberlain/functions-used.htm");
											html.replace("%val%", String.valueOf(val) + "%");
											sendHtmlMessage(player, html);
											return;
										}
									}
									
									int percent = Integer.valueOf(val);
									int fee;
									
									switch (percent)
									{
										case 0:
											fee = 0;
											html.setFile("data/html/chamberlain/functions-cancel_confirmed.htm");
											break;
										case 80:
											fee = Config.CS_HPREG1_FEE;
											break;
										case 120:
											fee = Config.CS_HPREG2_FEE;
											break;
										case 180:
											fee = Config.CS_HPREG3_FEE;
											break;
										case 240:
											fee = Config.CS_HPREG4_FEE;
											break;
										default: // 300
											fee = Config.CS_HPREG5_FEE;
											break;
									}
									
									if (!getCastle().updateFunctions(player, Castle.FUNC_RESTORE_HP, percent, fee, Config.CS_HPREG_FEE_RATIO, (getCastle().getFunction(Castle.FUNC_RESTORE_HP) == null)))
									{
										html.setFile("data/html/chamberlain/low_adena.htm");
										sendHtmlMessage(player, html);
									}
									sendHtmlMessage(player, html);
								}
								return;
							}
							else if (val.equalsIgnoreCase("mp"))
							{
								if (st.countTokens() >= 1)
								{
									if (Config.DEBUG)
									{
										_log.warning("Mp editing invoked");
									}
									
									val = st.nextToken();
									NpcHtmlMessage html = new NpcHtmlMessage(1);
									html.setFile("data/html/chamberlain/functions-apply_confirmed.htm");
									
									if (getCastle().getFunction(Castle.FUNC_RESTORE_MP) != null)
									{
										if (getCastle().getFunction(Castle.FUNC_RESTORE_MP).getLvl() == Integer.valueOf(val))
										{
											html.setFile("data/html/chamberlain/functions-used.htm");
											html.replace("%val%", String.valueOf(val) + "%");
											sendHtmlMessage(player, html);
											return;
										}
									}
									
									int percent = Integer.valueOf(val);
									int fee;
									
									switch (percent)
									{
										case 0:
											fee = 0;
											html.setFile("data/html/chamberlain/functions-cancel_confirmed.htm");
											break;
										case 5:
											fee = Config.CS_MPREG1_FEE;
											break;
										case 15:
											fee = Config.CS_MPREG2_FEE;
											break;
										case 30:
											fee = Config.CS_MPREG3_FEE;
											break;
										default: // 40
											fee = Config.CS_MPREG4_FEE;
											break;
									}
									
									if (!getCastle().updateFunctions(player, Castle.FUNC_RESTORE_MP, percent, fee, Config.CS_MPREG_FEE_RATIO, (getCastle().getFunction(Castle.FUNC_RESTORE_MP) == null)))
									{
										html.setFile("data/html/chamberlain/low_adena.htm");
										sendHtmlMessage(player, html);
									}
									sendHtmlMessage(player, html);
								}
								return;
							}
							else if (val.equalsIgnoreCase("exp"))
							{
								if (st.countTokens() >= 1)
								{
									if (Config.DEBUG)
									{
										_log.warning("Exp editing invoked");
									}
									
									val = st.nextToken();
									NpcHtmlMessage html = new NpcHtmlMessage(1);
									html.setFile("data/html/chamberlain/functions-apply_confirmed.htm");
									
									if (getCastle().getFunction(Castle.FUNC_RESTORE_EXP) != null)
									{
										if (getCastle().getFunction(Castle.FUNC_RESTORE_EXP).getLvl() == Integer.valueOf(val))
										{
											html.setFile("data/html/chamberlain/functions-used.htm");
											html.replace("%val%", String.valueOf(val) + "%");
											sendHtmlMessage(player, html);
											return;
										}
									}
									
									int percent = Integer.valueOf(val);
									int fee;
									
									switch (percent)
									{
										case 0:
											fee = 0;
											html.setFile("data/html/chamberlain/functions-cancel_confirmed.htm");
											break;
										case 15:
											fee = Config.CS_EXPREG1_FEE;
											break;
										case 25:
											fee = Config.CS_EXPREG2_FEE;
											break;
										case 35:
											fee = Config.CS_EXPREG3_FEE;
											break;
										default: // 50
											fee = Config.CS_EXPREG4_FEE;
											break;
									}
									
									if (!getCastle().updateFunctions(player, Castle.FUNC_RESTORE_EXP, percent, fee, Config.CS_EXPREG_FEE_RATIO, (getCastle().getFunction(Castle.FUNC_RESTORE_EXP) == null)))
									{
										html.setFile("data/html/chamberlain/low_adena.htm");
										sendHtmlMessage(player, html);
									}
									sendHtmlMessage(player, html);
								}
								return;
							}
						}
						
						NpcHtmlMessage html = new NpcHtmlMessage(1);
						html.setFile("data/html/chamberlain/edit_recovery.htm");
						String hp = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 80\">80%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 120\">120%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 180\">180%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 240\">240%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 300\">300%</a>]";
						String exp = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 15\">15%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 25\">25%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 35\">35%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 50\">50%</a>]";
						String mp = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 5\">5%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 15\">15%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 30\">30%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 40\">40%</a>]";
						
						if (getCastle().getFunction(Castle.FUNC_RESTORE_HP) != null)
						{
							html.replace("%hp_recovery%", String.valueOf(getCastle().getFunction(Castle.FUNC_RESTORE_HP).getLvl()) + "%</font> (<font color=\"FFAABB\">" + String.valueOf(getCastle().getFunction(Castle.FUNC_RESTORE_HP).getLease()) + "</font>Adena /" + String.valueOf(Config.CS_HPREG_FEE_RATIO / 1000 / 60 / 60 / 24) + " Day)");
							html.replace("%hp_period%", "Withdraw the fee for the next time at " + format.format(getCastle().getFunction(Castle.FUNC_RESTORE_HP).getEndTime()));
							html.replace("%change_hp%", "[<a action=\"bypass -h npc_%objectId%_manage recovery hp_cancel\">Deactivate</a>]" + hp);
						}
						else
						{
							html.replace("%hp_recovery%", "none");
							html.replace("%hp_period%", "none");
							html.replace("%change_hp%", hp);
						}
						
						if (getCastle().getFunction(Castle.FUNC_RESTORE_EXP) != null)
						{
							html.replace("%exp_recovery%", String.valueOf(getCastle().getFunction(Castle.FUNC_RESTORE_EXP).getLvl()) + "%</font> (<font color=\"FFAABB\">" + String.valueOf(getCastle().getFunction(Castle.FUNC_RESTORE_EXP).getLease()) + "</font>Adena /" + String.valueOf(Config.CS_EXPREG_FEE_RATIO / 1000 / 60 / 60 / 24) + " Day)");
							html.replace("%exp_period%", "Withdraw the fee for the next time at " + format.format(getCastle().getFunction(Castle.FUNC_RESTORE_EXP).getEndTime()));
							html.replace("%change_exp%", "[<a action=\"bypass -h npc_%objectId%_manage recovery exp_cancel\">Deactivate</a>]" + exp);
						}
						else
						{
							html.replace("%exp_recovery%", "none");
							html.replace("%exp_period%", "none");
							html.replace("%change_exp%", exp);
						}
						
						if (getCastle().getFunction(Castle.FUNC_RESTORE_MP) != null)
						{
							html.replace("%mp_recovery%", String.valueOf(getCastle().getFunction(Castle.FUNC_RESTORE_MP).getLvl()) + "%</font> (<font color=\"FFAABB\">" + String.valueOf(getCastle().getFunction(Castle.FUNC_RESTORE_MP).getLease()) + "</font>Adena /" + String.valueOf(Config.CS_MPREG_FEE_RATIO / 1000 / 60 / 60 / 24) + " Day)");
							html.replace("%mp_period%", "Withdraw the fee for the next time at " + format.format(getCastle().getFunction(Castle.FUNC_RESTORE_MP).getEndTime()));
							html.replace("%change_mp%", "[<a action=\"bypass -h npc_%objectId%_manage recovery mp_cancel\">Deactivate</a>]" + mp);
						}
						else
						{
							html.replace("%mp_recovery%", "none");
							html.replace("%mp_period%", "none");
							html.replace("%change_mp%", mp);
						}
						sendHtmlMessage(player, html);
					}
					else if (val.equalsIgnoreCase("other"))
					{
						if (st.countTokens() >= 1)
						{
							if (getCastle().getOwnerId() == 0)
							{
								player.sendMessage("This castle has no owner, so there are no functions to manage.");
								return;
							}
							
							val = st.nextToken();
							if (val.equalsIgnoreCase("tele_cancel"))
							{
								NpcHtmlMessage html = new NpcHtmlMessage(1);
								html.setFile("data/html/chamberlain/functions-cancel.htm");
								html.replace("%apply%", "other tele 0");
								sendHtmlMessage(player, html);
								return;
							}
							else if (val.equalsIgnoreCase("support_cancel"))
							{
								NpcHtmlMessage html = new NpcHtmlMessage(1);
								html.setFile("data/html/chamberlain/functions-cancel.htm");
								html.replace("%apply%", "other support 0");
								sendHtmlMessage(player, html);
								return;
							}
							else if (val.equalsIgnoreCase("edit_support"))
							{
								val = st.nextToken();
								NpcHtmlMessage html = new NpcHtmlMessage(1);
								html.setFile("data/html/chamberlain/functions-apply.htm");
								html.replace("%name%", "Insignia (Supplementary Magic)");
								int stage = Integer.valueOf(val);
								int cost;
								
								switch (stage)
								{
									case 1:
										cost = Config.CS_SUPPORT1_FEE;
										break;
									case 2:
										cost = Config.CS_SUPPORT2_FEE;
										break;
									case 3:
										cost = Config.CS_SUPPORT3_FEE;
										break;
									default:
										cost = Config.CS_SUPPORT4_FEE;
										break;
								}
								
								html.replace("%cost%", String.valueOf(cost) + "</font>Adena /" + String.valueOf(Config.CS_SUPPORT_FEE_RATIO / 1000 / 60 / 60 / 24) + " Day</font>)");
								html.replace("%use%", "Enables the use of supplementary magic.");
								html.replace("%apply%", "other support " + String.valueOf(stage));
								sendHtmlMessage(player, html);
								return;
							}
							else if (val.equalsIgnoreCase("edit_tele"))
							{
								val = st.nextToken();
								NpcHtmlMessage html = new NpcHtmlMessage(1);
								html.setFile("data/html/chamberlain/functions-apply.htm");
								html.replace("%name%", "Mirror (Teleportation Device)");
								int stage = Integer.valueOf(val);
								int cost;
								
								switch (stage)
								{
									case 1:
										cost = Config.CS_TELE1_FEE;
										break;
									default:
										cost = Config.CS_TELE2_FEE;
										break;
								}
								
								html.replace("%cost%", String.valueOf(cost) + "</font>Adena /" + String.valueOf(Config.CS_TELE_FEE_RATIO / 1000 / 60 / 60 / 24) + " Day</font>)");
								html.replace("%use%", "Teleports clan members in a castle to the target <font color=\"00FFFF\">Stage " + String.valueOf(stage) + "</font> staging area");
								html.replace("%apply%", "other tele " + String.valueOf(stage));
								sendHtmlMessage(player, html);
								return;
							}
							else if (val.equalsIgnoreCase("tele"))
							{
								if (st.countTokens() >= 1)
								{
									if (Config.DEBUG)
									{
										_log.warning("Tele editing invoked");
									}
									
									val = st.nextToken();
									NpcHtmlMessage html = new NpcHtmlMessage(1);
									html.setFile("data/html/chamberlain/functions-apply_confirmed.htm");
									if (getCastle().getFunction(Castle.FUNC_TELEPORT) != null)
									{
										if (getCastle().getFunction(Castle.FUNC_TELEPORT).getLvl() == Integer.valueOf(val))
										{
											html.setFile("data/html/chamberlain/functions-used.htm");
											html.replace("%val%", "Stage " + String.valueOf(val));
											sendHtmlMessage(player, html);
											return;
										}
									}
									
									int lvl = Integer.valueOf(val);
									int fee;
									
									switch (lvl)
									{
										case 0:
											fee = 0;
											html.setFile("data/html/chamberlain/functions-cancel_confirmed.htm");
											break;
										case 1:
											fee = Config.CS_TELE1_FEE;
											break;
										default:
											fee = Config.CS_TELE2_FEE;
											break;
									}
									
									if (!getCastle().updateFunctions(player, Castle.FUNC_TELEPORT, lvl, fee, Config.CS_TELE_FEE_RATIO, (getCastle().getFunction(Castle.FUNC_TELEPORT) == null)))
									{
										html.setFile("data/html/chamberlain/low_adena.htm");
										sendHtmlMessage(player, html);
									}
									
									sendHtmlMessage(player, html);
								}
								return;
							}
							else if (val.equalsIgnoreCase("support"))
							{
								if (st.countTokens() >= 1)
								{
									if (Config.DEBUG)
									{
										_log.warning("Support editing invoked");
									}
									
									val = st.nextToken();
									NpcHtmlMessage html = new NpcHtmlMessage(1);
									html.setFile("data/html/chamberlain/functions-apply_confirmed.htm");
									if (getCastle().getFunction(Castle.FUNC_SUPPORT) != null)
									{
										if (getCastle().getFunction(Castle.FUNC_SUPPORT).getLvl() == Integer.valueOf(val))
										{
											html.setFile("data/html/chamberlain/functions-used.htm");
											html.replace("%val%", "Stage " + String.valueOf(val));
											sendHtmlMessage(player, html);
											return;
										}
									}
									
									int lvl = Integer.valueOf(val);
									int fee;
									
									switch (lvl)
									{
										case 0:
											fee = 0;
											html.setFile("data/html/chamberlain/functions-cancel_confirmed.htm");
											break;
										case 1:
											fee = Config.CS_SUPPORT1_FEE;
											break;
										case 2:
											fee = Config.CS_SUPPORT2_FEE;
											break;
										case 3:
											fee = Config.CS_SUPPORT3_FEE;
											break;
										default:
											fee = Config.CS_SUPPORT4_FEE;
											break;
									}
									
									if (!getCastle().updateFunctions(player, Castle.FUNC_SUPPORT, lvl, fee, Config.CS_SUPPORT_FEE_RATIO, (getCastle().getFunction(Castle.FUNC_SUPPORT) == null)))
									{
										html.setFile("data/html/chamberlain/low_adena.htm");
										sendHtmlMessage(player, html);
									}
									else
									{
										sendHtmlMessage(player, html);
									}
								}
								return;
							}
						}
						
						NpcHtmlMessage html = new NpcHtmlMessage(1);
						html.setFile("data/html/chamberlain/edit_other.htm");
						String tele = "[<a action=\"bypass -h npc_%objectId%_manage other edit_tele 1\">Level 1</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_tele 2\">Level 2</a>]";
						String support = "[<a action=\"bypass -h npc_%objectId%_manage other edit_support 1\">Level 1</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_support 2\">Level 2</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_support 3\">Level 3</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_support 4\">Level 4</a>]";
						if (getCastle().getFunction(Castle.FUNC_TELEPORT) != null)
						{
							html.replace("%tele%", "Stage " + String.valueOf(getCastle().getFunction(Castle.FUNC_TELEPORT).getLvl()) + "</font> (<font color=\"FFAABB\">" + String.valueOf(getCastle().getFunction(Castle.FUNC_TELEPORT).getLease()) + "</font>Adena /" + String.valueOf(Config.CS_TELE_FEE_RATIO / 1000 / 60 / 60 / 24) + " Day)");
							html.replace("%tele_period%", "Withdraw the fee for the next time at " + format.format(getCastle().getFunction(Castle.FUNC_TELEPORT).getEndTime()));
							html.replace("%change_tele%", "[<a action=\"bypass -h npc_%objectId%_manage other tele_cancel\">Deactivate</a>]" + tele);
						}
						else
						{
							html.replace("%tele%", "none");
							html.replace("%tele_period%", "none");
							html.replace("%change_tele%", tele);
						}
						
						if (getCastle().getFunction(Castle.FUNC_SUPPORT) != null)
						{
							html.replace("%support%", "Stage " + String.valueOf(getCastle().getFunction(Castle.FUNC_SUPPORT).getLvl()) + "</font> (<font color=\"FFAABB\">" + String.valueOf(getCastle().getFunction(Castle.FUNC_SUPPORT).getLease()) + "</font>Adena /" + String.valueOf(Config.CS_SUPPORT_FEE_RATIO / 1000 / 60 / 60 / 24) + " Day)");
							html.replace("%support_period%", "Withdraw the fee for the next time at " + format.format(getCastle().getFunction(Castle.FUNC_SUPPORT).getEndTime()));
							html.replace("%change_support%", "[<a action=\"bypass -h npc_%objectId%_manage other support_cancel\">Deactivate</a>]" + support);
						}
						else
						{
							html.replace("%support%", "none");
							html.replace("%support_period%", "none");
							html.replace("%change_support%", support);
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
						html.setFile("data/html/chamberlain/manage.htm");
						sendHtmlMessage(player, html);
					}
				}
				else
				{
					NpcHtmlMessage html = new NpcHtmlMessage(1);
					html.setFile("data/html/chamberlain/chamberlain-noprivs.htm");
					sendHtmlMessage(player, html);
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("support"))
			{
				setTarget(player);
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
						if (getCastle().getFunction(Castle.FUNC_SUPPORT) == null)
						{
							return;
						}
						
						if (getCastle().getFunction(Castle.FUNC_SUPPORT).getLvl() == 0)
						{
							return;
						}
						
						NpcHtmlMessage html = new NpcHtmlMessage(1);
						int skill_lvl = 0;
						
						if (st.countTokens() >= 1)
						{
							skill_lvl = Integer.parseInt(st.nextToken());
						}
						
						skill = SkillTable.getInstance().getInfo(skill_id, skill_lvl);
						if (skill.getSkillType() == L2Skill.SkillType.SUMMON)
						{
							player.doCast(skill);
						}
						else
						{
							if ((skill.getMpConsume() + skill.getMpInitialConsume()) <= getCurrentMp())
							{
								doCast(skill);
							}
							else
							{
								html.setFile("data/html/chamberlain/support-no_mana.htm");
								html.replace("%mp%", String.valueOf((int) getCurrentMp()));
								sendHtmlMessage(player, html);
								return;
							}
						}
						
						html.setFile("data/html/chamberlain/support-done.htm");
						html.replace("%mp%", String.valueOf((int) getCurrentMp()));
						sendHtmlMessage(player, html);
					}
					catch (Exception e)
					{
						player.sendMessage("Invalid skill level, contact admin!");
					}
				}
				catch (Exception e)
				{
					player.sendMessage("Invalid skill level, contact your admin!");
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("support_back"))
			{
				NpcHtmlMessage html = new NpcHtmlMessage(1);
				if (getCastle().getFunction(Castle.FUNC_SUPPORT).getLvl() == 0)
				{
					return;
				}
				
				html.setFile("data/html/chamberlain/support" + getCastle().getFunction(Castle.FUNC_SUPPORT).getLvl() + ".htm");
				html.replace("%mp%", String.valueOf((int) getStatus().getCurrentMp()));
				sendHtmlMessage(player, html);
			}
			else if (actualCommand.equalsIgnoreCase("goto"))
			{
				int whereTo = Integer.parseInt(val);
				doTeleport(player, whereTo);
				return;
			}
			
			super.onBypassFeedback(player, command);
		}
	}
	
	@Override
	public void showChatWindow(L2PcInstance player)
	{
		String filename = "data/html/chamberlain/chamberlain-no.htm";
		
		int condition = validateCondition(player);
		if (condition > COND_ALL_FALSE)
		{
			if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
			{
				filename = "data/html/chamberlain/chamberlain-busy.htm"; // Busy because of siege
			}
			else if (condition == COND_CASTLE_OWNER)
			{
				filename = "data/html/chamberlain/chamberlain.htm"; // Owner message window
			}
		}
		
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(filename);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%npcId%", String.valueOf(getNpcId()));
		html.replace("%npcname%", getName());
		player.sendPacket(html);
		
		player.sendPacket(new ActionFailed());
	}
	
	private void sendHtmlMessage(L2PcInstance player, String htmlMessage)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setHtml(htmlMessage);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%npcname%", getName());
		player.sendPacket(html);
	}
	
	private void doTeleport(L2PcInstance player, int val)
	{
		if (Config.DEBUG)
		{
			_log.warning("doTeleport(L2PcInstance player, int val) is called");
		}
		
		L2TeleportLocation list = TeleportLocationTable.getInstance().getTemplate(val);
		if (list != null)
		{
			if (player.reduceAdena("Teleport", list.getPrice(), this, true))
			{
				if (Config.DEBUG)
				{
					_log.warning("Teleporting player " + player.getName() + " for Castle to new location: " + list.getLocX() + ":" + list.getLocY() + ":" + list.getLocZ());
				}
				
				player.teleToLocation(list.getLocX(), list.getLocY(), list.getLocZ());
			}
		}
		else
		{
			_log.warning("No teleport destination with id:" + val);
		}
		
		player.sendPacket(new ActionFailed());
	}
	
	@Override
	protected int validateCondition(L2PcInstance player)
	{
		if (player.isGM())
		{
			return COND_CASTLE_OWNER;
		}
		
		if ((getCastle() != null) && (getCastle().getCastleId() > 0))
		{
			if (player.getClan() != null)
			{
				if (getCastle().getSiege().getIsInProgress())
				{
					return COND_BUSY_BECAUSE_OF_SIEGE; // Busy because of siege
				}
				else if (getCastle().getOwnerId() == player.getClanId())
				{
					return COND_CASTLE_OWNER; // Owner
				}
			}
		}
		
		return super.validateCondition(player);
	}
	
	private String formatAdena(int amount)
	{
		String s = "";
		int rem = amount % 1000;
		s = Integer.toString(rem);
		amount = (amount - rem) / 1000;
		while (amount > 0)
		{
			if (rem < 99)
			{
				s = '0' + s;
			}
			if (rem < 9)
			{
				s = '0' + s;
			}
			rem = amount % 1000;
			s = Integer.toString(rem) + "," + s;
			amount = (amount - rem) / 1000;
		}
		return s;
	}
}