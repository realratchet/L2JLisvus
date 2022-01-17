# Made by Mr. Have fun! Version 0.2
# Quest: Testimony Of Life
# Fixed by Artful (http://L2PLanet.ru Lineage2 C3 Server)

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

MARK_OF_LIFE_ID = 3140
CARDIENS_LETTER_ID = 3141
CAMOMILE_CHARM_ID = 3142
HIERARCHS_LETTER_ID = 3143
MOONFLOWER_CHARM_ID = 3144
GRAIL_DIAGRAM_ID = 3145
THALIAS_LETTER1_ID = 3146
THALIAS_LETTER2_ID = 3147
THALIAS_INSTRUCTIONS_ID = 3148
PUSHKINS_LIST_ID = 3149
PURE_MITHRIL_CUP_ID = 3150
ARKENIAS_CONTRACT_ID = 3151
ARKENIAS_INSTRUCTIONS_ID = 3152
ADONIUS_LIST_ID = 3153
ANDARIEL_SCRIPTURE_COPY_ID = 3154
STARDUST_ID = 3155
ISAELS_INSTRUCTIONS_ID = 3156
ISAELS_LETTER_ID = 3157
GRAIL_OF_PURITY_ID = 3158
TEARS_OF_UNICORN_ID = 3159
WATER_OF_LIFE_ID = 3160
PURE_MITHRIL_ORE_ID = 3161
ANT_SOLDIER_ACID_ID = 3162
WYRMS_TALON1_ID = 3163
SPIDER_ICHOR_ID = 3164
HARPYS_DOWN_ID = 3165
TALINS_SPEAR_BLADE_ID = 3166
TALINS_SPEAR_SHAFT_ID = 3167
TALINS_RUBY_ID = 3168
TALINS_AQUAMARINE_ID = 3169
TALINS_AMETHYST_ID = 3170
TALINS_PERIDOT_ID = 3171
TALINS_SPEAR_ID = 3026

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [CAMOMILE_CHARM_ID, CARDIENS_LETTER_ID, WATER_OF_LIFE_ID, MOONFLOWER_CHARM_ID, HIERARCHS_LETTER_ID, STARDUST_ID, PURE_MITHRIL_CUP_ID, THALIAS_INSTRUCTIONS_ID, ISAELS_LETTER_ID, TEARS_OF_UNICORN_ID, GRAIL_DIAGRAM_ID, PUSHKINS_LIST_ID, THALIAS_LETTER1_ID, ARKENIAS_CONTRACT_ID, ANDARIEL_SCRIPTURE_COPY_ID, ARKENIAS_INSTRUCTIONS_ID, ADONIUS_LIST_ID, THALIAS_LETTER2_ID, TALINS_SPEAR_BLADE_ID, TALINS_SPEAR_SHAFT_ID, TALINS_RUBY_ID, TALINS_AQUAMARINE_ID, TALINS_AMETHYST_ID, TALINS_PERIDOT_ID, ISAELS_INSTRUCTIONS_ID, GRAIL_OF_PURITY_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1":
        htmltext = "7460-04.htm"
        st.set("cond","1")
        st.setState(State.STARTED)
        st.playSound("ItemSound.quest_accept")
        st.giveItems(CARDIENS_LETTER_ID,1)
    elif event == "7154_1" :
          htmltext = "7154-02.htm"
    elif event == "7154_2" :
          htmltext = "7154-03.htm"
    elif event == "7154_3" :
          htmltext = "7154-04.htm"
    elif event == "7154_4" :
          htmltext = "7154-05.htm"
    elif event == "7154_5" :
          htmltext = "7154-06.htm"
    elif event == "7154_6" :
          htmltext = "7154-07.htm"
          st.takeItems(CARDIENS_LETTER_ID,1)
          st.giveItems(MOONFLOWER_CHARM_ID,1)
          st.giveItems(HIERARCHS_LETTER_ID,1)
    elif event == "7371_1" :
          htmltext = "7371-02.htm"
    elif event == "7371_2" :
          htmltext = "7371-03.htm"
          st.takeItems(HIERARCHS_LETTER_ID,1)
          st.giveItems(GRAIL_DIAGRAM_ID,1)
    elif event == "7371_3" :
          if st.getPlayer().getLevel() < 38 :
            htmltext = "7371-10.htm"
            st.takeItems(STARDUST_ID,1)
            st.giveItems(THALIAS_INSTRUCTIONS_ID,1)
          else:
            htmltext = "7371-11.htm"
            st.takeItems(STARDUST_ID,1)
            st.giveItems(THALIAS_LETTER2_ID,1)
    elif event == "7300_1" :
          htmltext = "7300-02.htm"
    elif event == "7300_2" :
          htmltext = "7300-03.htm"
    elif event == "7300_3" :
          htmltext = "7300-04.htm"
    elif event == "7300_4" :
          htmltext = "7300-05.htm"
    elif event == "7300_5" :
          htmltext = "7300-06.htm"
          st.takeItems(GRAIL_DIAGRAM_ID,1)
          st.giveItems(PUSHKINS_LIST_ID,1)
    elif event == "7300_6" :
          htmltext = "7300-09.htm"
    elif event == "7300_7" :
          htmltext = "7300-10.htm"
          st.takeItems(PURE_MITHRIL_ORE_ID,st.getQuestItemsCount(PURE_MITHRIL_ORE_ID))
          st.takeItems(ANT_SOLDIER_ACID_ID,st.getQuestItemsCount(ANT_SOLDIER_ACID_ID))
          st.takeItems(WYRMS_TALON1_ID,st.getQuestItemsCount(WYRMS_TALON1_ID))
          st.takeItems(PUSHKINS_LIST_ID,1)
          st.giveItems(PURE_MITHRIL_CUP_ID,1)
    elif event == "7419_1" :
          htmltext = "7419-02.htm"
    elif event == "7419_2" :
          htmltext = "7419-03.htm"
    elif event == "7419_3" :
          htmltext = "7419-04.htm"
          st.takeItems(THALIAS_LETTER1_ID,1)
          st.giveItems(ARKENIAS_CONTRACT_ID,1)
          st.giveItems(ARKENIAS_INSTRUCTIONS_ID,1)
    elif event == "7375_1" :
          htmltext = "7375-02.htm"
          st.takeItems(ARKENIAS_INSTRUCTIONS_ID,1)
          st.giveItems(ADONIUS_LIST_ID,1)
    elif event == "7655_1" :
          htmltext = "7655-02.htm"
          st.takeItems(THALIAS_LETTER2_ID,1)
          st.giveItems(ISAELS_INSTRUCTIONS_ID,1)
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == 7460 and st.getInt("cond")==0 :
        if st.getInt("cond") < 15 :
          if st.getPlayer().getRace().ordinal() != 1 :
            htmltext = "7460-01.htm"
          else:
            if st.getPlayer().getLevel() < 37 :
              htmltext = "7460-02.htm"
              st.exitQuest(1)
            else:
              htmltext = "7460-03.htm"
              st.set("cond","1")
              return htmltext
        else:
          htmltext = "7460-03.htm"
          st.exitQuest(1)
   elif npcId == 7460 and st.getInt("cond")==1 and st.getQuestItemsCount(CARDIENS_LETTER_ID)==1 :
        htmltext = "7460-05.htm"
   elif npcId == 7460 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID)==1 :
        htmltext = "7460-06.htm"
   elif npcId == 7460 and st.getInt("cond")==1 and st.getQuestItemsCount(CAMOMILE_CHARM_ID)==1 :
        if st.getGameTicks() != st.getInt("id") :
          st.set("id",str(st.getGameTicks()))
          st.addExpAndSp(104591,11250)
          st.giveItems(7562,16)
          st.giveItems(MARK_OF_LIFE_ID,1)
          st.takeItems(CAMOMILE_CHARM_ID,1)
          htmltext = "7460-07.htm"
          st.set("cond","0")
          st.setState(State.COMPLETED)
          st.playSound("ItemSound.quest_finish")
   elif npcId == 7154 and st.getInt("cond")==1 and st.getQuestItemsCount(CARDIENS_LETTER_ID)==1 :
        htmltext = "7154-01.htm"
   elif npcId == 7154 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID)==1 and st.getQuestItemsCount(WATER_OF_LIFE_ID)==0 :
        htmltext = "7154-08.htm"
   elif npcId == 7154 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(WATER_OF_LIFE_ID) :
        htmltext = "7154-09.htm"
        st.takeItems(WATER_OF_LIFE_ID,1)
        st.takeItems(MOONFLOWER_CHARM_ID,1)
        st.giveItems(CAMOMILE_CHARM_ID,1)
   elif npcId == 7154 and st.getInt("cond")==1 and st.getQuestItemsCount(CAMOMILE_CHARM_ID)==1 :
        htmltext = "7154-10.htm"
   elif npcId == 7371 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(HIERARCHS_LETTER_ID) :
        htmltext = "7371-01.htm"
   elif npcId == 7371 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(GRAIL_DIAGRAM_ID) :
        htmltext = "7371-04.htm"
   elif npcId == 7371 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(PUSHKINS_LIST_ID) :
        htmltext = "7371-05.htm"
   elif npcId == 7371 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(PURE_MITHRIL_CUP_ID) :
        htmltext = "7371-06.htm"
        st.takeItems(PURE_MITHRIL_CUP_ID,1)
        st.giveItems(THALIAS_LETTER1_ID,1)
   elif npcId == 7371 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(THALIAS_LETTER1_ID) :
        htmltext = "7371-07.htm"
   elif npcId == 7371 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_CONTRACT_ID) :
        htmltext = "7371-08.htm"
   elif npcId == 7371 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(STARDUST_ID) :
        htmltext = "7371-09.htm"
   elif npcId == 7371 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(THALIAS_INSTRUCTIONS_ID) :
        if st.getPlayer().getLevel() < 38 :
          htmltext = "7371-12.htm"
        else:
          htmltext = "7371-13.htm"
          st.takeItems(THALIAS_INSTRUCTIONS_ID,1)
          st.giveItems(THALIAS_LETTER2_ID,1)
   elif npcId == 7371 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(THALIAS_LETTER2_ID) :
        htmltext = "7371-14.htm"
   elif npcId == 7371 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(ISAELS_INSTRUCTIONS_ID) :
        htmltext = "7371-15.htm"
   elif npcId == 7371 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(TALINS_SPEAR_ID) and st.getQuestItemsCount(ISAELS_LETTER_ID) :
        htmltext = "7371-16.htm"
        st.takeItems(ISAELS_LETTER_ID,1)
        st.giveItems(GRAIL_OF_PURITY_ID,1)
   elif npcId == 7371 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(TALINS_SPEAR_ID) and st.getQuestItemsCount(GRAIL_OF_PURITY_ID) :
        htmltext = "7371-17.htm"
   elif npcId == 7371 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(TEARS_OF_UNICORN_ID) :
        htmltext = "7371-18.htm"
        st.takeItems(TEARS_OF_UNICORN_ID,1)
        st.giveItems(WATER_OF_LIFE_ID,1)
   elif npcId == 7371 and st.getInt("cond")==1 and st.getQuestItemsCount(CAMOMILE_CHARM_ID) or st.getQuestItemsCount(WATER_OF_LIFE_ID) and st.getQuestItemsCount(MOONFLOWER_CHARM_ID)==1 :
        htmltext = "7371-19.htm"
   elif npcId == 7300 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(GRAIL_DIAGRAM_ID) :
        htmltext = "7300-01.htm"
   elif npcId == 7300 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(PUSHKINS_LIST_ID) :
        if st.getQuestItemsCount(PURE_MITHRIL_ORE_ID) >= 10 and st.getQuestItemsCount(ANT_SOLDIER_ACID_ID) >= 20 and st.getQuestItemsCount(WYRMS_TALON1_ID) >= 20 :
          htmltext = "7300-08.htm"
        else:
          htmltext = "7300-07.htm"
   elif npcId == 7300 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(PURE_MITHRIL_CUP_ID) :
        htmltext = "7300-11.htm"
   elif npcId == 7300 and st.getInt("cond")==1 and st.getQuestItemsCount(GRAIL_DIAGRAM_ID) == 0 and st.getQuestItemsCount(PUSHKINS_LIST_ID) == 0 and st.getQuestItemsCount(PURE_MITHRIL_CUP_ID) == 0 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID)==1 :
        htmltext = "7300-12.htm"
   elif npcId == 7419 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(THALIAS_LETTER1_ID) :
        htmltext = "7419-01.htm"
   elif npcId == 7419 and st.getInt("cond")==1 and (st.getQuestItemsCount(ARKENIAS_INSTRUCTIONS_ID) or st.getQuestItemsCount(ADONIUS_LIST_ID)) and st.getQuestItemsCount(MOONFLOWER_CHARM_ID)==1 :
        htmltext = "7419-05.htm"
   elif npcId == 7419 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(ANDARIEL_SCRIPTURE_COPY_ID) :
        htmltext = "7419-06.htm"
        st.takeItems(ARKENIAS_CONTRACT_ID,1)
        st.takeItems(ANDARIEL_SCRIPTURE_COPY_ID,1)
        st.giveItems(STARDUST_ID,1)
   elif npcId == 7419 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(STARDUST_ID) :
        htmltext = "7419-07.htm"
   elif npcId == 7419 and st.getInt("cond")==1 and st.getQuestItemsCount(THALIAS_LETTER1_ID) == 0 and st.getQuestItemsCount(ARKENIAS_CONTRACT_ID) == 0 and st.getQuestItemsCount(ANDARIEL_SCRIPTURE_COPY_ID) == 0 and st.getQuestItemsCount(STARDUST_ID) == 0 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID)==1 :
        htmltext = "7419-08.htm"
   elif npcId == 7375 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(ARKENIAS_INSTRUCTIONS_ID) :
        htmltext = "7375-01.htm"
   elif npcId == 7375 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(ADONIUS_LIST_ID) :
        if st.getQuestItemsCount(SPIDER_ICHOR_ID) >= 20 and st.getQuestItemsCount(HARPYS_DOWN_ID) >= 20 :
          htmltext = "7375-04.htm"
          st.takeItems(SPIDER_ICHOR_ID,st.getQuestItemsCount(SPIDER_ICHOR_ID))
          st.takeItems(HARPYS_DOWN_ID,st.getQuestItemsCount(HARPYS_DOWN_ID))
          st.takeItems(ADONIUS_LIST_ID,1)
          st.giveItems(ANDARIEL_SCRIPTURE_COPY_ID,1)
        else:
          htmltext = "7375-03.htm"
   elif npcId == 7375 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(ANDARIEL_SCRIPTURE_COPY_ID) :
        htmltext = "7375-05.htm"
   elif npcId == 7375 and st.getInt("cond")==1 and st.getQuestItemsCount(ARKENIAS_INSTRUCTIONS_ID) == 0 and st.getQuestItemsCount(ADONIUS_LIST_ID) == 0 and st.getQuestItemsCount(ANDARIEL_SCRIPTURE_COPY_ID) == 0 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID)==1 :
        htmltext = "7375-06.htm"
   elif npcId == 7655 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(THALIAS_LETTER2_ID) :
        htmltext = "7655-01.htm"
   elif npcId == 7655 and st.getInt("cond")==1 and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) and st.getQuestItemsCount(ISAELS_INSTRUCTIONS_ID) :
        if st.getQuestItemsCount(TALINS_SPEAR_BLADE_ID) and st.getQuestItemsCount(TALINS_SPEAR_SHAFT_ID) and st.getQuestItemsCount(TALINS_RUBY_ID) and st.getQuestItemsCount(TALINS_AQUAMARINE_ID) and st.getQuestItemsCount(TALINS_AMETHYST_ID) and st.getQuestItemsCount(TALINS_PERIDOT_ID) :
          htmltext = "7655-04.htm"
          st.takeItems(TALINS_SPEAR_BLADE_ID,1)
          st.takeItems(TALINS_SPEAR_SHAFT_ID,1)
          st.takeItems(TALINS_RUBY_ID,1)
          st.takeItems(TALINS_AQUAMARINE_ID,1)
          st.takeItems(TALINS_AMETHYST_ID,1)
          st.takeItems(TALINS_PERIDOT_ID,1)
          st.takeItems(ISAELS_INSTRUCTIONS_ID,1)
          st.giveItems(ISAELS_LETTER_ID,1)
          st.giveItems(TALINS_SPEAR_ID,1)
        else:
          htmltext = "7655-03.htm"
   elif npcId == 7655 and st.getInt("cond")==1 and st.getQuestItemsCount(TALINS_SPEAR_ID) and st.getQuestItemsCount(ISAELS_LETTER_ID) :
        htmltext = "7655-05.htm"
   elif npcId == 7655 and st.getInt("cond")==1 and st.getQuestItemsCount(GRAIL_OF_PURITY_ID) or st.getQuestItemsCount(WATER_OF_LIFE_ID) or st.getQuestItemsCount(CAMOMILE_CHARM_ID) and st.getQuestItemsCount(MOONFLOWER_CHARM_ID)==1 :
        htmltext = "7655-06.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("218_TestimonyOfLife")
   if st :
     if st.getState() != State.STARTED : return
     npcId = npc.getNpcId()
     if npcId == 550 :
      st.set("id","0")
      if st.getInt("cond") and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) == 1 and st.getQuestItemsCount(PUSHKINS_LIST_ID) == 1 and st.getQuestItemsCount(PURE_MITHRIL_ORE_ID)<10 :
       if st.getRandom(100)<50 :
         st.giveItems(PURE_MITHRIL_ORE_ID,1)
         if st.getQuestItemsCount(PURE_MITHRIL_ORE_ID) < 10 :
           st.playSound("ItemSound.quest_itemget")
         else:
           st.playSound("ItemSound.quest_middle")
     elif npcId == 176 :
      st.set("id","0")
      if st.getInt("cond") and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) == 1 and st.getQuestItemsCount(PUSHKINS_LIST_ID) == 1 and st.getQuestItemsCount(WYRMS_TALON1_ID)<20 :
       if st.getRandom(100)<50 :
        st.giveItems(WYRMS_TALON1_ID,1)
        if st.getQuestItemsCount(WYRMS_TALON1_ID) < 20 :
          st.playSound("ItemSound.quest_itemget")
        else:
          st.playSound("ItemSound.quest_middle")
     elif npcId == 82 or npcId == 84 or npcId == 86 :
      st.set("id","0")
      if st.getInt("cond") and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) == 1 and st.getQuestItemsCount(PUSHKINS_LIST_ID) == 1 and st.getQuestItemsCount(ANT_SOLDIER_ACID_ID)<20 :
       if st.getRandom(100)<80 :
        st.giveItems(ANT_SOLDIER_ACID_ID,1)
        if st.getQuestItemsCount(ANT_SOLDIER_ACID_ID) < 20 :
          st.playSound("ItemSound.quest_itemget")
        else:
          st.playSound("ItemSound.quest_middle")
     elif npcId == 87 or npcId == 88 :
      st.set("id","0")
      if st.getInt("cond") and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) == 1 and st.getQuestItemsCount(PUSHKINS_LIST_ID) == 1 and st.getQuestItemsCount(ANT_SOLDIER_ACID_ID)<20 :
       if st.getRandom(100)<50 :
        st.giveItems(ANT_SOLDIER_ACID_ID,1)
        if st.getQuestItemsCount(ANT_SOLDIER_ACID_ID) < 20 :
          st.playSound("ItemSound.quest_itemget")
        else:
          st.playSound("ItemSound.quest_middle")
     elif npcId == 233 :
      st.set("id","0")
      if st.getInt("cond") and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) == 1 and st.getQuestItemsCount(ADONIUS_LIST_ID) == 1 and st.getQuestItemsCount(SPIDER_ICHOR_ID)<20 :
       if st.getRandom(100)<50 :
        st.giveItems(SPIDER_ICHOR_ID,1)
        if st.getQuestItemsCount(SPIDER_ICHOR_ID) < 20 :
          st.playSound("ItemSound.quest_itemget")
        else:
          st.playSound("ItemSound.quest_middle")
     elif npcId == 145 :
      st.set("id","0")
      if st.getInt("cond") and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) == 1 and st.getQuestItemsCount(ADONIUS_LIST_ID) == 1 and st.getQuestItemsCount(HARPYS_DOWN_ID)<20 :
       if st.getRandom(100)<50 :
        st.giveItems(HARPYS_DOWN_ID,1)
        if st.getQuestItemsCount(HARPYS_DOWN_ID) < 20 :
          st.playSound("ItemSound.quest_itemget")
        else:
          st.playSound("ItemSound.quest_middle")
     elif npcId == 5077 :
      if st.getInt("cond") and st.getQuestItemsCount(MOONFLOWER_CHARM_ID) == 1 and st.getQuestItemsCount(TALINS_SPEAR_ID) == 1 and st.getQuestItemsCount(GRAIL_OF_PURITY_ID) == 1 and st.getQuestItemsCount(TEARS_OF_UNICORN_ID) == 0 :
        if st.getQuestItemsCount(TALINS_SPEAR_ID) > 0 :
          st.takeItems(GRAIL_OF_PURITY_ID,1)
          st.takeItems(TALINS_SPEAR_ID,1)
          st.giveItems(TEARS_OF_UNICORN_ID,1)
     elif npcId == 581 or npcId == 582 :
      st.set("id","0")
      if st.getInt("cond") and st.getQuestItemsCount(ISAELS_INSTRUCTIONS_ID) == 1 and st.getRandom(100) < 50 :
       if st.getQuestItemsCount(TALINS_SPEAR_BLADE_ID) == 0 :
        st.giveItems(TALINS_SPEAR_BLADE_ID,1)
        st.playSound("ItemSound.quest_itemget")
       elif st.getQuestItemsCount(TALINS_SPEAR_SHAFT_ID) == 0 :
        st.giveItems(TALINS_SPEAR_SHAFT_ID,1)
        st.playSound("ItemSound.quest_itemget")
       elif st.getQuestItemsCount(TALINS_RUBY_ID) == 0 :
        st.giveItems(TALINS_RUBY_ID,1)
        st.playSound("ItemSound.quest_itemget")
       elif st.getQuestItemsCount(TALINS_AQUAMARINE_ID) == 0 :
        st.giveItems(TALINS_AQUAMARINE_ID,1)
        st.playSound("ItemSound.quest_itemget")
       elif st.getQuestItemsCount(TALINS_AMETHYST_ID) == 0 :
        st.giveItems(TALINS_AMETHYST_ID,1)
        st.playSound("ItemSound.quest_itemget")
       elif st.getQuestItemsCount(TALINS_PERIDOT_ID) == 0 :
        st.giveItems(TALINS_PERIDOT_ID,1)
        st.playSound("ItemSound.quest_middle")
   return

QUEST = Quest(218,"218_TestimonyOfLife","Testimony of Life")
QUEST.addStartNpc(7460)

QUEST.addTalkId(7154)
QUEST.addTalkId(7300)
QUEST.addTalkId(7371)
QUEST.addTalkId(7375)
QUEST.addTalkId(7419)
QUEST.addTalkId(7460)
QUEST.addTalkId(7655)

QUEST.addKillId(145)
QUEST.addKillId(176)
QUEST.addKillId(233)
QUEST.addKillId(5077)
QUEST.addKillId(550)
QUEST.addKillId(581)
QUEST.addKillId(582)
QUEST.addKillId(82)
QUEST.addKillId(84)
QUEST.addKillId(86)
QUEST.addKillId(87)
QUEST.addKillId(88)