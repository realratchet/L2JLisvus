package custom.HeavyMedal;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.util.Rnd;

/**
 * @author Gnacik
 *
 * Retail Event : 'Heavy Medals'
 */
public class HeavyMedal extends Quest
{
	private final static int CAT_ROY = 8228;
	private final static int CAT_WINNIE = 8229;
	private final static int CAT_WENDY = 8230;
	private final static int GLITTERING_MEDAL = 6393;
	
	private final static int WIN_CHANCE = 50;
	private final static long WENDY_RANDOM_SPAWN_INTERVAL = 1800000L; // 30 minutes
	
	private final static int[] MEDALS = { 5, 10, 20, 40 };
	private final static int[] BADGES = { 6399, 6400, 6401, 6402 };
	
	private final static Location[] WINNIE_SPAWNS =
	{
		new Location(-44342, -113726, -240, 0),
		new Location(-44671, -115437, -240, 22500),
		new Location(-13073, 122841, -3117, 0),
		new Location(-13972, 121893, -2988, 32768),
		new Location(-14843, 123710, -3117, 8192),
		new Location(11327, 15682, -4584, 25000),
		new Location(11243, 17712, -4574, 57344),
		new Location(18154, 145192, -3054, 7400),
		new Location(19214, 144327, -3097, 32768),
		new Location(19459, 145775, -3086, 48000),
		new Location(17418, 170217, -3507, 36000),
		new Location(47146, 49382, -3059, 32000),
		new Location(44157, 50827, -3059, 57344),
		new Location(79798, 55629, -1560, 0),
		new Location(83328, 55769, -1525, 32768),
		new Location(80986, 54452, -1525, 32768),
		new Location(83329, 149095, -3405, 49152),
		new Location(82277, 148564, -3467, 0),
		new Location(81620, 148689, -3464, 32768),
		new Location(81691, 145610, -3467, 32768),
		new Location(114719, -178742, -821, 0),
		new Location(115708, -182422, -1449, 0),
		new Location(-80731, 151152, -3043, 28672),
		new Location(-84097, 150171, -3129, 4096),
		new Location(-82678, 151666, -3129, 49152),
		new Location(117459, 76664, -2695, 38000),
		new Location(115936, 76488, -2711, 59000),
		new Location(119576, 76940, -2275, 40960),
		new Location(-84516, 243015, -3730, 34000),
		new Location(-86031, 243153, -3730, 60000),
		new Location(147124, 27401, -2192, 40960),
		new Location(147985, 25664, -2000, 16384),
		new Location(111724, 221111, -3543, 16384),
		new Location(107899, 218149, -3675, 0),
		new Location(114920, 220080, -3632, 32768),
		new Location(147924, -58052, -2979, 49000),
		new Location(147285, -56461, -2776, 33000),
		new Location(44176, -48688, -800, 33000),
		new Location(44294, -47642, -792, 50000)
	};
	
	private final static Location[] ROY_SPAWNS =
	{
		new Location(-44337, -113669, -224, 0),
		new Location(-44628, -115409, -240, 22500),
		new Location(-13073, 122801, -3117, 0),
		new Location(-13949, 121934, -2988, 32768),
		new Location(-14786, 123686, -3117, 8192),
		new Location(11281, 15652, -4584, 25000),
		new Location(11303, 17732, -4574, 57344),
		new Location(18178, 145149, -3054, 7400),
		new Location(19208, 144380, -3097, 32768),
		new Location(19508, 145775, -3086, 48000),
		new Location(17396, 170259, -3507, 36000),
		new Location(47151, 49436, -3059, 32000),
		new Location(44122, 50784, -3059, 57344),
		new Location(79806, 55570, -1560, 0),
		new Location(83328, 55824, -1525, 32768),
		new Location(80986, 54504, -1525, 32768),
		new Location(83332, 149160, -3405, 49152),
		new Location(82277, 148598, -3467, 0),
		new Location(81621, 148725, -3467, 32768),
		new Location(81680, 145656, -3467, 32768),
		new Location(114733, -178691, -821, 0),
		new Location(115708, -182362, -1449, 0),
		new Location(-80789, 151073, -3043, 28672),
		new Location(-84049, 150176, -3129, 4096),
		new Location(-82623, 151666, -3129, 49152),
		new Location(117498, 76630, -2695, 38000),
		new Location(115914, 76449, -2711, 59000),
		new Location(119536, 76988, -2275, 40960),
		new Location(-84516, 242971, -3730, 34000),
		new Location(-86003, 243205, -3730, 60000),
		new Location(147184, 27405, -2192, 17000),
		new Location(147920, 25664, -2000, 16384),
		new Location(111776, 221104, -3543, 16384),
		new Location(107904, 218096, -3675, 0),
		new Location(114920, 220020, -3632, 32768),
		new Location(147888, -58048, -2979, 49000),
		new Location(147262, -56450, -2776, 33000),
		new Location(44176, -48732, -800, 33000),
		new Location(44319, -47640, -792, 50000)
	};
	
