# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

WERERAT_FANG = 1042
VAROOL_FOULCLAWS_FANG = 1043
ADENA = 57

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [WERERAT_FANG, VAROOL_FOULCLAWS_FANG]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7155-04.htm" :
        st.set("cond","1")
        st.setState(State.STARTED)
        st.playSound("ItemSound.quest_accept")
    elif event == "7155-08.htm" :
        st.exitQuest(1)
        st.playSound("ItemSound.quest_finish")
    return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if st.getInt("cond")==0 :
     if st.getPlayer().getRace().ordinal() != 1 :
       htmltext = "7155-00.htm"
       st.exitQuest(1)
     elif st.getPlayer().getLevel() >= 18 :
       htmltext = "7155-03.htm"
     else:
       htmltext = "7155-02.htm"
       st.exitQuest(1)
   else :
     rats=st.getQuestItemsCount(WERERAT_FANG)
     varool=st.getQuestItemsCount(VAROOL_FOULCLAWS_FANG)
     if rats or varool :
       htmltext = "7155-07.htm"
       st.giveItems(ADENA,rats*60+varool*10000)
       st.takeItems(WERERAT_FANG,-1)
       st.takeItems(VAROOL_FOULCLAWS_FANG,-1)
     else:
       htmltext = "7155-05.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("316_DestroyPlaguebringers")
   if st :
     if st.getState() != State.STARTED : return
     npcId = npc.getNpcId()
     if npcId == 5020 :
       if st.getQuestItemsCount(VAROOL_FOULCLAWS_FANG) == 0 and st.getRandom(10)>7:
         st.giveItems(VAROOL_FOULCLAWS_FANG,1)
         st.playSound("ItemSound.quest_middle")
     elif st.getRandom(10)>5 :
       st.giveItems(WERERAT_FANG,1)
       st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(316,"316_DestroyPlaguebringers","Destroy Plaguebringers")
QUEST.addStartNpc(7155)

QUEST.addTalkId(7155)

QUEST.addKillId(40)
QUEST.addKillId(47)
QUEST.addKillId(5020)