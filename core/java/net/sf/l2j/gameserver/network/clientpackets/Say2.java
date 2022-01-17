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
package net.sf.l2j.gameserver.network.clientpackets;

import java.nio.BufferUnderflowException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.handler.VoicedCommandHandler;
import net.sf.l2j.gameserver.instancemanager.PetitionManager;
import net.sf.l2j.gameserver.model.BlockList;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.PartyMatchRoom;
import net.sf.l2j.gameserver.model.PartyMatchRoomList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.16.2.12.2.7 $ $Date: 2005/04/11 10:06:11 $
 */
public class Say2 extends L2GameClientPacket
{
	private static final String _C__38_SAY2 = "[C] 38 Say2";
	private static Logger _log = Logger.getLogger(Say2.class.getName());
	private static Logger _logChat = Logger.getLogger("chat");

	public final static int ALL = 0;
	public final static int SHOUT = 1; // !
	public final static int TELL = 2;
	public final static int PARTY = 3; // #
	public final static int CLAN = 4; // @
	public final static int GM = 5;
	public final static int PETITION_PLAYER = 6; // used for petition
	public final static int PETITION_GM = 7; // * used for petition
	public final static int TRADE = 8; // +
	public final static int ALLIANCE = 9; // $
	public final static int ANNOUNCEMENT = 10;
	public final static int PARTY_ROOM = 14;
	public final static int CHANNEL_LEADER = 15; // (yellow)
	public final static int CHANNEL_ALL = 16; // (blue)
	public final static int HERO_VOICE = 17;

	public final static String[] chatNames =
	{
		"ALL  ",
		"SHOUT",
		"TELL ",
		"PARTY",
		"CLAN ",
		"GM   ",
		"PETITION_PLAYER",
		"PETITION_GM",
		"TRADE",
		"ALLIANCE",
		"ANNOUNCEMENT", // 10
		"WILLCRASHCLIENT:)",
		"FAKEALL?",
		"FAKEALL?",
		"FAKEALL?",
		"PARTY_ROOM",
		"CHANNEL_LEADER",
		"CHANNEL_ALL",
		"HERO_VOICE"
	};

	private String _text;
	private int _type;
	private String _target;

	@Override
	protected void readImpl()
	{
		_text = readS();

		try
		{
			_type = readD();

		}
		catch (BufferUnderflowException e)
		{
			
			_type = chatNames.length;

		}
		_target = (_type == TELL) ? readS() : null;
	}

	@Override
	public void runImpl()
	{
		if (Config.DEBUG)
		{
			_log.info("Say2: Msg Type = '" + _type + "' Text = '" + _text + "'.");
		}

		if ((_type < 0) || (_type >= chatNames.length))
		{
			_log.warning("Say2: Invalid type: " + _type);
			return;
		}

		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			_log.warning("[Say2.java] Active Character is null.");
			return;
		}

		if (_text.isEmpty())
		{
			_log.warning(activeChar.getName() + ": sending empty text. Possible packet hack!");
			return;
		}

		if (_text.length() >= 100)
		{
			return;
		}

		if (activeChar.isChatBanned())
		{
			if ((_type == ALL) || (_type == SHOUT) || (_type == TRADE) || (_type == HERO_VOICE) || (_type == PARTY_ROOM))
			{
				activeChar.sendPacket(new SystemMessage(966));
				return;
			}
		}

		if (activeChar.isInJail() && Config.JAIL_DISABLE_CHAT)
		{
			if ((_type == TELL) || (_type == SHOUT) || (_type == TRADE) || (_type == HERO_VOICE) || (_type == PARTY_ROOM))
			{
				activeChar.sendMessage("You cannot chat with players outside of the jail.");
				return;
			}
		}

		if (activeChar.isGM())
		{
			if (_type == PETITION_PLAYER)
			{
				_type = PETITION_GM;
			}
		}
		else
		{
			if (_type == ANNOUNCEMENT)
			{
				return;
			}
		}

