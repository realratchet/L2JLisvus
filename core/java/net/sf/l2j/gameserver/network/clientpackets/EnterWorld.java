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

import java.util.Base64;
import java.util.Collection;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.SevenSigns;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.communitybbs.Manager.RegionBBSManager;
import net.sf.l2j.gameserver.datatables.AdminCommandRightsData;
import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.instancemanager.CoupleManager;
import net.sf.l2j.gameserver.instancemanager.PetitionManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2ClassMasterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.model.entity.Couple;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.network.L2GameClient.GameClientState;
import net.sf.l2j.gameserver.network.serverpackets.Die;
import net.sf.l2j.gameserver.network.serverpackets.EtcStatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ExStorageMaxCount;
import net.sf.l2j.gameserver.network.serverpackets.GameGuardQuery;
import net.sf.l2j.gameserver.network.serverpackets.HennaInfo;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.MagicEffectIcons;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListAll;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import net.sf.l2j.gameserver.network.serverpackets.PledgeStatusChanged;
import net.sf.l2j.gameserver.network.serverpackets.QuestList;
import net.sf.l2j.gameserver.network.serverpackets.ShortCutInit;
import net.sf.l2j.gameserver.network.serverpackets.SignsSky;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;

/**
 * Enter World Packet Handler
 * <p>
 * <p>
 * 0000: 03
 * <p>
 * packet format rev656 cbdddd
 * <p>
 * @version $Revision: 1.16.2.1.2.7 $ $Date: 2005/03/29 23:15:33 $
 */
public class EnterWorld extends L2GameClientPacket
{
	private static final String _C__03_ENTERWORLD = "[C] 03 EnterWorld";
	private static Logger _log = Logger.getLogger(EnterWorld.class.getName());

	@Override
	protected void readImpl()
	{
	}

