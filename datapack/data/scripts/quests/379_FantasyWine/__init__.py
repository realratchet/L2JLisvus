# Made by disKret & DrLecter (adapted for L2JLisvus by roko91)

import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "379_FantasyWine"

#NPC
HARLAN = 7074

#MOBS
ENKU_CHAMPION = 291
ENKU_SHAMAN = 292

#ITEMS
LEAF = 5893
STONE = 5894

MOB = {
ENKU_CHAMPION:[LEAF,80],
ENKU_SHAMAN:[STONE,100]
}

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [LEAF, STONE]

 def onEvent (self,event,st) :
   htmltext = event
   leaf = st.getQuestItemsCount(LEAF)
   stone = st.getQuestItemsCount(STONE)
   if event == "7074-3.htm" :
     st.set("cond","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   elif event == "7074-6.htm" :
     if leaf == 80 and stone == 100 :
        st.takeItems(LEAF,leaf)
        st.takeItems(STONE,stone)
        item = st.getRandom(3)
        st.giveItems(5956+item,1)
        htmltext = "7074-"+str(6+item)+".htm"
        st.playSound("ItemSound.quest_finish")
        st.exitQuest(1)
     else :
        htmltext = "7074-4.htm"
   elif event == "7074-2a.htm" :
     st.exitQuest(1)
   return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   cond = st.getInt("cond")
   leaf = st.getQuestItemsCount(LEAF)
   stone = st.getQuestItemsCount(STONE)
   if cond == 0 :
     if st.getPlayer().getLevel() >= 20 :
       htmltext = "7074-0.htm"
     else:
       htmltext = "7074-0a.htm"
       st.exitQuest(1)
   elif cond == 1 :
     if leaf < 80 and stone  < 100 :
       htmltext = "7074-4.htm"
     elif leaf == 80 and stone < 100 :
       htmltext = "7074-4a.htm"
     elif leaf < 80 and stone == 100 :
       htmltext = "7074-4b.htm"
   elif cond == 2 and leaf == 80 and stone == 100 :
       htmltext = "7074-5.htm"
   return htmltext

 def onKill(self,npc,player,isPet):
   st = player.getQuestState(qn)
   if not st : return
   if st.getState() != State.STARTED :
      return
   npcId = npc.getNpcId()
   numItems,chance = divmod(100*Config.RATE_DROP_QUEST,100)
   item,count = MOB[npcId]
   if item :
      if st.getRandom(100) <chance :
         numItems = numItems + 1
      prevItems = st.getQuestItemsCount(item)
      if prevItems < count :
         if prevItems + numItems > count :
            numItems = count - prevItems
         if int(numItems) != 0 :
            st.giveItems(item,int(numItems))
         if (st.getQuestItemsCount(LEAF) != 80 and st.getQuestItemsCount(STONE) != 100) :
            st.playSound("ItemSound.quest_itemget")
         else :
            st.playSound("ItemSound.quest_middle")
            st.set("cond","2")
   return

QUEST = Quest(379,qn,"Fantasy Wine")
QUEST.addStartNpc(HARLAN)

QUEST.addTalkId(HARLAN)

QUEST.addKillId(ENKU_CHAMPION)
QUEST.addKillId(ENKU_SHAMAN)