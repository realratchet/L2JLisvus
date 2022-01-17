# Made by Vice - cleanup by DrLecter
# this script is part of the Official L2J Datapack Project.
# Visit http://forum.l2jdp.com for more details.
import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "618_IntoTheFlame"
#NPCs
KLEIN = 8540
HILDA = 8271

#QUEST ITEMS
VACUALITE_ORE,VACUALITE,FLOATING_STONE = range(7265,7268)

#CHANCE
CHANCE_FOR_QUEST_ITEMS = 50

class Quest (JQuest) :

   def __init__(self,id,name,descr):
       JQuest.__init__(self,id,name,descr)
       self.questItemIds = range(7265,7267)

   def onEvent (self,event,st) :
      htmltext = event
      cond = st.getInt("cond")
      if event == "8540-03.htm" and cond == 0 :
         st.setState(State.STARTED)
         st.set("cond","1")
         st.playSound("ItemSound.quest_accept")
      elif event == "8540-05.htm" :
         if st.getQuestItemsCount(VACUALITE) and cond == 4 :
            st.takeItems(VACUALITE,1)
            st.giveItems(FLOATING_STONE,1)
            st.playSound("ItemSound.quest_finish")
            st.exitQuest(1)
         else :
            htmltext = "8540-03.htm"
      elif event == "8271-02.htm" and cond == 1 :
         st.set("cond","2")
         st.playSound("ItemSound.quest_middle")
      elif event == "8271-05.htm" :
         if cond == 3 and st.getQuestItemsCount(VACUALITE_ORE) == 50 :
            st.takeItems(VACUALITE_ORE,-1)
            st.giveItems(VACUALITE,1)
            st.set("cond","4")
            st.playSound("ItemSound.quest_middle")
         else :
            htmltext = "8271-03.htm"
      return htmltext

   def onTalk (self,npc,st) :
      htmltext = JQuest.getNoQuestMsg()
      npcId = npc.getNpcId()
      cond = st.getInt("cond")
      id = st.getState()
      if npcId == KLEIN :
         if cond == 0 :
            if st.getPlayer().getLevel() < 60 :
               htmltext = "8540-01.htm"
               st.exitQuest(1)
            else :
               htmltext = "8540-02.htm"
         elif cond == 4 and st.getQuestItemsCount(VACUALITE) :
            htmltext = "8540-04.htm"
         else :
            htmltext = "8540-03.htm"
      elif npcId == HILDA :
         if cond == 1 :
            htmltext = "8271-01.htm"
         elif cond == 3 and st.getQuestItemsCount(VACUALITE_ORE) == 50 :
            htmltext = "8271-04.htm"
         elif cond == 4 :
            htmltext = "8271-06.htm"
         else :
            htmltext = "8271-03.htm"
      return htmltext

   def onKill(self,npc,player,isPet) :
      st = player.getQuestState(qn)
      if st :
         count = st.getQuestItemsCount(VACUALITE_ORE)
         if st.getInt("cond") == 2 and count < 50 :
            chance = CHANCE_FOR_QUEST_ITEMS * Config.RATE_DROP_QUEST
            numItems, chance = divmod(chance,100)
            if st.getRandom(100) < chance : 
               numItems += 1
            if numItems :
               if count + numItems >= 50 :
                  numItems = 50 - count
                  st.playSound("ItemSound.quest_middle")
                  st.set("cond","3")
               else:
                  st.playSound("ItemSound.quest_itemget")   
               st.giveItems(VACUALITE_ORE,int(numItems)) 
      return

QUEST = Quest(618,qn,"Into the Flame")
QUEST.addStartNpc(KLEIN)

QUEST.addTalkId(KLEIN)
QUEST.addTalkId(HILDA)

for mob in range(1274,1278)+range(1282,1286)+range(1290,1294) :
    QUEST.addKillId(mob)