# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

DARK_BEZOAR_ID = 1160
LESSER_HEALING_POTION_ID = 1060

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [DARK_BEZOAR_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
      st.set("id","0")
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
      htmltext = "7348-03.htm"
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == 7348 and st.getInt("cond")==0 :
      if st.getPlayer().getRace().ordinal() != 2 :
        htmltext = "7348-00.htm"
      elif st.getPlayer().getLevel() >= 3 :
        htmltext = "7348-02.htm"
        return htmltext
      else:
        htmltext = "7348-01.htm"
        st.exitQuest(1)
   elif npcId == 7348 and st.getInt("cond")==1 :
      if st.getQuestItemsCount(DARK_BEZOAR_ID)<13 :
        htmltext = "7348-04.htm"
      elif st.getQuestItemsCount(DARK_BEZOAR_ID) >= 13 :
          if st.getInt("id") != 165 :
            st.set("id","165")
            htmltext = "7348-05.htm"
            st.takeItems(DARK_BEZOAR_ID,st.getQuestItemsCount(DARK_BEZOAR_ID))
            st.giveItems(LESSER_HEALING_POTION_ID,5)
            st.addExpAndSp(1000,0)
            st.set("cond","0")
            st.setState(State.COMPLETED)
            st.playSound("ItemSound.quest_finish")
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("165_WildHunt")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId == 529 :
        st.set("id","0")
        if st.getInt("cond") == 1 :
          if st.getRandom(10)<4 and st.getQuestItemsCount(DARK_BEZOAR_ID)<13 :
            st.giveItems(DARK_BEZOAR_ID,1)
            if st.getQuestItemsCount(DARK_BEZOAR_ID) == 13 :
              st.playSound("ItemSound.quest_middle")
            else:
              st.playSound("ItemSound.quest_itemget")
      elif npcId == 532 :
        st.set("id","0")
        if st.getInt("cond") == 1 :
          if st.getRandom(10)<4 and st.getQuestItemsCount(DARK_BEZOAR_ID)<13 :
            st.giveItems(DARK_BEZOAR_ID,1)
            if st.getQuestItemsCount(DARK_BEZOAR_ID) == 13 :
              st.playSound("ItemSound.quest_middle")
            else:
              st.playSound("ItemSound.quest_itemget")
      elif npcId == 536 :
        st.set("id","0")
        if st.getInt("cond") == 1 :
          if st.getRandom(10)<4 and st.getQuestItemsCount(DARK_BEZOAR_ID)<13 :
            st.giveItems(DARK_BEZOAR_ID,1)
            if st.getQuestItemsCount(DARK_BEZOAR_ID) == 13 :
              st.playSound("ItemSound.quest_middle")
            else:
              st.playSound("ItemSound.quest_itemget")
      elif npcId == 456 :
        st.set("id","0")
        if st.getInt("cond") == 1 :
          if st.getRandom(10)<4 and st.getQuestItemsCount(DARK_BEZOAR_ID)<13 :
            st.giveItems(DARK_BEZOAR_ID,1)
            if st.getQuestItemsCount(DARK_BEZOAR_ID) == 13 :
              st.playSound("ItemSound.quest_middle")
            else:
              st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(165,"165_WildHunt","Wild Hunt")
QUEST.addStartNpc(7348)

QUEST.addTalkId(7348)

QUEST.addKillId(456)
QUEST.addKillId(529)
QUEST.addKillId(532)
QUEST.addKillId(536)