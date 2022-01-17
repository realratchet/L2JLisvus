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
package custom.HeroItems;

import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.util.StringUtil;

/**
 * @author DnR
 * 
 * Original made by DrLecter.
 */
public class HeroItems extends Quest
{
	private static final int[] MONUMENTS = {8690, 8769, 8770, 8771, 8772};
	
	private static final Map<Integer, String[]> ITEM_DATA = new LinkedHashMap<>();
	
	static
	{
		ITEM_DATA.put(6842, new String[]{"accessory_hero_cap_i00", "Wings of Destiny Circlet", "Hair accessory exclusively used by heroes.", "0", "Hair Accessory"});
		ITEM_DATA.put(6611, new String[]{"weapon_the_sword_of_hero_i00", "Infinity Blade", "During a critical attack, decreases one's P. Def and increases de-buff casting ability, damage shield effect, Max HP, Max MP, Max CP, and shield defense power. Also enhances damage to target during PvP.", "297/137", "Sword"});
		ITEM_DATA.put(6612, new String[]{"weapon_the_two_handed_sword_of_hero_i00", "Infinity Cleaver", "Increases Max HP, Max CP, critical power and critical chance. Inflicts extra damage when a critical attack occurs and has possibility of reflecting the skill back on the player. Also enhances damage to target during PvP.", "361/137", "Double Handed Sword"});
		ITEM_DATA.put(6613, new String[]{"weapon_the_axe_of_hero_i00", "Infinity Axe", "During a critical attack, it bestows one the ability to cause internal conflict to one's opponent. Damage shield function, Max HP, Max MP, Max CP as well as one's shield defense rate are increased. It also enhances damage to one's opponent during PvP.", "297/137", "Blunt"});
		ITEM_DATA.put(6614, new String[]{"weapon_the_mace_of_hero_i00", "Infinity Rod", "When good magic is casted upon a target, increases MaxMP, MaxCP, Casting Spd, and MP regeneration rate. Also recovers HP 100% and enhances damage to target during PvP.", "238/182", "Blunt"});
		ITEM_DATA.put(6615, new String[]{"weapon_the_hammer_of_hero_i00", "Infinity Crusher", "Increases MaxHP, MaxCP, and Atk. Spd. Stuns a target when a critical attack occurs and has possibility of reflecting the skill back on the player. Also enhances damage to target during PvP.", "361/137", "Blunt"});
		ITEM_DATA.put(6616, new String[]{"weapon_the_staff_of_hero_i00", "Infinity Scepter", "When casting good magic, it can recover HP by 100% at a certain rate, increases MAX MP, MaxCP, M. Atk., lower MP Consumption, increases the Magic Critical rate, and reduce the Magic Cancel. Enhances damage to target during PvP.", "290/182", "Blunt"});
		ITEM_DATA.put(6617, new String[]{"weapon_the_dagger_of_hero_i00", "Infinity Stinger", "Increases MaxMP, MaxCP, Atk. Spd., MP regen rate, and the success rate of Mortal and Deadly Blow from the back of the target. Silences the target when a critical attack occurs and has Vampiric Rage effect. Also enhances damage to target during PvP.", "260/137", "Dagger"});
		ITEM_DATA.put(6618, new String[]{"weapon_the_fist_of_hero_i00", "Infinity Fang", "Increases MaxHP, MaxMP, MaxCP and evasion. Stuns a target when a critical attack occurs and has possibility of reflecting the skill back on the player at a certain probability rate. Also enhances damage to target during PvP.", "361/137", "Dual Fist"});
		ITEM_DATA.put(6619, new String[]{"weapon_the_bow_of_hero_i00", "Infinity Bow", "Increases MaxMP/MaxCP and decreases re-use delay of a bow. Slows target when a critical attack occurs and has Cheap Shot effect. Also enhances damage to target during PvP.", "614/137", "Bow"});
		ITEM_DATA.put(6620, new String[]{"weapon_the_dualsword_of_hero_i00", "Infinity Wing", "When a critical attack occurs, increases MaxHP, MaxMP, MaxCP and critical chance. Silences the target and has possibility of reflecting the skill back on the target. Also enhances damage to target during PvP.", "361/137", "Dual Sword"});
		ITEM_DATA.put(6621, new String[]{"weapon_the_pole_of_hero_i00", "Infinity Spear", "During a critical attack, increases MaxHP, Max CP, Atk. Spd. and Accuracy. Casts dispel on a target and has possibility of reflecting the skill back on the target. Also enhances damage to target during PvP.", "297/137", "Pole"});
	}
	
