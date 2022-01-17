# Made by Mr. Have fun! - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

GLUDIO_LORDS_MARK = 1084
ORC_AMULET = 752
ORC_NECKLACE = 1085
WEREWOLF_FANG = 1086
ADENA = 57
SPIRITSHOT_FOR_BEGINNERS = 5790
SOULSHOT_FOR_BEGINNERS = 5789

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [ORC_AMULET, ORC_NECKLACE, WEREWOLF_FANG, GLUDIO_LORDS_MARK]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7039-03.htm" :
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
      st.giveItems(GLUDIO_LORDS_MARK,1)
    elif event == "7039-05.htm" :
      st.takeItems(GLUDIO_LORDS_MARK,1)
      st.exitQuest(1)
      st.playSound("ItemSound.quest_finish")
    return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if st.getInt("cond")==0 :
     if st.getPlayer().getLevel() >= 6 :
       htmltext = "7039-02.htm"
     else:
       htmltext = "7039-01.htm"
       st.exitQuest(1)
   else :
     orc_a=st.getQuestItemsCount(ORC_AMULET)
     orc_n=st.getQuestItemsCount(ORC_NECKLACE)
     wer_f=st.getQuestItemsCount(WEREWOLF_FANG)
     if orc_a==orc_n==wer_f==0 :
       htmltext = "7039-04.htm"
     else :
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
       st.giveItems(ADENA,5*orc_a+15*orc_n+10*wer_f)
       st.takeItems(ORC_AMULET,-1)
       st.takeItems(ORC_NECKLACE,-1)
       st.takeItems(WEREWOLF_FANG,-1)
       htmltext = "7039-07.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("257_GuardIsBusy1")
   if st :
     if st.getState() != State.STARTED : return
     npcId = npc.getNpcId()
     chance=5
     if npcId in [130,131,6] :
       item = ORC_AMULET
     elif npcId in [93,96,98] :
       item = ORC_NECKLACE
     else :
       item = WEREWOLF_FANG
       if npcId == 343 : chance = 4
       elif npcId == 342 : chance = 2
     if st.getQuestItemsCount(GLUDIO_LORDS_MARK) :
       if st.getRandom(10)<chance :
         st.giveItems(item,1)
         st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(257,"257_GuardIsBusy1","Guard Is Busy1")
QUEST.addStartNpc(7039)

QUEST.addTalkId(7039)

QUEST.addKillId(130)
QUEST.addKillId(131)
QUEST.addKillId(132)
QUEST.addKillId(342)
QUEST.addKillId(343)
QUEST.addKillId(6)
QUEST.addKillId(93)
QUEST.addKillId(96)
QUEST.addKillId(98)