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
package custom.NpcLocationInfo;

import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.util.Util;

/**
 * Npc Location Info AI.
 * @author Nyaran
 */
public class NpcLocationInfo extends Quest
{
	private static final int[] NPCS =
	{
		7598,
		7599,
		7600,
		7601,
		7602
	};
	
	private static final int[] RADAR_NPCS =
	{
		// Talking Island Village
		7006, // Gatekeeper Roxxy
		7039, // Captain Gilbert
		7040, // Guard Leon
		7041, // Guard Arnold
		7042, // Guard Abellos
		7043, // Guard Johnstone
		7044, // Guard Chiperan
		7045, // Guard Kenyos
		7046, // Guard Hanks
		7283, // Blacksmith Altran
		7003, // Trader Silvia
		7004, // Trader Katerina
		7001, // Trader Lector
		7002, // Trader Jackson
		7031, // High Priest Biotin
		7033, // Magister Baulro
		7035, // Magister Harrys
		7032, // Priest Yohanes
		7036, // Priest Petron
		7026, // Grand Master Bitz
		7027, // Master Gwinter
		7029, // Master Minia
		7028, // Master Pintage
		7054, // Warehouse Keeper Rant
		7055, // Warehouse Keeper Rolfe
		7005, // Warehouse Keeper Wilford
		7048, // Darin
		7312, // Lighthouse Keeper Rockswell
		7368, // Lilith
		7049, // Bonnie
		7047, // Wharf Manager Firon
		7497, // Edmond
		7050, // Elias
		7311, // Sir Collin Windawood
		7051, // Cristel
		
		// Dark Elven Village
		7134, // Gatekeeper Jasmine
		7224, // Sentry Knight Rayla
		7348, // Sentry Nelsya
		7355, // Sentry Roselyn
		7347, // Sentry Marion
		7432, // Sentry Irene
		7356, // Sentry Altima
		7349, // Sentry Jenna
		7346, // Sentry Kayleen
		7433, // Sentry Kathaway
		7357, // Sentry Kristin
		7431, // Sentry Eriel
		7430, // Sentry Trionell
		7307, // Blacksmith Karrod
		7138, // Trader Minaless
		7137, // Trader Vollodos
		7135, // Trader Iria
		7136, // Trader Payne
		7143, // Master Trudy
		7360, // Master Harant
		7145, // Master Vlasty
		7135, // Magister Harne
		7144, // Tetrarch Vellior
		7358, // Tetrarch Thifiell
		7359, // Tetrarch Kaitar
		7141, // Tetrarch Talloth
		7139, // Warehouse Keeper Dorankus
		7140, // Warehouse Keeper Erviante
		7350, // Warehouse Freightman Carlon
		7421, // Varika
		7419, // Arkenia
		7130, // Abyssal Celebrant Undrias
		7351, // Astaron
		7353, // Jughead
		7354, // Jewel
		
		// Elven Village
		7146, // Gatekeeper Mirabel
		7285, // Sentinel Gartrandell
		7284, // Sentinel Knight Alberius
		7221, // Sentinel Rayen
		7217, // Sentinel Berros
		7219, // Sentinel Veltress
		7220, // Sentinel Starden
		7218, // Sentinel Kendell
		7216, // Sentinel Wheeler
		7363, // Blacksmith Aios
		7149, // Trader Creamees
		7150, // Trader Herbiel
		7148, // Trader Ariel
		7147, // Trader Unoren
		7155, // Master Ellenia
		7156, // Master Cobendell
		7157, // Magister Greenis
		7158, // Magister Esrandell
		7154, // Hierarch Asterios
		7153, // Warehouse Keeper Markius
		7152, // Warehouse Keeper Julia
		7151, // Warehouse Freightman Chad
		7423, // Northwind
		7414, // Rosella
		12092, // Treant Bremec
		7223, // Arujien
		7362, // Andellia
		7222, // Alshupes
		7371, // Thalia
		12091, // Pixy Murika
		
		// Dwarven Villa
		7540, // Gatekeeper Wirphy
		7541, // Protector Paion
		7542, // Defender Runant
		7543, // Defender Ethan
		7544, // Defender Cromwell
		7545, // Defender Proton
		7546, // Defender Dinkey
		7547, // Defender Tardyon
		7548, // Defender Nathan
		7531, // Iron Gate's Lockirin
		7532, // Golden Wheel's Spiron
		7533, // Silver Scale's Balanki
		7534, // Bronze Key's Keef
		7535, // Filaur of the Gray Pillar
		7536, // Black Anvil's Arin
		7525, // Head Blacksmith Bronk
		7526, // Blacksmith Brunon
		7527, // Blacksmith Silvera
		7518, // Trader Garita
		7519, // Trader Mion
		7516, // Trader Reep
		7517, // Trader Shari
		7520, // Warehouse Chief Reed
		7521, // Warehouse Freightman Murdoc
		7522, // Warehouse Keeper Airy
		7523, // Collector Gouph
		7524, // Collector Pippi
		7537, // Daichir, Priest of the Eart
		7650, // Priest of the Earth Gerald
		7538, // Priest of the Earth Zimenf
		7539, // Priestess of the Earth Chichirin
		7671, // Captain Croto
		7651, // Wanderer Dorf
		7550, // Gauri Twinklerock
		7554, // Miner Bolter
		7553, // Maryse Redbonnet
		
		// Orc Village
		7576, // Gatekeeper Tamil
		7577, // Praetorian Rukain
		7578, // Centurion Nakusin
		7579, // Centurion Tamai
		7580, // Centurion Parugon
		7581, // Centurion Orinak
		7582, // Centurion Tiku
		7583, // Centurion Petukai
		7584, // Centurion Vapook
		7569, // Prefect Brukurse
		7570, // Prefect Karukia
		7571, // Seer Tanapi
		7572, // Seer Livina
		7564, // Blacksmith Sumari
		7560, // Trader Uska
		7561, // Trader Papuma
		7558, // Trader Jakal
		7559, // Trader Kunai
		7562, // Warehouse Keeper Grookin
		7563, // Warehouse Keeper Imantu
		7565, // Flame Lord Kakai
		7566, // Atuba Chief Varkees
		7567, // Neruga Chief Tantus
		7568, // Urutu Chief Hatos
		7585, // Tataru Zu Hestui
		7587, // Gantaki Zu Urutu
	};
	
	public static void main(String[] args)
    {
        // Quest class
        new NpcLocationInfo();
    }
	
	public NpcLocationInfo()
	{
		super(-1, NpcLocationInfo.class.getSimpleName(), "custom");
		for (int id : NPCS)
		{
			addStartNpc(id);
			addTalkId(id);
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmlText = event;
		QuestState qs = player.getQuestState(getName());
		if (qs == null)
		{
			return htmlText;
		}
		
		if (Util.isDigit(event))
		{
			htmlText = null;
			int npcId = Integer.parseInt(event);
			
			if (Util.contains(RADAR_NPCS, npcId))
			{
				int x = 0, y = 0, z = 0;
				final L2Spawn spawn = SpawnTable.getInstance().findAny(npcId);
				if (spawn != null)
				{
					x = spawn.getLocX();
					y = spawn.getLocY();
					z = spawn.getLocZ();
				}
				qs.addRadar(x, y, z);
				htmlText = "MoveToLoc.htm";
			}
		}
		return htmlText;
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, QuestState qs)
	{
		String htmltext = null;
		int npcId = npc.getNpcId();
		
		if (Util.contains(NPCS, npcId))
		{
			htmltext = String.valueOf(npcId) + ".htm";
		}
		
		return htmltext;
	}
}