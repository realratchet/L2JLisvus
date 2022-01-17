# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

INGREDIENT_LIST_ID = 1420
SONIAS_BOTANYBOOK_ID = 1421
RED_MANDRAGORA_ROOT_ID = 1422
WHITE_MANDRAGORA_ROOT_ID = 1423
RED_MANDRAGORA_SAP_ID = 1424
WHITE_MANDRAGORA_SAP_ID = 1425
JAYCUBS_INSECTBOOK_ID = 1426
NECTAR_ID = 1427
ROYAL_JELLY_ID = 1428
HONEY_ID = 1429
GOLDEN_HONEY_ID = 1430
PANOS_CONTRACT_ID = 1431
HOBGOBLIN_AMULET_ID = 1432
DIONIAN_POTATO_ID = 1433
GLYVKAS_BOTANYBOOK_ID = 1434
GREEN_MARSH_MOSS_ID = 1435
BROWN_MARSH_MOSS_ID = 1436
GREEN_MOSS_BUNDLE_ID = 1437
BROWN_MOSS_BUNDLE_ID = 1438
ROLANTS_CREATUREBOOK_ID = 1439
MONSTER_EYE_BODY_ID = 1440
MONSTER_EYE_MEAT_ID = 1441
JONAS_STEAK_DISH1_ID = 1442
JONAS_STEAK_DISH2_ID = 1443
JONAS_STEAK_DISH3_ID = 1444
JONAS_STEAK_DISH4_ID = 1445
JONAS_STEAK_DISH5_ID = 1446
MIRIENS_REVIEW1_ID = 1447
MIRIENS_REVIEW2_ID = 1448
MIRIENS_REVIEW3_ID = 1449
MIRIENS_REVIEW4_ID = 1450
MIRIENS_REVIEW5_ID = 1451
ADENA_ID = 57
JONAS_SALAD_RECIPE_ID = 1455
JONAS_SAUCE_RECIPE_ID = 1456
JONAS_STEAK_RECIPE_ID = 1457

def has_list(st) :
    return st.getQuestItemsCount(INGREDIENT_LIST_ID)

def has_review(st) :
    return st.getQuestItemsCount(MIRIENS_REVIEW1_ID)+\
           st.getQuestItemsCount(MIRIENS_REVIEW2_ID)+\
           st.getQuestItemsCount(MIRIENS_REVIEW3_ID)+\
           st.getQuestItemsCount(MIRIENS_REVIEW4_ID)+\
           st.getQuestItemsCount(MIRIENS_REVIEW5_ID)

def has_dish(st) :
    return st.getQuestItemsCount(JONAS_STEAK_DISH1_ID)+\
           st.getQuestItemsCount(JONAS_STEAK_DISH2_ID)+\
           st.getQuestItemsCount(JONAS_STEAK_DISH3_ID)+\
           st.getQuestItemsCount(JONAS_STEAK_DISH4_ID)+\
           st.getQuestItemsCount(JONAS_STEAK_DISH5_ID)

def special_ingredients(st):
    return st.getQuestItemsCount(WHITE_MANDRAGORA_SAP_ID)+\
           st.getQuestItemsCount(GOLDEN_HONEY_ID)+\
           st.getQuestItemsCount(BROWN_MOSS_BUNDLE_ID)

