# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

SILVERYS_RING_ID = 1632
PASS_1ST_ID = 1633
PASS_2ND_ID = 1634
PASS_FINAL_ID = 1635
RATMAN_TOOTH_ID = 1636
BIG_RATMAN_TOOTH_ID = 1637
KLUTOS_LETTER_ID = 1638
FOOTPRINT_ID = 1639
SECRET_BOX1_ID = 1640
SECRET_BOX2_ID = 1641
TOTEM_SPIRIT_CLAW_ID = 1622
TATARUS_LETTER_ID = 1623

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(1632, 1635) + range(1636,1642) + [1622, 1623]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7527_1" :
          if st.getPlayer().getClassId().getId() != 0x35 :
            if st.getPlayer().getClassId().getId() == 0x38 :
              htmltext = "7527-02a.htm"
            else:
              htmltext = "7527-02.htm"
          else:
            if st.getPlayer().getLevel()<19 :
              htmltext = "7527-03.htm"
            else:
              if st.getQuestItemsCount(PASS_FINAL_ID) != 0 :
                htmltext = "7527-04.htm"
              else:
                htmltext = "7527-05.htm"
                return htmltext
    elif event == "7527_2" :
          htmltext = "7527-11.htm"
          st.takeItems(TOTEM_SPIRIT_CLAW_ID,1)
          st.giveItems(TATARUS_LETTER_ID,1)
    elif event == "1" :
        st.set("id","0")
        st.set("cond","1")
        st.setState(State.STARTED)
        st.playSound("ItemSound.quest_accept")
        htmltext = "7527-06.htm"
        st.giveItems(SILVERYS_RING_ID,1)
    elif event == "7317_1" :
          htmltext = "7317-02.htm"
    elif event == "7317_2" :
          htmltext = "7317-05.htm"
    elif event == "7317_3" :
          htmltext = "7317-03.htm"
    elif event == "7317_4" :
          htmltext = "7317-04.htm"
          st.giveItems(KLUTOS_LETTER_ID,1)
          st.set("cond","4")
    elif event == "7317_5" :
          htmltext = "7317-06.htm"
    elif event == "7317_6" :
          htmltext = "7317-07.htm"
          st.giveItems(KLUTOS_LETTER_ID,1)
          st.set("cond","4")
    elif event == "7317_7" :
        if st.getQuestItemsCount(PASS_1ST_ID) and st.getQuestItemsCount(PASS_2ND_ID) and st.getQuestItemsCount(SECRET_BOX2_ID) :
          htmltext = "7317-10.htm"
          st.takeItems(PASS_1ST_ID,1)
          st.takeItems(PASS_2ND_ID,1)
          st.takeItems(SECRET_BOX2_ID,1)
          st.giveItems(PASS_FINAL_ID,1)
          st.set("cond","0")
          st.setState(State.COMPLETED)
          st.playSound("ItemSound.quest_finish")
        else :
          htmltext = "7317-08.htm"
    elif event == "7317_8" :
          htmltext = "7317-11.htm"
    elif event == "7317_9" :
        if st.getQuestItemsCount(PASS_1ST_ID) and st.getQuestItemsCount(PASS_2ND_ID) and st.getQuestItemsCount(SECRET_BOX2_ID) :
          htmltext = "7317-12.htm"
          st.takeItems(PASS_1ST_ID,1)
          st.takeItems(PASS_2ND_ID,1)
          st.takeItems(SECRET_BOX2_ID,1)
          st.giveItems(PASS_FINAL_ID,1)
          st.set("cond","0")
          st.setState(State.COMPLETED)
          st.playSound("ItemSound.quest_finish")
        else :
          htmltext = "7317-08.htm"
    elif event == "7298_1" :
          htmltext = "7298-02.htm"
    elif event == "7298_2" :
          htmltext = "7298-03.htm"
          st.takeItems(KLUTOS_LETTER_ID,1)
          st.giveItems(FOOTPRINT_ID,1)
          st.set("cond","5")
    elif event == "7298_3" :
          htmltext = "7298-06.htm"
          st.takeItems(SECRET_BOX1_ID,1)
          st.takeItems(FOOTPRINT_ID,1)
          st.giveItems(SECRET_BOX2_ID,1)
          st.giveItems(PASS_2ND_ID,1)
          st.set("cond","7")
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if npcId != 7527 and id != State.STARTED :
        return htmltext
   if npcId == 7527 and st.getInt("cond")==0 :
        htmltext = "7527-01.htm"
   elif npcId == 7527 and st.getInt("cond") and st.getQuestItemsCount(SILVERYS_RING_ID)==1 and (st.getQuestItemsCount(RATMAN_TOOTH_ID)+st.getQuestItemsCount(BIG_RATMAN_TOOTH_ID))<12 :
        htmltext = "7527-07.htm"
   elif npcId == 7527 and st.getInt("cond") and st.getQuestItemsCount(SILVERYS_RING_ID)==1 and st.getQuestItemsCount(RATMAN_TOOTH_ID)>=10 and st.getQuestItemsCount(BIG_RATMAN_TOOTH_ID)>=2 :
        htmltext = "7527-08.htm"
        st.takeItems(SILVERYS_RING_ID,st.getQuestItemsCount(SILVERYS_RING_ID))
        st.takeItems(RATMAN_TOOTH_ID,st.getQuestItemsCount(RATMAN_TOOTH_ID))
        st.takeItems(BIG_RATMAN_TOOTH_ID,st.getQuestItemsCount(BIG_RATMAN_TOOTH_ID))
        st.giveItems(PASS_1ST_ID,1)
        st.set("cond","3")
   elif npcId == 7527 and st.getInt("cond") and st.getQuestItemsCount(PASS_1ST_ID)==1 :
        htmltext = "7527-09.htm"
   elif npcId == 7317 and st.getInt("cond") and st.getQuestItemsCount(KLUTOS_LETTER_ID)==0 and st.getQuestItemsCount(PASS_1ST_ID) and st.getQuestItemsCount(PASS_2ND_ID)==0 and st.getQuestItemsCount(SECRET_BOX2_ID)==0 :
        htmltext = "7317-01.htm"
   elif npcId == 7317 and st.getInt("cond") and st.getQuestItemsCount(PASS_1ST_ID) and (st.getQuestItemsCount(KLUTOS_LETTER_ID) or st.getQuestItemsCount(FOOTPRINT_ID)) :
        htmltext = "7317-08.htm"
   elif npcId == 7317 and st.getInt("cond") and st.getQuestItemsCount(PASS_1ST_ID) and st.getQuestItemsCount(PASS_2ND_ID) and st.getQuestItemsCount(SECRET_BOX2_ID) :
        htmltext = "7317-09.htm"
   elif npcId == 7298 and st.getInt("cond") and st.getQuestItemsCount(PASS_1ST_ID) and st.getQuestItemsCount(KLUTOS_LETTER_ID) :
        htmltext = "7298-01.htm"
   elif npcId == 7298 and st.getInt("cond") and st.getQuestItemsCount(PASS_1ST_ID) and st.getQuestItemsCount(FOOTPRINT_ID) and st.getQuestItemsCount(SECRET_BOX1_ID)==0 :
        htmltext = "7298-04.htm"
   elif npcId == 7298 and st.getInt("cond") and st.getQuestItemsCount(PASS_1ST_ID) and st.getQuestItemsCount(FOOTPRINT_ID) and st.getQuestItemsCount(SECRET_BOX1_ID) :
        htmltext = "7298-05.htm"
   elif npcId == 7298 and st.getInt("cond") and st.getQuestItemsCount(PASS_1ST_ID) and st.getQuestItemsCount(PASS_2ND_ID) and st.getQuestItemsCount(SECRET_BOX2_ID) :
        htmltext = "7298-07.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("418_PathToArtisan")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId == 389 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(SILVERYS_RING_ID) == 1 and st.getQuestItemsCount(RATMAN_TOOTH_ID)<10 :
          if st.getRandom(10)<7 :
            if st.getQuestItemsCount(RATMAN_TOOTH_ID) == 9 and st.getQuestItemsCount(BIG_RATMAN_TOOTH_ID) == 2 :
              st.giveItems(RATMAN_TOOTH_ID,1)
              st.playSound("ItemSound.quest_middle")
              st.set("cond","2")
            else:
              st.giveItems(RATMAN_TOOTH_ID,1)
              st.playSound("ItemSound.quest_itemget")
      elif npcId == 390 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(SILVERYS_RING_ID) == 1 and st.getQuestItemsCount(BIG_RATMAN_TOOTH_ID)<2 :
          if st.getRandom(10)<5 :
            if st.getQuestItemsCount(BIG_RATMAN_TOOTH_ID) == 1 and st.getQuestItemsCount(RATMAN_TOOTH_ID) == 10 :
              st.giveItems(BIG_RATMAN_TOOTH_ID,1)
              st.playSound("ItemSound.quest_middle")
              st.set("cond","2")
            else:
              st.giveItems(BIG_RATMAN_TOOTH_ID,1)
              st.playSound("ItemSound.quest_itemget")
      elif npcId == 17 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(FOOTPRINT_ID) == 1 and st.getQuestItemsCount(SECRET_BOX1_ID)<1 :
          if st.getRandom(10)<2 :
            st.giveItems(SECRET_BOX1_ID,1)
            st.playSound("ItemSound.quest_middle")
            st.set("cond","6")
   return

QUEST = Quest(418,"418_PathToArtisan","Path To Artisan")
QUEST.addStartNpc(7527)

QUEST.addTalkId(7298)
QUEST.addTalkId(7317)
QUEST.addTalkId(7527)

QUEST.addKillId(17)
QUEST.addKillId(389)
QUEST.addKillId(390)