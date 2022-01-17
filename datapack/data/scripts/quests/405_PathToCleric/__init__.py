# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

LETTER_OF_ORDER1_ID = 1191
LETTER_OF_ORDER2_ID = 1192
BOOK_OF_LEMONIELL_ID = 1193
BOOK_OF_VIVI_ID = 1194
BOOK_OF_SIMLON_ID = 1195
BOOK_OF_PRAGA_ID = 1196
CERTIFICATE_OF_GALLINT_ID = 1197
PENDANT_OF_MOTHER_ID = 1198
NECKLACE_OF_MOTHER_ID = 1199
LEMONIELLS_COVENANT_ID = 1200
MARK_OF_FAITH_ID = 1201

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(1191,1201)

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
        st.set("id","0")
        if st.getPlayer().getLevel() >= 19 and st.getPlayer().getClassId().getId() == 0x0a and st.getQuestItemsCount(MARK_OF_FAITH_ID) == 0 :
          st.set("cond","1")
          st.setState(State.STARTED)
          st.playSound("ItemSound.quest_accept")
          st.giveItems(LETTER_OF_ORDER1_ID,1)
          htmltext = "7022-05.htm"
        elif st.getPlayer().getClassId().getId() != 0x0a :
            if st.getPlayer().getClassId().getId() == 0x0f :
              htmltext = "7022-02a.htm"
            else:
              htmltext = "7022-02.htm"
        elif st.getPlayer().getLevel()<19 and st.getPlayer().getClassId().getId() == 0x0a :
            htmltext = "7022-03.htm"
        elif st.getPlayer().getLevel() >= 19 and st.getPlayer().getClassId().getId() == 0x0a and st.getQuestItemsCount(MARK_OF_FAITH_ID) == 1 :
            htmltext = "7022-04.htm"
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if npcId != 7022 and id != State.STARTED :
        return htmltext
   if npcId == 7022 and st.getInt("cond")==0 :
        if st.getQuestItemsCount(MARK_OF_FAITH_ID) == 0 :
            htmltext = "7022-01.htm"
        else:
            htmltext = "7022-04.htm"
   elif npcId == 7022 and st.getInt("cond") and st.getQuestItemsCount(LETTER_OF_ORDER2_ID)==1 and st.getQuestItemsCount(LEMONIELLS_COVENANT_ID)==0 :
        htmltext = "7022-07.htm"
   elif npcId == 7022 and st.getInt("cond") and st.getQuestItemsCount(LETTER_OF_ORDER2_ID)==1 and st.getQuestItemsCount(LEMONIELLS_COVENANT_ID)==1 :
        htmltext = "7022-09.htm"
        st.takeItems(LEMONIELLS_COVENANT_ID,1)
        st.takeItems(LETTER_OF_ORDER2_ID,1)
        st.giveItems(MARK_OF_FAITH_ID,1)
        st.set("cond","0")
        st.setState(State.COMPLETED)
        st.playSound("ItemSound.quest_finish")
   elif npcId == 7022 and st.getInt("cond") and st.getQuestItemsCount(LETTER_OF_ORDER1_ID)==1 :
        if st.getQuestItemsCount(BOOK_OF_VIVI_ID) == 1 and st.getQuestItemsCount(BOOK_OF_SIMLON_ID)>0 and st.getQuestItemsCount(BOOK_OF_PRAGA_ID) == 1 :
            htmltext = "7022-08.htm"
            st.takeItems(BOOK_OF_PRAGA_ID,1)
            st.takeItems(BOOK_OF_VIVI_ID,1)
            st.takeItems(BOOK_OF_SIMLON_ID,3)
            st.takeItems(LETTER_OF_ORDER1_ID,1)
            st.giveItems(LETTER_OF_ORDER2_ID,1)
            st.set("cond","3")
        else:
            htmltext = "7022-06.htm"
   elif npcId == 7253 and st.getInt("cond") and st.getQuestItemsCount(LETTER_OF_ORDER1_ID)==1 :
        if st.getQuestItemsCount(BOOK_OF_SIMLON_ID) == 0 :
            htmltext = "7253-01.htm"
            st.giveItems(BOOK_OF_SIMLON_ID,3)
        elif st.getQuestItemsCount(BOOK_OF_SIMLON_ID)>0 :
            htmltext = "7253-02.htm"
   elif npcId == 7030 and st.getInt("cond") and st.getQuestItemsCount(LETTER_OF_ORDER1_ID)==1 :
        if st.getQuestItemsCount(BOOK_OF_VIVI_ID) == 0 :
            htmltext = "7030-01.htm"
            st.giveItems(BOOK_OF_VIVI_ID,1)
        elif st.getQuestItemsCount(BOOK_OF_VIVI_ID) == 1 :
            htmltext = "7030-02.htm"
   elif npcId == 7333 and st.getInt("cond") and st.getQuestItemsCount(LETTER_OF_ORDER1_ID)==1 :
        if st.getQuestItemsCount(BOOK_OF_PRAGA_ID) == 0 and st.getQuestItemsCount(NECKLACE_OF_MOTHER_ID) == 0 :
            htmltext = "7333-01.htm"
            st.giveItems(NECKLACE_OF_MOTHER_ID,1)
        elif st.getQuestItemsCount(BOOK_OF_PRAGA_ID) == 0 and st.getQuestItemsCount(NECKLACE_OF_MOTHER_ID) == 1 and st.getQuestItemsCount(PENDANT_OF_MOTHER_ID) == 0 :
            htmltext = "7333-02.htm"
        elif st.getQuestItemsCount(BOOK_OF_PRAGA_ID) == 0 and st.getQuestItemsCount(NECKLACE_OF_MOTHER_ID) == 1 and st.getQuestItemsCount(PENDANT_OF_MOTHER_ID) == 1 :
            htmltext = "7333-03.htm"
            st.takeItems(NECKLACE_OF_MOTHER_ID,1)
            st.takeItems(PENDANT_OF_MOTHER_ID,1)
            st.giveItems(BOOK_OF_PRAGA_ID,1)
            st.set("cond","2")
        elif st.getQuestItemsCount(BOOK_OF_PRAGA_ID)>0 :
            htmltext = "7333-04.htm"
   elif npcId == 7408 and st.getInt("cond") :
        if st.getQuestItemsCount(LETTER_OF_ORDER2_ID) == 0 :
          htmltext = "7408-02.htm"
        elif st.getQuestItemsCount(LETTER_OF_ORDER2_ID) == 1 and st.getQuestItemsCount(BOOK_OF_LEMONIELL_ID) == 0 and st.getQuestItemsCount(LEMONIELLS_COVENANT_ID) == 0 and st.getQuestItemsCount(CERTIFICATE_OF_GALLINT_ID) == 0 :
            htmltext = "7408-01.htm"
            st.giveItems(BOOK_OF_LEMONIELL_ID,1)
            st.set("cond","4")
        elif st.getQuestItemsCount(LETTER_OF_ORDER2_ID) == 1 and st.getQuestItemsCount(BOOK_OF_LEMONIELL_ID) == 1 and st.getQuestItemsCount(LEMONIELLS_COVENANT_ID) == 0 and st.getQuestItemsCount(CERTIFICATE_OF_GALLINT_ID) == 0 :
            htmltext = "7408-03.htm"
        elif st.getQuestItemsCount(LETTER_OF_ORDER2_ID) == 1 and st.getQuestItemsCount(BOOK_OF_LEMONIELL_ID) == 0 and st.getQuestItemsCount(LEMONIELLS_COVENANT_ID) == 0 and st.getQuestItemsCount(CERTIFICATE_OF_GALLINT_ID) == 1 :
            htmltext = "7408-04.htm"
            st.takeItems(CERTIFICATE_OF_GALLINT_ID,1)
            st.giveItems(LEMONIELLS_COVENANT_ID,1)
            st.set("cond","6")
        elif st.getQuestItemsCount(LETTER_OF_ORDER2_ID) == 1 and st.getQuestItemsCount(BOOK_OF_LEMONIELL_ID) == 0 and st.getQuestItemsCount(LEMONIELLS_COVENANT_ID) == 1 and st.getQuestItemsCount(CERTIFICATE_OF_GALLINT_ID) == 0 :
            htmltext = "7408-05.htm"
   elif npcId == 7017 and st.getInt("cond") and st.getQuestItemsCount(LETTER_OF_ORDER2_ID)==1 and st.getQuestItemsCount(LEMONIELLS_COVENANT_ID)==0 :
        if st.getQuestItemsCount(BOOK_OF_LEMONIELL_ID) == 1 and st.getQuestItemsCount(CERTIFICATE_OF_GALLINT_ID) == 0 :
            htmltext = "7017-01.htm"
            st.takeItems(BOOK_OF_LEMONIELL_ID,1)
            st.giveItems(CERTIFICATE_OF_GALLINT_ID,1)
            st.set("cond","5")
        elif st.getQuestItemsCount(BOOK_OF_LEMONIELL_ID) == 0 and st.getQuestItemsCount(CERTIFICATE_OF_GALLINT_ID) == 1 :
            htmltext = "7017-02.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("405_PathToCleric")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId == 26 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(PENDANT_OF_MOTHER_ID) == 0 :
          st.giveItems(PENDANT_OF_MOTHER_ID,1)
          st.playSound("ItemSound.quest_middle")
      elif npcId == 29 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(PENDANT_OF_MOTHER_ID) == 0 :
          st.giveItems(PENDANT_OF_MOTHER_ID,1)
          st.playSound("ItemSound.quest_middle")
   return

QUEST = Quest(405,"405_PathToCleric","Path To Cleric")
QUEST.addStartNpc(7022)

QUEST.addTalkId(7017)
QUEST.addTalkId(7022)
QUEST.addTalkId(7030)
QUEST.addTalkId(7253)
QUEST.addTalkId(7333)
QUEST.addTalkId(7408)

QUEST.addKillId(26)
QUEST.addKillId(29)