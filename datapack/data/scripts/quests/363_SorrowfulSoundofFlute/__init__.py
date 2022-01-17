# Jovial Accordian Written By Elektra
# Fixed by mr
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest


class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [4319]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
        st.set("cond","1")
        st.setState(State.STARTED)
        st.playSound("ItemSound.quest_accept")
        htmltext = "7956_2.htm"
    elif event == "5" :
        st.giveItems(4420,1)
        st.playSound("ItemSound.quest_finish")
        st.exitQuest(1)
        htmltext = "7956_5.htm"
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if npcId == 7956 and st.getInt("cond") == 0 :
        htmltext = "7956_1.htm"
   elif npcId == 7956 and st.getInt("cond") == 1 :
        htmltext = "<html><body>Find Blacksmith Opix..</body></html>"
   elif npcId == 7595 and st.getInt("cond") == 1 :
        st.set("cond","2")
        htmltext = "7595_1.htm"
   elif npcId == 7595 and st.getInt("cond") > 1 :
        htmltext = "<html><body>Go back to Nanarin..</body></html>"
   elif npcId == 7956 and st.getInt("cond") == 2 :
        st.giveItems(4319,1)
        st.set("cond","3")
        htmltext = "7956_3.htm"
   elif npcId == 7956 and st.getInt("cond") == 3 :
        htmltext = "<html><body>Find Barbado..</body></html>"
   elif npcId == 7959 and st.getInt("cond") == 3 :
        st.takeItems(4319,1)
        st.set("cond","4")
        htmltext = "7959_1.htm"
   elif npcId == 7959 and st.getInt("cond") == 4 :
        htmltext = "<html><body>Go back to Nanarin..</body></html>"
   elif npcId == 7956 and st.getInt("cond") == 4 :
        htmltext = "7956_4.htm"
   return htmltext


QUEST = Quest(363,"363_SorrowfulSoundofFlute","Sorrowful Sounds of Flute")
QUEST.addStartNpc(7956)

QUEST.addTalkId(7595)
QUEST.addTalkId(7959)
QUEST.addTalkId(7956)