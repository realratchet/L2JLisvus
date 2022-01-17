# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

PALLUS_TALISMAN_ID = 1237
LYCANTHROPE_SKULL_ID = 1238
VIRGILS_LETTER_ID = 1239
MORTE_TALISMAN_ID = 1240
PREDATOR_CARAPACE_ID = 1241
TRIMDEN_SILK_ID = 1242
COFFIN_ETERNAL_REST_ID = 1243
GAZE_OF_ABYSS_ID = 1244

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(1237,1244)

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
        st.set("id","0")
        st.set("cond","1")
        st.setState(State.STARTED)
        st.playSound("ItemSound.quest_accept")
        htmltext = "7329-06.htm"
        st.giveItems(PALLUS_TALISMAN_ID,1)
    elif event == "410_1" :
          if st.getPlayer().getLevel() >= 19 and st.getPlayer().getClassId().getId() == 0x1f and st.getQuestItemsCount(GAZE_OF_ABYSS_ID) == 0 :
            htmltext = "7329-05.htm"
            return htmltext
          elif st.getPlayer().getClassId().getId() != 0x1f :
              if st.getPlayer().getClassId().getId() == 0x20 :
                htmltext = "7329-02a.htm"
              else:
                htmltext = "7329-03.htm"
          elif st.getPlayer().getLevel()<19 and st.getPlayer().getClassId().getId() == 0x1f :
              htmltext = "7329-02.htm"
          elif st.getPlayer().getLevel() >= 19 and st.getPlayer().getClassId().getId() == 0x1f and st.getQuestItemsCount(GAZE_OF_ABYSS_ID) == 1 :
              htmltext = "7329-04.htm"
    elif event == "7329_2" :
            htmltext = "7329-10.htm"
            st.takeItems(PALLUS_TALISMAN_ID,1)
            st.takeItems(LYCANTHROPE_SKULL_ID,st.getQuestItemsCount(LYCANTHROPE_SKULL_ID))
            st.giveItems(VIRGILS_LETTER_ID,1)
            st.set("cond","3")
    elif event == "7422_1" :
          htmltext = "7422-02.htm"
          st.takeItems(VIRGILS_LETTER_ID,1)
          st.giveItems(MORTE_TALISMAN_ID,1)
          st.set("cond","4")
    elif event == "7422_2" :
            htmltext = "7422-06.htm"
            st.takeItems(MORTE_TALISMAN_ID,1)
            st.takeItems(TRIMDEN_SILK_ID,st.getQuestItemsCount(TRIMDEN_SILK_ID))
            st.takeItems(PREDATOR_CARAPACE_ID,st.getQuestItemsCount(PREDATOR_CARAPACE_ID))
            st.giveItems(COFFIN_ETERNAL_REST_ID,1)
            st.set("cond","6")
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if npcId != 7329 and id != State.STARTED :
        return htmltext
   if npcId == 7329 and st.getInt("cond")==0 :
        htmltext = "7329-01.htm"
   elif npcId == 7329 and st.getInt("cond") :
        if st.getQuestItemsCount(PALLUS_TALISMAN_ID) == 1 and st.getQuestItemsCount(LYCANTHROPE_SKULL_ID) == 0 :
          htmltext = "7329-07.htm"
        elif st.getQuestItemsCount(PALLUS_TALISMAN_ID) == 1 and st.getQuestItemsCount(LYCANTHROPE_SKULL_ID)>0 and st.getQuestItemsCount(LYCANTHROPE_SKULL_ID)<13 :
            htmltext = "7329-08.htm"
        elif st.getQuestItemsCount(PALLUS_TALISMAN_ID) == 1 and st.getQuestItemsCount(LYCANTHROPE_SKULL_ID) >= 13 :
            htmltext = "7329-09.htm"
        elif st.getQuestItemsCount(COFFIN_ETERNAL_REST_ID) == 1 :
            htmltext = "7329-11.htm"
            st.takeItems(COFFIN_ETERNAL_REST_ID,1)
            st.giveItems(GAZE_OF_ABYSS_ID,1)
            st.set("cond","0")
            st.setState(State.COMPLETED)
            st.playSound("ItemSound.quest_finish")
        elif st.getQuestItemsCount(MORTE_TALISMAN_ID) or st.getQuestItemsCount(VIRGILS_LETTER_ID) :
            htmltext = "7329-12.htm"
   elif npcId == 7422 and st.getInt("cond") :
        if st.getQuestItemsCount(VIRGILS_LETTER_ID) :
          htmltext = "7422-01.htm"
        elif st.getQuestItemsCount(MORTE_TALISMAN_ID) and st.getQuestItemsCount(TRIMDEN_SILK_ID) == 0 and st.getQuestItemsCount(PREDATOR_CARAPACE_ID) == 0 :
            htmltext = "7422-03.htm"
        elif st.getQuestItemsCount(MORTE_TALISMAN_ID) and st.getQuestItemsCount(TRIMDEN_SILK_ID)>0 and st.getQuestItemsCount(PREDATOR_CARAPACE_ID) == 0 :
            htmltext = "7422-04.htm"
        elif st.getQuestItemsCount(MORTE_TALISMAN_ID) and st.getQuestItemsCount(TRIMDEN_SILK_ID) == 0 and st.getQuestItemsCount(PREDATOR_CARAPACE_ID)>0 :
            htmltext = "7422-04.htm"
        elif st.getQuestItemsCount(MORTE_TALISMAN_ID) and st.getQuestItemsCount(TRIMDEN_SILK_ID) >= 5 and st.getQuestItemsCount(PREDATOR_CARAPACE_ID)>0 :
            htmltext = "7422-05.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("410_PathToPalusKnight")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId == 49 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(PALLUS_TALISMAN_ID) == 1 and st.getQuestItemsCount(LYCANTHROPE_SKULL_ID)<13 :
          st.giveItems(LYCANTHROPE_SKULL_ID,1)
          if st.getQuestItemsCount(LYCANTHROPE_SKULL_ID) == 13 :
            st.playSound("ItemSound.quest_middle")
            st.set("cond","2")
          else:
            st.playSound("ItemSound.quest_itemget")
      elif npcId == 38 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(MORTE_TALISMAN_ID) == 1 and st.getQuestItemsCount(PREDATOR_CARAPACE_ID)<1 :
          st.giveItems(PREDATOR_CARAPACE_ID,1)
          st.playSound("ItemSound.quest_middle")
          if st.getQuestItemsCount(TRIMDEN_SILK_ID) >= 5 and st.getQuestItemsCount(PREDATOR_CARAPACE_ID)>0 :
            st.set("cond","5")
      elif npcId == 43 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(MORTE_TALISMAN_ID) == 1 and st.getQuestItemsCount(TRIMDEN_SILK_ID)<5 :
          st.giveItems(TRIMDEN_SILK_ID,1)
          if st.getQuestItemsCount(TRIMDEN_SILK_ID) == 5 :
            st.playSound("ItemSound.quest_middle")
            if st.getQuestItemsCount(TRIMDEN_SILK_ID) >= 5 and st.getQuestItemsCount(PREDATOR_CARAPACE_ID)>0 :
              st.set("cond","5")
          else:
            st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(410,"410_PathToPalusKnight","Path To Palus Knight")
QUEST.addStartNpc(7329)

QUEST.addTalkId(7329)
QUEST.addTalkId(7422)

QUEST.addKillId(38)
QUEST.addKillId(43)
QUEST.addKillId(49)