def ingredients_count(st) :
    return st.getQuestItemsCount(RED_MANDRAGORA_SAP_ID)+\
           st.getQuestItemsCount(HONEY_ID)+\
           st.getQuestItemsCount(DIONIAN_POTATO_ID)+\
           st.getQuestItemsCount(GREEN_MOSS_BUNDLE_ID)+\
           st.getQuestItemsCount(MONSTER_EYE_MEAT_ID)+\
           special_ingredients(st)

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(1420,1452)

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
        st.set("cond","1")
        st.setState(State.STARTED)
        st.playSound("ItemSound.quest_accept")
        htmltext = "7469-03.htm"
        st.giveItems(INGREDIENT_LIST_ID,1)
    elif event == "7062_1" :
        htmltext = "7062-05.htm"
        st.takeItems(SONIAS_BOTANYBOOK_ID,1)
        st.takeItems(RED_MANDRAGORA_ROOT_ID,-1)
        st.takeItems(WHITE_MANDRAGORA_ROOT_ID,-1)
        st.giveItems(RED_MANDRAGORA_SAP_ID,1)
    elif event == "7073_1" :
        htmltext = "7073-05.htm"
        st.takeItems(JAYCUBS_INSECTBOOK_ID,1)
        st.takeItems(NECTAR_ID,-1)
        st.takeItems(ROYAL_JELLY_ID,-1)
        st.giveItems(HONEY_ID,1)
    elif event == "7067_1" :
        htmltext = "7067-05.htm"
        st.takeItems(GLYVKAS_BOTANYBOOK_ID,1)
        st.takeItems(GREEN_MARSH_MOSS_ID,-1)
        st.takeItems(BROWN_MARSH_MOSS_ID,-1)
        st.giveItems(GREEN_MOSS_BUNDLE_ID,1)
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if npcId == 7469 and st.getInt("cond")==0 :
      if st.getPlayer().getLevel() >= 24 :
         htmltext = "7469-02.htm"
         return htmltext
      else:
         htmltext = "7469-01.htm"
         st.exitQuest(1)
   elif npcId == 7469 and st.getInt("cond") and has_list(st) and ingredients_count(st) < 5 :
        htmltext = "7469-04.htm"
   elif npcId == 7469 and st.getInt("cond") and has_list(st) and ingredients_count(st) >= 5 :
          if special_ingredients(st) == 0 :
            if st.getRandom(10)<1 :
              htmltext = "7469-05t2.htm"
              st.giveItems(JONAS_STEAK_DISH2_ID,1)
            else:
              htmltext = "7469-05t1.htm"
              st.giveItems(JONAS_STEAK_DISH1_ID,1)
          elif special_ingredients(st) == 1 :
            if st.getRandom(10)<1 :
              htmltext = "7469-05t3.htm"
              st.giveItems(JONAS_STEAK_DISH3_ID,1)
            else:
              htmltext = "7469-05t2.htm"
              st.giveItems(JONAS_STEAK_DISH2_ID,1)
          elif special_ingredients(st) == 2 :
            if st.getRandom(10)<1 :
              htmltext = "7469-05t4.htm"
              st.giveItems(JONAS_STEAK_DISH4_ID,1)
            else:
              htmltext = "7469-05t3.htm"
              st.giveItems(JONAS_STEAK_DISH3_ID,1)
          elif special_ingredients(st) == 3 :
            if st.getRandom(10)<1 :
              htmltext = "7469-05t5.htm"
              st.giveItems(JONAS_STEAK_DISH5_ID,1)
              st.playSound("ItemSound.quest_jackpot")
            else:
              htmltext = "7469-05t4.htm"
              st.giveItems(JONAS_STEAK_DISH4_ID,1)
          st.takeItems(INGREDIENT_LIST_ID,1)
          st.takeItems(RED_MANDRAGORA_SAP_ID,1)
          st.takeItems(WHITE_MANDRAGORA_SAP_ID,1)
          st.takeItems(HONEY_ID,1)
          st.takeItems(GOLDEN_HONEY_ID,1)
          st.takeItems(DIONIAN_POTATO_ID,1)
          st.takeItems(GREEN_MOSS_BUNDLE_ID,1)
          st.takeItems(BROWN_MOSS_BUNDLE_ID,1)
          st.takeItems(MONSTER_EYE_MEAT_ID,1)
   elif npcId == 7469 and st.getInt("cond") and ingredients_count(st) == 0 and not has_list(st) and has_dish(st) and not has_review(st) :
        htmltext = "7469-06.htm"
   elif npcId == 7469 and st.getInt("cond") and ingredients_count(st) == 0 and not has_list(st) and not has_dish(st) and has_review(st) :
        if st.getQuestItemsCount(MIRIENS_REVIEW1_ID) :
          htmltext = "7469-06t1.htm"
          st.takeItems(MIRIENS_REVIEW1_ID,1)
          st.giveItems(ADENA_ID,7500)
          st.addExpAndSp(6000,0)
        elif st.getQuestItemsCount(MIRIENS_REVIEW2_ID) :
          htmltext = "7469-06t2.htm"
          st.takeItems(MIRIENS_REVIEW2_ID,1)
          st.giveItems(ADENA_ID,9000)
          st.addExpAndSp(7000,0)
        elif st.getQuestItemsCount(MIRIENS_REVIEW3_ID) :
          htmltext = "7469-06t3.htm"
          st.takeItems(MIRIENS_REVIEW3_ID,1)
          st.giveItems(ADENA_ID,5800)
          st.giveItems(JONAS_SALAD_RECIPE_ID,1)
          st.addExpAndSp(9000,0)
        elif st.getQuestItemsCount(MIRIENS_REVIEW4_ID) :
          htmltext = "7469-06t4.htm"
          st.takeItems(MIRIENS_REVIEW4_ID,1)
          st.giveItems(ADENA_ID,6800)
          st.giveItems(JONAS_SAUCE_RECIPE_ID,1)
          st.addExpAndSp(10500,0)
        elif st.getQuestItemsCount(MIRIENS_REVIEW5_ID) :
          htmltext = "7469-06t5.htm"
          st.takeItems(MIRIENS_REVIEW5_ID,1)
          st.giveItems(ADENA_ID,7800)
          st.giveItems(JONAS_STEAK_RECIPE_ID,1)
          st.addExpAndSp(12000,0)
        st.playSound("ItemSound.quest_finish")
        st.exitQuest(1)
   elif npcId == 7461 and st.getInt("cond") and has_list(st) :
        htmltext = "7461-01.htm"
   elif npcId == 7461 and st.getInt("cond") and ingredients_count(st) == 0 and not has_list(st) and has_dish(st) and not has_review(st) :
        if st.getQuestItemsCount(JONAS_STEAK_DISH1_ID) :
          htmltext = "7461-02t1.htm"
          st.takeItems(JONAS_STEAK_DISH1_ID,1)
          st.giveItems(MIRIENS_REVIEW1_ID,1)
        elif st.getQuestItemsCount(JONAS_STEAK_DISH2_ID) :
          htmltext = "7461-02t2.htm"
          st.takeItems(JONAS_STEAK_DISH2_ID,1)
          st.giveItems(MIRIENS_REVIEW2_ID,1)
        elif st.getQuestItemsCount(JONAS_STEAK_DISH3_ID) :
          htmltext = "7461-02t3.htm"
          st.takeItems(JONAS_STEAK_DISH3_ID,1)
          st.giveItems(MIRIENS_REVIEW3_ID,1)
        elif st.getQuestItemsCount(JONAS_STEAK_DISH4_ID) :
          htmltext = "7461-02t4.htm"
          st.takeItems(JONAS_STEAK_DISH4_ID,1)
          st.giveItems(MIRIENS_REVIEW4_ID,1)
        elif st.getQuestItemsCount(JONAS_STEAK_DISH5_ID) :
          htmltext = "7461-02t5.htm"
          st.takeItems(JONAS_STEAK_DISH5_ID,1)
          st.giveItems(MIRIENS_REVIEW5_ID,1)
   elif npcId == 7461 and st.getInt("cond") and ingredients_count(st) == 0 and not has_list(st) and not has_dish(st) and has_review(st) :
        htmltext = "7461-04.htm"
   elif npcId == 7062 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and not st.getQuestItemsCount(SONIAS_BOTANYBOOK_ID) and (st.getQuestItemsCount(RED_MANDRAGORA_SAP_ID)+st.getQuestItemsCount(WHITE_MANDRAGORA_SAP_ID)==0) :
        htmltext = "7062-01.htm"
        st.giveItems(SONIAS_BOTANYBOOK_ID,1)
   elif npcId == 7062 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(SONIAS_BOTANYBOOK_ID) and (st.getQuestItemsCount(RED_MANDRAGORA_ROOT_ID)+st.getQuestItemsCount(WHITE_MANDRAGORA_ROOT_ID))<40 and (st.getQuestItemsCount(RED_MANDRAGORA_SAP_ID)+st.getQuestItemsCount(WHITE_MANDRAGORA_SAP_ID)==0) :
        htmltext = "7062-02.htm"
   elif npcId == 7062 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(SONIAS_BOTANYBOOK_ID) and (st.getQuestItemsCount(RED_MANDRAGORA_ROOT_ID)+st.getQuestItemsCount(WHITE_MANDRAGORA_ROOT_ID))>=40 and st.getQuestItemsCount(WHITE_MANDRAGORA_ROOT_ID)<40 and (st.getQuestItemsCount(RED_MANDRAGORA_SAP_ID)+st.getQuestItemsCount(WHITE_MANDRAGORA_SAP_ID)==0) :
        htmltext = "7062-03.htm"
   elif npcId == 7062 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(SONIAS_BOTANYBOOK_ID) and (st.getQuestItemsCount(RED_MANDRAGORA_ROOT_ID)+st.getQuestItemsCount(WHITE_MANDRAGORA_ROOT_ID))>=40 and st.getQuestItemsCount(WHITE_MANDRAGORA_ROOT_ID)>=40 and (st.getQuestItemsCount(RED_MANDRAGORA_SAP_ID)+st.getQuestItemsCount(WHITE_MANDRAGORA_SAP_ID)==0) :
        htmltext = "7062-06.htm"
        st.takeItems(SONIAS_BOTANYBOOK_ID,1)
        st.takeItems(RED_MANDRAGORA_ROOT_ID,-1)
        st.takeItems(WHITE_MANDRAGORA_ROOT_ID,-1)
        st.giveItems(WHITE_MANDRAGORA_SAP_ID,1)
   elif npcId == 7062 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(SONIAS_BOTANYBOOK_ID)==0 and (st.getQuestItemsCount(RED_MANDRAGORA_SAP_ID)+st.getQuestItemsCount(WHITE_MANDRAGORA_SAP_ID)>0) :
        htmltext = "7062-07.htm"
   elif npcId == 7073 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(JAYCUBS_INSECTBOOK_ID)==0 and (st.getQuestItemsCount(HONEY_ID)+st.getQuestItemsCount(GOLDEN_HONEY_ID)==0) :
        htmltext = "7073-01.htm"
        st.giveItems(JAYCUBS_INSECTBOOK_ID,1)
   elif npcId == 7073 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(JAYCUBS_INSECTBOOK_ID) and st.getQuestItemsCount(NECTAR_ID)<20 :
        htmltext = "7073-02.htm"
   elif npcId == 7073 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(JAYCUBS_INSECTBOOK_ID) and st.getQuestItemsCount(NECTAR_ID)>=20 and st.getQuestItemsCount(ROYAL_JELLY_ID)<10 :
        htmltext = "7073-03.htm"
   elif npcId == 7073 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(JAYCUBS_INSECTBOOK_ID) and st.getQuestItemsCount(NECTAR_ID)>=20 and st.getQuestItemsCount(ROYAL_JELLY_ID)>=10 :
        htmltext = "7073-06.htm"
        st.takeItems(JAYCUBS_INSECTBOOK_ID,1)
        st.takeItems(NECTAR_ID,-1)
        st.takeItems(ROYAL_JELLY_ID,-1)
        st.giveItems(GOLDEN_HONEY_ID,1)
   elif npcId == 7073 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(JAYCUBS_INSECTBOOK_ID)==0 and (st.getQuestItemsCount(HONEY_ID)+st.getQuestItemsCount(GOLDEN_HONEY_ID)==1) :
        htmltext = "7073-07.htm"
   elif npcId == 7078 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(PANOS_CONTRACT_ID)==0 and st.getQuestItemsCount(DIONIAN_POTATO_ID)==0 :
        htmltext = "7078-01.htm"
        st.giveItems(PANOS_CONTRACT_ID,1)
   elif npcId == 7078 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(PANOS_CONTRACT_ID) and st.getQuestItemsCount(HOBGOBLIN_AMULET_ID)<30 :
        htmltext = "7078-02.htm"
   elif npcId == 7078 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(PANOS_CONTRACT_ID) and st.getQuestItemsCount(HOBGOBLIN_AMULET_ID)>=30 :
        htmltext = "7078-03.htm"
        st.takeItems(PANOS_CONTRACT_ID,1)
        st.takeItems(HOBGOBLIN_AMULET_ID,-1)
        st.giveItems(DIONIAN_POTATO_ID,1)
   elif npcId == 7078 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(PANOS_CONTRACT_ID)==0 and st.getQuestItemsCount(DIONIAN_POTATO_ID) :
        htmltext = "7078-04.htm"
   elif npcId == 7067 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(GLYVKAS_BOTANYBOOK_ID)==0 and (st.getQuestItemsCount(GREEN_MOSS_BUNDLE_ID)+st.getQuestItemsCount(BROWN_MOSS_BUNDLE_ID)==0) :
        htmltext = "7067-01.htm"
        st.giveItems(GLYVKAS_BOTANYBOOK_ID,1)
   elif npcId == 7067 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(GLYVKAS_BOTANYBOOK_ID) and (st.getQuestItemsCount(GREEN_MARSH_MOSS_ID)+st.getQuestItemsCount(BROWN_MARSH_MOSS_ID)<20) :
        htmltext = "7067-02.htm"
   elif npcId == 7067 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(GLYVKAS_BOTANYBOOK_ID) and (st.getQuestItemsCount(GREEN_MARSH_MOSS_ID)+st.getQuestItemsCount(BROWN_MARSH_MOSS_ID)>=20) and st.getQuestItemsCount(BROWN_MARSH_MOSS_ID)<20 :
        htmltext = "7067-03.htm"
   elif npcId == 7067 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(GLYVKAS_BOTANYBOOK_ID) and (st.getQuestItemsCount(GREEN_MARSH_MOSS_ID)+st.getQuestItemsCount(BROWN_MARSH_MOSS_ID)>=20) and st.getQuestItemsCount(BROWN_MARSH_MOSS_ID)>=20 :
        htmltext = "7067-06.htm"
        st.takeItems(GLYVKAS_BOTANYBOOK_ID,1)
        st.takeItems(GREEN_MARSH_MOSS_ID,-1)
        st.takeItems(BROWN_MARSH_MOSS_ID,-1)
        st.giveItems(BROWN_MOSS_BUNDLE_ID,1)
   elif npcId == 7067 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(GLYVKAS_BOTANYBOOK_ID)==0 and (st.getQuestItemsCount(GREEN_MOSS_BUNDLE_ID)+st.getQuestItemsCount(BROWN_MOSS_BUNDLE_ID)==1) :
        htmltext = "7067-07.htm"
   elif npcId == 7069 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(ROLANTS_CREATUREBOOK_ID)==0 and st.getQuestItemsCount(MONSTER_EYE_MEAT_ID)==0 :
        htmltext = "7069-01.htm"
        st.giveItems(ROLANTS_CREATUREBOOK_ID,1)
   elif npcId == 7069 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(ROLANTS_CREATUREBOOK_ID) and st.getQuestItemsCount(MONSTER_EYE_BODY_ID)<30 :
        htmltext = "7069-02.htm"
   elif npcId == 7069 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(ROLANTS_CREATUREBOOK_ID) and st.getQuestItemsCount(MONSTER_EYE_BODY_ID)>=30 :
        htmltext = "7069-03.htm"
        st.takeItems(ROLANTS_CREATUREBOOK_ID,1)
        st.takeItems(MONSTER_EYE_BODY_ID,-1)
        st.giveItems(MONSTER_EYE_MEAT_ID,1)
   elif npcId == 7069 and st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(ROLANTS_CREATUREBOOK_ID)==0 and st.getQuestItemsCount(MONSTER_EYE_MEAT_ID)==1 :
        htmltext = "7069-04.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("330_AdeptOfTaste")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId == 265 :
        if st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(ROLANTS_CREATUREBOOK_ID) and st.getQuestItemsCount(MONSTER_EYE_BODY_ID)<30 :
          n = st.getRandom(100)
          if n<75 :
            st.giveItems(MONSTER_EYE_BODY_ID,1)
            if st.getQuestItemsCount(MONSTER_EYE_BODY_ID) == 30 :
              st.playSound("ItemSound.quest_middle")
            else:
              st.playSound("ItemSound.quest_itemget")
          else:
            if st.getQuestItemsCount(MONSTER_EYE_BODY_ID) == 29 :
              st.giveItems(MONSTER_EYE_BODY_ID,1)
              st.playSound("ItemSound.quest_middle")
            else:
              st.giveItems(MONSTER_EYE_BODY_ID,2)
              st.playSound("ItemSound.quest_itemget")
      elif npcId == 266 :
        if st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(ROLANTS_CREATUREBOOK_ID) and st.getQuestItemsCount(MONSTER_EYE_BODY_ID)<30 :
          n = st.getRandom(10)
          if n<7 :
            st.giveItems(MONSTER_EYE_BODY_ID,1)
            if st.getQuestItemsCount(MONSTER_EYE_BODY_ID) == 30 :
              st.playSound("ItemSound.quest_middle")
            else:
              st.playSound("ItemSound.quest_itemget")
          else:
            if st.getQuestItemsCount(MONSTER_EYE_BODY_ID) == 29 :
              st.giveItems(MONSTER_EYE_BODY_ID,1)
              st.playSound("ItemSound.quest_middle")
            else:
              st.giveItems(MONSTER_EYE_BODY_ID,2)
              st.playSound("ItemSound.quest_itemget")
      elif npcId == 226 :
        if st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(GLYVKAS_BOTANYBOOK_ID) :
          n = st.getRandom(10)
          if n<9 :
            if st.getQuestItemsCount(GREEN_MARSH_MOSS_ID)<20 :
              st.giveItems(GREEN_MARSH_MOSS_ID,1)
              if st.getQuestItemsCount(GREEN_MARSH_MOSS_ID) == 20 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
          else:
            if st.getQuestItemsCount(BROWN_MARSH_MOSS_ID)<20 :
              st.giveItems(BROWN_MARSH_MOSS_ID,1)
              if st.getQuestItemsCount(BROWN_MARSH_MOSS_ID) == 20 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
      elif npcId == 228 :
        if st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(GLYVKAS_BOTANYBOOK_ID) :
          n = st.getRandom(100)
          if n<88 :
            if st.getQuestItemsCount(GREEN_MARSH_MOSS_ID)<20 :
              st.giveItems(GREEN_MARSH_MOSS_ID,1)
              if st.getQuestItemsCount(GREEN_MARSH_MOSS_ID) == 20 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
          else:
            if st.getQuestItemsCount(BROWN_MARSH_MOSS_ID)<20 :
              st.giveItems(BROWN_MARSH_MOSS_ID,1)
              if st.getQuestItemsCount(BROWN_MARSH_MOSS_ID) == 20 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
      elif npcId == 147 :
        if st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(PANOS_CONTRACT_ID) and st.getQuestItemsCount(HOBGOBLIN_AMULET_ID)<30 :
          st.giveItems(HOBGOBLIN_AMULET_ID,1)
          if st.getQuestItemsCount(HOBGOBLIN_AMULET_ID) == 30 :
            st.playSound("ItemSound.quest_middle")
          else:
            st.playSound("ItemSound.quest_itemget")
      elif npcId == 204 :
        if st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(JAYCUBS_INSECTBOOK_ID) :
          n = st.getRandom(100)
          if n<80 :
            if st.getQuestItemsCount(NECTAR_ID)<20 :
              st.giveItems(NECTAR_ID,1)
              if st.getQuestItemsCount(NECTAR_ID) == 20 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
          elif n>95 :
            if st.getQuestItemsCount(ROYAL_JELLY_ID)<10 :
              st.giveItems(ROYAL_JELLY_ID,1)
              if st.getQuestItemsCount(ROYAL_JELLY_ID) == 10 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
      elif npcId == 229 :
        if st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(JAYCUBS_INSECTBOOK_ID) :
          n = st.getRandom(100)
          if n<92 :
            if st.getQuestItemsCount(NECTAR_ID)<20 :
              st.giveItems(NECTAR_ID,1)
              if st.getQuestItemsCount(NECTAR_ID) == 20 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
          else:
            if st.getQuestItemsCount(ROYAL_JELLY_ID)<10 :
              st.giveItems(ROYAL_JELLY_ID,1)
              if st.getQuestItemsCount(ROYAL_JELLY_ID) == 10 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
      elif npcId == 223 :
        if st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(SONIAS_BOTANYBOOK_ID) and (st.getQuestItemsCount(RED_MANDRAGORA_SAP_ID)+st.getQuestItemsCount(WHITE_MANDRAGORA_SAP_ID) == 0) :
          n = st.getRandom(100)
          if n<67 :
            if st.getQuestItemsCount(RED_MANDRAGORA_ROOT_ID)<40 :
              st.giveItems(RED_MANDRAGORA_ROOT_ID,1)
              if st.getQuestItemsCount(RED_MANDRAGORA_ROOT_ID) == 40 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
          elif n>93 :
            if st.getQuestItemsCount(WHITE_MANDRAGORA_ROOT_ID)<40 :
              st.giveItems(WHITE_MANDRAGORA_ROOT_ID,1)
              if st.getQuestItemsCount(WHITE_MANDRAGORA_ROOT_ID) == 40 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
      elif npcId == 154 :
        if st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(SONIAS_BOTANYBOOK_ID) and (st.getQuestItemsCount(RED_MANDRAGORA_SAP_ID)+st.getQuestItemsCount(WHITE_MANDRAGORA_SAP_ID) == 0) :
          n = st.getRandom(100)
          if n<74 :
            if st.getQuestItemsCount(RED_MANDRAGORA_ROOT_ID)<40 :
              st.giveItems(RED_MANDRAGORA_ROOT_ID,1)
              if st.getQuestItemsCount(RED_MANDRAGORA_ROOT_ID) == 40 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
          elif n>92 :
            if st.getQuestItemsCount(WHITE_MANDRAGORA_ROOT_ID)<40 :
              st.giveItems(WHITE_MANDRAGORA_ROOT_ID,1)
              if st.getQuestItemsCount(WHITE_MANDRAGORA_ROOT_ID) == 40 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
      elif npcId == 155 :
        if st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(SONIAS_BOTANYBOOK_ID) and (st.getQuestItemsCount(RED_MANDRAGORA_SAP_ID)+st.getQuestItemsCount(WHITE_MANDRAGORA_SAP_ID) == 0) :
          n = st.getRandom(100)
          if n<80 :
            if st.getQuestItemsCount(RED_MANDRAGORA_ROOT_ID)<40 :
              st.giveItems(RED_MANDRAGORA_ROOT_ID,1)
              if st.getQuestItemsCount(RED_MANDRAGORA_ROOT_ID) == 40 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
          elif n>91 :
            if st.getQuestItemsCount(WHITE_MANDRAGORA_ROOT_ID)<40 :
              st.giveItems(WHITE_MANDRAGORA_ROOT_ID,1)
              if st.getQuestItemsCount(WHITE_MANDRAGORA_ROOT_ID) == 40 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
      elif npcId == 156 :
        if st.getInt("cond") and has_list(st) and ingredients_count(st)<5 and st.getQuestItemsCount(SONIAS_BOTANYBOOK_ID) and (st.getQuestItemsCount(RED_MANDRAGORA_SAP_ID)+st.getQuestItemsCount(WHITE_MANDRAGORA_SAP_ID) == 0) :
          n = st.getRandom(100)
          if n<90 :
            if st.getQuestItemsCount(RED_MANDRAGORA_ROOT_ID)<40 :
              st.giveItems(RED_MANDRAGORA_ROOT_ID,1)
              if st.getQuestItemsCount(RED_MANDRAGORA_ROOT_ID) == 40 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
          else:
            if st.getQuestItemsCount(WHITE_MANDRAGORA_ROOT_ID)<40 :
              st.giveItems(WHITE_MANDRAGORA_ROOT_ID,1)
              if st.getQuestItemsCount(WHITE_MANDRAGORA_ROOT_ID) == 40 :
                st.playSound("ItemSound.quest_middle")
              else:
                st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(330,"330_AdeptOfTaste","Adept Of Taste")
QUEST.addStartNpc(7469)

QUEST.addTalkId(7062)
QUEST.addTalkId(7067)
QUEST.addTalkId(7069)
QUEST.addTalkId(7073)
QUEST.addTalkId(7078)
QUEST.addTalkId(7461)
QUEST.addTalkId(7469)

QUEST.addKillId(147)
QUEST.addKillId(154)
QUEST.addKillId(155)
QUEST.addKillId(156)
QUEST.addKillId(204)
QUEST.addKillId(223)
QUEST.addKillId(226)
QUEST.addKillId(228)
QUEST.addKillId(229)
QUEST.addKillId(265)
QUEST.addKillId(266)