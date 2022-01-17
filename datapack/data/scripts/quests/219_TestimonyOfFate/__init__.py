# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

MARK_OF_FATE_ID = 3172
KAIRAS_LETTER1_ID = 3173
METHEUS_FUNERAL_JAR_ID = 3174
KASANDRAS_REMAINS_ID = 3175
HERBALISM_TEXTBOOK_ID = 3176
IXIAS_LIST_ID = 3177
MEDUSA_ICHOR_ID = 3178
M_SPIDER_FLUIDS_ID = 3179
DEAD_SEEKER_DUNG_ID = 3180
TYRANTS_BLOOD_ID = 3181
NIGHTSHADE_ROOT_ID = 3182
BELLADONNA_ID = 3183
ALDERS_SKULL1_ID = 3184
ALDERS_SKULL2_ID = 3185
ALDERS_RECEIPT_ID = 3186
ROAD_RATMAN_HEAD_ID = 3291
LETO_LIZARDMAN_FANG1_ID = 3292
KAIRAS_RECOMMEND_ID = 3189
KAIRAS_INSTRUCTIONS_ID = 3188
REVELATIONS_MANUSCRIPT_ID = 3187
THIFIELS_LETTER_ID = 3191
PALUS_CHARM_ID = 3190
ARKENIAS_LETTER_ID = 1246
ARKENIAS_NOTE_ID = 3192
RED_FAIRY_DUST_ID = 3198
TIMIRIRAN_SAP_ID = 3201
PIXY_GARNET_ID = 3193
GRANDIS_SKULL_ID = 3194
KARUL_BUGBEAR_SKULL_ID = 3195
BREKA_OVERLORD_SKULL_ID = 3196
LETO_OVERLORD_SKULL_ID = 3197
BLACK_WILLOW_LEAF_ID = 3200
TIMIRIRAN_SEED_ID = 3199

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [ALDERS_SKULL1_ID, KAIRAS_INSTRUCTIONS_ID, REVELATIONS_MANUSCRIPT_ID, KAIRAS_LETTER1_ID, KASANDRAS_REMAINS_ID, BELLADONNA_ID, HERBALISM_TEXTBOOK_ID, IXIAS_LIST_ID, MEDUSA_ICHOR_ID, TYRANTS_BLOOD_ID, M_SPIDER_FLUIDS_ID, DEAD_SEEKER_DUNG_ID, NIGHTSHADE_ROOT_ID, ALDERS_SKULL2_ID, ALDERS_RECEIPT_ID, KAIRAS_RECOMMEND_ID, ARKENIAS_LETTER_ID, PALUS_CHARM_ID, THIFIELS_LETTER_ID, ARKENIAS_NOTE_ID, RED_FAIRY_DUST_ID, TIMIRIRAN_SAP_ID, PIXY_GARNET_ID, GRANDIS_SKULL_ID, KARUL_BUGBEAR_SKULL_ID, BREKA_OVERLORD_SKULL_ID, LETO_OVERLORD_SKULL_ID, BLACK_WILLOW_LEAF_ID, TIMIRIRAN_SEED_ID, METHEUS_FUNERAL_JAR_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
      htmltext = "7476-05.htm"
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
      st.giveItems(KAIRAS_LETTER1_ID,1)
    elif event == "7476_1" :
          htmltext = "7476-04.htm"
    elif event == "7476_2" :
          if st.getPlayer().getLevel() >= 38 :
            htmltext = "7476-12.htm"
            st.giveItems(KAIRAS_RECOMMEND_ID,1)
            st.takeItems(REVELATIONS_MANUSCRIPT_ID,1)
          else:
            htmltext = "7476-13.htm"
            st.giveItems(KAIRAS_INSTRUCTIONS_ID,1)
            st.takeItems(REVELATIONS_MANUSCRIPT_ID,1)
    elif event == "7114_1" :
          htmltext = "7114-02.htm"
    elif event == "7114_2" :
          htmltext = "7114-03.htm"
    elif event == "7114_3" :
          htmltext = "7114-04.htm"
          st.giveItems(ALDERS_RECEIPT_ID,1)
          st.takeItems(ALDERS_SKULL2_ID,1)
    elif event == "7419_1" :
          htmltext = "7419-02.htm"
          st.giveItems(ARKENIAS_NOTE_ID,1)
          st.takeItems(THIFIELS_LETTER_ID,1)
    elif event == "7419_2" :
          htmltext = "7419-05.htm"
          st.giveItems(ARKENIAS_LETTER_ID,1)
          st.takeItems(ARKENIAS_NOTE_ID,1)
          st.takeItems(RED_FAIRY_DUST_ID,1)
          st.takeItems(TIMIRIRAN_SAP_ID,1)
    elif event == "12084_1" :
          htmltext = "12084-02.htm"
          st.giveItems(PIXY_GARNET_ID,1)
    elif event == "12089_1" :
          htmltext = "12089-02.htm"
          st.giveItems(TIMIRIRAN_SEED_ID,1)
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == 7476 and st.getInt("cond")==0 :
      if st.getPlayer().getRace().ordinal() == 2 and st.getPlayer().getLevel() >= 37 :
        htmltext = "7476-03.htm"
      elif st.getPlayer().getRace().ordinal() == 2 :
        htmltext = "7476-02.htm"
        st.exitQuest(1)
      else:
        htmltext = "7476-01.htm"
        st.exitQuest(1)
   elif npcId == 7476 and st.getInt("cond")==1 and st.getQuestItemsCount(KAIRAS_LETTER1_ID) :
      htmltext = "7476-06.htm"
   elif npcId == 7476 and st.getInt("cond")==1 and (st.getQuestItemsCount(METHEUS_FUNERAL_JAR_ID) or st.getQuestItemsCount(KASANDRAS_REMAINS_ID)) :
      htmltext = "7476-07.htm"
   elif npcId == 7476 and st.getInt("cond")==1 and (st.getQuestItemsCount(HERBALISM_TEXTBOOK_ID) or st.getQuestItemsCount(IXIAS_LIST_ID)) :
      htmltext = "7476-08.htm"
   elif npcId == 7476 and st.getInt("cond")==1 and st.getQuestItemsCount(ALDERS_SKULL1_ID) :
      htmltext = "7476-09.htm"
      st.giveItems(ALDERS_SKULL2_ID,1)
      st.takeItems(ALDERS_SKULL1_ID,1)
      st.addSpawn(7613,78977,149036,-3597,300000)
   elif npcId == 7476 and st.getInt("cond")==1 and (st.getQuestItemsCount(ALDERS_SKULL2_ID) or st.getQuestItemsCount(ALDERS_RECEIPT_ID)) :
      htmltext = "7476-10.htm"
   elif npcId == 7476 and st.getInt("cond")==1 and st.getQuestItemsCount(REVELATIONS_MANUSCRIPT_ID) :
      htmltext = "7476-11.htm"
   elif npcId == 7476 and st.getInt("cond")==1 and st.getQuestItemsCount(KAIRAS_INSTRUCTIONS_ID) and st.getPlayer().getLevel()<38 :
      htmltext = "7476-14.htm"
   elif npcId == 7476 and st.getInt("cond")==1 and st.getQuestItemsCount(KAIRAS_INSTRUCTIONS_ID) and st.getPlayer().getLevel()>=38 :
      htmltext = "7476-15.htm"
      st.giveItems(KAIRAS_RECOMMEND_ID,1)
      st.takeItems(KAIRAS_INSTRUCTIONS_ID,1)
   elif npcId == 7476 and st.getInt("cond")==1 and st.getQuestItemsCount(KAIRAS_RECOMMEND_ID) and st.getPlayer().getLevel()>=38 :
      htmltext = "7476-16.htm"
   elif npcId == 7476 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) :
      htmltext = "7476-17.htm"
   elif npcId == 7614 and st.getInt("cond")>=1 and st.getQuestItemsCount(KAIRAS_LETTER1_ID) :
      htmltext = "7614-01.htm"
      st.giveItems(METHEUS_FUNERAL_JAR_ID,1)
      st.takeItems(KAIRAS_LETTER1_ID,1)
   elif npcId == 7614 and st.getInt("cond")==1 and st.getQuestItemsCount(METHEUS_FUNERAL_JAR_ID) and st.getQuestItemsCount(KASANDRAS_REMAINS_ID)==0 :
      htmltext = "7614-02.htm"
   elif npcId == 7614 and st.getInt("cond")==1 and st.getQuestItemsCount(METHEUS_FUNERAL_JAR_ID)==0 and st.getQuestItemsCount(KASANDRAS_REMAINS_ID) :
      htmltext = "7614-03.htm"
      st.giveItems(HERBALISM_TEXTBOOK_ID,1)
      st.takeItems(KASANDRAS_REMAINS_ID,1)
   elif npcId == 7614 and st.getInt("cond")==1 and (st.getQuestItemsCount(HERBALISM_TEXTBOOK_ID) or st.getQuestItemsCount(IXIAS_LIST_ID)) :
      htmltext = "7614-04.htm"
   elif npcId == 7614 and st.getInt("cond")==1 and st.getQuestItemsCount(BELLADONNA_ID) :
      htmltext = "7614-05.htm"
      st.giveItems(ALDERS_SKULL1_ID,1)
      st.takeItems(BELLADONNA_ID,1)
   elif npcId == 7614 and st.getInt("cond")==1 and (st.getQuestItemsCount(ALDERS_SKULL1_ID) or st.getQuestItemsCount(ALDERS_SKULL2_ID) or st.getQuestItemsCount(ALDERS_RECEIPT_ID) or st.getQuestItemsCount(REVELATIONS_MANUSCRIPT_ID) or st.getQuestItemsCount(KAIRAS_INSTRUCTIONS_ID) or st.getQuestItemsCount(KAIRAS_RECOMMEND_ID)) :
      htmltext = "7614-06.htm"
   elif npcId == 7463 and st.getInt("cond")==1 and st.getQuestItemsCount(HERBALISM_TEXTBOOK_ID) :
      htmltext = "7463-01.htm"
      st.giveItems(IXIAS_LIST_ID,1)
      st.takeItems(HERBALISM_TEXTBOOK_ID,1)
   elif npcId == 7463 and st.getInt("cond")==1 and st.getQuestItemsCount(IXIAS_LIST_ID) and (st.getQuestItemsCount(MEDUSA_ICHOR_ID)<10 or st.getQuestItemsCount(M_SPIDER_FLUIDS_ID)<10 or st.getQuestItemsCount(DEAD_SEEKER_DUNG_ID)<10 or st.getQuestItemsCount(TYRANTS_BLOOD_ID)<10 or st.getQuestItemsCount(NIGHTSHADE_ROOT_ID)<10) :
      htmltext = "7463-02.htm"
   elif npcId == 7463 and st.getInt("cond")==1 and st.getQuestItemsCount(IXIAS_LIST_ID) and st.getQuestItemsCount(MEDUSA_ICHOR_ID)>=10 and st.getQuestItemsCount(M_SPIDER_FLUIDS_ID)>=10 and st.getQuestItemsCount(DEAD_SEEKER_DUNG_ID)>=10 and st.getQuestItemsCount(TYRANTS_BLOOD_ID)>=10 and st.getQuestItemsCount(NIGHTSHADE_ROOT_ID)>=10 :
      htmltext = "7463-03.htm"
      st.giveItems(BELLADONNA_ID,1)
      st.takeItems(IXIAS_LIST_ID,1)
      st.takeItems(MEDUSA_ICHOR_ID,st.getQuestItemsCount(MEDUSA_ICHOR_ID))
      st.takeItems(TYRANTS_BLOOD_ID,st.getQuestItemsCount(TYRANTS_BLOOD_ID))
      st.takeItems(M_SPIDER_FLUIDS_ID,st.getQuestItemsCount(M_SPIDER_FLUIDS_ID))
      st.takeItems(DEAD_SEEKER_DUNG_ID,st.getQuestItemsCount(DEAD_SEEKER_DUNG_ID))
      st.takeItems(NIGHTSHADE_ROOT_ID,st.getQuestItemsCount(NIGHTSHADE_ROOT_ID))
   elif npcId == 7463 and st.getInt("cond")==1 and st.getQuestItemsCount(BELLADONNA_ID) :
      htmltext = "7463-04.htm"
   elif npcId == 7463 and st.getInt("cond")==1 and (st.getQuestItemsCount(ALDERS_SKULL1_ID) or st.getQuestItemsCount(ALDERS_SKULL2_ID) or st.getQuestItemsCount(ALDERS_RECEIPT_ID) or st.getQuestItemsCount(REVELATIONS_MANUSCRIPT_ID) or st.getQuestItemsCount(KAIRAS_INSTRUCTIONS_ID) or st.getQuestItemsCount(KAIRAS_RECOMMEND_ID)) :
      htmltext = "7463-05.htm"
   elif npcId == 7613 and st.getInt("cond")==1 and (st.getQuestItemsCount(ALDERS_SKULL1_ID) or st.getQuestItemsCount(ALDERS_SKULL2_ID)) :
      htmltext = "7613-02.htm"
   elif npcId == 7114 and st.getInt("cond")==1 and st.getQuestItemsCount(ALDERS_SKULL2_ID) :
      htmltext = "7114-01.htm"
   elif npcId == 7114 and st.getInt("cond")==1 and st.getQuestItemsCount(ALDERS_RECEIPT_ID) :
      htmltext = "7114-05.htm"
   elif npcId == 7114 and st.getInt("cond")==1 and (st.getQuestItemsCount(REVELATIONS_MANUSCRIPT_ID) or st.getQuestItemsCount(KAIRAS_INSTRUCTIONS_ID) or st.getQuestItemsCount(KAIRAS_RECOMMEND_ID)) :
      htmltext = "7114-06.htm"
   elif npcId == 7210 and st.getInt("cond")==1 and st.getQuestItemsCount(ALDERS_RECEIPT_ID) :
      htmltext = "7210-01.htm"
      st.giveItems(REVELATIONS_MANUSCRIPT_ID,1)
      st.takeItems(ALDERS_RECEIPT_ID,1)
   elif npcId == 7210 and st.getInt("cond")==1 and st.getQuestItemsCount(REVELATIONS_MANUSCRIPT_ID) :
      htmltext = "7210-02.htm"
   elif npcId == 7358 and st.getInt("cond")==1 and st.getQuestItemsCount(KAIRAS_RECOMMEND_ID) :
      htmltext = "7358-01.htm"
      st.giveItems(THIFIELS_LETTER_ID,1)
      st.giveItems(PALUS_CHARM_ID,1)
      st.takeItems(KAIRAS_RECOMMEND_ID,1)
   elif npcId == 7358 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(THIFIELS_LETTER_ID) :
      htmltext = "7358-02.htm"
   elif npcId == 7358 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) :
      htmltext = "7358-03.htm"
   elif npcId == 7358 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_LETTER_ID) :
      st.addExpAndSp(68183,1750)
      htmltext = "7358-04.htm"
      st.giveItems(7562,16)
      st.giveItems(MARK_OF_FATE_ID,1)
      st.takeItems(ARKENIAS_LETTER_ID,1)
      st.takeItems(PALUS_CHARM_ID,1)
      st.set("cond","0")
      st.setState(State.COMPLETED)
      st.playSound("ItemSound.quest_finish")
   elif npcId == 7419 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(THIFIELS_LETTER_ID) :
      htmltext = "7419-01.htm"
   elif npcId == 7419 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) and (st.getQuestItemsCount(RED_FAIRY_DUST_ID)<1 or st.getQuestItemsCount(TIMIRIRAN_SAP_ID)<1) :
      htmltext = "7419-03.htm"
   elif npcId == 7419 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) and (st.getQuestItemsCount(RED_FAIRY_DUST_ID)>=1 and st.getQuestItemsCount(TIMIRIRAN_SAP_ID)>=1) :
      htmltext = "7419-04.htm"
   elif npcId == 7419 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_LETTER_ID) :
      htmltext = "7419-06.htm"
   elif npcId == 12084 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) and st.getQuestItemsCount(RED_FAIRY_DUST_ID)==0 and st.getQuestItemsCount(PIXY_GARNET_ID)==0 :
      htmltext = "12084-01.htm"
   elif npcId == 12084 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) and st.getQuestItemsCount(RED_FAIRY_DUST_ID)==0 and st.getQuestItemsCount(PIXY_GARNET_ID) and (st.getQuestItemsCount(GRANDIS_SKULL_ID)<10 or st.getQuestItemsCount(KARUL_BUGBEAR_SKULL_ID)<10 or st.getQuestItemsCount(BREKA_OVERLORD_SKULL_ID)<10 or st.getQuestItemsCount(LETO_OVERLORD_SKULL_ID)<10) :
      htmltext = "12084-03.htm"
   elif npcId == 12084 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) and st.getQuestItemsCount(RED_FAIRY_DUST_ID)==0 and st.getQuestItemsCount(PIXY_GARNET_ID) and st.getQuestItemsCount(GRANDIS_SKULL_ID)>=10 and st.getQuestItemsCount(KARUL_BUGBEAR_SKULL_ID)>=10 and st.getQuestItemsCount(BREKA_OVERLORD_SKULL_ID)>=10 and st.getQuestItemsCount(LETO_OVERLORD_SKULL_ID)>=10 :
      htmltext = "12084-04.htm"
      st.giveItems(RED_FAIRY_DUST_ID,1)
      st.takeItems(PIXY_GARNET_ID,1)
      st.takeItems(GRANDIS_SKULL_ID,st.getQuestItemsCount(GRANDIS_SKULL_ID))
      st.takeItems(KARUL_BUGBEAR_SKULL_ID,st.getQuestItemsCount(KARUL_BUGBEAR_SKULL_ID))
      st.takeItems(BREKA_OVERLORD_SKULL_ID,st.getQuestItemsCount(BREKA_OVERLORD_SKULL_ID))
      st.takeItems(LETO_OVERLORD_SKULL_ID,st.getQuestItemsCount(LETO_OVERLORD_SKULL_ID))
   elif npcId == 12084 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) and st.getQuestItemsCount(RED_FAIRY_DUST_ID) and st.getQuestItemsCount(PIXY_GARNET_ID)==0 :
      htmltext = "12084-05.htm"
   elif npcId == 12089 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) and st.getQuestItemsCount(TIMIRIRAN_SAP_ID)==0 and st.getQuestItemsCount(TIMIRIRAN_SEED_ID)==0 :
      htmltext = "12089-01.htm"
   elif npcId == 12089 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) and st.getQuestItemsCount(TIMIRIRAN_SAP_ID)==0 and st.getQuestItemsCount(TIMIRIRAN_SEED_ID) and st.getQuestItemsCount(BLACK_WILLOW_LEAF_ID)==0 :
      htmltext = "12089-03.htm"
   elif npcId == 12089 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) and st.getQuestItemsCount(TIMIRIRAN_SAP_ID)==0 and st.getQuestItemsCount(TIMIRIRAN_SEED_ID) and st.getQuestItemsCount(BLACK_WILLOW_LEAF_ID) :
      htmltext = "12089-04.htm"
      st.giveItems(TIMIRIRAN_SAP_ID,1)
      st.takeItems(BLACK_WILLOW_LEAF_ID,1)
      st.takeItems(TIMIRIRAN_SEED_ID,1)
   elif npcId == 12089 and st.getInt("cond")==1 and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) and st.getQuestItemsCount(TIMIRIRAN_SAP_ID) and st.getQuestItemsCount(TIMIRIRAN_SEED_ID)==0 :
      htmltext = "12089-05.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("219_TestimonyOfFate")
   if st :
     if st.getState() != State.STARTED : return
     npcId = npc.getNpcId()
     if npcId == 144 :
      if st.getInt("cond") and st.getQuestItemsCount(METHEUS_FUNERAL_JAR_ID) and st.getQuestItemsCount(KASANDRAS_REMAINS_ID) == 0 :
        st.giveItems(KASANDRAS_REMAINS_ID,1)
        st.takeItems(METHEUS_FUNERAL_JAR_ID,1)
        st.playSound("Itemsound.quest_middle")
     elif npcId == 158 :
      if st.getInt("cond") and st.getQuestItemsCount(IXIAS_LIST_ID) and st.getQuestItemsCount(MEDUSA_ICHOR_ID) < 10 :
        if st.getRandom(2) == 1 :
          if st.getQuestItemsCount(MEDUSA_ICHOR_ID) == 9 :
            st.giveItems(MEDUSA_ICHOR_ID,1)
            st.playSound("Itemsound.quest_middle")
          else:
            st.giveItems(MEDUSA_ICHOR_ID,1)
            st.playSound("Itemsound.quest_itemget")
     elif npcId == 233 :
      if st.getInt("cond") and st.getQuestItemsCount(IXIAS_LIST_ID) and st.getQuestItemsCount(M_SPIDER_FLUIDS_ID) < 10 :
        if st.getRandom(2) == 1 :
          if st.getQuestItemsCount(M_SPIDER_FLUIDS_ID) == 9 :
            st.giveItems(M_SPIDER_FLUIDS_ID,1)
            st.playSound("Itemsound.quest_middle")
          else:
            st.giveItems(M_SPIDER_FLUIDS_ID,1)
            st.playSound("Itemsound.quest_itemget")
     elif npcId == 202 :
      if st.getInt("cond") and st.getQuestItemsCount(IXIAS_LIST_ID) and st.getQuestItemsCount(DEAD_SEEKER_DUNG_ID) < 10 :
        if st.getRandom(2) == 1 :
          if st.getQuestItemsCount(DEAD_SEEKER_DUNG_ID) == 9 :
            st.giveItems(DEAD_SEEKER_DUNG_ID,1)
            st.playSound("Itemsound.quest_middle")
          else:
            st.giveItems(DEAD_SEEKER_DUNG_ID,1)
            st.playSound("Itemsound.quest_itemget")
     elif npcId == 192 :
      if st.getInt("cond") and st.getQuestItemsCount(IXIAS_LIST_ID) and st.getQuestItemsCount(TYRANTS_BLOOD_ID) < 10 :
        if st.getRandom(2) == 1 :
          if st.getQuestItemsCount(TYRANTS_BLOOD_ID) == 9 :
            st.giveItems(TYRANTS_BLOOD_ID,1)
            st.playSound("Itemsound.quest_middle")
          else:
            st.giveItems(TYRANTS_BLOOD_ID,1)
            st.playSound("Itemsound.quest_itemget")
     elif npcId == 193 :
      if st.getInt("cond") and st.getQuestItemsCount(IXIAS_LIST_ID) and st.getQuestItemsCount(TYRANTS_BLOOD_ID) < 10 :
        if st.getRandom(10) < 6 :
          if st.getQuestItemsCount(TYRANTS_BLOOD_ID) == 9 :
            st.giveItems(TYRANTS_BLOOD_ID,1)
            st.playSound("Itemsound.quest_middle")
          else:
            st.giveItems(TYRANTS_BLOOD_ID,1)
            st.playSound("Itemsound.quest_itemget")
     elif npcId == 230 :
      if st.getInt("cond") and st.getQuestItemsCount(IXIAS_LIST_ID) and st.getQuestItemsCount(NIGHTSHADE_ROOT_ID) < 10 :
        if st.getRandom(10) < 3 :
          if st.getQuestItemsCount(NIGHTSHADE_ROOT_ID) == 9 :
            st.giveItems(NIGHTSHADE_ROOT_ID,1)
            st.playSound("Itemsound.quest_middle")
          else:
            st.giveItems(NIGHTSHADE_ROOT_ID,1)
            st.playSound("Itemsound.quest_itemget")
     elif npcId == 157 :
      if st.getInt("cond") and st.getQuestItemsCount(IXIAS_LIST_ID) and st.getQuestItemsCount(NIGHTSHADE_ROOT_ID) < 10 :
        if st.getRandom(10) < 4 :
          if st.getQuestItemsCount(NIGHTSHADE_ROOT_ID) == 9 :
            st.giveItems(NIGHTSHADE_ROOT_ID,1)
            st.playSound("Itemsound.quest_middle")
          else:
            st.giveItems(NIGHTSHADE_ROOT_ID,1)
            st.playSound("Itemsound.quest_itemget")
     elif npcId == 232 :
      if st.getInt("cond") and st.getQuestItemsCount(IXIAS_LIST_ID) and st.getQuestItemsCount(NIGHTSHADE_ROOT_ID) < 10 :
        if st.getRandom(10) < 5 :
          if st.getQuestItemsCount(NIGHTSHADE_ROOT_ID) == 9 :
            st.giveItems(NIGHTSHADE_ROOT_ID,1)
            st.playSound("Itemsound.quest_middle")
          else:
            st.giveItems(NIGHTSHADE_ROOT_ID,1)
            st.playSound("Itemsound.quest_itemget")
     elif npcId == 234 :
      if st.getInt("cond") and st.getQuestItemsCount(IXIAS_LIST_ID) and st.getQuestItemsCount(NIGHTSHADE_ROOT_ID) < 10 :
        if st.getRandom(10) < 6 :
          if st.getQuestItemsCount(NIGHTSHADE_ROOT_ID) == 9 :
            st.giveItems(NIGHTSHADE_ROOT_ID,1)
            st.playSound("Itemsound.quest_middle")
          else:
            st.giveItems(NIGHTSHADE_ROOT_ID,1)
            st.playSound("Itemsound.quest_itemget")
     elif npcId == 5079 :
      if st.getInt("cond") and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) and st.getQuestItemsCount(TIMIRIRAN_SAP_ID) == 0 and st.getQuestItemsCount(TIMIRIRAN_SEED_ID) and st.getQuestItemsCount(BLACK_WILLOW_LEAF_ID) == 0 :
        st.giveItems(BLACK_WILLOW_LEAF_ID,1)
        st.playSound("Itemsound.quest_middle")
     elif npcId == 554 :
      if st.getInt("cond") and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) and st.getQuestItemsCount(RED_FAIRY_DUST_ID) == 0 and st.getQuestItemsCount(PIXY_GARNET_ID) and st.getQuestItemsCount(GRANDIS_SKULL_ID) < 10 :
        if st.getQuestItemsCount(GRANDIS_SKULL_ID) == 9 :
          st.giveItems(GRANDIS_SKULL_ID,1)
          st.playSound("Itemsound.quest_middle")
        else:
          st.giveItems(GRANDIS_SKULL_ID,1)
          st.playSound("Itemsound.quest_itemget")
     elif npcId == 600 :
      if st.getInt("cond") and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) and st.getQuestItemsCount(RED_FAIRY_DUST_ID) == 0 and st.getQuestItemsCount(PIXY_GARNET_ID) and st.getQuestItemsCount(KARUL_BUGBEAR_SKULL_ID) < 10 :
        if st.getQuestItemsCount(KARUL_BUGBEAR_SKULL_ID) == 9 :
          st.giveItems(KARUL_BUGBEAR_SKULL_ID,1)
          st.playSound("Itemsound.quest_middle")
        else:
          st.giveItems(KARUL_BUGBEAR_SKULL_ID,1)
          st.playSound("Itemsound.quest_itemget")
     elif npcId == 270 :
      if st.getInt("cond") and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) and st.getQuestItemsCount(RED_FAIRY_DUST_ID) == 0 and st.getQuestItemsCount(PIXY_GARNET_ID) and st.getQuestItemsCount(BREKA_OVERLORD_SKULL_ID) < 10 :
        if st.getQuestItemsCount(BREKA_OVERLORD_SKULL_ID) == 9 :
          st.giveItems(BREKA_OVERLORD_SKULL_ID,1)
          st.playSound("Itemsound.quest_middle")
        else:
          st.giveItems(BREKA_OVERLORD_SKULL_ID,1)
          st.playSound("Itemsound.quest_itemget")
     elif npcId == 582 :
      if st.getInt("cond") and st.getQuestItemsCount(PALUS_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_NOTE_ID) and st.getQuestItemsCount(RED_FAIRY_DUST_ID) == 0 and st.getQuestItemsCount(PIXY_GARNET_ID) and st.getQuestItemsCount(LETO_OVERLORD_SKULL_ID) < 10 :
        if st.getQuestItemsCount(LETO_OVERLORD_SKULL_ID) == 9 :
          st.giveItems(LETO_OVERLORD_SKULL_ID,1)
          st.playSound("Itemsound.quest_middle")
        else:
          st.giveItems(LETO_OVERLORD_SKULL_ID,1)
          st.playSound("Itemsound.quest_itemget")
   return

QUEST = Quest(219,"219_TestimonyOfFate","Testimony Of Fate")
QUEST.addStartNpc(7476)

QUEST.addTalkId(12084)
QUEST.addTalkId(12089)
QUEST.addTalkId(7114)
QUEST.addTalkId(7210)
QUEST.addTalkId(7358)
QUEST.addTalkId(7419)
QUEST.addTalkId(7463)
QUEST.addTalkId(7476)
QUEST.addTalkId(7613)
QUEST.addTalkId(7614)

QUEST.addKillId(144)
QUEST.addKillId(157)
QUEST.addKillId(158)
QUEST.addKillId(192)
QUEST.addKillId(193)
QUEST.addKillId(202)
QUEST.addKillId(230)
QUEST.addKillId(232)
QUEST.addKillId(233)
QUEST.addKillId(234)
QUEST.addKillId(270)
QUEST.addKillId(5079)
QUEST.addKillId(554)
QUEST.addKillId(582)
QUEST.addKillId(600)