# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

SHILENS_CALL_ID = 1245
ARKENIAS_LETTER_ID = 1246
LEIKANS_NOTE_ID = 1247
ONYX_BEASTS_MOLAR_ID = 1248
SHILENS_TEARS_ID = 1250
ARKENIA_RECOMMEND_ID = 1251
IRON_HEART_ID = 1252

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(1245,1252)

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
        if st.getPlayer().getLevel() >= 19 and st.getPlayer().getClassId().getId() == 0x1f and st.getQuestItemsCount(IRON_HEART_ID) == 0 :
          st.set("cond","1")
          st.setState(State.STARTED)
          st.playSound("ItemSound.quest_accept")
          st.giveItems(SHILENS_CALL_ID,1)
          htmltext = "7416-05.htm"
        elif st.getPlayer().getClassId().getId() != 0x1f :
            if st.getPlayer().getClassId().getId() == 0x23 :
              htmltext = "7416-02a.htm"
            else:
              htmltext = "7416-02.htm"
              st.exitQuest(1)
        elif st.getPlayer().getLevel()<19 and st.getPlayer().getClassId().getId() == 0x1f :
            htmltext = "7416-03.htm"
            st.exitQuest(1)
        elif st.getPlayer().getLevel() >= 19 and st.getPlayer().getClassId().getId() == 0x1f and st.getQuestItemsCount(IRON_HEART_ID) == 1 :
            htmltext = "7416-04.htm"
    elif event == "7419_1" :
          htmltext = "7419-05.htm"
          st.giveItems(ARKENIAS_LETTER_ID,1)
          st.takeItems(SHILENS_CALL_ID,1)
          st.set("cond","2")
          st.playSound("ItemSound.quest_middle")
    elif event == "7382_1" :
          htmltext = "7382-03.htm"
          st.giveItems(LEIKANS_NOTE_ID,1)
          st.takeItems(ARKENIAS_LETTER_ID,1)
          st.set("cond","3")
          st.playSound("ItemSound.quest_middle")
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
     st.set("onlyone","0")
   if npcId == 7416 and st.getInt("cond")==0 :
     if st.getQuestItemsCount(IRON_HEART_ID) == 0 :
        htmltext = "7416-01.htm"
     else:
        htmltext = "7416-04.htm"
   elif npcId == 7416 and st.getInt("cond")>=1 :
        if st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 0 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 0 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 1 and st.getQuestItemsCount(IRON_HEART_ID) == 0 :
          htmltext = "7416-06.htm"
          st.takeItems(ARKENIA_RECOMMEND_ID,1)
          st.giveItems(IRON_HEART_ID,1)
          st.set("cond","0")
          st.setState(State.COMPLETED)
          st.playSound("ItemSound.quest_finish")
        elif st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 1 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 0 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 0 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 0 :
            htmltext = "7416-07.htm"
        elif st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 0 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 1 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 0 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 0 :
            htmltext = "7416-08.htm"
        elif st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 0 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 0 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 0 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 0 :
            htmltext = "7416-09.htm"
        elif st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 0 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 0 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 1 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 0 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 0 :
            htmltext = "7416-10.htm"
        elif st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 0 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 0 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 0 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 1 :
            htmltext = "7416-11.htm"
   elif npcId == 7419 and st.getInt("cond")>=1 :
        if st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 0 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 0 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 0 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 1 :
          htmltext = "7419-01.htm"
        elif st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 1 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 0 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 0 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 0 :
            htmltext = "7419-07.htm"
        elif st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 0 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 0 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 1 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 0 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 0 :
            htmltext = "7419-08.htm"
            st.giveItems(ARKENIA_RECOMMEND_ID,1)
            st.takeItems(SHILENS_TEARS_ID,1)
            st.set("cond","7")
            st.playSound("ItemSound.quest_middle")
        elif st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 0 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 0 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 1 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 0 :
            htmltext = "7419-09.htm"
        elif st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 0 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 1 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 0 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 0 :
            htmltext = "7419-10.htm"
        elif st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 0 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 0 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 0 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 0 :
            htmltext = "7419-11.htm"
   elif npcId == 7382 and st.getInt("cond")>=1 :
        if st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 1 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 0 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 0 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 0 and st.getQuestItemsCount(ONYX_BEASTS_MOLAR_ID) == 0 :
          htmltext = "7382-01.htm"
        elif st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 0 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 1 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 0 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 0 and st.getQuestItemsCount(ONYX_BEASTS_MOLAR_ID) == 0 :
          htmltext = "7382-05.htm"
        elif st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 0 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 1 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 0 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 0 and st.getQuestItemsCount(ONYX_BEASTS_MOLAR_ID)<10 :
            htmltext = "7382-06.htm"
        elif st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 0 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 1 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 0 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 0 and st.getQuestItemsCount(ONYX_BEASTS_MOLAR_ID) >= 10 :
            st.set("cond","5")
            st.playSound("ItemSound.quest_middle")
            htmltext = "7382-07.htm"
            st.takeItems(ONYX_BEASTS_MOLAR_ID,10)
            st.takeItems(LEIKANS_NOTE_ID,1)
        elif st.getQuestItemsCount(SHILENS_TEARS_ID) == 1 :
            htmltext = "7382-08.htm"
        elif st.getInt("cond") >= 1 and st.getQuestItemsCount(ARKENIAS_LETTER_ID) == 0 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 0 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 and st.getQuestItemsCount(ARKENIA_RECOMMEND_ID) == 0 and st.getQuestItemsCount(IRON_HEART_ID) == 0 and st.getQuestItemsCount(SHILENS_CALL_ID) == 0 and st.getQuestItemsCount(ONYX_BEASTS_MOLAR_ID) == 0 :
            htmltext = "7382-09.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("411_PathToAssassin")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId == 5036 :
        if st.getInt("cond") >= 1 and st.getQuestItemsCount(SHILENS_TEARS_ID) == 0 :
          st.giveItems(SHILENS_TEARS_ID,1)
          st.playSound("ItemSound.quest_middle")
          st.set("cond","6")
      elif npcId == 369 :
        if st.getInt("cond") >= 1 and st.getQuestItemsCount(LEIKANS_NOTE_ID) == 1 and st.getQuestItemsCount(ONYX_BEASTS_MOLAR_ID)<10 :
          st.giveItems(ONYX_BEASTS_MOLAR_ID,1)
          if st.getQuestItemsCount(ONYX_BEASTS_MOLAR_ID) == 10 :
              st.playSound("ItemSound.quest_middle")
              st.set("cond","4")              
          else:
              st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(411,"411_PathToAssassin","Path To Assassin")
QUEST.addStartNpc(7416)

QUEST.addTalkId(7382)
QUEST.addTalkId(7416)
QUEST.addTalkId(7419)

QUEST.addKillId(369)
QUEST.addKillId(5036)