	public static void main(String[] args)
    {
        // Quest class
        new HeroItems();
    }
	
	public HeroItems()
	{
		super(-1, HeroItems.class.getSimpleName(), "custom");
		
		for (int id : MONUMENTS)
		{
			addStartNpc(id);
			addTalkId(id);
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2NpcInstance npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestState(getName());
		if (st == null || !player.isHero())
		{
			return htmltext;
		}
		
		if (event.equals("buy"))
		{
			htmltext = getItemList();
		}
		else if (StringUtil.isDigit(event))
		{
			int itemId = Integer.parseInt(event);
			if (ITEM_DATA.containsKey(itemId))
			{
				htmltext = getItemDetails(itemId);
			}
		}
		else if (event.startsWith("_"))
		{
			event = event.replace("_", "");
			if (StringUtil.isDigit(event))
			{
				int itemId = Integer.parseInt(event);
				if (ITEM_DATA.containsKey(itemId))
				{
					boolean canGet = true;
					switch (itemId)
					{
						case 6842:
							if (st.getQuestItemsCount(itemId) > 0)
							{
								htmltext = "You can't have more than a circlet and a weapon.";
								canGet = false;
							}
							break;
						default:
							for (int id : ITEM_DATA.keySet())
							{
								if (id == 6842)
								{
									continue;
								}
								
								if (st.getQuestItemsCount(id) > 0)
								{
									htmltext = "You already have an " + ITEM_DATA.get(id)[1] + ".";
									canGet = false;
									break;
								}
							}
							break;
					}
					
					if (canGet)
					{
						st.giveItems(itemId, 1);
						htmltext = "Enjoy your " + ITEM_DATA.get(itemId)[1] + ".";
						if (itemId != 6842)
						{
							st.playSound("ItemSound.quest_fanfare_2");
						}
					}
				}
			}
			st.exitQuest(true);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		String htmltext = getNoQuestMsg();
		L2PcInstance player = st.getPlayer();
		
		if (player.isHero())
		{
			htmltext = getItemList();
		}
		else
		{
			st.exitQuest(true);
		}
		
		return htmltext;
	}
	
	private String getItemList()
	{
		String htmltext = "<html><body><font color=\"LEVEL\">List of Hero Items:</font><table border=0 width=300>";

		for (int itemId : ITEM_DATA.keySet())
		{
			if (!ITEM_DATA.containsKey(itemId))
			{
				continue;
			}
	 
			String[] item = ITEM_DATA.get(itemId);
			htmltext += "<tr><td width=35 height=45><img src=icon." + item[0] + " width=32 height=32 align=left></td>"
				+ "<td valign=top><a action=\"bypass -h Quest HeroItems " + itemId + "\"><font color=\"FFFFFF\">" + item[1] + "</font></a></td></tr>";
		}
 
		htmltext += "</table></body></html>";
 
		return htmltext;
	}
	
	private String getItemDetails(int itemId)
	{
		String[] item = ITEM_DATA.get(itemId);
		
		String htmltext = "<html><body><font color=\"LEVEL\">List of Hero Items:</font><table border=0 width=300>";
		htmltext += "<tr><td align=left><font color=\"LEVEL\">Item Information</font></td><td align=right>"
			+ "<button value=Back action=\"bypass -h Quest HeroItems buy\" width=40 height=15 back=sek.cbui94 fore=sek.cbui92>"
			+ "</td><td width=5><br></td></tr></table><table border=0 bgcolor=\"000000\" width=500 height=160><tr><td valign=top>"
			+ "<table border=0><tr><td valign=top width=35><img src=icon." + item[0] + " width=32 height=32 align=left></td>"
			+ "<td valign=top width=400><table border=0 width=100%><tr><td><font color=\"FFFFFF\">" + item[1] + "</font></td>"
			+ "</tr></table></td></tr></table><br><font color=\"LEVEL\">Item info:</font>"
			+ "<table border=0 bgcolor=\"000000\" width=290 height=220><tr><td valign=top><font color=\"B09878\">" + item[2] + "</font>"
			+ "</td></tr><tr><td><br>Type:" + item[4] + "<br><br>Patk/Matk: " + item[3] + "<br><br>"
			+ "<table border=0 width=300><tr><td align=center><button value=Obtain action=\"bypass -h Quest HeroItems _" + itemId + "\" width=60 height=15 "
			+ "back=sek.cbui94 fore=sek.cbui92></td></tr></table></td></tr></table></td></tr>";
		htmltext += "</table></body></html>";
		
		return htmltext;
	}
}
