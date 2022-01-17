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

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2TamedBeastInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

/**
 * Growth-capable mobs: Polymorphing upon successful feeding.
 * @author Fulminus
 * Adapted for C4 by DnR.
 */
public class FeedableBeasts extends Quest
{
	private static final int GOLDEN_SPICE = 6643;
	private static final int CRYSTAL_SPICE = 6644;
	private static final int SKILL_GOLDEN_SPICE = 2188;
	private static final int SKILL_CRYSTAL_SPICE = 2189;
    private static final int[] TAMED_BEASTS = {12783, 12784, 12785, 12786, 12787, 12788};

    // Αll mobs that can eat...
    private static final int[] FEEDABLE_BEASTS = 
    {
    	1451, 1452, 1453, 1454, 1455, 1456, 1457, 1458, 1459, 1460, 1461, 1462, 1463,
    	1464, 1465, 1466, 1467, 1468, 1469, 1470, 1471, 1472, 1473, 1474, 1475, 1476,
    	1477, 1478, 1479, 1480, 1481, 1482, 1483, 1484, 1485, 1486, 1487, 1488, 1489,
    	1490, 1491, 1492, 1493, 1494, 1495, 1496, 1497, 1498, 1499, 1500, 1501, 1502,
    	1503, 1504, 1505, 1506, 1507, 12783, 12784, 12785, 12786, 12787, 12788
    };
    
    private static final Map<Integer,Integer> FOOD_SKILL = new HashMap<>();
    static
    {
    	FOOD_SKILL.put(GOLDEN_SPICE, SKILL_GOLDEN_SPICE);
		FOOD_SKILL.put(CRYSTAL_SPICE, SKILL_CRYSTAL_SPICE);
    }
    
    private static final String[][] TEXT = 
    {
    	{
    		"What did you just do to me?","You want to tame me, huh?",
    		"Do not give me this. Perhaps you will be in danger.",
    		"Bah bah. What is this unpalatable thing?",
    		"My belly has been complaining.  This hit the spot.",
    		"What is this? Can I eat it?","You don't need to worry about me.",
    		"Delicious food, thanks.","I am starting to like you!","Gulp"
    	},
    	{
    		"I do not think you have given up on the idea of taming me.",
    		"That is just food to me.  Perhaps I can eat your hand too.",
    		"Will eating this make me fat? Ha ha","Why do you always feed me?",
    		"Do not trust me.  I may betray you"
    	},
    	{
    		"Destroy","Look what you have done!",
    		"Strange feeling...!  Evil intentions grow in my heart...!",
    		"It is happenning!","This is sad...Good is sad...!"
    	}
    };
    
    private static final String[] TAMED_TEXT = 
    {
    	"Refills! Yeah!","I am such a gluttonous beast, it is embarrassing! Ha ha",
    	"Your cooperative feeling has been getting better and better.",
    	"I will help you!","The weather is really good.  Wanna go for a picnic?",
    	"I really like you! This is tasty...",
    	"If you do not have to leave this place, then I can help you.",
    	"What can I help you with?","I am not here only for food!",
    	"Yam, yam, yam, yam, yam!"
    };

    private final Map<Integer,Integer> _feedInfo = new HashMap<>();
    private final Map<Integer,GrowthCapableMob> _growthCapableMobs = new HashMap<>();

    // All mobs that grow by eating
    private class GrowthCapableMob
    {
    	private int _growthLevel;
    	private int _chance;
    	
    	private Map<Integer, int[][]> _spiceToMob = new HashMap<>();
    	
    	public GrowthCapableMob(int growthLevel, int chance)
    	{
    		_growthLevel = growthLevel;
    		_chance = chance;
    	}
    	
    	public void addMobs(int spice, int[][] Mobs)
    	{
    		_spiceToMob.put(spice, Mobs);
    	}
    	
    	public Integer getMob(int spice,int mobType, int classType)
    	{
    		if (_spiceToMob.containsKey(spice))
    		{
    			return _spiceToMob.get(spice)[mobType][classType];
    		}
    		return null;
    	}
    	
    	public Integer getRandomMob(int spice)
    	{
    		int[][] temp;
    		temp = _spiceToMob.get(spice);
    		int rand = Rnd.get(temp[0].length);
    		return temp[0][rand];
    	}
    	
    	public Integer getChance()
    	{
    		return _chance;
    	}
    	
    	public Integer getGrowthLevel()
    	{
    		return _growthLevel;
    	}
    }
    
