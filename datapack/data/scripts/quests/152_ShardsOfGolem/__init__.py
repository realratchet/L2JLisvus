# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

HARRYS_RECEIPT1_ID = 1008
HARRYS_RECEIPT2_ID = 1009
GOLEM_SHARD_ID = 1010
TOOL_BOX_ID = 1011
COTTON_TUNIC_ID = 1100

#NPC
HARRIS=7035
ALTRAN=7283

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(1008,1012)

 def onAdvEvent (self,event,npc, player) :
    htmltext = event
    st = player.getQuestState("152_ShardsOfGolem")
    if not st : return
    id = st.getState()
    cond = st.getInt("cond")
    if id != State.COMPLETED :
       if event == "7035-04.htm" and cond == 0 :
          st.set("cond","1")
          st.setState(State.STARTED)
          st.playSound("ItemSound.quest_accept")
          st.giveItems(HARRYS_RECEIPT1_ID,1)
       elif event == "7283-02.htm" and cond == 1 and st.getQuestItemsCount(HARRYS_RECEIPT1_ID) :
          st.takeItems(HARRYS_RECEIPT1_ID,-1)
          st.giveItems(HARRYS_RECEIPT2_ID,1)
          st.set("cond","2")
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   cond = st.getInt("cond")
   receipt1 = st.getQuestItemsCount(HARRYS_RECEIPT1_ID)
   receipt2 = st.getQuestItemsCount(HARRYS_RECEIPT2_ID)
   toolbox = st.getQuestItemsCount(TOOL_BOX_ID)
   shards = st.getQuestItemsCount(GOLEM_SHARD_ID)
   if id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == HARRIS :
      if cond == 0 :
         if st.getPlayer().getLevel() >= 10 :
            htmltext = "7035-03.htm"
         else:
            htmltext = "7035-02.htm"
            st.exitQuest(1)
      elif cond == 1 and receipt1 and not toolbox :
        htmltext = "7035-05.htm"
      elif cond == 3 and toolbox :
        st.takeItems(TOOL_BOX_ID,-1)
        st.takeItems(HARRYS_RECEIPT2_ID,-1)
        st.unset("cond")
        st.setState(State.COMPLETED)
        st.playSound("ItemSound.quest_finish")
        st.giveItems(WOODEN_BP_ID,1)
        st.addExpAndSp(5000,0)
        htmltext = "7035-06.htm"
   elif npcId == ALTRAN and id == State.STARTED:
      if cond == 1 and receipt1 :
        htmltext = "7283-01.htm"
      elif cond == 2 and receipt2 and shards < 5 and not toolbox :
        htmltext = "7283-03.htm"
      elif cond == 3 and receipt2 and shards >= 5 and not toolbox :
        st.takeItems(GOLEM_SHARD_ID,-1)
        st.giveItems(TOOL_BOX_ID,1)
        htmltext = "7283-04.htm"
      elif cond == 3 and receipt2 and toolbox :
        htmltext = "7283-05.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("152_ShardsOfGolem")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId == 16 :
        count = st.getQuestItemsCount(GOLEM_SHARD_ID)
        if st.getInt("cond") == 2 and st.getRandom(100) < 30 and count < 5 :
          st.giveItems(GOLEM_SHARD_ID,1)
          if count == 4 :
            st.playSound("ItemSound.quest_middle")
          else:
            st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(152,"152_ShardsOfGolem","Shards Of Golem")
QUEST.addStartNpc(HARRIS)

QUEST.addTalkId(HARRIS)
QUEST.addTalkId(ALTRAN)

QUEST.addKillId(16)