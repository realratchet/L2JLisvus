# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

ORC_AMULET = 1114
ORC_NECKLACE = 1115
ADENA = 57
SPIRITSHOT_FOR_BEGINNERS = 5790
SOULSHOT_FOR_BEGINNERS = 5789

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [ORC_AMULET, ORC_NECKLACE]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7221-03.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    elif event == "7221-06.htm" :
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
       htmltext = "7221-00.htm"
       st.exitQuest(1)
     elif st.getPlayer().getLevel()<6 :
       htmltext = "7221-01.htm"
       st.exitQuest(1)
     else :
       htmltext = "7221-02.htm"
   else :
     amulet = st.getQuestItemsCount(ORC_AMULET)
     necklace = st.getQuestItemsCount(ORC_NECKLACE)
     if amulet == necklace == 0 :
       htmltext = "7221-04.htm"
     else :
       htmltext = "7221-05.htm"
       st.giveItems(ADENA,amulet*5+necklace*15)
       st.takeItems(ORC_AMULET,-1)
       st.takeItems(ORC_NECKLACE,-1)
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
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("260_HuntForOrcs1")
   if st :
     if st.getState() != State.STARTED : return
     item=ORC_AMULET
     if npc.getNpcId() in range(471,474) :
       item = ORC_NECKLACE
     if st.getRandom(10)>4 :
       st.giveItems(item,1)
       st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(260,"260_HuntForOrcs1","Hunt For Orcs1")
QUEST.addStartNpc(7221)

QUEST.addTalkId(7221)

QUEST.addKillId(468)
QUEST.addKillId(469)
QUEST.addKillId(470)
QUEST.addKillId(471)
QUEST.addKillId(472)
QUEST.addKillId(473)