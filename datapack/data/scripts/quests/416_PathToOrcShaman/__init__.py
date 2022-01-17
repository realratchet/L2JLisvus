# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

FIRE_CHARM_ID = 1616
KASHA_BEAR_PELT_ID = 1617
KASHA_BSPIDER_HUSK_ID = 1618
KASHA_BSPIDER_HUSK_ID = 1618
FIERY_EGG1_ID = 1619
FIERY_EGG1_ID = 1619
HESTUI_MASK_ID = 1620
HESTUI_MASK_ID = 1620
FIERY_EGG2_ID = 1621
TOTEM_SPIRIT_CLAW_ID = 1622
TATARUS_LETTER_ID = 1623
FLAME_CHARM_ID = 1624
GRIZZLY_BLOOD_ID = 1625
BLOOD_CAULDRON_ID = 1626
SPIRIT_NET_ID = 1627
BOUND_DURKA_SPIRIT_ID = 1628
DURKA_PARASITE_ID = 1629
TOTEM_SPIRIT_BLOOD_ID = 1630
MASK_OF_MEDIUM_ID = 1631

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(1616,1631)

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
        st.set("id","0")
        st.set("cond","1")
        st.setState(State.STARTED)
        st.playSound("ItemSound.quest_accept")
        htmltext = "7585-06.htm"
        st.giveItems(FIRE_CHARM_ID,1)
    elif event == "7585_1" :
          if st.getPlayer().getClassId().getId() != 0x31 :
            if st.getPlayer().getClassId().getId() == 0x32 :
              htmltext = "7585-02a.htm"
            else:
              htmltext = "7585-02.htm"
          else:
            if st.getPlayer().getLevel()<19 :
              htmltext = "7585-03.htm"
            else:
              if st.getQuestItemsCount(MASK_OF_MEDIUM_ID) != 0 :
                htmltext = "7585-04.htm"
              else:
                htmltext = "7585-05.htm"
                return htmltext
    elif event == "7585_2" :
          htmltext = "7585-11.htm"
          st.takeItems(TOTEM_SPIRIT_CLAW_ID,1)
          st.giveItems(TATARUS_LETTER_ID,1)
          st.set("cond","5")
    elif event == "7592_1" :
          htmltext = "7592-02.htm"
    elif event == "7592_2" :
          htmltext = "7592-03.htm"
          st.takeItems(HESTUI_MASK_ID,1)
          st.takeItems(FIERY_EGG2_ID,1)
          st.giveItems(TOTEM_SPIRIT_CLAW_ID,1)
          st.set("cond","4")
    elif event == "7502_2" :
          htmltext = "7502-07.htm"
          st.takeItems(TOTEM_SPIRIT_BLOOD_ID,st.getQuestItemsCount(TOTEM_SPIRIT_BLOOD_ID))
          st.giveItems(MASK_OF_MEDIUM_ID,1)
          st.set("cond","0")
          st.setState(State.COMPLETED)
          st.playSound("ItemSound.quest_finish")
    elif event == "7593_1" :
          htmltext = "7593-02.htm"
    elif event == "7593_2" :
          htmltext = "7593-03.htm"
          st.takeItems(BLOOD_CAULDRON_ID,1)
          st.giveItems(SPIRIT_NET_ID,1)
          st.set("cond","9")
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if npcId != 7585 and id != State.STARTED :
        return htmltext
   if npcId == 7585 and st.getInt("cond")==0 :
        htmltext = "7585-01.htm"
   elif npcId == 7585 and st.getInt("cond") and st.getQuestItemsCount(FIRE_CHARM_ID)==1 and ((st.getQuestItemsCount(KASHA_BEAR_PELT_ID)+st.getQuestItemsCount(KASHA_BSPIDER_HUSK_ID)+st.getQuestItemsCount(FIERY_EGG1_ID))<3) :
        htmltext = "7585-07.htm"
   elif npcId == 7585 and st.getInt("cond") and st.getQuestItemsCount(FIRE_CHARM_ID)==1 and ((st.getQuestItemsCount(KASHA_BEAR_PELT_ID)+st.getQuestItemsCount(KASHA_BSPIDER_HUSK_ID)+st.getQuestItemsCount(FIERY_EGG1_ID))>=3) :
        htmltext = "7585-08.htm"
        st.takeItems(FIRE_CHARM_ID,1)
        st.takeItems(KASHA_BEAR_PELT_ID,1)
        st.takeItems(KASHA_BSPIDER_HUSK_ID,1)
        st.takeItems(FIERY_EGG1_ID,1)
        st.giveItems(HESTUI_MASK_ID,1)
        st.giveItems(FIERY_EGG2_ID,1)
        st.set("cond","3")
   elif npcId == 7585 and st.getInt("cond") and st.getQuestItemsCount(HESTUI_MASK_ID)==1 and st.getQuestItemsCount(FIERY_EGG2_ID)==1 :
        htmltext = "7585-09.htm"
   elif npcId == 7585 and st.getInt("cond") and st.getQuestItemsCount(TOTEM_SPIRIT_CLAW_ID)==1 :
        htmltext = "7585-10.htm"
   elif npcId == 7585 and st.getInt("cond") and st.getQuestItemsCount(TATARUS_LETTER_ID)==1 :
        htmltext = "7585-12.htm"
   elif npcId == 7585 and st.getInt("cond") and (st.getQuestItemsCount(GRIZZLY_BLOOD_ID) or st.getQuestItemsCount(FLAME_CHARM_ID) or st.getQuestItemsCount(BLOOD_CAULDRON_ID) or st.getQuestItemsCount(SPIRIT_NET_ID) or st.getQuestItemsCount(BOUND_DURKA_SPIRIT_ID) or st.getQuestItemsCount(TOTEM_SPIRIT_BLOOD_ID)) :
        htmltext = "7585-13.htm"
   elif npcId == 7592 and st.getInt("cond") and st.getQuestItemsCount(HESTUI_MASK_ID) and st.getQuestItemsCount(FIERY_EGG2_ID) :
        htmltext = "7592-01.htm"
   elif npcId == 7592 and st.getInt("cond") and st.getQuestItemsCount(TOTEM_SPIRIT_CLAW_ID) :
        htmltext = "7592-04.htm"
   elif npcId == 7592 and st.getInt("cond") and (st.getQuestItemsCount(GRIZZLY_BLOOD_ID) or st.getQuestItemsCount(FLAME_CHARM_ID) or st.getQuestItemsCount(BLOOD_CAULDRON_ID) or st.getQuestItemsCount(SPIRIT_NET_ID) or st.getQuestItemsCount(BOUND_DURKA_SPIRIT_ID) or st.getQuestItemsCount(TOTEM_SPIRIT_BLOOD_ID) or st.getQuestItemsCount(TATARUS_LETTER_ID)) :
        htmltext = "7592-05.htm"
   elif npcId == 7502 and st.getInt("cond") and st.getQuestItemsCount(TATARUS_LETTER_ID) :
        htmltext = "7502-01.htm"
        st.giveItems(FLAME_CHARM_ID,1)
        st.takeItems(TATARUS_LETTER_ID,1)
        st.set("cond","6")
   elif npcId == 7502 and st.getInt("cond") and st.getQuestItemsCount(FLAME_CHARM_ID)==1 and st.getQuestItemsCount(GRIZZLY_BLOOD_ID)<3 :
        htmltext = "7502-02.htm"
   elif npcId == 7502 and st.getInt("cond") and st.getQuestItemsCount(FLAME_CHARM_ID)==1 and st.getQuestItemsCount(GRIZZLY_BLOOD_ID)>=3 :
        htmltext = "7502-03.htm"
        st.takeItems(FLAME_CHARM_ID,1)
        st.takeItems(GRIZZLY_BLOOD_ID,st.getQuestItemsCount(GRIZZLY_BLOOD_ID))
        st.giveItems(BLOOD_CAULDRON_ID,1)
        st.set("cond","8")
   elif npcId == 7502 and st.getInt("cond") and st.getQuestItemsCount(BLOOD_CAULDRON_ID)==1 :
        htmltext = "7502-04.htm"
   elif npcId == 7502 and st.getInt("cond") and (st.getQuestItemsCount(BOUND_DURKA_SPIRIT_ID)==1 or st.getQuestItemsCount(SPIRIT_NET_ID)==1) :
        htmltext = "7502-05.htm"
   elif npcId == 7502 and st.getInt("cond") and st.getQuestItemsCount(TOTEM_SPIRIT_BLOOD_ID)==1 :
        htmltext = "7502-06.htm"
   elif npcId == 7593 and st.getInt("cond") and st.getQuestItemsCount(BLOOD_CAULDRON_ID) :
        htmltext = "7593-01.htm"
   elif npcId == 7593 and st.getInt("cond") and st.getQuestItemsCount(SPIRIT_NET_ID) and st.getQuestItemsCount(BOUND_DURKA_SPIRIT_ID)==0 :
        htmltext = "7593-04.htm"
   elif npcId == 7593 and st.getInt("cond") and st.getQuestItemsCount(SPIRIT_NET_ID)==0 and st.getQuestItemsCount(BOUND_DURKA_SPIRIT_ID) :
        htmltext = "7593-05.htm"
        st.takeItems(BOUND_DURKA_SPIRIT_ID,1)
        st.giveItems(TOTEM_SPIRIT_BLOOD_ID,1)
        st.set("cond","11")
   elif npcId == 7593 and st.getInt("cond")==1 and st.getQuestItemsCount(TOTEM_SPIRIT_BLOOD_ID) :
        htmltext = "7593-06.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("416_PathToOrcShaman")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId == 479 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(FIRE_CHARM_ID) == 1 and st.getQuestItemsCount(KASHA_BEAR_PELT_ID)<1 :
          if st.getQuestItemsCount(KASHA_BEAR_PELT_ID)+st.getQuestItemsCount(KASHA_BSPIDER_HUSK_ID)+st.getQuestItemsCount(FIERY_EGG1_ID) == 2 :
            st.giveItems(KASHA_BEAR_PELT_ID,1)
            st.playSound("ItemSound.quest_middle")
            st.set("cond","2")
          else:
            st.giveItems(KASHA_BEAR_PELT_ID,1)
            st.playSound("ItemSound.quest_itemget")
      elif npcId == 478 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(FIRE_CHARM_ID) == 1 and st.getQuestItemsCount(KASHA_BSPIDER_HUSK_ID)<1 :
          if st.getQuestItemsCount(KASHA_BEAR_PELT_ID)+st.getQuestItemsCount(KASHA_BSPIDER_HUSK_ID)+st.getQuestItemsCount(FIERY_EGG1_ID) == 2 :
            st.giveItems(KASHA_BSPIDER_HUSK_ID,1)
            st.playSound("ItemSound.quest_middle")
            st.set("cond","2")
          else:
            st.giveItems(KASHA_BSPIDER_HUSK_ID,1)
            st.playSound("ItemSound.quest_itemget")
      elif npcId == 415 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(FIRE_CHARM_ID) == 1 and st.getQuestItemsCount(FIERY_EGG1_ID)<1 :
          if st.getQuestItemsCount(KASHA_BEAR_PELT_ID)+st.getQuestItemsCount(KASHA_BSPIDER_HUSK_ID)+st.getQuestItemsCount(FIERY_EGG1_ID) == 2 :
            st.giveItems(FIERY_EGG1_ID,1)
            st.playSound("ItemSound.quest_middle")
            st.set("cond","2")
          else:
            st.giveItems(FIERY_EGG1_ID,1)
            st.playSound("ItemSound.quest_itemget")
      elif npcId == 335 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(FLAME_CHARM_ID) == 1 and st.getQuestItemsCount(GRIZZLY_BLOOD_ID)<3 :
          if st.getQuestItemsCount(GRIZZLY_BLOOD_ID) == 2 :
            st.giveItems(GRIZZLY_BLOOD_ID,1)
            st.playSound("ItemSound.quest_middle")
            st.set("cond","7")
          else:
            st.giveItems(GRIZZLY_BLOOD_ID,1)
            st.playSound("ItemSound.quest_itemget")
      elif npcId == 38 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(SPIRIT_NET_ID) == 1 and st.getQuestItemsCount(BOUND_DURKA_SPIRIT_ID) == 0 and st.getQuestItemsCount(DURKA_PARASITE_ID)<8 :
          n = st.getRandom(10)
          if st.getQuestItemsCount(DURKA_PARASITE_ID) == 5 and n<1 :
            st.takeItems(DURKA_PARASITE_ID,st.getQuestItemsCount(DURKA_PARASITE_ID))
            st.addSpawn(5056)
            st.playSound("ItemSound.quest_itemget")
          elif st.getQuestItemsCount(DURKA_PARASITE_ID) == 6 and n<2 :
            st.takeItems(DURKA_PARASITE_ID,st.getQuestItemsCount(DURKA_PARASITE_ID))
            st.playSound("ItemSound.quest_itemget")
            st.addSpawn(5056)
          elif st.getQuestItemsCount(DURKA_PARASITE_ID) == 7 and n<2 :
            st.takeItems(DURKA_PARASITE_ID,st.getQuestItemsCount(DURKA_PARASITE_ID))
            st.playSound("ItemSound.quest_itemget")
            st.addSpawn(5056)
          elif st.getQuestItemsCount(DURKA_PARASITE_ID) >= 7 :
            st.addSpawn(5056)
            st.playSound("ItemSound.quest_itemget")
            st.takeItems(DURKA_PARASITE_ID,st.getQuestItemsCount(DURKA_PARASITE_ID))
          else:
            st.giveItems(DURKA_PARASITE_ID,1)
            st.playSound("ItemSound.quest_itemget")
      elif npcId == 43 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(SPIRIT_NET_ID) == 1 and st.getQuestItemsCount(BOUND_DURKA_SPIRIT_ID) == 0 and st.getQuestItemsCount(DURKA_PARASITE_ID)<8 :
          n = st.getRandom(10)
          if st.getQuestItemsCount(DURKA_PARASITE_ID) == 5 and n<1 :
            st.takeItems(DURKA_PARASITE_ID,st.getQuestItemsCount(DURKA_PARASITE_ID))
            st.addSpawn(5056)
            st.playSound("ItemSound.quest_itemget")
          elif st.getQuestItemsCount(DURKA_PARASITE_ID) == 6 and n<2 :
            st.takeItems(DURKA_PARASITE_ID,st.getQuestItemsCount(DURKA_PARASITE_ID))
            st.playSound("ItemSound.quest_itemget")
            st.addSpawn(5056)
          elif st.getQuestItemsCount(DURKA_PARASITE_ID) == 7 and n<2 :
            st.takeItems(DURKA_PARASITE_ID,st.getQuestItemsCount(DURKA_PARASITE_ID))
            st.playSound("ItemSound.quest_itemget")
            st.addSpawn(5056)
          elif st.getQuestItemsCount(DURKA_PARASITE_ID) >= 7 :
            st.addSpawn(5056)
            st.playSound("ItemSound.quest_itemget")
            st.takeItems(DURKA_PARASITE_ID,st.getQuestItemsCount(DURKA_PARASITE_ID))
          else:
            st.giveItems(DURKA_PARASITE_ID,1)
            st.playSound("ItemSound.quest_itemget")
      elif npcId == 5056 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(SPIRIT_NET_ID) == 1 and st.getQuestItemsCount(BOUND_DURKA_SPIRIT_ID) == 0 :
          st.giveItems(BOUND_DURKA_SPIRIT_ID,1)
          st.takeItems(SPIRIT_NET_ID,1)
          st.takeItems(DURKA_PARASITE_ID,st.getQuestItemsCount(DURKA_PARASITE_ID))
          st.playSound("ItemSound.quest_middle")
          st.set("cond","10")
   return

QUEST = Quest(416,"416_PathToOrcShaman","Path To Orc Shaman")
QUEST.addStartNpc(7585)

QUEST.addTalkId(7502)
QUEST.addTalkId(7585)
QUEST.addTalkId(7592)
QUEST.addTalkId(7593)

QUEST.addKillId(335)
QUEST.addKillId(38)
QUEST.addKillId(415)
QUEST.addKillId(43)
QUEST.addKillId(478)
QUEST.addKillId(479)
QUEST.addKillId(5056)