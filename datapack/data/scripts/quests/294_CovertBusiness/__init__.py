# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

BAT_FANG = 1491
RING_OF_RACCOON = 1508
ADENA = 57
DROP = {
480:[[6,10,1],[3,6,2],[0,3,3]],
370:[[7,10,1],[4,7,2],[2,4,3],[0,2,4]]
}
class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [BAT_FANG]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7534-03.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if st.getInt("cond")==0 :
     if st.getPlayer().getRace().ordinal() != 4 :
       htmltext = "7534-00.htm"
       st.exitQuest(1)
     elif st.getPlayer().getLevel() >= 10 :
       htmltext = "7534-02.htm"
     else:
       htmltext = "7534-01.htm"
       st.exitQuest(1)
   else:
     if st.getQuestItemsCount(BAT_FANG)<100 :
       htmltext = "7534-04.htm"
     else :
       if st.getQuestItemsCount(RING_OF_RACCOON) ==0 :
         htmltext = "7534-05.htm"
         st.giveItems(RING_OF_RACCOON,1)
       else :
         htmltext = "7534-06.htm"
         st.giveItems(ADENA,2400)
       st.addExpAndSp(0,600)
       st.takeItems(BAT_FANG,-1)
       st.exitQuest(1)
       st.playSound("ItemSound.quest_finish")
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("294_CovertBusiness")
   if st :
     if st.getState() != State.STARTED : return
     if st.getInt("cond") == 1:
       npcId = npc.getNpcId()
       count=st.getQuestItemsCount(BAT_FANG)
       chance = st.getRandom(10)
       for i in DROP[npcId]:
          if i[0]<=chance<i[1]:
             qty=i[2]
       if count+qty>100 :
         qty=100-count
       if count+qty==100:
         st.playSound("ItemSound.quest_middle")
         st.set("cond","2")
       else :
         st.playSound("ItemSound.quest_itemget")
       st.giveItems(BAT_FANG,qty)
   return

QUEST = Quest(294,"294_CovertBusiness","Covert Business")
QUEST.addStartNpc(7534)

QUEST.addTalkId(7534)

QUEST.addKillId(370)
QUEST.addKillId(480)