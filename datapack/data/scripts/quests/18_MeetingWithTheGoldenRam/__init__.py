# Contributed by t0rm3nt0r to the Official L2J Datapack Project (adapted for L2JLisvus by roko91).
# With some minor cleanup by DrLecter.
# Visit http://forum.l2jdp.com for more details.

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "18_MeetingWithTheGoldenRam"

DONAL = 8314
DAISY = 8315
ABERCROMBIE = 8555
BOX = 7245

class Quest (JQuest) :

 def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)
 
 def onEvent (self,event,st) :
     htmltext = event
     if event == "8314-03.htm" :
       if st.getPlayer().getLevel() >= 66 :
         st.set("cond","1")
         st.setState(State.STARTED)
         st.playSound("ItemSound.quest_accept")
       else :
         htmltext = "8314-02.htm"
         st.exitQuest(1)
     elif event == "8315-02.htm" :
       st.set("cond","2")
       htmltext = "8315-02.htm"
       st.giveItems(BOX,1)
     elif event == "8555-02.htm" :
       st.giveItems(57,15000)
       st.takeItems(BOX,-1)
       st.addExpAndSp(50000,0)
       st.unset("cond")
       st.playSound("ItemSound.quest_finish")
       st.setState(State.COMPLETED)
     return htmltext

 def onTalk (self,npc,st):
     npcId = npc.getNpcId()
     htmltext = JQuest.getNoQuestMsg()
     id = st.getState()
     cond = st.getInt("cond")
     if id == State.COMPLETED :
       htmltext = htmltext = JQuest.getAlreadyCompletedMsg()
     elif id == State.CREATED and npcId == DONAL :
       htmltext = "8314-01.htm"
     elif id == State.STARTED :
       if npcId == DONAL : 
         htmltext = "8314-04.htm"
       elif npcId == DAISY :
         if cond < 2 :
           htmltext = "8315-01.htm"
         else :
           htmltext = "8315-03.htm"
       elif npcId == ABERCROMBIE and cond == 2 and st.getQuestItemsCount(BOX):
           htmltext = "8555-01.htm"
     return htmltext

QUEST = Quest(18, qn, "Meeting With The Golden Ram")
QUEST.addStartNpc(DONAL)

QUEST.addTalkId(DONAL)
QUEST.addTalkId(DAISY)
QUEST.addTalkId(ABERCROMBIE)