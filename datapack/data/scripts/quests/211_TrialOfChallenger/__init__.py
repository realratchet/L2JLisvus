# Fix by Cromir for Kilah
# Quest: Trial Of Challenger

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

LETTER_OF_KASH_ID = 2628
SCROLL_OF_SHYSLASSY_ID = 2631
WATCHERS_EYE1_ID = 2629
BROKEN_KEY_ID = 2632
MITHRIL_SCALE_GAITERS_MATERIAL_ID = 2918
BRIGANDINE_GAUNTLET_PATTERN_ID = 2927
MANTICOR_SKIN_GAITERS_PATTERN_ID = 1943
GAUNTLET_OF_REPOSE_OF_THE_SOUL_PATTERN_ID = 1946
IRON_BOOTS_DESIGN_ID = 1940
TOME_OF_BLOOD_PAGE_ID = 2030
ELVEN_NECKLACE_BEADS_ID = 1904
WHITE_TUNIC_PATTERN_ID = 1936
ADENA_ID = 57
MARK_OF_CHALLENGER_ID = 2627
WATCHERS_EYE2_ID = 2630

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [SCROLL_OF_SHYSLASSY_ID, LETTER_OF_KASH_ID, WATCHERS_EYE1_ID, BROKEN_KEY_ID, WATCHERS_EYE2_ID]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
      htmltext = "7644-05.htm"
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
    elif event == "7644_1" :
          htmltext = "7644-04.htm"
    elif event == "7645_1" :
          htmltext = "7645-02.htm"
          st.takeItems(LETTER_OF_KASH_ID,1)
          st.set("cond","4")
          st.playSound("Itemsound.quest_middle")
    elif event == "7647_1" :
          if st.getQuestItemsCount(BROKEN_KEY_ID) == 1 :
             st.giveItems(SCROLL_OF_SHYSLASSY_ID,1)
             st.playSound("Itemsound.quest_middle")
             if st.getRandom(10) < 2 :
              htmltext = "7647-03.htm"
              st.takeItems(BROKEN_KEY_ID,1)
              st.playSound("ItemSound.quest_jackpot")
              n = st.getRandom(100)
              if n > 90 :
                 st.giveItems(MITHRIL_SCALE_GAITERS_MATERIAL_ID,1)
                 st.giveItems(BRIGANDINE_GAUNTLET_PATTERN_ID,1)
                 st.giveItems(MANTICOR_SKIN_GAITERS_PATTERN_ID,1)
                 st.giveItems(GAUNTLET_OF_REPOSE_OF_THE_SOUL_PATTERN_ID,1)
                 st.giveItems(IRON_BOOTS_DESIGN_ID,1)
                 st.playSound("Itemsound.quest_middle")
              elif n > 70 :
                 st.giveItems(TOME_OF_BLOOD_PAGE_ID,1)
                 st.giveItems(ELVEN_NECKLACE_BEADS_ID,1)
                 st.playSound("Itemsound.quest_middle")
              elif n > 40 :
                 st.giveItems(WHITE_TUNIC_PATTERN_ID,1)
                 st.playSound("Itemsound.quest_middle")
              else:
                 st.giveItems(IRON_BOOTS_DESIGN_ID,1)
                 st.playSound("Itemsound.quest_middle")
             else:
              htmltext = "7647-02.htm"
              n = st.getRandom(1000)+1
              st.takeItems(BROKEN_KEY_ID,1)
              st.giveItems(ADENA_ID,n)
              st.playSound("Itemsound.quest_middle")
          else:
            htmltext = "7647-04.htm"
            st.takeItems(BROKEN_KEY_ID,1)
    elif event == "7646_1" :
          htmltext = "7646-02.htm"
    elif event == "7646_2" :
          htmltext = "7646-03.htm"
    elif event == "7646_3" :
          htmltext = "7646-04.htm"
          st.set("cond","8")
          st.takeItems(WATCHERS_EYE2_ID,1)
    elif event == "7646_4" :
          htmltext = "7646-06.htm"
          st.set("cond","8")
          st.takeItems(WATCHERS_EYE2_ID,1)
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
     if npcId == 7644 :
        if st.getPlayer().getClassId().ordinal() in [0x01,0x13,0x20,0x2d,0x2f] :
           if st.getPlayer().getLevel() >= 35 :
              htmltext = "7644-03.htm"
           else :
              htmltext = "7644-01.htm"
              st.exitQuest(1)
        else :
           htmltext = "7644-02.htm"
           st.exitQuest(1)
   elif npcId == 7644 and id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == 7644 and st.getInt("cond")==1 :
      htmltext = "7644-06.htm"
   elif npcId == 7644 and st.getInt("cond")==2 and st.getQuestItemsCount(SCROLL_OF_SHYSLASSY_ID)==1 :
      htmltext = "7644-07.htm"
      st.takeItems(SCROLL_OF_SHYSLASSY_ID,1)
      st.giveItems(LETTER_OF_KASH_ID,1)
      st.set("cond","3")
      st.playSound("Itemsound.quest_middle")
   elif npcId == 7644 and st.getInt("cond")==1 and st.getQuestItemsCount(LETTER_OF_KASH_ID)==1 :
      htmltext = "7644-08.htm"
   elif npcId == 7644 and st.getInt("cond")>=7 :
      htmltext = "7644-09.htm"
   elif npcId == 7645 and st.getInt("cond")==3 and st.getQuestItemsCount(LETTER_OF_KASH_ID)==1 :
      htmltext = "7645-01.htm"
   elif npcId == 7645 and st.getInt("cond")==4 and st.getQuestItemsCount(WATCHERS_EYE1_ID)==0 :
      htmltext = "7645-03.htm"
   elif npcId == 7645 and st.getInt("cond")==5 and st.getQuestItemsCount(WATCHERS_EYE1_ID) :
      htmltext = "7645-04.htm"
      st.takeItems(WATCHERS_EYE1_ID,1)
      st.set("cond","6")
      st.playSound("Itemsound.quest_middle")
   elif npcId == 7645 and st.getInt("cond")==6 :
      htmltext = "7645-05.htm"
   elif npcId == 7645 and st.getInt("cond")>=7 :
      htmltext = "7645-06.htm"
   elif npcId == 7647 and st.getInt("cond")==2 :
      htmltext = "7647-01.htm"
   elif npcId == 7646 and st.getInt("cond")==7 and st.getQuestItemsCount(WATCHERS_EYE2_ID) :
      htmltext = "7646-01.htm"
   elif npcId == 7646 and st.getInt("cond")==7 :
      htmltext = "7646-06a.htm"
   elif npcId == 7646 and st.getInt("cond")==10 :
      st.addExpAndSp(72394,11250)
      st.giveItems(7562,8)
      htmltext = "7646-07.htm"
      st.takeItems(BROKEN_KEY_ID,1)
      st.giveItems(7562,8)
      st.giveItems(MARK_OF_CHALLENGER_ID,1)
      st.setState(State.COMPLETED)
      st.playSound("ItemSound.quest_finish")
      st.set("cond","0")
   elif npcId == 7535 and st.getInt("cond")==7 :
      if st.getPlayer().getLevel() >= 36 :
        htmltext = "7535-01.htm"
        st.addRadar(176560,-184969,-3729);
        st.set("cond","8")
        st.playSound("Itemsound.quest_middle")
      else:
        htmltext = "7535-03.htm"
   elif npcId == 7535 and st.getInt("cond")==8 :
      htmltext = "7535-02.htm"
      st.addRadar(176560,-184969,-3729);
      st.set("cond","9")
      st.playSound("Itemsound.quest_middle")
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("211_TrialOfChallenger")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId == 5110 :
        if st.getInt("cond") == 1 and st.getQuestItemsCount(BROKEN_KEY_ID) == 0 :
          st.giveItems(BROKEN_KEY_ID,1)
          st.addSpawn(7647,npc,True,0)
          st.playSound("ItemSound.quest_middle")
          st.set("cond","2")
      elif npcId == 5112 :
        if st.getInt("cond") == 4 and st.getQuestItemsCount(WATCHERS_EYE1_ID) == 0 :
          st.giveItems(WATCHERS_EYE1_ID,1)
          st.set("cond","5")
          st.playSound("ItemSound.quest_middle")
      elif npcId == 5113 :
        if st.getInt("cond") == 6 and st.getQuestItemsCount(WATCHERS_EYE2_ID) == 0 :
           st.giveItems(WATCHERS_EYE2_ID,1)
           st.playSound("ItemSound.quest_middle")
           st.set("cond","7")
           st.addSpawn(7646,npc,True,0)
      elif npcId == 5114 :
        if st.getInt("cond") == 9 :
           st.set("cond","10")
           st.playSound("ItemSound.quest_middle")
           st.addSpawn(7646,npc,True,0)
   return

QUEST = Quest(211,"211_TrialOfChallenger","Trial Of Challenger")
QUEST.addStartNpc(7644)

QUEST.addTalkId(7535)
QUEST.addTalkId(7644)
QUEST.addTalkId(7645)
QUEST.addTalkId(7646)
QUEST.addTalkId(7647)

QUEST.addKillId(5110)
QUEST.addKillId(5112)
QUEST.addKillId(5113)
QUEST.addKillId(5114)