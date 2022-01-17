# Made by Mr. Have fun! Version 0.2
#
# Updated by ElgarL
#

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

MARK_OF_GUILDSMAN_ID = 3119
VALKONS_RECOMMEND_ID = 3120
MANDRAGORA_BERRY_ID = 3121
ALLTRANS_INSTRUCTIONS_ID = 3122
ALLTRANS_RECOMMEND1_ID = 3123
ALLTRANS_RECOMMEND2_ID = 3124
NORMANS_INSTRUCTIONS_ID = 3125
NORMANS_RECEIPT_ID = 3126
DUNINGS_INSTRUCTIONS_ID = 3127
DUNINGS_KEY_ID = 3128
NORMANS_LIST_ID = 3129
GRAY_BONE_POWDER_ID = 3130
GRANITE_WHETSTONE_ID = 3131
RED_PIGMENT_ID = 3132
BRAIDED_YARN_ID = 3133
JOURNEYMAN_GEM_ID = 3134
PINTERS_INSTRUCTIONS_ID = 3135
AMBER_BEAD_ID = 3136
AMBER_LUMP_ID = 3137
JOURNEYMAN_DECO_BEADS_ID = 3138
JOURNEYMAN_RING_ID = 3139
RP_JOURNEYMAN_RING_ID = 3024
ADENA_ID = 57
RP_AMBER_BEAD_ID = 3025

