# Silver Haired Shaman - Version 0.1 by DrLecter (adapted for L2JLisvus by roko91)

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "366_SilverHairedShaman"

#NPC
DIETER=7111
#Items
HAIR=5874
ADENA=57
#BASE CHANCE FOR DROP
CHANCE = 55

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [HAIR]

 def onEvent (self,event,st) :
   htmltext = event
   cond = st.getInt("cond")
   if event == "7111-2.htm" and cond == 0 :
     st.set("cond","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   elif event == "7111-6.htm" :
     st.exitQuest(1)
     st.playSound("ItemSound.quest_finish")
   return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   npcId = npc.getNpcId()
   id = st.getState()
   cond=st.getInt("cond")
   if cond == 0 :
     if st.getPlayer().getLevel() >= 48 :
       htmltext = "7111-1.htm"
     else:
       htmltext = "7111-0.htm"
       st.exitQuest(1)
   else :
     hair=st.getQuestItemsCount(HAIR)
     if not hair :
       htmltext = "7111-3.htm"
     else :
       st.giveItems(ADENA,12070+500*hair)
       st.takeItems(HAIR,-1)
       htmltext = "7111-4.htm"
   return htmltext

 def onKill(self,npc,player,isPet):
   partyMember = self.getRandomPartyMemberState(player,npc,State.STARTED)
   if not partyMember : return
   st = partyMember.getQuestState(qn)
   if st.getRandom(100) < CHANCE + ((npc.getNpcId() - 985) * 2) :
     st.giveItems(HAIR,1)
     st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(366,qn,"Silver Haired Shaman")
QUEST.addStartNpc(DIETER)

QUEST.addTalkId(DIETER)

for mob in range(986,989) :
    QUEST.addKillId(mob)