    public static void main(String[] args)
    {
        // Quest class
        new FeedableBeasts();
    }
    
	public FeedableBeasts()
	{
		super(-1, "feedable_beasts", "ai");
		// Register NPCs
		for (int id : FEEDABLE_BEASTS)
		{
			registerNPC(id);
		}
        
        GrowthCapableMob temp;

        final int[][] Kookabura_0_Gold = {{1452, 1453, 1454, 1455}};
        final int[][] Kookabura_0_Crystal = {{1456, 1457, 1458, 1459}};
        final int[][] Kookabura_1_Gold_1= {{1460, 1462}};
        final int[][] Kookabura_1_Gold_2 = {{1461, 1463}};
        final int[][] Kookabura_1_Crystal_1 = {{1464, 1466}};
        final int[][] Kookabura_1_Crystal_2 = {{1465, 1467}};
        final int[][] Kookabura_2_1 = {{1468, 1469}, {12783, 12784}};
        final int[][] Kookabura_2_2 = {{1468, 1469}, {12783, 12784}};
        
        final int[][] Buffalo_0_Gold = {{1471, 1472, 1473, 1474}};
        final int[][] Buffalo_0_Crystal = {{1475, 1476, 1477, 1478}};
        final int[][] Buffalo_1_Gold_1 = {{1479, 1481}};
        final int[][] Buffalo_1_Gold_2 = {{1481, 1482}};
        final int[][] Buffalo_1_Crystal_1 = {{1483, 1485}};
        final int[][] Buffalo_1_Crystal_2 = {{1484, 1486}};
        final int[][] Buffalo_2_1 = {{1487, 1488}, {12785, 12786}};
        final int[][] Buffalo_2_2 = {{1487, 1488}, {12785, 12786}};

        final int[][] Cougar_0_Gold = {{1490, 1491, 1492, 1493}};
        final int[][] Cougar_0_Crystal = {{1494, 1495, 1496, 1497}};
        final int[][] Cougar_1_Gold_1 = {{1498, 1500}};
        final int[][] Cougar_1_Gold_2 = {{1499, 1501}};
        final int[][] Cougar_1_Crystal_1 = {{1502, 1504}};
        final int[][] Cougar_1_Crystal_2 = {{1503, 1505}};
        final int[][] Cougar_2_1 = {{1506, 1507}, {12787, 12788}};
        final int[][] Cougar_2_2 = {{1506, 1507}, {12787, 12788}};

        // Alpen Kookabura
        temp = new GrowthCapableMob(0,100);
        temp.addMobs(GOLDEN_SPICE,Kookabura_0_Gold);
        temp.addMobs(CRYSTAL_SPICE,Kookabura_0_Crystal);
        _growthCapableMobs.put(1451, temp);
        
        temp = new GrowthCapableMob(1,40);
        temp.addMobs(GOLDEN_SPICE,Kookabura_1_Gold_1);
        _growthCapableMobs.put(1452, temp);
        _growthCapableMobs.put(1454, temp);
                
        temp = new GrowthCapableMob(1,40);
        temp.addMobs(GOLDEN_SPICE,Kookabura_1_Gold_2);
        _growthCapableMobs.put(1453, temp);
        _growthCapableMobs.put(1455, temp);        

        temp = new GrowthCapableMob(1,40);
        temp.addMobs(CRYSTAL_SPICE,Kookabura_1_Crystal_1);
        _growthCapableMobs.put(1456, temp);
        _growthCapableMobs.put(1458, temp);
                
        temp = new GrowthCapableMob(1,40);
        temp.addMobs(CRYSTAL_SPICE,Kookabura_1_Crystal_2);
        _growthCapableMobs.put(1457, temp);
        _growthCapableMobs.put(1459, temp);
                
        temp = new GrowthCapableMob(2,25);
        temp.addMobs(GOLDEN_SPICE,Kookabura_2_1);
        _growthCapableMobs.put(1460, temp);
        _growthCapableMobs.put(1462, temp);
                
        temp = new GrowthCapableMob(2,25);
        temp.addMobs(GOLDEN_SPICE,Kookabura_2_2);
        _growthCapableMobs.put(1461, temp);
        _growthCapableMobs.put(1463, temp);
                
        temp = new GrowthCapableMob(2,25);
        temp.addMobs(CRYSTAL_SPICE,Kookabura_2_1);
        _growthCapableMobs.put(1464, temp);
        _growthCapableMobs.put(1466, temp);
                
        temp = new GrowthCapableMob(2,25);
        temp.addMobs(CRYSTAL_SPICE,Kookabura_2_2);
        _growthCapableMobs.put(1465, temp);
        _growthCapableMobs.put(1467, temp);
        
        // Alpen Buffalo
        temp = new GrowthCapableMob(0,100);
        temp.addMobs(GOLDEN_SPICE,Buffalo_0_Gold);
        temp.addMobs(CRYSTAL_SPICE,Buffalo_0_Crystal);
        _growthCapableMobs.put(1470, temp);
        
        temp = new GrowthCapableMob(1,40);
        temp.addMobs(GOLDEN_SPICE,Buffalo_1_Gold_1);
        _growthCapableMobs.put(1471, temp);
        _growthCapableMobs.put(1473, temp);
                
        temp = new GrowthCapableMob(1,40);
        temp.addMobs(GOLDEN_SPICE,Buffalo_1_Gold_2);
        _growthCapableMobs.put(1472, temp);
        _growthCapableMobs.put(1474, temp);        

        temp = new GrowthCapableMob(1,40);
        temp.addMobs(CRYSTAL_SPICE,Buffalo_1_Crystal_1);
        _growthCapableMobs.put(1475, temp);
        _growthCapableMobs.put(1477, temp);
                
        temp = new GrowthCapableMob(1,40);
        temp.addMobs(CRYSTAL_SPICE,Buffalo_1_Crystal_2);
        _growthCapableMobs.put(1476, temp);
        _growthCapableMobs.put(1478, temp);
                
        temp = new GrowthCapableMob(2,25);
        temp.addMobs(GOLDEN_SPICE,Buffalo_2_1);
        _growthCapableMobs.put(1479, temp);
        _growthCapableMobs.put(1481, temp);
                
        temp = new GrowthCapableMob(2,25);
        temp.addMobs(GOLDEN_SPICE,Buffalo_2_2);
        _growthCapableMobs.put(1480, temp);
        _growthCapableMobs.put(1482, temp);
                
        temp = new GrowthCapableMob(2,25);
        temp.addMobs(CRYSTAL_SPICE,Buffalo_2_1);
        _growthCapableMobs.put(1483, temp);
        _growthCapableMobs.put(1485, temp);
                
        temp = new GrowthCapableMob(2,25);
        temp.addMobs(CRYSTAL_SPICE,Buffalo_2_2);
        _growthCapableMobs.put(1484, temp);
        _growthCapableMobs.put(1486, temp);
        
        // Alpen Cougar
        temp = new GrowthCapableMob(0,100);
        temp.addMobs(GOLDEN_SPICE,Cougar_0_Gold);
        temp.addMobs(CRYSTAL_SPICE,Cougar_0_Crystal);
        _growthCapableMobs.put(1489, temp);
        
        temp = new GrowthCapableMob(1,40);
        temp.addMobs(GOLDEN_SPICE,Cougar_1_Gold_1);
        _growthCapableMobs.put(1490, temp);
        _growthCapableMobs.put(1492, temp);
                
        temp = new GrowthCapableMob(1,40);
        temp.addMobs(GOLDEN_SPICE,Cougar_1_Gold_2);
        _growthCapableMobs.put(1491, temp);
        _growthCapableMobs.put(1493, temp);        

        temp = new GrowthCapableMob(1,40);
        temp.addMobs(CRYSTAL_SPICE,Cougar_1_Crystal_1);
        _growthCapableMobs.put(1494, temp);
        _growthCapableMobs.put(1496, temp);
                
        temp = new GrowthCapableMob(1,40);
        temp.addMobs(CRYSTAL_SPICE,Cougar_1_Crystal_2);
        _growthCapableMobs.put(1495, temp);
        _growthCapableMobs.put(1497, temp);
                
        temp = new GrowthCapableMob(2,25);
        temp.addMobs(GOLDEN_SPICE,Cougar_2_1);
        _growthCapableMobs.put(1498, temp);
        _growthCapableMobs.put(1500, temp);
                
        temp = new GrowthCapableMob(2,25);
        temp.addMobs(GOLDEN_SPICE,Cougar_2_2);
        _growthCapableMobs.put(1499, temp);
        _growthCapableMobs.put(1501, temp);
                
        temp = new GrowthCapableMob(2,25);
        temp.addMobs(CRYSTAL_SPICE,Cougar_2_1);
        _growthCapableMobs.put(1502, temp);
        _growthCapableMobs.put(1504, temp);
                
        temp = new GrowthCapableMob(2,25);
        temp.addMobs(CRYSTAL_SPICE,Cougar_2_2);
        _growthCapableMobs.put(1503, temp);
        _growthCapableMobs.put(1505, temp);        
	}
	
