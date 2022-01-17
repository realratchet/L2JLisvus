# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

GIANT_SPIDER_SKIN = 1495
ADENA = 57
HEALING_POTION = 1061
WOODEN_ARROW = 17

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [GIANT_SPIDER_SKIN]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7497-03.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    elif event == "7497-06.htm" :
      st.exitQuest(1)
      st.playSound("ItemSound.quest_finish")
    elif event == "7405-04.htm" :
      st.giveItems(HEALING_POTION,1)
      st.takeItems(GIANT_SPIDER_SKIN,10)
    elif event == "7405-05.htm" :
      st.giveItems(WOODEN_ARROW,50)
      st.takeItems(GIANT_SPIDER_SKIN,10)
    elif event == "7405-07.htm" :
      if st.getQuestItemsCount(GIANT_SPIDER_SKIN) >= 10 :
        htmltext = "7405-06.htm"
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id != State.STARTED :
     st.set("cond","0")
   if st.getInt("cond")==0 :
     if st.getPlayer().getLevel() >= 15 :
       htmltext = "7497-02.htm"
     else:
       htmltext = "7497-01.htm"
       st.exitQuest(1)
   else :
     count=st.getQuestItemsCount(GIANT_SPIDER_SKIN)
     if npcId == 7497 :
       if count == 0 :
         htmltext = "7497-04.htm"
       else :
         htmltext = "7497-05.htm"
         st.giveItems(ADENA,count*25)
         st.takeItems(GIANT_SPIDER_SKIN,-1)
     else :
       if count < 10 :
         htmltext = "7405-01.htm"
       else :
         htmltext = "7405-02.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("259_RanchersPlea")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      st.giveItems(GIANT_SPIDER_SKIN,1)
      st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(259,"259_RanchersPlea","Ranchers Plea")
QUEST.addStartNpc(7497)

QUEST.addTalkId(7405)
QUEST.addTalkId(7497)

QUEST.addKillId(103)
QUEST.addKillId(106)
QUEST.addKillId(108)