class Quest (JQuest) :

 def __init__(self,id,name,descr):
    JQuest.__init__(self,id,name,descr)
    self.questItemIds = [RP_JOURNEYMAN_RING_ID, ALLTRANS_INSTRUCTIONS_ID, RP_JOURNEYMAN_RING_ID, VALKONS_RECOMMEND_ID, MANDRAGORA_BERRY_ID, ALLTRANS_RECOMMEND1_ID, DUNINGS_KEY_ID, NORMANS_INSTRUCTIONS_ID, NORMANS_LIST_ID, NORMANS_RECEIPT_ID, ALLTRANS_RECOMMEND2_ID, PINTERS_INSTRUCTIONS_ID, RP_AMBER_BEAD_ID, AMBER_BEAD_ID, DUNINGS_INSTRUCTIONS_ID]
    self.spoiledList = []

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
          if st.getQuestItemsCount(ADENA_ID) >= 2000 :
            htmltext = "7103-06.htm"
            st.set("cond","1")
            st.setState(State.STARTED)
            st.playSound("ItemSound.quest_accept")
            st.giveItems(VALKONS_RECOMMEND_ID,1)
            st.takeItems(ADENA_ID,2000)
          else :
            htmltext = "7103-05a.htm"
    elif event == "7103_1" :
          htmltext = "7103-04.htm"
    elif event == "7103_2" :
          if st.getQuestItemsCount(ADENA_ID) >= 2000 :
            htmltext = "7103-05.htm"
          else :
            htmltext = "7103-05a.htm"
    elif event == "7103_3" :
          if st.getGameTicks() != st.getInt("id") :
            st.set("id",str(st.getGameTicks()))
            htmltext = "7103-09a.htm"
            st.set("cond","0")
            st.setState(State.COMPLETED)
            st.playSound("ItemSound.quest_finish")
            st.addExpAndSp(32000,3900)
            st.takeItems(JOURNEYMAN_RING_ID,st.getQuestItemsCount(JOURNEYMAN_RING_ID))
            st.takeItems(ALLTRANS_INSTRUCTIONS_ID,1)
            st.takeItems(RP_JOURNEYMAN_RING_ID,1)
            st.giveItems(7562,8)
            st.giveItems(MARK_OF_GUILDSMAN_ID,1)
    elif event == "7103_4" :
          st.addExpAndSp(80933,12250)
          st.giveItems(7562,8)
          htmltext = "7103-09b.htm"
          st.set("cond","0")
          st.setState(State.COMPLETED)
          st.playSound("ItemSound.quest_finish")
          st.takeItems(JOURNEYMAN_RING_ID,st.getQuestItemsCount(JOURNEYMAN_RING_ID))
          st.takeItems(ALLTRANS_INSTRUCTIONS_ID,1)
          st.takeItems(RP_JOURNEYMAN_RING_ID,1)
          st.giveItems(7562,8)
          st.giveItems(MARK_OF_GUILDSMAN_ID,1)
    elif event == "7103_7c" :
          htmltext = "7103-07c.htm"
          st.set("cond","3")
    elif event == "7283_1" :
          htmltext = "7283-03.htm"
          st.takeItems(VALKONS_RECOMMEND_ID,1)
          st.takeItems(MANDRAGORA_BERRY_ID,1)
          st.giveItems(ALLTRANS_INSTRUCTIONS_ID,1)
          st.giveItems(RP_JOURNEYMAN_RING_ID,1)
          st.giveItems(ALLTRANS_RECOMMEND1_ID,1)
          st.giveItems(ALLTRANS_RECOMMEND2_ID,1)
          st.set("cond","5")
    elif event == "7210_1" :
          htmltext = "7210-02.htm"
    elif event == "7210_2" :
          htmltext = "7210-03.htm"
    elif event == "7210_3" :
          htmltext = "7210-04.htm"
          st.giveItems(NORMANS_INSTRUCTIONS_ID,1)
          st.takeItems(ALLTRANS_RECOMMEND1_ID,1)
          st.giveItems(NORMANS_RECEIPT_ID,1)
    elif event == "7210_4" :
          htmltext = "7210-08.htm"
    elif event == "7210_5" :
          htmltext = "7210-09.htm"
    elif event == "7210_6" :
          htmltext = "7210-10.htm"
          st.takeItems(DUNINGS_KEY_ID,st.getQuestItemsCount(DUNINGS_KEY_ID))
          st.giveItems(NORMANS_LIST_ID,1)
          st.takeItems(NORMANS_INSTRUCTIONS_ID,1)
    elif event == "7688_1" :
          htmltext = "7688-02.htm"
          st.giveItems(DUNINGS_INSTRUCTIONS_ID,1)
          st.takeItems(NORMANS_RECEIPT_ID,1)
    elif event == "7298_1" :
          htmltext = "7298-03.htm"
    elif event == "7298_2" :
          if st.getPlayer().getClassId().getId() == 0x36 :
            htmltext = "7298-04.htm"
            st.giveItems(PINTERS_INSTRUCTIONS_ID,1)
            st.takeItems(ALLTRANS_RECOMMEND2_ID,1)
          else:
            htmltext = "7298-05.htm"
            st.giveItems(RP_AMBER_BEAD_ID,1)
            st.takeItems(ALLTRANS_RECOMMEND2_ID,1)
            st.giveItems(PINTERS_INSTRUCTIONS_ID,1)
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.COMPLETED :
        htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == 7103 and st.getInt("cond")==0 :
          if (st.getPlayer().getClassId().getId() == 0x38 or st.getPlayer().getClassId().getId() == 0x36) :
            if st.getPlayer().getLevel() < 35 :
              htmltext = "7103-02.htm"
              st.exitQuest(1)
            else:
              htmltext = "7103-03.htm"
          else:
            htmltext = "7103-01.htm"
            st.exitQuest(1)
   elif npcId == 7103 and st.getInt("cond")==1 and id == State.STARTED :
        htmltext = "7103-06.htm"
   elif npcId == 7103 and st.getInt("cond")==2 and st.getQuestItemsCount(VALKONS_RECOMMEND_ID)==1 :
        htmltext = "7103-07.htm"
   elif npcId == 7103 and st.getInt("cond")==3 and st.getQuestItemsCount(VALKONS_RECOMMEND_ID)==1 :
        htmltext = "7103-07c.htm"
   elif npcId == 7103 and st.getInt("cond")>=3 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID)==1 :
        if st.getQuestItemsCount(JOURNEYMAN_RING_ID) < 7 :
          htmltext = "7103-08.htm"
        elif st.getInt("cond") == 6 :
          htmltext = "7103-09.htm"
   elif npcId == 7283 and st.getInt("cond")==1 and st.getQuestItemsCount(VALKONS_RECOMMEND_ID)==1 and st.getQuestItemsCount(MANDRAGORA_BERRY_ID)==0 :
        htmltext = "7283-01.htm"
        st.set("cond","2")
   elif npcId == 7283 and st.getInt("cond")==4 and st.getQuestItemsCount(VALKONS_RECOMMEND_ID)==1 and st.getQuestItemsCount(MANDRAGORA_BERRY_ID)==1 :
        htmltext = "7283-02.htm"
   elif npcId == 7283 and st.getInt("cond")==5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID)==1 :
        if st.getQuestItemsCount(JOURNEYMAN_RING_ID) < 7 :
          htmltext = "7283-04.htm"
        else:
          htmltext = "7283-05.htm"
   elif npcId == 7210 and st.getInt("cond")==5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID)==1 and st.getQuestItemsCount(ALLTRANS_RECOMMEND1_ID)==1 :
        htmltext = "7210-01.htm"
   elif npcId == 7210 and st.getInt("cond")==5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID) and st.getQuestItemsCount(NORMANS_INSTRUCTIONS_ID) and st.getQuestItemsCount(NORMANS_RECEIPT_ID) :
        htmltext = "7210-05.htm"
   elif npcId == 7210 and st.getInt("cond")==5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID) and st.getQuestItemsCount(NORMANS_INSTRUCTIONS_ID) and st.getQuestItemsCount(DUNINGS_INSTRUCTIONS_ID) :
        htmltext = "7210-06.htm"
   elif npcId == 7210 and st.getInt("cond")==5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID) and st.getQuestItemsCount(NORMANS_INSTRUCTIONS_ID) and st.getQuestItemsCount(DUNINGS_KEY_ID)>=30 :
        htmltext = "7210-07.htm"
   elif npcId == 7210 and st.getInt("cond")==5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID) and st.getQuestItemsCount(NORMANS_LIST_ID) :
        if st.getQuestItemsCount(GRAY_BONE_POWDER_ID) >= 70 and st.getQuestItemsCount(GRANITE_WHETSTONE_ID) >= 70 and st.getQuestItemsCount(RED_PIGMENT_ID) >= 70 and st.getQuestItemsCount(BRAIDED_YARN_ID) >= 70 :
          htmltext = "7210-12.htm"
          st.takeItems(NORMANS_LIST_ID,1)
          st.takeItems(GRAY_BONE_POWDER_ID,st.getQuestItemsCount(GRAY_BONE_POWDER_ID))
          st.takeItems(GRANITE_WHETSTONE_ID,st.getQuestItemsCount(GRANITE_WHETSTONE_ID))
          st.takeItems(RED_PIGMENT_ID,st.getQuestItemsCount(RED_PIGMENT_ID))
          st.takeItems(BRAIDED_YARN_ID,st.getQuestItemsCount(BRAIDED_YARN_ID))
          st.giveItems(JOURNEYMAN_GEM_ID,7)
          if st.getQuestItemsCount(JOURNEYMAN_DECO_BEADS_ID) == 7 :
            st.set("cond","6")
        else:
          htmltext = "7210-11.htm"
   elif npcId == 7210 and st.getInt("cond")==5 and st.getQuestItemsCount(NORMANS_INSTRUCTIONS_ID) == 0 and st.getQuestItemsCount(NORMANS_LIST_ID) == 0 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID)==1 and (st.getQuestItemsCount(JOURNEYMAN_GEM_ID) or st.getQuestItemsCount(JOURNEYMAN_RING_ID)) :
        htmltext = "7210-13.htm"
   elif npcId == 7688 and st.getInt("cond")==5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID) and st.getQuestItemsCount(NORMANS_INSTRUCTIONS_ID) and st.getQuestItemsCount(NORMANS_RECEIPT_ID) :
        htmltext = "7688-01.htm"
   elif npcId == 7688 and st.getInt("cond")==5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID) and st.getQuestItemsCount(NORMANS_INSTRUCTIONS_ID) and st.getQuestItemsCount(DUNINGS_INSTRUCTIONS_ID) :
        htmltext = "7688-03.htm"
   elif npcId == 7688 and st.getInt("cond")==5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID) and st.getQuestItemsCount(NORMANS_INSTRUCTIONS_ID) and st.getQuestItemsCount(DUNINGS_KEY_ID)>=30 :
        htmltext = "7688-04.htm"
   elif npcId == 7688 and st.getInt("cond")==5 and st.getQuestItemsCount(NORMANS_RECEIPT_ID) == 0 and st.getQuestItemsCount(DUNINGS_INSTRUCTIONS_ID) == 0 and st.getQuestItemsCount(DUNINGS_KEY_ID) == 0 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID)==1 :
        htmltext = "7688-01.htm"
   elif npcId == 7298 and st.getInt("cond")==5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID) and st.getQuestItemsCount(ALLTRANS_RECOMMEND2_ID) :
        if st.getPlayer().getLevel() < 36 :
          htmltext = "7298-01.htm"
        else:
          htmltext = "7298-02.htm"
   elif npcId == 7298 and st.getInt("cond")==5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID) and st.getQuestItemsCount(PINTERS_INSTRUCTIONS_ID) :
        if st.getQuestItemsCount(AMBER_BEAD_ID) < 70 :
          htmltext = "7298-06.htm"
        else:
          htmltext = "7298-07.htm"
          st.takeItems(PINTERS_INSTRUCTIONS_ID,1)
          st.takeItems(AMBER_BEAD_ID,st.getQuestItemsCount(AMBER_BEAD_ID))
          st.takeItems(RP_AMBER_BEAD_ID,st.getQuestItemsCount(RP_AMBER_BEAD_ID))
          st.giveItems(JOURNEYMAN_DECO_BEADS_ID,7)
          if st.getQuestItemsCount(JOURNEYMAN_GEM_ID) == 7 :
            st.set("cond","6")
   elif npcId == 7298 and st.getInt("cond")>=5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID)==1 and st.getQuestItemsCount(PINTERS_INSTRUCTIONS_ID)==0 and (st.getQuestItemsCount(JOURNEYMAN_DECO_BEADS_ID) or st.getQuestItemsCount(JOURNEYMAN_RING_ID)) :
        htmltext = "7298-08.htm"
   return htmltext

 def onSkillSee (self,npc,player,skill,targets,isPet):
   if npc not in targets :
     return
   st = player.getQuestState("216_TrialOfGuildsman")
   if st :
     skillId = skill.getId()
     if skillId == 254 and not npc in self.spoiledList and player.getClassId().getId() == 0x36 :
       if st.getQuestItemsCount(AMBER_BEAD_ID) < 70 :
         st.giveItems(AMBER_BEAD_ID,5)
         self.spoiledList.append(npc)
         if st.getQuestItemsCount(AMBER_BEAD_ID) >= 70 :
            st.playSound("ItemSound.quest_middle")
         else:
            st.playSound("Itemsound.quest_itemget")
   return

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("216_TrialOfGuildsman")
   if st :
     if st.getState() != State.STARTED : return
     npcId = npc.getNpcId()
     if npcId == 223 :
       st.set("id","0")
       if st.getInt("cond") == 3 and st.getQuestItemsCount(VALKONS_RECOMMEND_ID) == 1 and st.getQuestItemsCount(MANDRAGORA_BERRY_ID) == 0 :
         st.giveItems(MANDRAGORA_BERRY_ID,1)
         st.set("cond","4")
         st.playSound("ItemSound.quest_middle")
     elif npcId == 154 or npcId == 155 or npcId == 156:
       st.set("id","0")
       if st.getInt("cond") == 3 and st.getQuestItemsCount(VALKONS_RECOMMEND_ID) == 1 and st.getQuestItemsCount(MANDRAGORA_BERRY_ID) == 0 :
         st.giveItems(MANDRAGORA_BERRY_ID,1)
         st.set("cond","4")
         st.playSound("ItemSound.quest_middle")
     elif npcId == 267 or npcId == 268 or npcId == 271 or npcId == 269 or npcId == 270:
      st.set("id","0")
      if st.getInt("cond") == 5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID) == 1 and st.getQuestItemsCount(NORMANS_INSTRUCTIONS_ID) == 1 and st.getQuestItemsCount(DUNINGS_INSTRUCTIONS_ID) == 1 :
       if st.getRandom(100) <= 30 and st.getQuestItemsCount(DUNINGS_KEY_ID) <= 29 :
        if st.getQuestItemsCount(DUNINGS_KEY_ID) == 29 :
          st.giveItems(DUNINGS_KEY_ID,1)
          st.takeItems(DUNINGS_INSTRUCTIONS_ID,1)
          st.playSound("ItemSound.quest_middle")
        else:
          st.giveItems(DUNINGS_KEY_ID,1)
          st.playSound("ItemSound.quest_itemget")
     elif npcId == 201 or npcId == 200:
      st.set("id","0")
      if st.getInt("cond") == 5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID) == 1 and st.getQuestItemsCount(NORMANS_LIST_ID) == 1 and st.getQuestItemsCount(GRAY_BONE_POWDER_ID) < 70 :
       st.giveItems(GRAY_BONE_POWDER_ID,2)
       if st.getQuestItemsCount(GRAY_BONE_POWDER_ID) >= 70 :
        st.playSound("ItemSound.quest_middle")
       else:
        st.playSound("ItemSound.quest_itemget")
     elif npcId == 83 :
      st.set("id","0")
      if st.getInt("cond") == 5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID) == 1 and st.getQuestItemsCount(NORMANS_LIST_ID) == 1 and st.getQuestItemsCount(GRANITE_WHETSTONE_ID) < 70 :
       st.giveItems(GRANITE_WHETSTONE_ID,7)
       if st.getQuestItemsCount(GRANITE_WHETSTONE_ID) >= 70 :
        st.playSound("ItemSound.quest_middle")
       else:
        st.playSound("ItemSound.quest_itemget")
     elif npcId == 202 :
      st.set("id","0")
      if st.getInt("cond") == 5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID) == 1 and st.getQuestItemsCount(NORMANS_LIST_ID) == 1 and st.getQuestItemsCount(RED_PIGMENT_ID) < 70 :
       st.giveItems(RED_PIGMENT_ID,7)
       if st.getQuestItemsCount(RED_PIGMENT_ID) >= 70 :
        st.playSound("ItemSound.quest_middle")
       else:
        st.playSound("ItemSound.quest_itemget")
     elif npcId == 168 :
      st.set("id","0")
      if st.getInt("cond") == 5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID) == 1 and st.getQuestItemsCount(NORMANS_LIST_ID) == 1 and st.getQuestItemsCount(BRAIDED_YARN_ID) < 70 :
       st.giveItems(BRAIDED_YARN_ID,10)
       if st.getQuestItemsCount(BRAIDED_YARN_ID) >= 70 :
        st.playSound("ItemSound.quest_middle")
       else:
        st.playSound("ItemSound.quest_itemget")
     elif npcId == 79 or npcId == 81 or npcId == 80 :
      if npc in self.spoiledList :
       self.spoiledList.remove(npc)
      st.set("id","0")
      if st.getInt("cond") == 5 and st.getQuestItemsCount(ALLTRANS_INSTRUCTIONS_ID) == 1 and st.getQuestItemsCount(PINTERS_INSTRUCTIONS_ID) == 1 :
       if st.getQuestItemsCount(AMBER_BEAD_ID) < 70 :
        if st.getRandom(100) <= 30 :
          st.giveItems(AMBER_BEAD_ID,1)
          if st.getQuestItemsCount(AMBER_BEAD_ID) >= 70 :
            st.playSound("ItemSound.quest_middle")
          else:
            st.playSound("Itemsound.quest_itemget")
        elif player.getClassId().getId() == 0x38 :
          st.giveItems(AMBER_LUMP_ID,1)
          st.playSound("Itemsound.quest_itemget")
   return

QUEST = Quest(216,"216_TrialOfGuildsman","Trial Of Guildsman")
QUEST.addStartNpc(7103)

QUEST.addTalkId(7103)
QUEST.addTalkId(7210)
QUEST.addTalkId(7283)
QUEST.addTalkId(7298)
QUEST.addTalkId(7688)

QUEST.addSkillSeeId(79)
QUEST.addSkillSeeId(80)
QUEST.addSkillSeeId(81)

QUEST.addKillId(154)
QUEST.addKillId(155)
QUEST.addKillId(156)
QUEST.addKillId(168)
QUEST.addKillId(200)
QUEST.addKillId(201)
QUEST.addKillId(202)
QUEST.addKillId(223)
QUEST.addKillId(267)
QUEST.addKillId(268)
QUEST.addKillId(269)
QUEST.addKillId(270)
QUEST.addKillId(271)
QUEST.addKillId(79)
QUEST.addKillId(80)
QUEST.addKillId(81)
QUEST.addKillId(83)