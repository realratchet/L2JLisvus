# Made by disKret
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

#NPC
BABENCO = 7334
BATHIS = 7332

#MOBS
M_LIZARDMAN = 919
M_LIZARDMAN_SCOUT = 920
M_LIZARDMAN_GUARD = 921

#QUEST DROPS
BLACK_BONE_NECKLACE = 7178
RED_BONE_NECKLACE = 7179
INCENSE_POUCH = 7180
GEM_OF_MAILLE = 7181

#REWARDS
GREEN_COLORED_LURE_HG = 6521
BABy_DUCK_RODE = 6529
FISHING_SHOT_NG = 6535

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(7178,7182)

 def onEvent (self,event,st) :
   htmltext = event
   if event == "7334-1.htm" :
     st.set("cond","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   if event == "7332-1.htm" :
     st.set("cond","2")
   if event == "7332-3.htm" :
     if st.getQuestItemsCount(BLACK_BONE_NECKLACE) == st.getQuestItemsCount(RED_BONE_NECKLACE) == 100 :   
       st.takeItems(BLACK_BONE_NECKLACE,100)
       st.takeItems(RED_BONE_NECKLACE,100)       
       st.set("cond","4")
     else :
       htmltext = "You don't have all the items required."
   if event == "7332-5.htm" :
     if st.getQuestItemsCount(INCENSE_POUCH) == st.getQuestItemsCount(GEM_OF_MAILLE) == 30 :
       st.takeItems(INCENSE_POUCH,30)
       st.takeItems(GEM_OF_MAILLE,30)
       st.giveItems(GREEN_COLORED_LURE_HG,60)
       st.giveItems(BABy_DUCK_RODE,1)
       st.giveItems(FISHING_SHOT_NG,500)
       st.setState(State.COMPLETED)
       st.set("cond","0")
       st.playSound("ItemSound.quest_finish")
     else :
       htmltext = "You don't have all the items required."
   return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   npcId = npc.getNpcId()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   cond = st.getInt("cond")
   if npcId == BABENCO and cond == 0 :
     if id == State.COMPLETED :
       htmltext = JQuest.getAlreadyCompletedMsg()
     elif st.getPlayer().getLevel() >= 20 : # and st.getPlayer().getLevel() <= 28:
       htmltext = "7334-0.htm"
     else :
       st.exitQuest(1)
   elif npcId == BATHIS :
     if st.getInt("cond") == 1 :
       htmltext = "7332-0.htm"
     elif st.getQuestItemsCount(BLACK_BONE_NECKLACE) == st.getQuestItemsCount(RED_BONE_NECKLACE) == 100 :
       htmltext = "7332-2.htm"
     elif st.getQuestItemsCount(INCENSE_POUCH) == st.getQuestItemsCount(GEM_OF_MAILLE) == 30 :
       htmltext = "7332-4.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   partyMember = self.getRandomPartyMember(player,npc,"2")
   npcId = npc.getNpcId()
   if partyMember:
     st = partyMember.getQuestState("39_RedEyedInvaders")
     if npcId in [919,920] and st.getQuestItemsCount(BLACK_BONE_NECKLACE) < 100 :
       st.giveItems(BLACK_BONE_NECKLACE,1)
       if st.getQuestItemsCount(BLACK_BONE_NECKLACE) == 100 and st.getQuestItemsCount(RED_BONE_NECKLACE) == 100:
         st.playSound("ItemSound.quest_middle")
         st.set("cond","3")
       else:
         st.playSound("ItemSound.quest_itemget")	
     if npcId == 921 and st.getQuestItemsCount(RED_BONE_NECKLACE) < 100 :
       st.giveItems(RED_BONE_NECKLACE,1)
       if st.getQuestItemsCount(BLACK_BONE_NECKLACE) == 100 and st.getQuestItemsCount(RED_BONE_NECKLACE) == 100:
         st.playSound("ItemSound.quest_middle")
         st.set("cond","3")
       else:
         st.playSound("ItemSound.quest_itemget")
   else :
     partyMember = self.getRandomPartyMember(player,npc,"4")
     if partyMember :
       st = partyMember.getQuestState("39_RedEyedInvaders")
       if npcId in [920,921] and st.getQuestItemsCount(INCENSE_POUCH) < 30 :
         st.giveItems(INCENSE_POUCH,1)
         if st.getQuestItemsCount(INCENSE_POUCH) == 30 and st.getQuestItemsCount(GEM_OF_MAILLE) == 30:
           st.playSound("ItemSound.quest_middle")
           st.set("cond","5")
         else:
           st.playSound("ItemSound.quest_itemget")	
       if npcId == 925 and st.getQuestItemsCount(GEM_OF_MAILLE) < 30 :
         st.giveItems(GEM_OF_MAILLE,1)
         if st.getQuestItemsCount(INCENSE_POUCH) == 30 and st.getQuestItemsCount(GEM_OF_MAILLE) == 30:
           st.playSound("ItemSound.quest_middle")
           st.set("cond","5")
         else:
           st.playSound("ItemSound.quest_itemget")	
   return

QUEST = Quest(39,"39_RedEyedInvaders","Red Eyed Invaders")
QUEST.addStartNpc(7334)

QUEST.addTalkId(7334)
QUEST.addTalkId(7332)

QUEST.addKillId(919)
QUEST.addKillId(920)
QUEST.addKillId(921)
QUEST.addKillId(925)