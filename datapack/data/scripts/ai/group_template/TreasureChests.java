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
package ai.group_template;

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2ChestInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * @author Karakan
 * 
 * Treasure Chest AI.
 */
public class TreasureChests extends Quest
{
	// Deluxe Key skill
	private static final int SKILL_DELUXE_KEY = 2229;
	
	// Base chance for BOX to be opened
	private static final int BASE_CHANCE = 100;
	
	// Percent to decrease base chance when grade of DELUXE key not match
	private static final int LEVEL_DECREASE = 40;
	
	// Chance for a chest to actually be a BOX (as opposed to being a mimic).
	private static final int IS_BOX = 40;
	
	private static final int[] NPC_IDS =
	{
		13100, 13101, 13102, 13103, 13104, 13105, 13106, 13107, 13108, 13109,
		13110, 13111, 13112, 13113, 13114, 13115, 13116, 13117, 13118, 13119,
		13120, 13121, 1801, 1802, 1803, 1804, 1805, 1806, 1807, 1808, 1809, 1810,
		1671, 1694, 1717, 1740, 1763, 1786, 13213, 13215, 13217, 13219, 13221,
		13223, 1811, 1812, 1813, 1814, 1815, 1816, 1817, 1818, 1819, 1820, 1821, 1822
	};
	
	public static void main(String[] args)
    {
        // Quest class
        new TreasureChests();
    }
	
	public TreasureChests()
	{
		super(-1, "treasurechests", "ai");
		for (int id : NPC_IDS)
		{
			registerNPC(id);
		}
	}
	
	@Override
	public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		if (npc instanceof L2ChestInstance)
		{
			// This behavior only runs when the target of skill is the passed npc (chest)
			// i.e. when the player is attempting to open the chest using a skill
			if (!Util.contains(targets, npc))
			{
				return super.onSkillSee(npc, caster, skill, targets, isPet);
			}
			
			final L2ChestInstance chest = ((L2ChestInstance) npc);
			final int npcId = chest.getNpcId();
			final int skillId = skill.getId();
			final int skillLevel = skill.getLevel();
			
			if (!Util.contains(NPC_IDS, npcId))
			{
				return super.onSkillSee(npc, caster, skill, targets, isPet);
			}
			// If this has already been interacted, no further ai decisions are needed
			// If it's the first interaction, check if this is a box or mimic
			if (!chest.isInteracted())
			{
				chest.setInteracted();
				if (Rnd.get(100) < IS_BOX)
				{
					// If it's a box, either it will be successfully opened by a proper key, or instantly disappear
					if (skillId == SKILL_DELUXE_KEY)
					{
						// Check the chance to open the box
						int keyLevelNeeded = chest.getLevel() / 10;
						keyLevelNeeded -= skillLevel;
						if (keyLevelNeeded < 0)
							keyLevelNeeded *= -1;
						final int chance = BASE_CHANCE - keyLevelNeeded * LEVEL_DECREASE;
						
						// Success, pretend-death with rewards
						if (Rnd.get(100) < chance)
						{
							caster.broadcastPacket(new SocialAction(caster.getObjectId(), 3));
							chest.setMustRewardExpSp(false);
							chest.setSpecialDrop();
							chest.reduceCurrentHp(99999999, caster, false, false);
							return null;
						}
					}
					// Used a skill other than chest-key, or used a chest-key but failed to open: disappear with no rewards
					caster.broadcastPacket(new SocialAction(caster.getObjectId(), 13));
					npc.onDecay();
				}
				else
				{
					final L2Character originalCaster = isPet ? caster.getPet() : caster;
					chest.setRunning();
					chest.addDamageHate(originalCaster, 0, 999);
					chest.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, originalCaster);
				}
			}
		}
		
		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}
	
	@Override
	public String onAttack(L2NpcInstance npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (npc instanceof L2ChestInstance)
		{
			final L2ChestInstance chest = ((L2ChestInstance) npc);
			final int npcId = chest.getNpcId();
			// Check if the chest and skills used are valid for this script. Exit if invalid.
			if (!Util.contains(NPC_IDS, npcId))
			{
				return super.onAttack(npc, attacker, damage, isPet);
			}
			
			// If this was a mimic, set the target, start the skills and become aggro
			if (!chest.isInteracted())
			{
				chest.setInteracted();
				if (Rnd.get(100) < IS_BOX)
				{
					chest.getSpawn().decreaseCount(chest);
					chest.deleteMe();
				}
				else
				{
					// If this weren't a box, upon interaction start the mimic behaviors...
					// TODO: perhaps a self-buff (skill id 4245) with random chance goes here?
					final L2Character originalAttacker = isPet ? attacker.getPet() : attacker;
					chest.setRunning();
					chest.addDamageHate(originalAttacker, 0, (damage * 100) / (chest.getLevel() + 7));
					chest.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, originalAttacker);
				}
			}
		}
		
		return super.onAttack(npc, attacker, damage, isPet);
	}
}
