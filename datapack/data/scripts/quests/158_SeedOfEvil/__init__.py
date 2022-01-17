# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

CLAY_TABLET_ID = 1025
ENCHANT_ARMOR_D = 956

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [CLAY_TABLET_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
        st.set("id","0")
        st.set("cond","1")
        st.setState(State.STARTED)
        st.playSound("ItemSound.quest_accept")
        htmltext = "7031-04.htm"
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == 7031 and st.getInt("cond")==0 :
        if st.getPlayer().getLevel() >= 21 :
          htmltext = "7031-03.htm"
          return htmltext
        else:
          htmltext = "7031-02.htm"
          st.exitQuest(1)
   elif npcId == 7031 and st.getInt("cond")!=0 and st.getQuestItemsCount(CLAY_TABLET_ID)==0 :
        htmltext = "7031-05.htm"
   elif npcId == 7031 and st.getInt("cond")!=0 and st.getQuestItemsCount(CLAY_TABLET_ID)!=0 :
        if st.getInt("id") != 158 :
          st.set("id","158")
          st.takeItems(CLAY_TABLET_ID,st.getQuestItemsCount(CLAY_TABLET_ID))
          st.set("cond","0")
          st.setState(State.COMPLETED)
          st.playSound("ItemSound.quest_finish")
          st.giveItems(ENCHANT_ARMOR_D,1)
          htmltext = "7031-06.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("158_SeedOfEvil")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId == 5016 :
        st.set("id","0")
        if st.getInt("cond") != 0 and st.getQuestItemsCount(CLAY_TABLET_ID) == 0 :
          st.giveItems(CLAY_TABLET_ID,1)
          st.playSound("ItemSound.quest_middle")
   return

QUEST = Quest(158,"158_SeedOfEvil","Seed Of Evil")
QUEST.addStartNpc(7031)

QUEST.addTalkId(7031)

QUEST.addKillId(5016)