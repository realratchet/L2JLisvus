# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

SEEDS_OF_DESPAIR_ID = 1254
SEEDS_OF_ANGER_ID = 1253
SEEDS_OF_HORROR_ID = 1255
SEEDS_OF_LUNACY_ID = 1256
FAMILYS_ASHES_ID = 1257
KNEE_BONE_ID = 1259
HEART_OF_LUNACY_ID = 1260
JEWEL_OF_DARKNESS_ID = 1261
LUCKY_KEY_ID = 1277
CANDLE_ID = 1278
HUB_SCENT_ID = 1279

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(1254, 1258)+[KNEE_BONE_ID, HEART_OF_LUNACY_ID]+range(1277,1280)

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
        st.set("id","0")
        if st.getInt("cond") == 0 :
          if st.getPlayer().getLevel() >= 19 and st.getPlayer().getClassId().getId() == 0x26 and st.getQuestItemsCount(JEWEL_OF_DARKNESS_ID) == 0 :
            st.set("cond","1")
            st.setState(State.STARTED)
            st.playSound("ItemSound.quest_accept")
            st.giveItems(SEEDS_OF_DESPAIR_ID,1)
            htmltext = "7421-05.htm"
          elif st.getPlayer().getClassId().getId() != 0x26 :
              if st.getPlayer().getClassId().getId() == 0x27 :
                htmltext = "7421-02a.htm"
              else:
                htmltext = "7421-03.htm"
          elif st.getPlayer().getLevel()<19 and st.getPlayer().getClassId().getId() == 0x26 :
              htmltext = "7421-02.htm"
          elif st.getPlayer().getLevel() >= 19 and st.getPlayer().getClassId().getId() == 0x26 and st.getQuestItemsCount(JEWEL_OF_DARKNESS_ID) == 1 :
              htmltext = "7421-04.htm"
    elif event == "412_1" :
          if st.getQuestItemsCount(SEEDS_OF_ANGER_ID) :
            htmltext = "7421-06.htm"
          else:
            htmltext = "7421-07.htm"
    elif event == "412_2" :
            if st.getQuestItemsCount(SEEDS_OF_HORROR_ID) :
              htmltext = "7421-09.htm"
            else:
              htmltext = "7421-10.htm"
    elif event == "412_3" :
            if st.getQuestItemsCount(SEEDS_OF_LUNACY_ID) :
              htmltext = "7421-12.htm"
            elif st.getQuestItemsCount(SEEDS_OF_LUNACY_ID) == 0 and st.getQuestItemsCount(SEEDS_OF_DESPAIR_ID) :
                htmltext = "7421-13.htm"
                st.giveItems(HUB_SCENT_ID,1)
    elif event == "412_4" :
          htmltext = "7415-03.htm"
          st.giveItems(LUCKY_KEY_ID,1)
    elif event == "7418_1" :
          htmltext = "7418-02.htm"
          st.giveItems(CANDLE_ID,1)
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if npcId != 7421 and id != State.STARTED :
        return htmltext
   if npcId == 7421 and st.getInt("cond")==0 :
        if st.getQuestItemsCount(JEWEL_OF_DARKNESS_ID) == 0 :
          htmltext = "7421-01.htm"
          st.set("cond","0")
        else:
          htmltext = "7421-04.htm"
   elif npcId == 7421 and st.getInt("cond")==1 :
        if st.getQuestItemsCount(SEEDS_OF_DESPAIR_ID) and st.getQuestItemsCount(SEEDS_OF_HORROR_ID) and st.getQuestItemsCount(SEEDS_OF_LUNACY_ID) and st.getQuestItemsCount(SEEDS_OF_ANGER_ID) :
            htmltext = "7421-16.htm"
            st.takeItems(SEEDS_OF_HORROR_ID,1)
            st.takeItems(SEEDS_OF_ANGER_ID,1)
            st.takeItems(SEEDS_OF_LUNACY_ID,1)
            st.takeItems(SEEDS_OF_DESPAIR_ID,1)
            st.giveItems(JEWEL_OF_DARKNESS_ID,1)
            st.set("cond","0")
            st.setState(State.COMPLETED)
            st.playSound("ItemSound.quest_finish")
        elif st.getQuestItemsCount(SEEDS_OF_DESPAIR_ID) == 1 and st.getQuestItemsCount(FAMILYS_ASHES_ID) == 0 and st.getQuestItemsCount(LUCKY_KEY_ID) == 0 and st.getQuestItemsCount(CANDLE_ID) == 0 and st.getQuestItemsCount(HUB_SCENT_ID) == 0 and st.getQuestItemsCount(KNEE_BONE_ID) == 0 and st.getQuestItemsCount(HEART_OF_LUNACY_ID) == 0 :
          htmltext = "7421-17.htm"
        elif st.getQuestItemsCount(SEEDS_OF_DESPAIR_ID) == 1 and st.getInt("id") == 1 and st.getQuestItemsCount(SEEDS_OF_ANGER_ID) == 0 :
            htmltext = "7421-08.htm"
        elif st.getQuestItemsCount(SEEDS_OF_DESPAIR_ID) == 1 and st.getInt("id") == 2 and st.getQuestItemsCount(SEEDS_OF_HORROR_ID) :
            htmltext = "7421-19.htm"
        elif st.getQuestItemsCount(SEEDS_OF_DESPAIR_ID) == 1 and st.getInt("id") == 3 and st.getQuestItemsCount(HEART_OF_LUNACY_ID) == 0 :
            htmltext = "7421-13.htm"
   elif npcId == 7419 and st.getInt("cond")==1 :
        if st.getQuestItemsCount(HUB_SCENT_ID) == 0 and st.getQuestItemsCount(HEART_OF_LUNACY_ID) == 0 :
            htmltext = "7419-01.htm"
            st.giveItems(HUB_SCENT_ID,1)
        elif st.getQuestItemsCount(HUB_SCENT_ID) and st.getQuestItemsCount(HEART_OF_LUNACY_ID)<3 :
            htmltext = "7419-02.htm"
        elif st.getQuestItemsCount(HUB_SCENT_ID) and st.getQuestItemsCount(HEART_OF_LUNACY_ID) >= 3 :
            htmltext = "7419-03.htm"
            st.giveItems(SEEDS_OF_LUNACY_ID,1)
            st.takeItems(HEART_OF_LUNACY_ID,3)
            st.takeItems(HUB_SCENT_ID,1)
   elif npcId == 7415 and st.getInt("cond")==1 and st.getQuestItemsCount(SEEDS_OF_ANGER_ID)==0 :
        if st.getQuestItemsCount(SEEDS_OF_DESPAIR_ID) == 1 and st.getQuestItemsCount(FAMILYS_ASHES_ID) == 0 and st.getQuestItemsCount(LUCKY_KEY_ID) == 0 :
          htmltext = "7415-01.htm"
        elif st.getQuestItemsCount(SEEDS_OF_DESPAIR_ID) == 1 and st.getQuestItemsCount(FAMILYS_ASHES_ID)<3 and st.getQuestItemsCount(LUCKY_KEY_ID) == 1 :
            htmltext = "7415-04.htm"
        elif st.getQuestItemsCount(SEEDS_OF_DESPAIR_ID) == 1 and st.getQuestItemsCount(FAMILYS_ASHES_ID) >= 3 and st.getQuestItemsCount(LUCKY_KEY_ID) == 1 :
            htmltext = "7415-05.htm"
            st.giveItems(SEEDS_OF_ANGER_ID,1)
            st.takeItems(FAMILYS_ASHES_ID,3)
            st.takeItems(LUCKY_KEY_ID,1)
   elif npcId == 7415 and st.getInt("cond")==1 and st.getQuestItemsCount(SEEDS_OF_ANGER_ID)==1 :
        htmltext = "7415-06.htm"
   elif npcId == 7418 and st.getInt("cond")>0 and st.getQuestItemsCount(SEEDS_OF_HORROR_ID)==0 :
        if st.getQuestItemsCount(SEEDS_OF_DESPAIR_ID) == 1 and st.getQuestItemsCount(CANDLE_ID) == 0 and st.getQuestItemsCount(KNEE_BONE_ID) == 0 :
          htmltext = "7418-01.htm"
        elif st.getQuestItemsCount(SEEDS_OF_DESPAIR_ID) == 1 and st.getQuestItemsCount(CANDLE_ID) == 1 and st.getQuestItemsCount(KNEE_BONE_ID)<2 :
            htmltext = "7418-03.htm"
        elif st.getQuestItemsCount(SEEDS_OF_DESPAIR_ID) == 1 and st.getQuestItemsCount(CANDLE_ID) == 1 and st.getQuestItemsCount(KNEE_BONE_ID) >= 2 :
            htmltext = "7418-04.htm"
            st.giveItems(SEEDS_OF_HORROR_ID,1)
            st.takeItems(CANDLE_ID,1)
            st.takeItems(KNEE_BONE_ID,2)
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("412_PathToDarkwizard")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId == 15 :
        st.set("id","0")
        if st.getInt("cond") == 1 and st.getQuestItemsCount(LUCKY_KEY_ID) == 1 and st.getQuestItemsCount(FAMILYS_ASHES_ID)<3 :
          if st.getRandom(2) == 0 :
            st.giveItems(FAMILYS_ASHES_ID,1)
            if st.getQuestItemsCount(FAMILYS_ASHES_ID) == 3 :
              st.playSound("ItemSound.quest_middle")
            else:
              st.playSound("ItemSound.quest_itemget")
      elif npcId == 517 :
        st.set("id","0")
        if st.getInt("cond") == 1 and st.getQuestItemsCount(CANDLE_ID) == 1 and st.getQuestItemsCount(KNEE_BONE_ID)<2 :
          if st.getRandom(2) == 0 :
            st.giveItems(KNEE_BONE_ID,1)
            if st.getQuestItemsCount(KNEE_BONE_ID) == 2 :
              st.playSound("ItemSound.quest_middle")
            else:
              st.playSound("ItemSound.quest_itemget")
      elif npcId == 518 :
        st.set("id","0")
        if st.getInt("cond") == 1 and st.getQuestItemsCount(CANDLE_ID) == 1 and st.getQuestItemsCount(KNEE_BONE_ID)<2 :
          if st.getRandom(2) == 0 :
            st.giveItems(KNEE_BONE_ID,1)
            if st.getQuestItemsCount(KNEE_BONE_ID) == 2 :
              st.playSound("ItemSound.quest_middle")
            else:
              st.playSound("ItemSound.quest_itemget")
      elif npcId == 22 :
        st.set("id","0")
        if st.getInt("cond") == 1 and st.getQuestItemsCount(CANDLE_ID) == 1 and st.getQuestItemsCount(KNEE_BONE_ID)<2 :
          if st.getRandom(2) == 0 :
            st.giveItems(KNEE_BONE_ID,1)
            if st.getQuestItemsCount(KNEE_BONE_ID) == 2 :
              st.playSound("ItemSound.quest_middle")
            else:
              st.playSound("ItemSound.quest_itemget")
      elif npcId == 45 :
        st.set("id","0")
        if st.getInt("cond") == 1 and st.getQuestItemsCount(HUB_SCENT_ID) == 1 and st.getQuestItemsCount(HEART_OF_LUNACY_ID)<3 :
          if st.getRandom(2) == 0 :
            st.giveItems(HEART_OF_LUNACY_ID,1)
            if st.getQuestItemsCount(HEART_OF_LUNACY_ID) == 3 :
              st.playSound("ItemSound.quest_middle")
            else:
              st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(412,"412_PathToDarkwizard","Path To Darkwizard")
QUEST.addStartNpc(7421)

QUEST.addTalkId(7415)
QUEST.addTalkId(7418)
QUEST.addTalkId(7419)
QUEST.addTalkId(7421)

QUEST.addKillId(15)
QUEST.addKillId(22)
QUEST.addKillId(45)
QUEST.addKillId(517)
QUEST.addKillId(518)