	@Override
	public void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			_log.warning("EnterWorld failed! activeChar is null...");
			getClient().closeNow();
			return;
		}

		if (Config.DEBUG)
		{
			if (L2World.getInstance().getPlayer(activeChar.getObjectId()) != null)
			{
				_log.warning("User already exists in player OID map! User " + activeChar.getName() + " is a character clone!");
			}
		}

		// Check if dual-boxing is allowed by counting the number of clients per IP
		if (!isDualBoxingAllowed(activeChar))
		{
			return;
		}
		
		getClient().setState(GameClientState.IN_GAME);
		
		if (activeChar.isGM())
		{
			if (Config.GM_STARTUP_INVULNERABLE && AdminCommandRightsData.getInstance().checkAccess(activeChar, "admin_invul"))
			{
				activeChar.setIsInvul(true);
			}

			if (Config.GM_STARTUP_INVISIBLE && AdminCommandRightsData.getInstance().checkAccess(activeChar, "admin_invisible"))
			{
				activeChar.getAppearance().setInvisible();
			}

			if (Config.GM_STARTUP_SILENCE && AdminCommandRightsData.getInstance().checkAccess(activeChar, "admin_silence"))
			{
				activeChar.setMessageRefusal(true);
			}

			if (Config.GM_STARTUP_AUTO_LIST && AdminCommandRightsData.getInstance().checkAccess(activeChar, "admin_gmliston"))
			{
				GmListTable.getInstance().addGm(activeChar, false);
			}
			else
			{
				GmListTable.getInstance().addGm(activeChar, true);
			}

			if (Config.GM_NAME_COLOR_ENABLED)
			{
				if (activeChar.getAccessLevel() >= Config.MASTER_ACCESS_LEVEL)
				{
					activeChar.getAppearance().setNameColor(Config.ADMIN_NAME_COLOR);
				}
				else if (activeChar.getAccessLevel() > 0)
				{
					activeChar.getAppearance().setNameColor(Config.GM_NAME_COLOR);
				}
			}
		}

		if (activeChar.getCurrentHp() < 0.5)
		{
			activeChar.setIsDead(true);
		}

		L2Clan clan = activeChar.getClan();
		if (clan != null)
		{
			if (activeChar.isClanLeader() && (clan.getLevel() > 3))
			{
				SiegeManager.getInstance().addSiegeSkills(activeChar);
			}

			for (Siege siege : SiegeManager.getInstance().getSieges())
			{
				if (!siege.getIsInProgress())
				{
					continue;
				}
				if (siege.checkIsAttacker(clan))
				{
					activeChar.setSiegeState((byte) 1);
					activeChar.setSiegeSide(siege.getCastle().getCastleId());
				}
				if (siege.checkIsDefender(clan))
				{
					activeChar.setSiegeState((byte) 2);
					activeChar.setSiegeSide(siege.getCastle().getCastleId());
				}
			}
		}

		sendPacket(new UserInfo(activeChar));
		activeChar.getMacroses().sendUpdate();
		sendPacket(new ItemList(activeChar, false));
		sendPacket(new ShortCutInit(activeChar));
		sendPacket(new HennaInfo(activeChar));
		Quest.playerEnter(activeChar);
		activeChar.sendPacket(new QuestList());
		
		/**
		 * A dummy magic skill cast packet for fixing heading issues 
		 * that occur when casting certain skills right after login.
		 */
		activeChar.sendPacket(new MagicSkillUse(activeChar, 1322, 1, 0, 0));
		
		loadTutorial(activeChar);

		if (Config.PLAYER_SPAWN_PROTECTION > 0)
		{
			activeChar.setProtection(true);
		}

		activeChar.spawnMe(activeChar.getX(), activeChar.getY(), activeChar.getZ());

		if (SevenSigns.getInstance().isSealValidationPeriod())
		{
			sendPacket(new SignsSky());
		}

		updateLoginEffectIcons(activeChar);
		activeChar.sendPacket(new EtcStatusUpdate(activeChar));

		// Expand Skill
		activeChar.sendPacket(new ExStorageMaxCount(activeChar));

		// Default enter world message
		sendPacket(new SystemMessage(SystemMessage.WELCOME_TO_LINEAGE_II));

		if (Config.DISPLAY_SERVER_VERSION)
		{
			activeChar.sendMessage("L2JLisvus tag: " + Config.PROJECT_TAG);
		}

		SevenSigns.getInstance().sendCurrentPeriodMsg(activeChar);
		Announcements.getInstance().showAnnouncements(activeChar);

		String serverNews = HtmCache.getInstance().getHtm("data/html/servnews.htm");
		if (serverNews != null)
		{
			NpcHtmlMessage htmlMsg = new NpcHtmlMessage(0);
			htmlMsg.setHtml(serverNews);
			sendPacket(htmlMsg);
		}

		// just in case player gets disconnected
		L2ClassMasterInstance.showQuestionMark(activeChar);

		PetitionManager.getInstance().checkPetitionMessages(activeChar);

		if (clan != null)
		{
			sendPacket(new PledgeShowMemberListAll(clan, activeChar));
			sendPacket(new PledgeStatusChanged(clan));
		}

		notifyClanMembers(activeChar);

		activeChar.onPlayerEnter();
		
		// Load engagement and notify partner
		if (Config.ALLOW_WEDDING)
		{
			restoreEngagement(activeChar);
		}

		if (Olympiad.getInstance().playerInStadium(activeChar))
		{
			activeChar.doRevive();
			if (!activeChar.isGM())
			{
				activeChar.sendMessage("You have been teleported to the nearest town due to being in an Olympiad Stadium.");
			}
		}

		if (activeChar.isAlikeDead())
		{
			// no broadcast needed since the player will already spawn dead to others
			sendPacket(new Die(activeChar));
		}

		if (!activeChar.isGM() && activeChar.getSiegeState() < 2 && activeChar.isInsideZone(L2Character.ZONE_SIEGE))
		{
			activeChar.teleToLocation(MapRegionTable.TeleportWhereType.Town);
			activeChar.sendMessage("You have been teleported to the nearest town due to being in a siege zone.");
		}

		if (activeChar.getClanJoinExpiryTime() > System.currentTimeMillis())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.CLAN_MEMBERSHIP_TERMINATED));
		}

		if (clan != null)
		{
			// Add message if clan hall not paid. Possibly this is custom...
			ClanHall clanHall = ClanHallManager.getInstance().getClanHallByOwner(clan);
			if ((clanHall != null) && !clanHall.getPaid())
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_MAKE_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW));
			}
		}

		RegionBBSManager.getInstance().changeCommunityBoard();

		if (Config.GAMEGUARD_ENFORCE)
		{
			activeChar.sendPacket(new GameGuardQuery());
		}
	}

	private final boolean isDualBoxingAllowed(L2PcInstance activeChar)
	{
		if (Config.MAX_CLIENTS_PER_IP <= 0)
		{
			return true;
		}
		
		// Allow dual boxing for GMs
		if (activeChar.isGM())
		{
			return true;
		}
		
		// The number of clients 
		int activeClientCount = 1; // Count current client instance
		// The IP for this new client
		final String ip = getClient().getConnection().getInetAddress().getHostAddress();

		// Check all players inside world
		Collection<L2PcInstance> players = L2World.getInstance().getAllPlayers();
		for (L2PcInstance otherPlayer : players)
		{
			if (activeChar == otherPlayer || otherPlayer.getClient() == null)
			{
				continue;
			}
			
			// Allow login if second character is offline
			if (otherPlayer.inOfflineMode())
			{
				continue;
			}
			
			// Allow dual boxing for GMs
			if (otherPlayer.isGM())
			{
				continue;
			}

			// Check if IPs are identical
			final String otherIp = otherPlayer.getClient().getConnection().getInetAddress().getHostAddress();
			if (otherIp != null && otherIp.equals(ip))
			{
				activeClientCount++;
				if (activeClientCount > Config.MAX_CLIENTS_PER_IP)
				{
					break;
				}
			}
		}

		// Now, kick this character
		if (activeClientCount > Config.MAX_CLIENTS_PER_IP)
		{
			activeChar.logout();
			return false;
		}
		
		return true;
	}
	
	private final void loadTutorial(L2PcInstance activeChar)
	{
		QuestState qs = activeChar.getQuestState("255_Tutorial");
		if (qs != null)
		{
			qs.getQuest().notifyEvent("UC", null, activeChar);
		}
	}

	/**
	 * @param activeChar
	 */
	private final void notifyClanMembers(L2PcInstance activeChar)
	{
		L2Clan clan = activeChar.getClan();
		if (clan != null)
		{
			clan.getClanMember(activeChar.getObjectId()).setPlayerInstance(activeChar);
			SystemMessage msg = new SystemMessage(SystemMessage.CLAN_MEMBER_S1_LOGGED_IN);
			msg.addString(activeChar.getName());

			clan.broadcastToOtherOnlineMembers(msg, activeChar);

			msg = null;

			clan.broadcastToOtherOnlineMembers(new PledgeShowMemberListUpdate(activeChar), activeChar);
		}
	}

	private final void updateLoginEffectIcons(L2PcInstance activeChar)
	{
		L2Effect[] effects = activeChar.getAllEffects();
		if (effects != null && effects.length > 0)
		{
			boolean hasEffects = false;
			MagicEffectIcons mi = new MagicEffectIcons();
			for (L2Effect e : activeChar.getAllEffects())
			{
				if (e == null)
				{
					continue;
				}

				switch (e.getEffectType())
				{
					case COMBAT_POINT_HEAL_OVER_TIME:
					case HEAL_OVER_TIME:
					case MANA_HEAL_OVER_TIME:
						e.exit();
						break;
					default:
						if (e.getSkill().getId() == 4082)
						{
							e.exit();
						}
						else if (e.getShowIcon() && e.getInUse())
						{
							e.addIcon(mi);
							hasEffects = true;
						}
						break;
				}
			}

			if (hasEffects)
			{
				activeChar.sendPacket(mi);
			}
		}
	}

	/**
	 * Restores player engagement and notifies online partner.
	 * 
	 * @param activeChar
	 */
	private final void restoreEngagement(L2PcInstance activeChar)
	{
		int objectId = activeChar.getObjectId();
		for (Couple couple : CoupleManager.getInstance().getCouples())
		{
			if (couple.getPlayer1Id() == objectId || couple.getPlayer2Id() == objectId)
			{
				if (couple.isMarried())
				{
					activeChar.setIsMarried(true);
				}

				activeChar.setCoupleId(couple.getId());
				activeChar.setPartnerId(couple.getPlayer1Id() == objectId ? couple.getPlayer2Id() : couple.getPlayer1Id());
				
				// Also, notify partner
				L2PcInstance partner = L2World.getInstance().getPlayer(activeChar.getPartnerId());
				if (partner != null && partner.isOnline())
				{
					partner.sendMessage("Your partner has logged in.");
				}
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__03_ENTERWORLD;
	}
}