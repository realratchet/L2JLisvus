# Contributed by t0rm3nt0r to the Official L2J Datapack Project (adapted for L2JLisvus by roko91).

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "340_SubjugationOfLizardmen"

#NPC
WEIZ = 7385
ADONIUS = 7375
LEVIAN = 7037
CHEST = 7989
#Quest item
CARGO = 4255
HOLY = 4256
ROSARY = 4257
TOTEM = 4258
#Mobs
MOBS_1 = [ 8, 10, 14 ]
MOBS_2 = [ 357, 1100, 356, 1101 ]
BIFRON = 10146

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [TOTEM]
 
 def onAdvEvent (self,event,npc,player) :
     st = player.getQuestState(qn)
     if not st: return
     htmltext = event
     if event == "7385-03.htm" :
       st.set("cond","1")
       st.setState(State.STARTED)
       st.playSound("ItemSound.quest_accept")
     elif event == "7385-07.htm" :
       st.takeItems(CARGO,-1)
       st.giveItems(57,4090)       
       st.set("cond","2")
       st.playSound("ItemSound.quest_middle")
     elif event == "7385-09.htm" :
       st.takeItems(CARGO,-1)
       st.giveItems(57,4090)
     elif event == "7385-10.htm" :
       st.takeItems(CARGO,-1)
       st.giveItems(57,4090)
       st.exitQuest(1)
     elif event == "7375-02.htm" :
       st.set("cond","3")
       st.playSound("ItemSound.quest_middle")
     elif event == "7037-02.htm" :
       st.set("cond","5")
       st.playSound("ItemSound.quest_middle")
     elif event == "7989-02.htm" :
       st.giveItems(TOTEM,1)
       st.set("cond","6")
       st.playSound("ItemSound.quest_middle")
       npc.doDie(npc)
     return htmltext

 def onTalk (self,npc,st):
     npcId = npc.getNpcId()
     htmltext = JQuest.getNoQuestMsg()
     id = st.getState()
     cond = st.getInt("cond")
     kargo = st.getQuestItemsCount(CARGO)
     rosary = st.getQuestItemsCount(ROSARY)
     holy = st.getQuestItemsCount(HOLY)
     totem = st.getQuestItemsCount(TOTEM)
     if id == State.CREATED and npcId == WEIZ :
       if st.getPlayer().getLevel() < 17 :
         htmltext = "7385-01.htm"
         st.exitQuest(1)
       else :
         htmltext = "7385-02.htm"
     elif id == State.STARTED :
       if npcId == WEIZ :
         if cond == 1 :
           if kargo < 30 :
             htmltext = "7385-05.htm"
           else :
             htmltext = "7385-06.htm"
         elif cond == 2 :
           htmltext = "7385-11.htm"
         elif cond == 7 :
           st.giveItems(57,14700)
           htmltext = "7385-13.htm"
           st.exitQuest(1)
           st.playSound("ItemSound.quest_finish")
       elif npcId == ADONIUS :
         if cond == 2 :
           htmltext = "7375-01.htm"
         elif cond == 3 :
           if rosary == 1 and holy == 1 :
             st.takeItems(HOLY,-1)
             st.takeItems(ROSARY,-1)             
             htmltext = "7375-04.htm"
             st.set("cond","4")
             st.playSound("ItemSound.quest_middle")
           else :
             htmltext = "7375-03.htm"
         elif cond == 4 :
           htmltext = "7375-05.htm"
       elif npcId == LEVIAN :
         if cond == 4 :
           htmltext = "7037-01.htm"
         elif cond == 5 :
           htmltext = "7037-03.htm"
         elif cond == 6 :
           st.takeItems(TOTEM,-1)
           st.set("cond","7")
           st.playSound("ItemSound.quest_middle")
           htmltext = "7037-04.htm"
         elif cond == 7 :
           htmltext = "7037-05.htm"
       elif npcId == CHEST :
         if cond == 5 :
           htmltext = "7989-01.htm"
         elif cond == 6 :
           htmltext = "7989-03.htm"
     return htmltext
    
 def onKill(self,npc,player,isPet):
     st = player.getQuestState(qn)
     if not st : return
     if st.getState() != State.STARTED : return
     npcId = npc.getNpcId()
     chance = st.getRandom(100)
     kargo = st.getQuestItemsCount(CARGO)
     holy = st.getQuestItemsCount(HOLY)
     rosary = st.getQuestItemsCount(ROSARY)
     if st:
       if npcId in MOBS_1 :
         if (chance < 40) and (kargo < 30) :
           st.giveItems(CARGO,1)
           st.playSound("ItemSound.quest_itemget")
       elif npcId in MOBS_2 :
         if (chance < 15) and (not holy) :
           st.giveItems(HOLY,1)
           st.playSound("ItemSound.quest_middle")
         elif (chance < 15) and (not rosary) :
           st.giveItems(ROSARY,1)
           st.playSound("ItemSound.quest_middle")
       elif npcId == BIFRON :
         st.addSpawn(CHEST,npc,30000)
     return

QUEST = Quest(340, qn, "Subjugation of Lizardmen")
QUEST.addStartNpc(WEIZ)

QUEST.addTalkId(WEIZ)
QUEST.addTalkId(ADONIUS)
QUEST.addTalkId(LEVIAN)
QUEST.addTalkId(CHEST)

for i in MOBS_1 + MOBS_2 + [10146] :
    QUEST.addKillId(i)