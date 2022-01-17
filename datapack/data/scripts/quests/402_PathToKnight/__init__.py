# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

MARK_OF_ESQUIRE_ID = 1271
SWORD_OF_RITUAL_ID = 1161
COIN_OF_LORDS1_ID = 1162
COIN_OF_LORDS2_ID = 1163
COIN_OF_LORDS3_ID = 1164
COIN_OF_LORDS4_ID = 1165
COIN_OF_LORDS5_ID = 1166
COIN_OF_LORDS6_ID = 1167
GLUDIO_GUARDS_MARK1_ID = 1168
BUGBEAR_NECKLACE_ID = 1169
EINHASAD_CHURCH_MARK1_ID = 1170
EINHASAD_CRUCIFIX_ID = 1171
GLUDIO_GUARDS_MARK2_ID = 1172
POISON_SPIDER_LEG1_ID = 1173
EINHASAD_CHURCH_MARK2_ID = 1174
LIZARDMAN_TOTEM_ID = 1175
GLUDIO_GUARDS_MARK3_ID = 1176
GIANT_SPIDER_HUSK_ID = 1177
EINHASAD_CHURCH_MARK3_ID = 1178
HORRIBLE_SKULL_ID = 1179

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(1162,1180)

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7417_1" :
          if st.getPlayer().getClassId().getId() == 0x00 :
            if st.getPlayer().getLevel() >= 19 :
              if st.getQuestItemsCount(SWORD_OF_RITUAL_ID)>0 :
                htmltext = "7417-04.htm"
              else:
                htmltext = "7417-05.htm"
            else :
              htmltext = "7417-02.htm"
          else:
            if st.getPlayer().getClassId().getId() == 0x04 :
              htmltext = "7417-02a.htm"
            else:
              htmltext = "7417-03.htm"
    elif event == "7417_2" :
          htmltext = "7417-07.htm"
    elif event == "1" :
        st.set("id","0")
        st.set("cond","1")
        st.setState(State.STARTED)
        st.playSound("ItemSound.quest_accept")
        st.giveItems(MARK_OF_ESQUIRE_ID,1)
        htmltext = "7417-08.htm"
    elif event == "7332_1" :
          htmltext = "7332-02.htm"
          st.giveItems(GLUDIO_GUARDS_MARK1_ID,1)
    elif event == "7289_1" :
          htmltext = "7289-03.htm"
          st.giveItems(EINHASAD_CHURCH_MARK1_ID,1)
    elif event == "7379_1" :
          htmltext = "7379-02.htm"
          st.giveItems(GLUDIO_GUARDS_MARK2_ID,1)
    elif event == "7037_1" :
          htmltext = "7037-02.htm"
          st.giveItems(EINHASAD_CHURCH_MARK2_ID,1)
    elif event == "7039_1" :
          htmltext = "7039-02.htm"
          st.giveItems(GLUDIO_GUARDS_MARK3_ID,1)
    elif event == "7031_1" :
          htmltext = "7031-02.htm"
          st.giveItems(EINHASAD_CHURCH_MARK3_ID,1)
    elif event == "7417_3" :
          htmltext = "7417-15.htm"
    elif event == "7417_4" :
          htmltext = "7417-13.htm"
          st.takeItems(COIN_OF_LORDS1_ID,st.getQuestItemsCount(COIN_OF_LORDS1_ID))
          st.takeItems(COIN_OF_LORDS2_ID,st.getQuestItemsCount(COIN_OF_LORDS2_ID))
          st.takeItems(COIN_OF_LORDS3_ID,st.getQuestItemsCount(COIN_OF_LORDS3_ID))
          st.takeItems(COIN_OF_LORDS4_ID,st.getQuestItemsCount(COIN_OF_LORDS4_ID))
          st.takeItems(COIN_OF_LORDS5_ID,st.getQuestItemsCount(COIN_OF_LORDS5_ID))
          st.takeItems(COIN_OF_LORDS6_ID,st.getQuestItemsCount(COIN_OF_LORDS6_ID))
          st.takeItems(GLUDIO_GUARDS_MARK1_ID,st.getQuestItemsCount(GLUDIO_GUARDS_MARK1_ID))
          st.takeItems(GLUDIO_GUARDS_MARK2_ID,st.getQuestItemsCount(GLUDIO_GUARDS_MARK2_ID))
          st.takeItems(GLUDIO_GUARDS_MARK3_ID,st.getQuestItemsCount(GLUDIO_GUARDS_MARK3_ID))
          st.takeItems(EINHASAD_CHURCH_MARK1_ID,st.getQuestItemsCount(EINHASAD_CHURCH_MARK1_ID))
          st.takeItems(EINHASAD_CHURCH_MARK2_ID,st.getQuestItemsCount(EINHASAD_CHURCH_MARK2_ID))
          st.takeItems(EINHASAD_CHURCH_MARK3_ID,st.getQuestItemsCount(EINHASAD_CHURCH_MARK3_ID))
          st.takeItems(BUGBEAR_NECKLACE_ID,st.getQuestItemsCount(BUGBEAR_NECKLACE_ID))
          st.takeItems(EINHASAD_CRUCIFIX_ID,st.getQuestItemsCount(EINHASAD_CRUCIFIX_ID))
          st.takeItems(POISON_SPIDER_LEG1_ID,st.getQuestItemsCount(POISON_SPIDER_LEG1_ID))
          st.takeItems(LIZARDMAN_TOTEM_ID,st.getQuestItemsCount(LIZARDMAN_TOTEM_ID))
          st.takeItems(GIANT_SPIDER_HUSK_ID,st.getQuestItemsCount(GIANT_SPIDER_HUSK_ID))
          st.takeItems(HORRIBLE_SKULL_ID,st.getQuestItemsCount(HORRIBLE_SKULL_ID))
          st.takeItems(MARK_OF_ESQUIRE_ID,st.getQuestItemsCount(MARK_OF_ESQUIRE_ID))
          st.giveItems(SWORD_OF_RITUAL_ID,1)
          st.set("cond","0")
          st.setState(State.COMPLETED)
          st.playSound("ItemSound.quest_finish")
    elif event == "7417_5" :
          htmltext = "7417-14.htm"
          st.takeItems(COIN_OF_LORDS1_ID,st.getQuestItemsCount(COIN_OF_LORDS1_ID))
          st.takeItems(COIN_OF_LORDS2_ID,st.getQuestItemsCount(COIN_OF_LORDS2_ID))
          st.takeItems(COIN_OF_LORDS3_ID,st.getQuestItemsCount(COIN_OF_LORDS3_ID))
          st.takeItems(COIN_OF_LORDS4_ID,st.getQuestItemsCount(COIN_OF_LORDS4_ID))
          st.takeItems(COIN_OF_LORDS5_ID,st.getQuestItemsCount(COIN_OF_LORDS5_ID))
          st.takeItems(COIN_OF_LORDS6_ID,st.getQuestItemsCount(COIN_OF_LORDS6_ID))
          st.takeItems(GLUDIO_GUARDS_MARK1_ID,st.getQuestItemsCount(GLUDIO_GUARDS_MARK1_ID))
          st.takeItems(GLUDIO_GUARDS_MARK1_ID,st.getQuestItemsCount(GLUDIO_GUARDS_MARK2_ID))
          st.takeItems(GLUDIO_GUARDS_MARK1_ID,st.getQuestItemsCount(GLUDIO_GUARDS_MARK3_ID))
          st.takeItems(HORRIBLE_SKULL_ID,st.getQuestItemsCount(EINHASAD_CHURCH_MARK1_ID))
          st.takeItems(HORRIBLE_SKULL_ID,st.getQuestItemsCount(EINHASAD_CHURCH_MARK2_ID))
          st.takeItems(HORRIBLE_SKULL_ID,st.getQuestItemsCount(EINHASAD_CHURCH_MARK3_ID))
          st.takeItems(HORRIBLE_SKULL_ID,st.getQuestItemsCount(BUGBEAR_NECKLACE_ID))
          st.takeItems(HORRIBLE_SKULL_ID,st.getQuestItemsCount(EINHASAD_CRUCIFIX_ID))
          st.takeItems(HORRIBLE_SKULL_ID,st.getQuestItemsCount(POISON_SPIDER_LEG1_ID))
          st.takeItems(HORRIBLE_SKULL_ID,st.getQuestItemsCount(LIZARDMAN_TOTEM_ID))
          st.takeItems(HORRIBLE_SKULL_ID,st.getQuestItemsCount(GIANT_SPIDER_HUSK_ID))
          st.takeItems(HORRIBLE_SKULL_ID,st.getQuestItemsCount(HORRIBLE_SKULL_ID))
          st.giveItems(SWORD_OF_RITUAL_ID,1)
          st.set("cond","0")
          st.setState(State.COMPLETED)
          st.playSound("ItemSound.quest_finish")
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if npcId != 7417 and id != State.STARTED :
        return htmltext
   if npcId == 7417 and st.getInt("cond")==0 :
        htmltext = "7417-01.htm"
   elif npcId == 7417 and st.getInt("cond")==1 and st.getQuestItemsCount(MARK_OF_ESQUIRE_ID)>0 and (st.getQuestItemsCount(COIN_OF_LORDS1_ID)+st.getQuestItemsCount(COIN_OF_LORDS2_ID)+st.getQuestItemsCount(COIN_OF_LORDS3_ID)+st.getQuestItemsCount(COIN_OF_LORDS4_ID)+st.getQuestItemsCount(COIN_OF_LORDS5_ID)+st.getQuestItemsCount(COIN_OF_LORDS6_ID))<3 :
        htmltext = "7417-09.htm"
   elif npcId == 7332 and st.getInt("cond")==1 and st.getQuestItemsCount(GLUDIO_GUARDS_MARK1_ID)==0 and st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) and st.getQuestItemsCount(COIN_OF_LORDS1_ID)==0 :
        htmltext = "7332-01.htm"
   elif npcId == 7332 and st.getInt("cond")==1 and st.getQuestItemsCount(GLUDIO_GUARDS_MARK1_ID)>0 :
        if st.getQuestItemsCount(BUGBEAR_NECKLACE_ID)<10 :
          htmltext = "7332-03.htm"
        else:
          htmltext = "7332-04.htm"
          st.takeItems(BUGBEAR_NECKLACE_ID,st.getQuestItemsCount(BUGBEAR_NECKLACE_ID))
          st.takeItems(GLUDIO_GUARDS_MARK1_ID,1)
          st.giveItems(COIN_OF_LORDS1_ID,1)
   elif npcId == 7332 and st.getInt("cond")==1 and st.getQuestItemsCount(COIN_OF_LORDS1_ID)>0 :
        htmltext = "7332-05.htm"
   elif npcId == 7289 and st.getInt("cond")==1 and st.getQuestItemsCount(EINHASAD_CHURCH_MARK1_ID)==0 and st.getQuestItemsCount(COIN_OF_LORDS2_ID)==0 and st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) :
        htmltext = "7289-01.htm"
   elif npcId == 7289 and st.getInt("cond")==1 and st.getQuestItemsCount(EINHASAD_CHURCH_MARK1_ID)>0 :
        if st.getQuestItemsCount(EINHASAD_CRUCIFIX_ID)<12 :
          htmltext = "7289-04.htm"
        else:
          htmltext = "7289-05.htm"
          st.takeItems(EINHASAD_CRUCIFIX_ID,st.getQuestItemsCount(EINHASAD_CRUCIFIX_ID))
          st.takeItems(EINHASAD_CHURCH_MARK1_ID,1)
          st.giveItems(COIN_OF_LORDS2_ID,1)
   elif npcId == 7289 and st.getInt("cond")==1 and st.getQuestItemsCount(COIN_OF_LORDS2_ID)>0 :
        htmltext = "7289-06.htm"
   elif npcId == 7379 and st.getInt("cond")==1 and st.getQuestItemsCount(MARK_OF_ESQUIRE_ID)>0 and st.getQuestItemsCount(COIN_OF_LORDS3_ID)==0 and st.getQuestItemsCount(GLUDIO_GUARDS_MARK2_ID)==0 :
        htmltext = "7379-01.htm"
   elif npcId == 7379 and st.getInt("cond")==1 and st.getQuestItemsCount(GLUDIO_GUARDS_MARK2_ID)>0 :
        if st.getQuestItemsCount(POISON_SPIDER_LEG1_ID)<20 :
          htmltext = "7379-03.htm"
        else:
          htmltext = "7379-04.htm"
          st.takeItems(POISON_SPIDER_LEG1_ID,st.getQuestItemsCount(POISON_SPIDER_LEG1_ID))
          st.takeItems(GLUDIO_GUARDS_MARK2_ID,1)
          st.giveItems(COIN_OF_LORDS3_ID,1)
   elif npcId == 7379 and st.getInt("cond")==1 and st.getQuestItemsCount(COIN_OF_LORDS3_ID)>0 :
        htmltext = "7379-05.htm"
   elif npcId == 7037 and st.getInt("cond")==1 and st.getQuestItemsCount(EINHASAD_CHURCH_MARK2_ID)==0 and st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) and st.getQuestItemsCount(COIN_OF_LORDS4_ID)==0 :
        htmltext = "7037-01.htm"
   elif npcId == 7037 and st.getInt("cond")==1 and st.getQuestItemsCount(EINHASAD_CHURCH_MARK2_ID)>0 :
        if st.getQuestItemsCount(LIZARDMAN_TOTEM_ID)<20 :
          htmltext = "7037-03.htm"
        else:
          htmltext = "7037-04.htm"
          st.takeItems(LIZARDMAN_TOTEM_ID,st.getQuestItemsCount(LIZARDMAN_TOTEM_ID))
          st.takeItems(EINHASAD_CHURCH_MARK2_ID,1)
          st.giveItems(COIN_OF_LORDS4_ID,1)
   elif npcId == 7037 and st.getInt("cond")==1 and st.getQuestItemsCount(COIN_OF_LORDS4_ID)>0 :
        htmltext = "7037-05.htm"
   elif npcId == 7039 and st.getInt("cond")==1 and st.getQuestItemsCount(GLUDIO_GUARDS_MARK3_ID)==0 and st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) and st.getQuestItemsCount(COIN_OF_LORDS5_ID)==0 :
        htmltext = "7039-01.htm"
   elif npcId == 7039 and st.getInt("cond")==1 and st.getQuestItemsCount(GLUDIO_GUARDS_MARK3_ID)>0 :
        if st.getQuestItemsCount(GIANT_SPIDER_HUSK_ID)<20 :
          htmltext = "7039-03.htm"
        else:
          htmltext = "7039-04.htm"
          st.takeItems(GIANT_SPIDER_HUSK_ID,st.getQuestItemsCount(GIANT_SPIDER_HUSK_ID))
          st.takeItems(GLUDIO_GUARDS_MARK3_ID,1)
          st.giveItems(COIN_OF_LORDS5_ID,1)
   elif npcId == 7039 and st.getInt("cond")==1 and st.getQuestItemsCount(COIN_OF_LORDS5_ID)>0 :
        htmltext = "7039-05.htm"
   elif npcId == 7031 and st.getInt("cond")==1 and st.getQuestItemsCount(EINHASAD_CHURCH_MARK3_ID)==0 and st.getQuestItemsCount(MARK_OF_ESQUIRE_ID) and st.getQuestItemsCount(COIN_OF_LORDS6_ID)==0 :
        htmltext = "7031-01.htm"
   elif npcId == 7031 and st.getInt("cond")==1 and st.getQuestItemsCount(EINHASAD_CHURCH_MARK3_ID)>0 :
        if st.getQuestItemsCount(HORRIBLE_SKULL_ID)<10 :
          htmltext = "7031-03.htm"
        else:
          htmltext = "7031-04.htm"
          st.takeItems(HORRIBLE_SKULL_ID,st.getQuestItemsCount(HORRIBLE_SKULL_ID))
          st.takeItems(EINHASAD_CHURCH_MARK3_ID,1)
          st.giveItems(COIN_OF_LORDS6_ID,1)
   elif npcId == 7031 and st.getInt("cond")==1 and st.getQuestItemsCount(COIN_OF_LORDS6_ID)>0 :
        htmltext = "7031-05.htm"
   elif npcId == 7311 and st.getInt("cond")==1 and st.getQuestItemsCount(MARK_OF_ESQUIRE_ID)>0 :
        htmltext = "7311-01.htm"
   elif npcId == 7653 and st.getInt("cond")==1 and st.getQuestItemsCount(MARK_OF_ESQUIRE_ID)>0 :
        htmltext = "7653-01.htm"
   elif npcId == 7417 and st.getInt("cond")==1 and st.getQuestItemsCount(MARK_OF_ESQUIRE_ID)>0 and (st.getQuestItemsCount(COIN_OF_LORDS1_ID)+st.getQuestItemsCount(COIN_OF_LORDS2_ID)+st.getQuestItemsCount(COIN_OF_LORDS3_ID)+st.getQuestItemsCount(COIN_OF_LORDS4_ID)+st.getQuestItemsCount(COIN_OF_LORDS5_ID)+st.getQuestItemsCount(COIN_OF_LORDS6_ID))==3 :
        htmltext = "7417-10.htm"
   elif npcId == 7417 and st.getInt("cond")==1 and st.getQuestItemsCount(MARK_OF_ESQUIRE_ID)>0 and (st.getQuestItemsCount(COIN_OF_LORDS1_ID)+st.getQuestItemsCount(COIN_OF_LORDS2_ID)+st.getQuestItemsCount(COIN_OF_LORDS3_ID)+st.getQuestItemsCount(COIN_OF_LORDS4_ID)+st.getQuestItemsCount(COIN_OF_LORDS5_ID)+st.getQuestItemsCount(COIN_OF_LORDS6_ID))>3 and (st.getQuestItemsCount(COIN_OF_LORDS1_ID)+st.getQuestItemsCount(COIN_OF_LORDS2_ID)+st.getQuestItemsCount(COIN_OF_LORDS3_ID)+st.getQuestItemsCount(COIN_OF_LORDS4_ID)+st.getQuestItemsCount(COIN_OF_LORDS5_ID)+st.getQuestItemsCount(COIN_OF_LORDS6_ID))<6 :
        htmltext = "7417-11.htm"
   elif npcId == 7417 and st.getInt("cond")==1 and st.getQuestItemsCount(MARK_OF_ESQUIRE_ID)>0 and (st.getQuestItemsCount(COIN_OF_LORDS1_ID)+st.getQuestItemsCount(COIN_OF_LORDS2_ID)+st.getQuestItemsCount(COIN_OF_LORDS3_ID)+st.getQuestItemsCount(COIN_OF_LORDS4_ID)+st.getQuestItemsCount(COIN_OF_LORDS5_ID)+st.getQuestItemsCount(COIN_OF_LORDS6_ID))==6 :
        htmltext = "7417-12.htm"
        st.takeItems(COIN_OF_LORDS1_ID,st.getQuestItemsCount(COIN_OF_LORDS1_ID))
        st.takeItems(COIN_OF_LORDS2_ID,st.getQuestItemsCount(COIN_OF_LORDS2_ID))
        st.takeItems(COIN_OF_LORDS3_ID,st.getQuestItemsCount(COIN_OF_LORDS3_ID))
        st.takeItems(COIN_OF_LORDS4_ID,st.getQuestItemsCount(COIN_OF_LORDS4_ID))
        st.takeItems(COIN_OF_LORDS5_ID,st.getQuestItemsCount(COIN_OF_LORDS5_ID))
        st.takeItems(COIN_OF_LORDS6_ID,st.getQuestItemsCount(COIN_OF_LORDS6_ID))
        st.takeItems(GLUDIO_GUARDS_MARK1_ID,st.getQuestItemsCount(GLUDIO_GUARDS_MARK1_ID))
        st.takeItems(GLUDIO_GUARDS_MARK2_ID,st.getQuestItemsCount(GLUDIO_GUARDS_MARK2_ID))
        st.takeItems(GLUDIO_GUARDS_MARK3_ID,st.getQuestItemsCount(GLUDIO_GUARDS_MARK3_ID))
        st.takeItems(EINHASAD_CHURCH_MARK1_ID,st.getQuestItemsCount(EINHASAD_CHURCH_MARK1_ID))
        st.takeItems(EINHASAD_CHURCH_MARK2_ID,st.getQuestItemsCount(EINHASAD_CHURCH_MARK2_ID))
        st.takeItems(EINHASAD_CHURCH_MARK3_ID,st.getQuestItemsCount(EINHASAD_CHURCH_MARK3_ID))
        st.takeItems(BUGBEAR_NECKLACE_ID,st.getQuestItemsCount(BUGBEAR_NECKLACE_ID))
        st.takeItems(EINHASAD_CRUCIFIX_ID,st.getQuestItemsCount(EINHASAD_CRUCIFIX_ID))
        st.takeItems(POISON_SPIDER_LEG1_ID,st.getQuestItemsCount(POISON_SPIDER_LEG1_ID))
        st.takeItems(LIZARDMAN_TOTEM_ID,st.getQuestItemsCount(LIZARDMAN_TOTEM_ID))
        st.takeItems(GIANT_SPIDER_HUSK_ID,st.getQuestItemsCount(GIANT_SPIDER_HUSK_ID))
        st.takeItems(HORRIBLE_SKULL_ID,st.getQuestItemsCount(HORRIBLE_SKULL_ID))
        st.takeItems(MARK_OF_ESQUIRE_ID,st.getQuestItemsCount(MARK_OF_ESQUIRE_ID))
        st.giveItems(SWORD_OF_RITUAL_ID,1)
        st.set("cond","0")
        st.setState(State.COMPLETED)
        st.playSound("ItemSound.quest_finish")
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("402_PathToKnight")
   if st :
     if st.getState() != State.STARTED : return
     npcId = npc.getNpcId()
     if npcId == 775 :
      st.set("id","0")
      if st.getInt("cond") == 1 and st.getQuestItemsCount(GLUDIO_GUARDS_MARK1_ID)>0 :
        st.giveItems(BUGBEAR_NECKLACE_ID,1)
        if st.getQuestItemsCount(BUGBEAR_NECKLACE_ID) == 10 :
          st.playSound("ItemSound.quest_middle")
        else:
          st.playSound("ItemSound.quest_itemget")
     elif npcId == 5024 :
      st.set("id","0")
      if st.getQuestItemsCount(EINHASAD_CHURCH_MARK1_ID)  :
        st.giveItems(EINHASAD_CRUCIFIX_ID,1)
        if st.getQuestItemsCount(EINHASAD_CRUCIFIX_ID) == 12 :
          st.playSound("ItemSound.quest_middle")
        else:
          st.playSound("ItemSound.quest_itemget")
     elif npcId == 38 :
      st.set("id","0")
      if st.getInt("cond") and st.getQuestItemsCount(GLUDIO_GUARDS_MARK2_ID)>0 and st.getQuestItemsCount(POISON_SPIDER_LEG1_ID)<20 :
        st.giveItems(POISON_SPIDER_LEG1_ID,1)
        if st.getQuestItemsCount(POISON_SPIDER_LEG1_ID) == 20 :
          st.playSound("ItemSound.quest_middle")
        else:
          st.playSound("ItemSound.quest_itemget")
     elif npcId == 43 :
      st.set("id","0")
      if st.getInt("cond") and st.getQuestItemsCount(GLUDIO_GUARDS_MARK2_ID)>0 and st.getQuestItemsCount(POISON_SPIDER_LEG1_ID)<20 :
        st.giveItems(POISON_SPIDER_LEG1_ID,1)
        if st.getQuestItemsCount(POISON_SPIDER_LEG1_ID) == 20 :
          st.playSound("ItemSound.quest_middle")
        else:
          st.playSound("ItemSound.quest_itemget")
     elif npcId == 50 :
      st.set("id","0")
      if st.getQuestItemsCount(GLUDIO_GUARDS_MARK2_ID)>0 and st.getQuestItemsCount(POISON_SPIDER_LEG1_ID)<20 :
        st.giveItems(POISON_SPIDER_LEG1_ID,1)
        if st.getQuestItemsCount(POISON_SPIDER_LEG1_ID) == 20 :
          st.playSound("ItemSound.quest_middle")
        else:
          st.playSound("ItemSound.quest_itemget")
     elif npcId == 30 :
      st.set("id","0")
      if st.getQuestItemsCount(EINHASAD_CHURCH_MARK2_ID) and st.getQuestItemsCount(LIZARDMAN_TOTEM_ID)<20 and st.getRandom(10)<5 :
        st.giveItems(LIZARDMAN_TOTEM_ID,1)
        if st.getQuestItemsCount(LIZARDMAN_TOTEM_ID) == 20 :
          st.playSound("ItemSound.quest_middle")
        else:
          st.playSound("ItemSound.quest_itemget")
     elif npcId == 27 :
      st.set("id","0")
      if st.getQuestItemsCount(EINHASAD_CHURCH_MARK2_ID) :
        st.giveItems(LIZARDMAN_TOTEM_ID,1)
        if st.getQuestItemsCount(LIZARDMAN_TOTEM_ID) == 20 :
          st.playSound("ItemSound.quest_middle")
        else:
          st.playSound("ItemSound.quest_itemget")
     elif npcId == 24 :
      st.set("id","0")
      if st.getQuestItemsCount(EINHASAD_CHURCH_MARK2_ID) :
        st.giveItems(LIZARDMAN_TOTEM_ID,1)
        if st.getQuestItemsCount(LIZARDMAN_TOTEM_ID) == 20 :
          st.playSound("ItemSound.quest_middle")
        else:
          st.playSound("ItemSound.quest_itemget")
     elif npcId == 103 :
      st.set("id","0")
      if st.getQuestItemsCount(GLUDIO_GUARDS_MARK3_ID)>0 and st.getRandom(10)<4 :
        st.giveItems(GIANT_SPIDER_HUSK_ID,1)
        if st.getQuestItemsCount(GIANT_SPIDER_HUSK_ID) == 20 :
          st.playSound("ItemSound.quest_middle")
        else:
          st.playSound("ItemSound.quest_itemget")
     elif npcId == 106 :
      st.set("id","0")
      if st.getQuestItemsCount(GLUDIO_GUARDS_MARK2_ID)>0 and st.getRandom(10)<4 :
        st.giveItems(GIANT_SPIDER_HUSK_ID,1)
        if st.getQuestItemsCount(GIANT_SPIDER_HUSK_ID) == 20 :
          st.playSound("ItemSound.quest_middle")
        else:
          st.playSound("ItemSound.quest_itemget")
     elif npcId == 108 :
      st.set("id","0")
      if st.getQuestItemsCount(GLUDIO_GUARDS_MARK3_ID)>0 and st.getRandom(10)<4 :
        st.giveItems(GIANT_SPIDER_HUSK_ID,1)
        if st.getQuestItemsCount(GIANT_SPIDER_HUSK_ID) == 20 :
          st.playSound("ItemSound.quest_middle")
        else:
          st.playSound("ItemSound.quest_itemget")
     elif npcId == 404 :
      st.set("id","0")
      if st.getQuestItemsCount(EINHASAD_CHURCH_MARK3_ID) :
        st.giveItems(HORRIBLE_SKULL_ID,1)
        if st.getQuestItemsCount(HORRIBLE_SKULL_ID) == 10 :
          st.playSound("ItemSound.quest_middle")
        else:
          st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(402,"402_PathToKnight","Path To Knight")
QUEST.addStartNpc(7417)

QUEST.addTalkId(7031)
QUEST.addTalkId(7037)
QUEST.addTalkId(7039)
QUEST.addTalkId(7289)
QUEST.addTalkId(7311)
QUEST.addTalkId(7332)
QUEST.addTalkId(7379)
QUEST.addTalkId(7417)
QUEST.addTalkId(7653)

QUEST.addKillId(103)
QUEST.addKillId(106)
QUEST.addKillId(108)
QUEST.addKillId(24)
QUEST.addKillId(27)
QUEST.addKillId(30)
QUEST.addKillId(38)
QUEST.addKillId(404)
QUEST.addKillId(43)
QUEST.addKillId(50)
QUEST.addKillId(5024)
QUEST.addKillId(775)