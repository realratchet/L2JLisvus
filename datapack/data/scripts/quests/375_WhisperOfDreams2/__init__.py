# Whisper of Dreams, part 2 version 0.1
# by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

#Quest info
QUEST_NUMBER,QUEST_NAME,QUEST_DESCRIPTION = 375,"WhisperOfDreams2","Whisper of Dreams, part 2"

#Variables
#Alternative rewards. Set this to a non-zero value and recipes will be 100% instead of 60%
ALT_RP_100=0

#Quest items
MSTONE = 5887
K_HORN = 5888
CH_SKULL = 5889

#Quest collections
REWARDS = [5348,5350,5352]

#Messages
default = JQuest.getNoQuestMsg()

#NPCs
MANAKIA = 7515

#Mobs & Drop
DROPLIST = {624:[CH_SKULL,"awaitSkull"],629:[K_HORN,"awaitHorn"]}

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [CH_SKULL, K_HORN]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7515-6.htm" :
       st.setState(State.STARTED)
       st.set("awaitSkull","1")
       st.set("awaitHorn","1")
       st.set("cond","1")
       st.playSound("ItemSound.quest_accept")
    elif event == "7515-7.htm" :
       st.playSound("ItemSound.quest_finish")
       st.exitQuest(1)
    elif event == "7515-8.htm" :
       st.set("awaitSkull","1")
       st.set("awaitHorn","1")
    return htmltext

 def onTalk (self,npc,st):
   htmltext = default
   id = st.getState()
   if id == State.CREATED :
      st.set("cond","0")
      htmltext = "7515-1.htm"
      if st.getPlayer().getLevel() < 60 :
         htmltext = "7515-2.htm"
         st.exitQuest(1)
      elif not st.getQuestItemsCount(MSTONE) :
         htmltext = "7515-3.htm"
         st.exitQuest(1)
   elif id == State.STARTED :
      if st.getQuestItemsCount(CH_SKULL)==st.getQuestItemsCount(K_HORN)==100 :
         st.takeItems(CH_SKULL,-1)
         st.takeItems(K_HORN,-1)
         item=REWARDS[st.getRandom(len(REWARDS))]
         if ALT_RP_100 : item += 1
         st.giveItems(item,1)
         htmltext="7515-4.htm"
         st.exitQuest(1)
      else :
         htmltext = "7515-5.htm"
   return htmltext

 def onKill (self,npc,player,isPet) :
    npcid = npc.getNpcId()
    item, partyCond  = DROPLIST[npcid]
    partyMember = self.getRandomPartyMember(player, npc, partyCond, "1")
    if not partyMember : return
    st = partyMember.getQuestState(str(QUEST_NUMBER)+"_"+QUEST_NAME)
    if st :
      count = st.getQuestItemsCount(item)
      if count < 100 :
         st.giveItems(item,1)
         if count + 1 >= 100 :
            st.playSound("ItemSound.quest_middle")
            st.unset(partyCond)
         else :
            st.playSound("ItemSound.quest_itemget")
    return  

# Quest class and state definition
QUEST = Quest(QUEST_NUMBER, str(QUEST_NUMBER)+"_"+QUEST_NAME, QUEST_DESCRIPTION)
# Quest NPC starter initialization
QUEST.addStartNpc(MANAKIA)

# Quest initialization
QUEST.addTalkId(MANAKIA)

for i in DROPLIST.keys() :
  QUEST.addKillId(i)