	private final static Location[] WENDY_SPAWNS =
	{
		new Location(147213, 31920, -2481, 0), 	// Aden
		new Location(21515, 145670, -3141, 0), 	// Dion
		new Location(-14472, 126597, -3141, 16384),	// Gludio
		new Location(81106, 52966, -1560, -16384),	// Oren
		new Location(42538, -46610, -798, 0),	// Rune
		new Location(118949, 219045, -3564, 0),	// Innadril
		new Location(147535, -59528, -2982, 0),	// Goddard
	};
	
	private static Location _lastWendyLocation = null;
	
	public static void main(String[] args)
	{
		new HeavyMedal();
	}
	
	public HeavyMedal()
	{
		super(-1, HeavyMedal.class.getSimpleName(), "custom");
		
		addStartNpc(CAT_ROY);
		addStartNpc(CAT_WINNIE);
		addTalkId(CAT_ROY);
		addTalkId(CAT_WINNIE);
		
		for (Location loc : ROY_SPAWNS)
		{
			addSpawn(CAT_ROY, loc.getX(), loc.getY(), loc.getZ(), loc.getHeading(), false, 0);
		}
		
		for (Location loc : WINNIE_SPAWNS)
		{
			addSpawn(CAT_WINNIE, loc.getX(), loc.getY(), loc.getZ(), loc.getHeading(), false, 0);
		}
		
		
		// Get new random spawn location for Wendy the Cat
		_lastWendyLocation = WENDY_SPAWNS[Rnd.get(WENDY_SPAWNS.length)];
		
		// Spawn Wendy the Cat using the random spawn location
		L2NpcInstance npc = addSpawn(CAT_WENDY, _lastWendyLocation.getX(), _lastWendyLocation.getY(), _lastWendyLocation.getZ(), _lastWendyLocation.getHeading(), false, 0);
		
		// Execute first random spawn task
		cancelQuestTimers("wendy_random_spawn");
		startQuestTimer("wendy_random_spawn", WENDY_RANDOM_SPAWN_INTERVAL, npc, null);
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("wendy_random_spawn"))
		{
			// Delete old spawn
			npc.deleteMe();
			
			List<Location> spawns = new ArrayList<>();
			for (Location loc : WENDY_SPAWNS)
			{
				// Exclude old spawn location
				if (loc != _lastWendyLocation)
				{
					spawns.add(loc);
				}
			}
			
			// Get new random spawn location
			_lastWendyLocation = spawns.get(Rnd.get(spawns.size()));
			
			// Spawn Wendy the Cat
			L2NpcInstance cat = addSpawn(CAT_WENDY, _lastWendyLocation.getX(), _lastWendyLocation.getY(), _lastWendyLocation.getZ(), _lastWendyLocation.getHeading(), false, 0);
			
			// Execute random spawn task
			startQuestTimer("wendy_random_spawn", WENDY_RANDOM_SPAWN_INTERVAL, cat, null);
		}
		// Player-based events
		else
		{
			String htmlText = getNoQuestMsg();
			QuestState st = player.getQuestState(getName());
			if (st != null)
			{
				htmlText = event;
				
				int level = checkLevel(st);
				if (event.equalsIgnoreCase("game"))
				{
					if (st.getQuestItemsCount(GLITTERING_MEDAL) < MEDALS[level])
					{
						htmlText = CAT_WINNIE + "-no.htm";
					}
					else
					{
						htmlText = CAT_WINNIE + "-game.htm";
					}
				}
				else if (event.equalsIgnoreCase("heads") || event.equalsIgnoreCase("tails"))
				{
					if (st.getQuestItemsCount(GLITTERING_MEDAL) < MEDALS[level])
					{
						htmlText = CAT_WINNIE + "-" + event.toLowerCase() + "-10.htm";
					}
					else
					{
						st.takeItems(GLITTERING_MEDAL, MEDALS[level]);
						
						if (Rnd.get(100) > WIN_CHANCE)
						{
							level = 0;
						}
						else
						{
							if (level > 0)
							{
								st.takeItems(BADGES[level - 1], -1);
							}
							st.giveItems(BADGES[level], 1);
							st.playSound("Itemsound.quest_itemget");
							level++;
						}
						htmlText = CAT_WINNIE + "-" + event.toLowerCase() + "-" + String.valueOf(level) + ".htm";
					}
				}
			}
			return htmlText;
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, L2PcInstance player)
	{
		String htmlText = getNoQuestMsg();
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmlText;
		}
		
		final int level = checkLevel(st);
		htmlText = String.valueOf(npc.getNpcId()) + "-lvl-" + String.valueOf(level) + ".htm";
		
		return htmlText;
	}
	
	private int checkLevel(QuestState st)
	{
		int level = 0;
		if (st == null)
		{
			return 0;
		}
		
		if (st.getQuestItemsCount(6402) > 0)
		{
			level = 4;
		}
		else if (st.getQuestItemsCount(6401) > 0)
		{
			level = 3;
		}
		else if (st.getQuestItemsCount(6400) > 0)
		{
			level = 2;
		}
		else if (st.getQuestItemsCount(6399) > 0)
		{
			level = 1;
		}
		
		return level;
	}
}