    public void spawnNext(L2NpcInstance npc, int growthLevel, L2PcInstance player, int food)
    {
        int npcId = npc.getNpcId();
        int nextNpcId = 0;

        // Find the next mob to spawn, based on the current npcId, growth level, and food.
        if (growthLevel == 2)
        {
            // If tamed, the mob that will spawn depends on the class type (fighter/mage) of the player!
            if (Rnd.get(2) == 0)
            {
                if (player.getClassId().isMage())
                {
                    nextNpcId = _growthCapableMobs.get(npcId).getMob(food, 1, 1);
                }
                else
                {
                    nextNpcId = _growthCapableMobs.get(npcId).getMob(food, 1, 0);
                }
            }  
            else
            {
                /**
                 * If not tamed, there is a small chance that it has "mad cow" disease.
                 * That is a stronger-than-normal animal that attacks its feeder.
                 */
                if (Rnd.get(5) == 0)
                {
                    nextNpcId = _growthCapableMobs.get(npcId).getMob(food, 0, 1);
                }
                else
                {
                    nextNpcId = _growthCapableMobs.get(npcId).getMob(food, 0, 0);
                }
            }
        }
        else
        {
            // All other levels of growth are straight-forward
        	nextNpcId = _growthCapableMobs.get(npcId).getRandomMob(food);
        }
        
        // Remove the feedinfo of the mob that got despawned, if any
        if (_feedInfo.containsKey(npc.getObjectId())) 
        {
            if (_feedInfo.get(npc.getObjectId()) == player.getObjectId())
                _feedInfo.remove(npc.getObjectId());
        }
        // Despawn the old mob
        if (_growthCapableMobs.get(npcId).getGrowthLevel() == 0)
        {
        	npc.getSpawn().decreaseCount(npc);
        	npc.deleteMe();
        }
        else
        {
            npc.deleteMe();
        }
        
        /**
         * If this is finally a trained mob, then despawn any other trained mobs that the 
         * player might have and initialize the Tamed Beast.
         */
        if (Util.contains(TAMED_BEASTS, nextNpcId))
        {
            L2TamedBeastInstance oldTrained = player.getTrainedBeast();
            if (oldTrained != null)
            {
                oldTrained.doDespawn();
            }
                
            L2NpcTemplate template = NpcTable.getInstance().getTemplate(nextNpcId);
            L2TamedBeastInstance nextNpc = new L2TamedBeastInstance(IdFactory.getInstance().getNextId(), template, player, FOOD_SKILL.get(food), npc.getX(), npc.getY(), npc.getZ());
            nextNpc.setRunning();

            int objectId = nextNpc.getObjectId();
            
            QuestState st = player.getQuestState("20_BringUpWithLove");
            if (st != null)
            {
                if (Rnd.get(100) <= 5 && st.getQuestItemsCount(7185) == 0)
                {
                	/**
                	 * If player has quest 20 going, give quest item.
                	 * It's easier to hardcode it in here than to try and repeat this stuff in the quest.
                	 */
                    st.giveItems(7185,1);
                    st.set("cond","2");		
                }
            }
            // Also, perform a rare random chat
            int rand = Rnd.get(20);
            if (rand == 0)
            {
            	npc.broadcastPacket(new NpcSay(objectId,0,nextNpc.getNpcId(), player.getName()+", will you show me your hideaway?"));
            }
            else if (rand == 1)
            {
            	npc.broadcastPacket(new NpcSay(objectId,0,nextNpc.getNpcId(), player.getName()+", whenever I look at spice, I think about you."));
            }
            else if (rand == 2)
            {
            	npc.broadcastPacket(new NpcSay(objectId,0,nextNpc.getNpcId(), player.getName()+", you do not need to return to the village.  I will give you strength"));
            }
            else if (rand == 3)
            {
            	npc.broadcastPacket(new NpcSay(objectId,0,nextNpc.getNpcId(), "Thanks, "+player.getName()+".  I hope I can help you"));
            }
            else if (rand == 4)
            {
            	npc.broadcastPacket(new NpcSay(objectId,0,nextNpc.getNpcId(), player.getName()+", what can I do to help you?"));
            }
        }
        else
        {
            /**
             * If not trained, the newly spawned mob will automatically be aggro against its feeder 
             * (what happened to "never bite the hand that feeds you" anyway?!).
             */
            L2Attackable nextNpc = (L2Attackable) this.addSpawn(nextNpcId,npc);
            
            // Register the player in the feed info for the mob that just spawned
            _feedInfo.put(nextNpc.getObjectId(),player.getObjectId());
            nextNpc.setRunning();
            nextNpc.addDamageHate(player,0,99999);
            nextNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
        }
    }

