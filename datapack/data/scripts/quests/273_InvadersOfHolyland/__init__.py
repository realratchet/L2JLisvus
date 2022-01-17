# Made by Mr. - Version 0.3 by DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

BLACK_SOULSTONE = 1475
RED_SOULSTONE = 1476
ADENA = 57
SOULSHOT_FOR_BEGINNERS = 5789

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [BLACK_SOULSTONE, RED_SOULSTONE]

 def onEvent (self,event,st) :
    htmltext = event
    if event in ["7566-03.htm","7566-08.htm"] : # -i'll continue- event kept here for backwards compatibility only.. should be removed some day
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    elif event == "7566-07.htm" :
      st.exitQuest(1)
      st.playSound("ItemSound.quest_finish")
    return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id in [State.CREATED,State.COMPLETED] :
     st.set("cond","0")
   if st.getInt("cond")==0 :
     if st.getPlayer().getRace().ordinal() != 3 :
        htmltext = "7566-00.htm"
        st.exitQuest(1)
     elif st.getPlayer().getLevel() < 6 :
        htmltext = "7566-01.htm"
        st.exitQuest(1)
     else:
        htmltext = "7566-02.htm"
   else :
     red=st.getQuestItemsCount(RED_SOULSTONE)
     black=st.getQuestItemsCount(BLACK_SOULSTONE)
     if red+black == 0 :
        htmltext = "7566-04.htm"
     elif red == 0 :
        htmltext = "7566-05.htm"
        st.giveItems(ADENA,black*3)
        st.takeItems(BLACK_SOULSTONE,black)
        st.playSound("ItemSound.quest_finish")
     else:
        htmltext = "7566-06.htm"
        if black :
           st.giveItems(ADENA,black*3)
           st.takeItems(BLACK_SOULSTONE,black)
        st.giveItems(ADENA,red*5)
        st.takeItems(RED_SOULSTONE,red)
        qs = st.getPlayer().getQuestState("255_Tutorial")
        if qs :
           newbiegift=qs.getInt("newbiegift")
           if newbiegift != 3 and st.getPlayer().isNewbie() :
              st.showQuestionMark(26)
              st.playTutorialVoice("tutorial_voice_026")
              st.giveItems(SOULSHOT_FOR_BEGINNERS,6000)
              qs.set("newbiegift","3")
        st.playSound("ItemSound.quest_finish")
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("273_InvadersOfHolyland")
   if st :
     if st.getState() != State.STARTED : return
     npcId = npc.getNpcId()
     if npcId == 311 : chance = 90
     if npcId == 312 : chance = 87
     if npcId == 313 : chance = 77
     if st.getRandom(100) <= chance :
        st.giveItems(BLACK_SOULSTONE,1)
     else:
        st.giveItems(RED_SOULSTONE,1)
     st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(273,"273_InvadersOfHolyland","Invaders Of Holyland")
QUEST.addStartNpc(7566)

QUEST.addTalkId(7566)

QUEST.addKillId(311)
QUEST.addKillId(312)
QUEST.addKillId(313)