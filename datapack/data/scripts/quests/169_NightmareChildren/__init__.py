# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

CRACKED_SKULL_ID = 1030
PERFECT_SKULL_ID = 1031
BONE_GAITERS_ID = 31

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [CRACKED_SKULL_ID, PERFECT_SKULL_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
      st.set("id","0")
      htmltext = "7145-04.htm"
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    elif event == "169_1" :
          if st.getInt("id") != 169 :
            st.set("id","169")
            htmltext = "7145-08.htm"
            st.giveItems(BONE_GAITERS_ID,1)
            st.giveItems(57,17150)
            st.takeItems(CRACKED_SKULL_ID,st.getQuestItemsCount(CRACKED_SKULL_ID))
            st.takeItems(PERFECT_SKULL_ID,st.getQuestItemsCount(PERFECT_SKULL_ID))
            st.set("cond","0")
            st.setState(State.COMPLETED)
            st.playSound("ItemSound.quest_finish")
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == 7145 and st.getInt("cond")==0 :
      if st.getPlayer().getRace().ordinal() != 2 :
        htmltext = "7145-00.htm"
      elif st.getPlayer().getLevel() >= 15 :
        htmltext = "7145-03.htm"
        return htmltext
      else:
        htmltext = "7145-02.htm"
        st.exitQuest(1)
   elif npcId == 7145 and st.getInt("cond") == 1 :
      if st.getQuestItemsCount(CRACKED_SKULL_ID) >= 1 and st.getQuestItemsCount(PERFECT_SKULL_ID) == 0 :
        htmltext = "7145-06.htm"
      elif st.getQuestItemsCount(PERFECT_SKULL_ID) >= 1 :
          htmltext = "7145-07.htm"
      elif st.getQuestItemsCount(CRACKED_SKULL_ID) == 0 and st.getQuestItemsCount(PERFECT_SKULL_ID) == 0 :
          htmltext = "7145-05.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("169_NightmareChildren")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId == 105 :
         st.set("id","0")
         if st.getInt("cond") == 1 :
           if st.getRandom(10)>7 and st.getQuestItemsCount(PERFECT_SKULL_ID) == 0 :
             st.giveItems(PERFECT_SKULL_ID,1)
             st.playSound("ItemSound.quest_middle")
           if st.getRandom(10)>4 :
             st.giveItems(CRACKED_SKULL_ID,1)
             st.playSound("ItemSound.quest_itemget")
      elif npcId == 25 :
         st.set("id","0")
         if st.getInt("cond") == 1 :
           if st.getRandom(10)>7 and st.getQuestItemsCount(PERFECT_SKULL_ID) == 0 :
             st.giveItems(PERFECT_SKULL_ID,1)
             st.playSound("ItemSound.quest_middle")
           if st.getRandom(10)>4 :
             st.giveItems(CRACKED_SKULL_ID,1)
             st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(169,"169_NightmareChildren","Nightmare Children")
QUEST.addStartNpc(7145)

QUEST.addTalkId(7145)

QUEST.addKillId(105)
QUEST.addKillId(25)