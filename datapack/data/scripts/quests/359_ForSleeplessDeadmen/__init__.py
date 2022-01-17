# For Sleepless Deadmen version 0.1 
# by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

#Quest info
QUEST_NUMBER,QUEST_NAME,QUEST_DESCRIPTION = 359,"ForSleeplessDeadmen","For Sleepless Deadmen"

#Variables
DROP_RATE = 10  #in %
DROP_MAX = 100 #unless you change this

REQUIRED=60  #how many items will be paid for a reward

#Quest items
REMAINS = 5869

#Rewards
REWARDS=range(6341,6347)+range(5494,5496)

#Messages
default = JQuest.getNoQuestMsg()

#NPCs
ORVEN = 7857

#Mobs
MOBS = range(1006,1009)

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [REMAINS]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7857-6.htm" :
       st.setState(State.STARTED)
       st.set("cond","1")
       st.playSound("ItemSound.quest_accept")
    elif event == "7857-7.htm" :
       st.exitQuest(1)
       st.playSound("ItemSound.quest_finish")
    elif event == "7857-8.htm" :
       st.set("cond","1")
       st.giveItems(REWARDS[st.getRandom(len(REWARDS))] ,4)
    return htmltext

 def onTalk (self,npc,st):
   htmltext = default
   id = st.getState()
   if id == State.CREATED :
      st.set("cond","0")
      if st.getPlayer().getLevel() < 60 :
         st.exitQuest(1)
         htmltext = "7857-1.htm"
      else :
         htmltext = "7857-2.htm"
   elif id == State.STARTED :
      cond=st.getInt("cond")
      if cond == 3 :
         htmltext = "7857-3.htm"
      elif cond == 2 and st.getQuestItemsCount(REMAINS) >= REQUIRED :
         st.takeItems(REMAINS,REQUIRED)
         st.set("cond","3")
         htmltext = "7857-4.htm"
      else :
         htmltext = "7857-5.htm"
   return htmltext

 def onKill (self,npc,player,isPet) :
   st = player.getQuestState(str(QUEST_NUMBER)+"_"+QUEST_NAME)
   if st :
     if st.getState() != State.STARTED : return
     count = st.getQuestItemsCount(REMAINS)
     if count < REQUIRED and st.getRandom(DROP_MAX) < DROP_RATE :
        st.giveItems(REMAINS,1)
        if count + 1 >= REQUIRED :
           st.playSound("ItemSound.quest_middle")
           st.set("cond","2")
        else :
           st.playSound("ItemSound.quest_itemget")
   return

# Quest class and state definition
QUEST = Quest(QUEST_NUMBER, str(QUEST_NUMBER)+"_"+QUEST_NAME, QUEST_DESCRIPTION)
# Quest NPC starter initialization
QUEST.addStartNpc(ORVEN)

# Quest initialization
QUEST.addTalkId(ORVEN)

for i in MOBS :
  QUEST.addKillId(i)