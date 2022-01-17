# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

REORIA_LETTER2_ID = 1207
PRIGUNS_TEAR_LETTER1_ID = 1208
PRIGUNS_TEAR_LETTER2_ID = 1209
PRIGUNS_TEAR_LETTER3_ID = 1210
PRIGUNS_TEAR_LETTER4_ID = 1211
MORETTIS_HERB_ID = 1212
MORETTIS_LETTER_ID = 1214
PRIGUNS_LETTER_ID = 1215
HONORARY_GUARD_ID = 1216
REORIA_RECOMMENDATION_ID = 1217
RUSTED_KEY_ID = 1293

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(1207,1217)+[RUSTED_KEY_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
      st.set("id","0")
      if st.getPlayer().getClassId().getId() == 0x12 :
        if st.getPlayer().getLevel() >= 19 :
          if st.getQuestItemsCount(REORIA_RECOMMENDATION_ID)>0 :
            htmltext = "7328-04.htm"
          else:
            htmltext = "7328-05.htm"
            st.giveItems(REORIA_LETTER2_ID,1)
            st.set("cond","1")
            st.setState(State.STARTED)
            st.playSound("ItemSound.quest_accept")
        else :
          htmltext = "7328-03.htm"
      else:
        if st.getPlayer().getClassId().getId() == 0x16 :
          htmltext = "7328-02a.htm"
        else:
          htmltext = "7328-02.htm"
    elif event == "7337_1" :
          st.takeItems(REORIA_LETTER2_ID,1)
          st.set("cond","2")
          htmltext = "7337-03.htm"
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if npcId != 7328 and id != State.STARTED :
        return htmltext
   if npcId == 7328 and st.getInt("cond")==0 :
        htmltext = "7328-01.htm"
   elif npcId == 7328 and st.getInt("cond") and st.getQuestItemsCount(REORIA_LETTER2_ID)>0 :
        htmltext = "7328-06.htm"
   elif npcId == 7328 and st.getInt("cond") and st.getQuestItemsCount(REORIA_LETTER2_ID)==0 and st.getQuestItemsCount(HONORARY_GUARD_ID)==0 :
        htmltext = "7328-08.htm"
   elif npcId == 7337 and st.getInt("cond") and st.getQuestItemsCount(REORIA_LETTER2_ID)>0 and st.getQuestItemsCount(PRIGUNS_TEAR_LETTER1_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER2_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER3_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER4_ID)==0 :
        htmltext = "7337-01.htm"
   elif npcId == 7337 and st.getQuestItemsCount(MORETTIS_LETTER_ID)<1 and st.getQuestItemsCount(PRIGUNS_LETTER_ID)==0 and st.getQuestItemsCount(HONORARY_GUARD_ID)==0 :
        if st.getQuestItemsCount(PRIGUNS_TEAR_LETTER1_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER2_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER3_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER4_ID)<1 :
          htmltext = "7337-04.htm"
        elif st.getQuestItemsCount(PRIGUNS_TEAR_LETTER1_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER2_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER3_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER4_ID)>0 and st.getQuestItemsCount(PRIGUNS_TEAR_LETTER1_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER2_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER3_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER4_ID)<4 :
          htmltext = "7337-05.htm"
        else:
          htmltext = "7337-06.htm"
          st.takeItems(PRIGUNS_TEAR_LETTER1_ID,1)
          st.takeItems(PRIGUNS_TEAR_LETTER2_ID,1)
          st.takeItems(PRIGUNS_TEAR_LETTER3_ID,1)
          st.takeItems(PRIGUNS_TEAR_LETTER4_ID,1)
          st.giveItems(MORETTIS_HERB_ID,1)
          st.giveItems(MORETTIS_LETTER_ID,1)
          st.set("cond","4")
   elif npcId == 7334 and st.getInt("cond") :
        htmltext = "7334-01.htm"
   elif npcId == 7426 and st.getInt("cond") and st.getQuestItemsCount(MORETTIS_LETTER_ID) and st.getQuestItemsCount(MORETTIS_HERB_ID) :
        if st.getQuestItemsCount(RUSTED_KEY_ID)<1 :
          htmltext = "7426-01.htm"
          st.set("cond","5")
        else:
          htmltext = "7426-02.htm"
          st.takeItems(RUSTED_KEY_ID,1)
          st.takeItems(MORETTIS_HERB_ID,1)
          st.takeItems(MORETTIS_LETTER_ID,1)
          st.giveItems(PRIGUNS_LETTER_ID,1)
          st.set("cond","7")
   elif npcId == 7426 and st.getInt("cond") and st.getQuestItemsCount(PRIGUNS_LETTER_ID) :
        htmltext = "7426-04.htm"
   elif npcId == 7337 and st.getInt("cond") and st.getQuestItemsCount(PRIGUNS_LETTER_ID)>0 :
        if st.getQuestItemsCount(MORETTIS_HERB_ID) :
          htmltext = "7337-09.htm"
        else:
          htmltext = "7337-07.htm"
          st.takeItems(PRIGUNS_LETTER_ID,1)
          st.giveItems(HONORARY_GUARD_ID,1)
          st.set("cond","8")
   elif npcId == 7337 and st.getInt("cond") and st.getQuestItemsCount(HONORARY_GUARD_ID)>0 :
        htmltext = "7337-08.htm"
   elif npcId == 7328 and st.getInt("cond") and st.getQuestItemsCount(HONORARY_GUARD_ID)>0 :
        htmltext = "7328-07.htm"
        st.takeItems(HONORARY_GUARD_ID,1)
        st.giveItems(REORIA_RECOMMENDATION_ID,1)
        st.set("cond","0")
        st.setState(State.COMPLETED)
        st.playSound("ItemSound.quest_finish")
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("407_PathToElvenScout")
   if st :
     if st.getState() != State.STARTED : return
     npcId = npc.getNpcId()
     if npcId == 53 :
        st.set("id","0")
        if st.getInt("cond") :
          if st.getQuestItemsCount(PRIGUNS_TEAR_LETTER1_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER2_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER3_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER4_ID) < 4 :
            if st.getQuestItemsCount(PRIGUNS_TEAR_LETTER1_ID)<1 :
              st.giveItems(PRIGUNS_TEAR_LETTER1_ID,1)
              if st.getQuestItemsCount(PRIGUNS_TEAR_LETTER1_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER2_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER3_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER4_ID) == 4 :
                st.playSound("ItemSound.quest_middle")
                st.set("cond","3")
              else:
                st.playSound("ItemSound.quest_itemget")
            else:
              if st.getQuestItemsCount(PRIGUNS_TEAR_LETTER2_ID)<1 :
                st.giveItems(PRIGUNS_TEAR_LETTER2_ID,1)
                if st.getQuestItemsCount(PRIGUNS_TEAR_LETTER1_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER2_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER3_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER4_ID) == 4 :
                  st.playSound("ItemSound.quest_middle")
                  st.set("cond","3")
                else:
                  st.playSound("ItemSound.quest_itemget")
              else:
                if st.getQuestItemsCount(PRIGUNS_TEAR_LETTER3_ID)<1 :
                  st.giveItems(PRIGUNS_TEAR_LETTER3_ID,1)
                  if st.getQuestItemsCount(PRIGUNS_TEAR_LETTER1_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER2_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER3_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER4_ID) == 4 :
                    st.playSound("ItemSound.quest_middle")
                    st.set("cond","3")
                  else:
                    st.playSound("ItemSound.quest_itemget")
                else:
                  if st.getQuestItemsCount(PRIGUNS_TEAR_LETTER4_ID)<1 :
                    st.giveItems(PRIGUNS_TEAR_LETTER4_ID,1)
                    if st.getQuestItemsCount(PRIGUNS_TEAR_LETTER1_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER2_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER3_ID)+st.getQuestItemsCount(PRIGUNS_TEAR_LETTER4_ID) == 4 :
                      st.playSound("ItemSound.quest_middle")
                      st.set("cond","3")
                    else:
                      st.playSound("ItemSound.quest_itemget")
     elif npcId == 5031 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(MORETTIS_HERB_ID) == 1 and st.getQuestItemsCount(MORETTIS_LETTER_ID) == 1 and st.getQuestItemsCount(RUSTED_KEY_ID) == 0 and st.getRandom(10)<6 :
          st.giveItems(RUSTED_KEY_ID,1)
          st.playSound("ItemSound.quest_middle")
          st.set("cond","6")
   return

QUEST = Quest(407,"407_PathToElvenScout","Path To Elven Scout")
QUEST.addStartNpc(7328)

QUEST.addTalkId(7328)
QUEST.addTalkId(7334)
QUEST.addTalkId(7337)
QUEST.addTalkId(7426)

QUEST.addKillId(5031)
QUEST.addKillId(53)