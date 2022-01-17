# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

FLAME_SHARD = 1020
ICE_SHARD = 1021
ADENA = 57

DROPLIST={
109:[30,FLAME_SHARD],
110:[30,ICE_SHARD],
112:[40,FLAME_SHARD],
113:[40,ICE_SHARD],
114:[50,FLAME_SHARD],
115:[50,ICE_SHARD]
}

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [FLAME_SHARD, ICE_SHARD]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7004-04.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    elif event == "7004-08.htm" :
      st.exitQuest(1)
      st.playSound("ItemSound.quest_finish")
    return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if st.getInt("cond")==0 :
     if st.getPlayer().getLevel() >= 17 :
       htmltext = "7004-03.htm"
     else:
       htmltext = "7004-02.htm"
       st.exitQuest(1)
   else :
     flame=st.getQuestItemsCount(FLAME_SHARD)
     ice=st.getQuestItemsCount(ICE_SHARD)
     if flame==ice==0 :
       htmltext = "7004-05.htm"
     else :
       st.giveItems(ADENA,60*(flame+ice))
       st.takeItems(FLAME_SHARD,-1)
       st.takeItems(ICE_SHARD,-1)
       htmltext = "7004-07.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("306_CrystalOfFireice")
   if st :
     if st.getState() != State.STARTED : return
     npcId = npc.getNpcId()
     chance,item=DROPLIST[npcId]
     if st.getRandom(100)<chance :
       st.giveItems(item,1)
       st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(306,"306_CrystalOfFireice","Crystal Of Fireice")
QUEST.addStartNpc(7004)

QUEST.addTalkId(7004)

QUEST.addKillId(109)
QUEST.addKillId(110)
QUEST.addKillId(112)
QUEST.addKillId(113)
QUEST.addKillId(114)
QUEST.addKillId(115)