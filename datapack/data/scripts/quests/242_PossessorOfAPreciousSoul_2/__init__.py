# Made by disKret
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

#NPC
VIRGIL = 8742
KASSANDRA = 8743
OGMAR = 8744
FALLEN_UNICORN = 8746
PURE_UNICORN = 8747
CORNERSTONE = 8748
MYSTERIOUS_KNIGHT = 8751
ANGEL_CORPSE = 8752
KALIS = 7759
MATILD = 7738

#QUEST ITEM
VIRGILS_LETTER = 7677
GOLDEN_HAIR = 7590
ORB_OF_BINDING = 7595
SORCERY_INGREDIENT = 7596
CARADINE_LETTER = 7678

#CHANCE FOR HAIR DROP
CHANCE_FOR_HAIR = 20

#MOB
RESTRAINER_OF_GLORY = 5317

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [GOLDEN_HAIR, ORB_OF_BINDING, SORCERY_INGREDIENT]

 def onEvent (self,event,st) :
   htmltext = event
   cond = st.getInt("cond")
   if event == "8742-3.htm" :
     if cond == 0 :
       st.setState(State.STARTED)
       st.takeItems(VIRGILS_LETTER,1)
       st.set("cond","1")
       st.playSound("ItemSound.quest_accept")
   elif event == "8743-5.htm" :
     if cond == 1 :
       st.set("cond","2")
       st.setState(State.STARTED)
       st.playSound("ItemSound.quest_accept")
   elif event == "8744-2.htm" :
     if cond == 2 :
       st.set("cond","3")
       st.playSound("ItemSound.quest_middle")
   elif event == "8751-2.htm" :
     if cond == 3 :
       st.set("cond","4")
       st.playSound("ItemSound.quest_middle")
   elif event == "7759-2.htm" :
     if cond == 6 :
       st.set("cond","7")
       st.playSound("ItemSound.quest_middle")
   elif event == "7738-2.htm" :
     if cond == 7 :
       st.set("cond","8")
       st.giveItems(SORCERY_INGREDIENT,1)
       st.playSound("ItemSound.quest_middle")
   elif event == "7759-5.htm" :
     if cond == 8 :
       st.set("cond","9")
       st.set("awaitsDrops","1")
       st.takeItems(GOLDEN_HAIR,1)
       st.takeItems(SORCERY_INGREDIENT,1)
       st.playSound("ItemSound.quest_middle")
   return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   cornerstones = st.getInt("cornerstones")
   npcId = npc.getNpcId()
   id = st.getState()
   if npcId != VIRGIL and id != State.STARTED :
     return htmltext
   if id == State.CREATED :
     st.set("cond","0")
     st.set("cornerstones","0")
   cond = st.getInt("cond")
   if st.getPlayer().isSubClassActive() :
     if npcId == VIRGIL :
         if cond == 0 and st.getQuestItemsCount(VIRGILS_LETTER) == 1 :
            if id == State.COMPLETED :
                htmltext = JQuest.getAlreadyCompletedMsg()
            elif st.getPlayer().getLevel() < 60 : 
                htmltext = "8742-2.htm"
                st.exitQuest(1)
            elif st.getPlayer().getLevel() >= 60 :
                htmltext = "8742-1.htm"
         elif cond == 1 :
             htmltext = "8742-4.htm"
         elif cond == 11 :
             htmltext = "8742-6.htm"
             st.set("cond","0")
             st.set("cornerstones","0")
             st.giveItems(CARADINE_LETTER,1)
             st.addExpAndSp(455764,0)
             st.playSound("ItemSound.quest_finish")
             st.setState(State.COMPLETED)
     elif npcId == KASSANDRA :
         if cond == 1 :
             htmltext = "8743-1.htm"
         elif cond == 2 :
             htmltext = "8743-6.htm"
         elif cond == 11 :
             htmltext = "8743-7.htm"
     elif npcId == OGMAR :
         if cond == 2 :
             htmltext = "8744-1.htm"
         elif cond == 3 :
             htmltext = "8744-3.htm"
     elif npcId == MYSTERIOUS_KNIGHT :
         if cond == 3 :
             htmltext = "8751-1.htm"
         elif cond == 4 :
             htmltext = "8751-3.htm"
         elif cond == 5 and st.getQuestItemsCount(GOLDEN_HAIR) == 1 :
             htmltext = "8751-4.htm"
             st.set("cond","6")
             st.playSound("ItemSound.quest_middle")
         elif cond == 6 :
             htmltext = "8751-5.htm"
     elif npcId == ANGEL_CORPSE :
         if cond == 4 :
             npc.doDie(npc)
             chance = st.getRandom(100)
             if CHANCE_FOR_HAIR < chance :
               htmltext = "8752-2.htm"
             else :
               st.set("cond","5")
               st.giveItems(GOLDEN_HAIR,1)
               st.playSound("ItemSound.quest_middle")
               htmltext = "8752-1.htm"
         elif cond == 5 :
             htmltext = "8752-2.htm"
     elif npcId == KALIS :
         if cond == 6 :
             htmltext = "7759-1.htm"
         elif cond == 7 :
             htmltext = "7759-3.htm"
         elif cond == 8 and st.getQuestItemsCount(SORCERY_INGREDIENT) == 1 :
             htmltext = "7759-4.htm"
         elif cond == 9 :
             htmltext = "7759-6.htm"
     elif npcId == MATILD :
         if cond == 7 :
             htmltext = "7738-1.htm"
         elif cond == 8 :
             htmltext = "7738-3.htm"
     elif npcId == FALLEN_UNICORN and npc.getCurrentHp() > 0 :
         if cond == 9 :
             htmltext = "8746-1.htm"
         elif cond == 10 :
             htmltext = "8746-2.htm"
             npc.doDie(npc)
             st.addSpawn(PURE_UNICORN,npc.getX(),npc.getY(),npc.getZ(),60000)
     elif npcId == CORNERSTONE :
         if cond == 9 and st.getQuestItemsCount(ORB_OF_BINDING) == 0 :
             htmltext = "8748-1.htm"
         elif cond == 9 and st.getQuestItemsCount(ORB_OF_BINDING) >= 1 :
             htmltext = "8748-2.htm"
             st.takeItems(ORB_OF_BINDING,1)
             npc.doDie(npc)
             st.set("cornerstones",str(cornerstones+1))
             st.playSound("ItemSound.quest_middle")
             if cornerstones == 3 :
                 st.set("cond","10")
                 st.playSound("ItemSound.quest_middle")
     elif npcId == PURE_UNICORN :
         if cond == 10 :
             st.set("cond","11")
             st.playSound("ItemSound.quest_middle")
             htmltext = "8747-1.htm"
         elif cond == 11 :
             htmltext = "8747-2.htm"
   else :
     htmltext = "<html><body>This quest may only be undertaken by sub-class characters of level 60 or above.</body></html>"
   return htmltext

 def onKill (self,npc,player,isPet):
    # get a random party member that awaits for drops from this quest
    partyMember = self.getRandomPartyMember(player,npc,"awaitsDrops","1")
    if not partyMember : return
    st = partyMember.getQuestState("242_PossessorOfAPreciousSoul_2")
    if st.getInt("cond") == 9 and st.getQuestItemsCount(ORB_OF_BINDING) <= 4 :
      st.giveItems(ORB_OF_BINDING,1)
      st.playSound("ItemSound.quest_itemget")
      if st.getQuestItemsCount(ORB_OF_BINDING) == 5 :
        st.unset("awaitsDrops")
    return 

QUEST = Quest(242,"242_PossessorOfAPreciousSoul_2","Possessor Of A Precious Soul - 2")
QUEST.addStartNpc(VIRGIL)

QUEST.addTalkId(VIRGIL)
QUEST.addTalkId(KASSANDRA)
QUEST.addTalkId(OGMAR)
QUEST.addTalkId(MYSTERIOUS_KNIGHT)
QUEST.addTalkId(ANGEL_CORPSE)
QUEST.addTalkId(KALIS)
QUEST.addTalkId(MATILD)
QUEST.addTalkId(FALLEN_UNICORN)
QUEST.addTalkId(CORNERSTONE)
QUEST.addTalkId(PURE_UNICORN)

QUEST.addKillId(RESTRAINER_OF_GLORY)