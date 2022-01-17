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
package teleports.GatekeeperSpirit;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.SevenSigns;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.zone.type.L2BossZone;

/**
 * @author DnR
 */
public class GatekeeperSpirit extends Quest
{
	private static final int GATEKEEPER_SPIRIT = 8111;
	
	private static final int[] _lilithCoords = {185551, -9298, -5498};
	private static final int[] _anakimCoords = {184397, -11957, -5498};
	
	public static void main(String[] args)
	{
		// Quest class
		new GatekeeperSpirit();
	}
	
	public GatekeeperSpirit()
	{
		super(-1, GatekeeperSpirit.class.getSimpleName(), "teleports");
		
		addStartNpc(GATEKEEPER_SPIRIT);
		addFirstTalkId(GATEKEEPER_SPIRIT);
		addTalkId(GATEKEEPER_SPIRIT);
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmlText = "default.htm";
		
		int playerCabal = SevenSigns.getInstance().getPlayerCabal(player);
		int sealAvariceOwner = SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_AVARICE);
		int compWinner = SevenSigns.getInstance().getCabalHighestScore();
		if ((Config.SEVEN_SIGNS_DUNGEON_NPC_ACCESS || playerCabal != SevenSigns.CABAL_NULL && playerCabal == sealAvariceOwner) && playerCabal == compWinner)
		{
			L2BossZone zone;
			if (sealAvariceOwner == SevenSigns.CABAL_DAWN)
			{
				zone = GrandBossManager.getInstance().getZone(_lilithCoords[0], _lilithCoords[1], _lilithCoords[2]);
				if (zone != null)
				{
					zone.allowPlayerEntry(player, 30000);
				}
				player.teleToLocation(_lilithCoords[0], _lilithCoords[1], _lilithCoords[2]);
				return null;
			}
			else if (sealAvariceOwner == SevenSigns.CABAL_DUSK)
			{
				zone = GrandBossManager.getInstance().getZone(_anakimCoords[0], _anakimCoords[1], _anakimCoords[2]);
				if (zone != null)
				{
					zone.allowPlayerEntry(player, 30000);
				}
				player.teleToLocation(_anakimCoords[0], _anakimCoords[1], _anakimCoords[2]);
				return null;
			}
		}
		return htmlText;
	}
	
	@Override
	public String onFirstTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmlText = "default.htm";
		int playerCabal = SevenSigns.getInstance().getPlayerCabal(player);
		int sealAvariceOwner = SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_AVARICE);
		int compWinner = SevenSigns.getInstance().getCabalHighestScore();
		if ((Config.SEVEN_SIGNS_DUNGEON_NPC_ACCESS || playerCabal != SevenSigns.CABAL_NULL && playerCabal == sealAvariceOwner) && playerCabal == compWinner)
		{
			if (sealAvariceOwner == SevenSigns.CABAL_DAWN)
			{
				htmlText = "dawn.htm";
			}
			else if (sealAvariceOwner == SevenSigns.CABAL_DUSK)
			{
				htmlText = "dusk.htm";
			}
		}
		return htmlText;
	}
}