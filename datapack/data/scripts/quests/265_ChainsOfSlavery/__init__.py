# Made by Mr. Have fun! - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

IMP_SHACKLES = 1368
ADENA = 57
SPIRITSHOT_FOR_BEGINNERS = 5790
SOULSHOT_FOR_BEGINNERS = 5789

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [IMP_SHACKLES]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7357-03.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    elif event == "7357-06.htm" :
      st.exitQuest(1)
      st.playSound("ItemSound.quest_finish")
    return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if st.getInt("cond")==0 :
     if st.getPlayer().getRace().ordinal() != 2 :
       htmltext = "7357-00.htm"
       st.exitQuest(1)
     else :
       if st.getPlayer().getLevel()<6 :
          htmltext = "7357-01.htm"
          st.exitQuest(1)
       else:
          htmltext = "7357-02.htm"
   else :
     count=st.getQuestItemsCount(IMP_SHACKLES)
     if count :
       st.giveItems(ADENA,13*count)
       st.takeItems(IMP_SHACKLES,-1)
       qs = st.getPlayer().getQuestState("255_Tutorial")
       if qs :
          newbiegift=qs.getInt("newbiegift")
          if newbiegift != 3 and st.getPlayer().isNewbie() :
             st.showQuestionMark(26)
             if st.getPlayer().getClassId().isMage() :
                st.playTutorialVoice("tutorial_voice_027")
                st.giveItems(SPIRITSHOT_FOR_BEGINNERS,3000)
             else :
                st.playTutorialVoice("tutorial_voice_026")
                st.giveItems(SOULSHOT_FOR_BEGINNERS,6000)
             qs.set("newbiegift","3")
       htmltext = "7357-05.htm"
     else:
       htmltext = "7357-04.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("265_ChainsOfSlavery")
   if st :
     if st.getState() != State.STARTED : return
     npcId = npc.getNpcId()
     chance=5+(npcId^4)
     if st.getRandom(10)<chance :
       st.giveItems(IMP_SHACKLES,1)
       st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(265,"265_ChainsOfSlavery","Chains Of Slavery")
QUEST.addStartNpc(7357)

QUEST.addTalkId(7357)

QUEST.addKillId(4)
QUEST.addKillId(5)