		if (Config.LOG_CHAT)
		{
			LogRecord record = new LogRecord(Level.INFO, _text);
			record.setLoggerName("chat");

			if (_type == TELL)
			{
				record.setParameters(new Object[]
				{
					chatNames[_type],
					"[" + activeChar.getName() + " to " + _target + "]"
				});
			}
			else
			{
				record.setParameters(new Object[]
				{
					chatNames[_type],
					"[" + activeChar.getName() + "]"
				});
			}

			_logChat.log(record);
		}

		CreatureSay cs = new CreatureSay(activeChar.getObjectId(), _type, activeChar.getName(), _text);

		switch (_type)
		{
			case TELL:
				L2PcInstance receiver = L2World.getInstance().getPlayer(_target);
				if ((receiver != null) && !BlockList.isBlocked(receiver, activeChar))
				{
					
					if (Config.JAIL_DISABLE_CHAT && receiver.isInJail())
					{
						activeChar.sendMessage("Player is in jail.");
						return;
					}

					if (receiver.isChatBanned())
					{
						activeChar.sendMessage("Player is chat-banned.");
						return;
					}

					// In case of message refusal mode, GMs with higher access level should be able to send messages to GMs with lower access level
					if (!receiver.getMessageRefusal() || activeChar.getAccessLevel() > receiver.getAccessLevel())
					{
						receiver.sendPacket(cs);
						activeChar.sendPacket(new CreatureSay(activeChar.getObjectId(), _type, "->" + receiver.getName(), _text));
					}
					else
					{
						activeChar.sendPacket(new SystemMessage(SystemMessage.THE_PERSON_IS_IN_MESSAGE_REFUSAL_MODE));
					}
				}
				else
				{
					SystemMessage sm = new SystemMessage(SystemMessage.S1_IS_NOT_ONLINE);
					sm.addString(_target);
					activeChar.sendPacket(sm);
					sm = null;
				}
				break;
			case SHOUT:
				if (Config.DEFAULT_GLOBAL_CHAT.equalsIgnoreCase("on") || (Config.DEFAULT_GLOBAL_CHAT.equalsIgnoreCase("gm") && activeChar.isGM()))
				{
					int region = MapRegionTable.getInstance().getMapRegion(activeChar.getX(), activeChar.getY());
					for (L2PcInstance player : L2World.getInstance().getAllPlayers())
					{
						if (region == MapRegionTable.getInstance().getMapRegion(player.getX(), player.getY()) && !BlockList.isBlocked(player, activeChar))
						{
							player.sendPacket(cs);
						}
					}
				}
				else if (Config.DEFAULT_GLOBAL_CHAT.equalsIgnoreCase("global"))
				{
					if (!activeChar.isGM() && !getClient().getFloodProtectors().getGlobalChat().tryPerformAction("global chat"))
					{
						activeChar.sendMessage("Do not spam shout channel.");
						return;
					}

					for (L2PcInstance player : L2World.getInstance().getAllPlayers())
					{
						if (!BlockList.isBlocked(player, activeChar))
						{
							player.sendPacket(cs);
						}
					}

				}
				break;
			case TRADE:
				if (Config.DEFAULT_TRADE_CHAT.equalsIgnoreCase("on") || (Config.DEFAULT_TRADE_CHAT.equalsIgnoreCase("gm") && activeChar.isGM()))
				{
					int region = MapRegionTable.getInstance().getMapRegion(activeChar.getX(), activeChar.getY());
					for (L2PcInstance player : L2World.getInstance().getAllPlayers())
					{
						if (region == MapRegionTable.getInstance().getMapRegion(player.getX(), player.getY()) && !BlockList.isBlocked(player, activeChar))
						{
							player.sendPacket(cs);
						}
					}
				}
				else if (Config.DEFAULT_TRADE_CHAT.equalsIgnoreCase("global"))
				{
					if (!activeChar.isGM() && !activeChar.getFloodProtectors().getGlobalChat().tryPerformAction("global chat"))
					{
						activeChar.sendMessage("Do not spam trade channel.");
						return;
					}
					
					for (L2PcInstance player : L2World.getInstance().getAllPlayers())
					{
						if (!BlockList.isBlocked(player, activeChar))
						{
							player.sendPacket(cs);
						}
					}
				}
				break;
			case ALL:
				if (_text.startsWith("."))
				{
					StringTokenizer st = new StringTokenizer(_text);
					IVoicedCommandHandler vch;
					String command = "";
					String target = "";
					if (st.countTokens() > 1)
					{
						command = st.nextToken().substring(1);
						target = _text.substring(command.length() + 2);
						vch = VoicedCommandHandler.getInstance().getVoicedCommandHandler(command);
					}
					else
					{
						command = _text.substring(1);
						if (Config.DEBUG)
						{
							_log.info("Command: " + command);
						}
						vch = VoicedCommandHandler.getInstance().getVoicedCommandHandler(command);
					}

					if (vch != null)
					{
						vch.useVoicedCommand(command, activeChar, target);
					}
					else
					{
						if (Config.DEBUG)
						{
							_log.warning("No handler registered for bypass '" + command + "'");
						}
					}
				}
				else
				{
					for (L2PcInstance player : activeChar.getKnownList().getKnownPlayers().values())
					{
						if ((player != null) && activeChar.isInsideRadius(player, 1250, false, true) && !BlockList.isBlocked(player, activeChar))
						{
							player.sendPacket(cs);
						}
					}
					activeChar.sendPacket(cs);
				}
				break;
			case CLAN:
				if (activeChar.getClan() != null)
				{
					activeChar.getClan().broadcastToOnlineMembers(cs);
				}
				break;
			case ALLIANCE:
				if (activeChar.getClan() != null)
				{
					activeChar.getClan().broadcastToOnlineAllyMembers(cs);
				}
				break;
			case PARTY:
				if (activeChar.isInParty())
				{
					activeChar.getParty().broadcastToPartyMembers(cs);
				}
				break;
			case PETITION_PLAYER:
			case PETITION_GM:
				if (!PetitionManager.getInstance().isPlayerInConsultation(activeChar))
				{
					activeChar.sendPacket(new SystemMessage(745));
					break;
				}

				PetitionManager.getInstance().sendActivePetitionMessage(activeChar, _text);
				break;
			case PARTY_ROOM:
				if (activeChar.isInPartyMatchRoom())
				{
					PartyMatchRoom _room = PartyMatchRoomList.getInstance().getPlayerRoom(activeChar);
					if (_room != null)
					{
						for (L2PcInstance member : _room.getPartyMembers())
						{
							member.sendPacket(cs);
						}
					}
				}
				break;
			case CHANNEL_LEADER:
			case CHANNEL_ALL:
				if (activeChar.getParty() != null)
				{
					if (!activeChar.getParty().getPartyMembers().get(0).equals(activeChar))
					{
						activeChar.sendPacket(new SystemMessage(SystemMessage.NO_RIGHT_TO_USE_CHANNEL));
						return;
					}

					if (activeChar.getParty().getCommandChannel() != null)
					{
						if (activeChar.getParty().getCommandChannel().getParties().size() < Config.ALT_CHANNEL_ACTIVATION_COUNT)
						{
							activeChar.sendMessage("The command channel is activated only if at least " + Config.ALT_CHANNEL_ACTIVATION_COUNT + " parties participate in.");
							return;
						}

						if (_type == CHANNEL_LEADER)
						{
							if (activeChar.getParty().getCommandChannel().getChannelLeader().equals(activeChar))
							{
								activeChar.getParty().getCommandChannel().broadcastToChannelMembers(cs);
							}
						}
						else
						{
							activeChar.getParty().getCommandChannel().broadcastToChannelMembers(cs);
						}
					}
				}
				break;
			case HERO_VOICE:
				if (activeChar.isHero())
				{
					if (!getClient().getFloodProtectors().getHeroVoice().tryPerformAction("hero voice"))
					{
						int interval = Config.FLOOD_PROTECTOR_HERO_VOICE.FLOOD_PROTECTION_INTERVAL / 10;
						activeChar.sendMessage("Action failed. Heroes are only able to use the hero voice once every " + interval + " seconds.");
						return;
					}

					for (L2PcInstance player : L2World.getInstance().getAllPlayers())
					{
						if (!BlockList.isBlocked(player, activeChar))
						{
							player.sendPacket(cs);
						}
					}
				}
				break;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__38_SAY2;
	}
}