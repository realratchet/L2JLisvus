# Made by Mr. - Version 0.3 by kmarty and DrLecter
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

SORIUS_LETTER1_ID = 1202
KLUTO_BOX_ID = 1203
ELVEN_KNIGHT_BROOCH_ID = 1204
TOPAZ_PIECE_ID = 1205
EMERALD_PIECE_ID = 1206
KLUTO_MEMO_ID = 1276
#messages
default = JQuest.getNoQuestMsg()

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [SORIUS_LETTER1_ID, EMERALD_PIECE_ID, TOPAZ_PIECE_ID, KLUTO_MEMO_ID, KLUTO_BOX_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7327-05.htm" :
       if st.getPlayer().getClassId().getId() != 0x12 :
          if st.getPlayer().getClassId().getId() == 0x13 :
             htmltext = "7327-02a.htm"
          else:
             htmltext = "7327-02.htm"
             st.exitQuest(1)
       else:
          if st.getPlayer().getLevel()<19 :
             htmltext = "7327-03.htm"
             st.exitQuest(1)
          else:
             if st.getQuestItemsCount(ELVEN_KNIGHT_BROOCH_ID) :
                htmltext = "7327-04.htm"
    elif event == "7327-06.htm" :
       st.set("cond","1")
       st.setState(State.STARTED)
       st.playSound("ItemSound.quest_accept")
    elif event == "7317-02.htm" :
       if st.getInt("cond") == 3 :
          st.takeItems(SORIUS_LETTER1_ID,-1)
          if st.getQuestItemsCount(KLUTO_MEMO_ID) == 0 :
             st.giveItems(KLUTO_MEMO_ID,1)
             st.set("cond","4")
          else :
             htmltext = default
       else :
          htmltext = default
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = default
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
     cond=0
   else :
     cond=st.getInt("cond")
   if npcId == 7327 :
        if cond == 0 :
            htmltext = "7327-01.htm"
        elif cond == 1 :
            if st.getQuestItemsCount(TOPAZ_PIECE_ID)==0 :
              htmltext = "7327-07.htm"
            else:
              htmltext = "7327-08.htm"
        elif cond == 2 :
            if st.getQuestItemsCount(SORIUS_LETTER1_ID) == 0 :
              st.giveItems(SORIUS_LETTER1_ID,1)
            st.set("cond","3")
            htmltext = "7327-09.htm"
        elif cond in [3, 4, 5] :
            htmltext = "7327-11.htm"
        elif cond == 6 :
            st.takeItems(KLUTO_BOX_ID,-1)
            st.set("cond","0")
            st.setState(State.COMPLETED)
            st.playSound("ItemSound.quest_finish")
            if st.getQuestItemsCount(ELVEN_KNIGHT_BROOCH_ID) == 0 :
              st.giveItems(ELVEN_KNIGHT_BROOCH_ID,1)
            htmltext = "7327-10.htm"
   elif npcId == 7317 :
        if cond == 3 :
            htmltext = "7317-01.htm"
        elif  cond == 4 :
            if st.getQuestItemsCount(EMERALD_PIECE_ID)==0 :
              htmltext = "7317-03.htm"
            else:
              htmltext = "7317-04.htm"
        elif cond == 5 :
            st.takeItems(EMERALD_PIECE_ID,-1)
            st.takeItems(TOPAZ_PIECE_ID,-1)
            if st.getQuestItemsCount(KLUTO_BOX_ID) == 0 :
              st.giveItems(KLUTO_BOX_ID,1)
            st.takeItems(KLUTO_MEMO_ID,-1)
            st.set("cond","6")
            htmltext = "7317-05.htm"
        elif cond == 6 :
            htmltext = "7317-06.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("406_PathToElvenKnight")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId != 782 :
        if st.getInt("cond")==1 and st.getQuestItemsCount(TOPAZ_PIECE_ID)<20 and st.getRandom(100)<70 :
            st.giveItems(TOPAZ_PIECE_ID,1)
            if st.getQuestItemsCount(TOPAZ_PIECE_ID) == 20 :
              st.playSound("ItemSound.quest_middle")
              st.set("cond","2")
            else:
              st.playSound("ItemSound.quest_itemget")
      else :
        if st.getInt("cond")==4 and st.getQuestItemsCount(EMERALD_PIECE_ID)<20 and st.getRandom(100)<50 :
            st.giveItems(EMERALD_PIECE_ID,1)
            if st.getQuestItemsCount(EMERALD_PIECE_ID) == 20 :
              st.playSound("ItemSound.quest_middle")
              st.set("cond","5")
            else:
              st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(406,"406_PathToElvenKnight","Path To Elven Knight")
QUEST.addStartNpc(7327)

QUEST.addTalkId(7317)
QUEST.addTalkId(7327)

QUEST.addKillId(35)
QUEST.addKillId(42)
QUEST.addKillId(45)
QUEST.addKillId(51)
QUEST.addKillId(54)
QUEST.addKillId(60)
QUEST.addKillId(782)