	@Override
	public String onSkillSee(L2NpcInstance npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
        /**
         * This behavior is only run when the target of skill is the passed npc (chest) 
         * i.e. when the player is attempting to open the chest using a skill.
         */
        if (!Util.contains(targets, npc))
        {
        	return super.onSkillSee(npc,caster,skill,targets,isPet);
        }
        
        // Gather some values on local variables
        int npcId = npc.getNpcId();
        int skillId = skill.getId();
        // Check if the npc and skills used are valid for this script.  Exit if invalid.
        if (!Util.contains(FEEDABLE_BEASTS,npcId) || (skillId !=SKILL_GOLDEN_SPICE && skillId != SKILL_CRYSTAL_SPICE))
        {
        	return super.onSkillSee(npc,caster,skill,targets,isPet);
        }

        // First gather some values on local variables
        int objectId = npc.getObjectId();
        int growthLevel = 3;  // If a mob is in FEEDABLE_BEASTS but not in _growthCapableMobs, then it's at max growth (3)
        if (_growthCapableMobs.containsKey(npcId))
        {
            growthLevel = _growthCapableMobs.get(npcId).getGrowthLevel();
        }

        /**
         * Prevent exploit which allows 2 players to simultaneously raise the same 0-growth beast.
         * If the mob is at 0th level (when it still listens to all feeders) lock it to the first feeder!
         */
        if (growthLevel == 0 && _feedInfo.containsKey(objectId))
        {
        	return super.onSkillSee(npc,caster,skill,targets,isPet);
        }
        
		_feedInfo.put(objectId,caster.getObjectId());

        int food = 0;
        if (skillId == SKILL_GOLDEN_SPICE)
        {
            food = GOLDEN_SPICE;
        }
        else if (skillId == SKILL_CRYSTAL_SPICE)
        {
            food = CRYSTAL_SPICE;
        }

        // Display the social action of the beast eating the food.
        npc.broadcastPacket(new SocialAction(objectId, 2));

        // If this pet can't grow, it's all done.
        if (_growthCapableMobs.containsKey(npcId))
        {
            // Do nothing if this mob doesn't eat the specified food (food gets consumed but has no effect).
            if (_growthCapableMobs.get(npcId).getMob(food, 0, 0) == null )
            {
            	return super.onSkillSee(npc,caster,skill,targets,isPet);
            }

            // Rare random talk...
            if (Rnd.get(20) == 0)
            {
                npc.broadcastPacket(new NpcSay(objectId,0,npc.getNpcId(),TEXT[growthLevel][Rnd.get(TEXT[growthLevel].length)]));
            }

            if (growthLevel > 0 && _feedInfo.get(objectId) != caster.getObjectId())
            {
                /**
                 * Check if this is the same player as the one who raised it from growth 0.
                 * If no, then do not allow a chance to raise the pet (food gets consumed but has no effect).
                 */
                return super.onSkillSee(npc,caster,skill,targets,isPet);
            }

            // Polymorph the mob, with a certain chance, given its current growth level
            if (Rnd.get(100) < _growthCapableMobs.get(npcId).getChance())
            {
                this.spawnNext(npc, growthLevel,caster,food);
            }
        }
        else if (Util.contains(TAMED_BEASTS,npcId) && npc instanceof L2TamedBeastInstance)
        {
        	L2TamedBeastInstance beast = ((L2TamedBeastInstance)npc);
            if (skillId == beast.getFoodType())
            {
                beast.onReceiveFood();
                beast.broadcastPacket(new NpcSay(objectId,0,npcId,TAMED_TEXT[Rnd.get(TAMED_TEXT.length)]));
            }
        }
        return super.onSkillSee(npc,caster,skill,targets,isPet);
	}

	@Override
	public String onKill(L2NpcInstance npc, L2PcInstance killer, boolean isPet)
	{
        // Remove the feed info of the mob that got killed, if any
        if (_feedInfo.containsKey(npc.getObjectId()))
        {
            _feedInfo.remove(npc.getObjectId());
        }
        return super.onKill(npc,killer,isPet);
	}
}
