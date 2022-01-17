# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

POMEGRANATE_ID = 1593
LEATHER_POUCH1_ID = 1594
LEATHER_POUCH2_ID = 1595
LEATHER_POUCH3_ID = 1596
LEATHER_POUCH1FULL_ID = 1597
LEATHER_POUCH2FULL_ID = 1598
LEATHER_POUCH3FULL_ID = 1599
KASHA_BEAR_CLAW_ID = 1600
KASHA_BSPIDER_TALON_ID = 1601
S_SALAMANDER_SCALE_ID = 1602
SCROLL_FIERY_SPIRIT_ID = 1603
ROSHEEKS_LETTER_ID = 1604
GANTAKIS_LETTER_ID = 1605
FIG_ID = 1606
LEATHER_PURSE4_ID = 1607
LEATHER_POUCH4FULL_ID = 1608
VUKU_TUSK_ID = 1609
RATMAN_FANG_ID = 1610
LANGK_TOOTH_ID = 1611
FELIM_TOOTH_ID = 1612
SCROLL_IRON_WILL_ID = 1613
TORUKUS_LETTER_ID = 1614
KHAVATARI_TOTEM_ID = 1615

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(1593,1615)

 def onEvent (self,event,st) :
    htmltext = event
    if event == "7587_1" :
          if st.getPlayer().getClassId().getId() != 0x2c :
            if st.getPlayer().getClassId().getId() == 0x2f :
              htmltext = "7587-02a.htm"
              st.exitQuest(1)
            else:
              htmltext = "7587-02.htm"
              st.exitQuest(1)
          else:
            if st.getPlayer().getLevel()<19 :
              htmltext = "7587-03.htm"
            else:
              if st.getQuestItemsCount(KHAVATARI_TOTEM_ID) != 0 :
                htmltext = "7587-04.htm"
              else:
                htmltext = "7587-05.htm"
                return htmltext
    elif event == "1" :
        st.set("id","0")
        st.set("cond","1")
        st.setState(State.STARTED)
        st.playSound("ItemSound.quest_accept")
        htmltext = "7587-06.htm"
        st.giveItems(POMEGRANATE_ID,1)
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if npcId != 7587 and id != State.STARTED :
        return htmltext
   if npcId == 7587 and st.getInt("cond")==0 and id != State.COMPLETED :
        htmltext = "7587-01.htm"
   elif npcId == 7587 and st.getInt("cond")==0 and id == State.COMPLETED :
      htmltext = "7587-04.htm"
   elif npcId == 7587 and st.getInt("cond") and st.getQuestItemsCount(SCROLL_FIERY_SPIRIT_ID)==0 and st.getQuestItemsCount(POMEGRANATE_ID)==1 and st.getQuestItemsCount(GANTAKIS_LETTER_ID)==0 and st.getQuestItemsCount(ROSHEEKS_LETTER_ID)==0 and ((st.getQuestItemsCount(LEATHER_POUCH1_ID)+st.getQuestItemsCount(LEATHER_POUCH2_ID)+st.getQuestItemsCount(LEATHER_POUCH3_ID)+st.getQuestItemsCount(LEATHER_POUCH1FULL_ID)+st.getQuestItemsCount(LEATHER_POUCH2FULL_ID)+st.getQuestItemsCount(LEATHER_POUCH3FULL_ID))==0) :
        htmltext = "7587-07.htm"
   elif npcId == 7587 and st.getInt("cond") and st.getQuestItemsCount(SCROLL_FIERY_SPIRIT_ID)==0 and st.getQuestItemsCount(POMEGRANATE_ID)==0 and st.getQuestItemsCount(GANTAKIS_LETTER_ID)==0 and st.getQuestItemsCount(ROSHEEKS_LETTER_ID)==0 and ((st.getQuestItemsCount(LEATHER_POUCH1_ID)+st.getQuestItemsCount(LEATHER_POUCH2_ID)+st.getQuestItemsCount(LEATHER_POUCH3_ID)+st.getQuestItemsCount(LEATHER_POUCH1FULL_ID)+st.getQuestItemsCount(LEATHER_POUCH2FULL_ID)+st.getQuestItemsCount(LEATHER_POUCH3FULL_ID))==1) :
        htmltext = "7587-08.htm"
   elif npcId == 7587 and st.getInt("cond") and st.getQuestItemsCount(SCROLL_FIERY_SPIRIT_ID)==1 and st.getQuestItemsCount(POMEGRANATE_ID)==0 and st.getQuestItemsCount(GANTAKIS_LETTER_ID)==0 and st.getQuestItemsCount(ROSHEEKS_LETTER_ID)==1 and ((st.getQuestItemsCount(LEATHER_POUCH1_ID)+st.getQuestItemsCount(LEATHER_POUCH2_ID)+st.getQuestItemsCount(LEATHER_POUCH3_ID)+st.getQuestItemsCount(LEATHER_POUCH1FULL_ID)+st.getQuestItemsCount(LEATHER_POUCH2FULL_ID)+st.getQuestItemsCount(LEATHER_POUCH3FULL_ID))==0) :
        htmltext = "7587-09.htm"
        st.takeItems(ROSHEEKS_LETTER_ID,1)
        st.giveItems(GANTAKIS_LETTER_ID,1)
        st.set("cond","9")
   elif npcId == 7587 and st.getInt("cond") and st.getQuestItemsCount(SCROLL_FIERY_SPIRIT_ID)==1 and st.getQuestItemsCount(POMEGRANATE_ID)==0 and st.getQuestItemsCount(GANTAKIS_LETTER_ID)==1 and st.getQuestItemsCount(ROSHEEKS_LETTER_ID)==0 and ((st.getQuestItemsCount(LEATHER_POUCH1_ID)+st.getQuestItemsCount(LEATHER_POUCH2_ID)+st.getQuestItemsCount(LEATHER_POUCH3_ID)+st.getQuestItemsCount(LEATHER_POUCH1FULL_ID)+st.getQuestItemsCount(LEATHER_POUCH2FULL_ID)+st.getQuestItemsCount(LEATHER_POUCH3FULL_ID))==0) :
        htmltext = "7587-10.htm"
   elif npcId == 7587 and st.getInt("cond") and st.getQuestItemsCount(SCROLL_FIERY_SPIRIT_ID)==1 and st.getQuestItemsCount(POMEGRANATE_ID)==0 and st.getQuestItemsCount(GANTAKIS_LETTER_ID)==0 and st.getQuestItemsCount(ROSHEEKS_LETTER_ID)==0 and ((st.getQuestItemsCount(LEATHER_POUCH1_ID)+st.getQuestItemsCount(LEATHER_POUCH2_ID)+st.getQuestItemsCount(LEATHER_POUCH3_ID)+st.getQuestItemsCount(LEATHER_POUCH1FULL_ID)+st.getQuestItemsCount(LEATHER_POUCH2FULL_ID)+st.getQuestItemsCount(LEATHER_POUCH3FULL_ID))==0) :
        htmltext = "7587-11.htm"
   elif npcId == 7590 and st.getInt("cond") and st.getQuestItemsCount(POMEGRANATE_ID) :
        htmltext = "7590-01.htm"
        st.takeItems(POMEGRANATE_ID,1)
        st.giveItems(LEATHER_POUCH1_ID,1)
        st.set("cond","2")
   elif npcId == 7590 and st.getInt("cond") and st.getQuestItemsCount(LEATHER_POUCH1_ID) and st.getQuestItemsCount(LEATHER_POUCH1FULL_ID)==0 :
        htmltext = "7590-02.htm"
   elif npcId == 7590 and st.getInt("cond") and st.getQuestItemsCount(LEATHER_POUCH1_ID)==0 and st.getQuestItemsCount(LEATHER_POUCH1FULL_ID) :
        htmltext = "7590-03.htm"
        st.takeItems(LEATHER_POUCH1FULL_ID,1)
        st.giveItems(LEATHER_POUCH2_ID,1)
        st.set("cond","4")
   elif npcId == 7590 and st.getInt("cond") and st.getQuestItemsCount(LEATHER_POUCH2_ID)==1 and st.getQuestItemsCount(LEATHER_POUCH2FULL_ID)==0 :
        htmltext = "7590-04.htm"
   elif npcId == 7590 and st.getInt("cond") and st.getQuestItemsCount(LEATHER_POUCH2_ID)==0 and st.getQuestItemsCount(LEATHER_POUCH2FULL_ID)==1 :
        htmltext = "7590-05.htm"
        st.takeItems(LEATHER_POUCH2FULL_ID,1)
        st.giveItems(LEATHER_POUCH3_ID,1)
        st.set("cond","6")
   elif npcId == 7590 and st.getInt("cond") and st.getQuestItemsCount(LEATHER_POUCH3_ID)==1 and st.getQuestItemsCount(LEATHER_POUCH3FULL_ID)==0 :
        htmltext = "7590-06.htm"
   elif npcId == 7590 and st.getInt("cond") and st.getQuestItemsCount(LEATHER_POUCH3_ID)==0 and st.getQuestItemsCount(LEATHER_POUCH3FULL_ID)==1 :
        htmltext = "7590-07.htm"
        st.takeItems(LEATHER_POUCH3FULL_ID,1)
        st.giveItems(SCROLL_FIERY_SPIRIT_ID,1)
        st.giveItems(ROSHEEKS_LETTER_ID,1)
        st.set("cond","8")
   elif npcId == 7590 and st.getInt("cond") and st.getQuestItemsCount(ROSHEEKS_LETTER_ID)==1 and st.getQuestItemsCount(SCROLL_FIERY_SPIRIT_ID)==1 :
        htmltext = "7590-08.htm"
   elif npcId == 7590 and st.getInt("cond") and st.getQuestItemsCount(ROSHEEKS_LETTER_ID)==0 and st.getQuestItemsCount(SCROLL_FIERY_SPIRIT_ID)==1 :
        htmltext = "7590-09.htm"
   elif npcId == 7501 and st.getInt("cond") and st.getQuestItemsCount(GANTAKIS_LETTER_ID) :
        htmltext = "7501-01.htm"
        st.takeItems(GANTAKIS_LETTER_ID,1)
        st.giveItems(FIG_ID,1)
        st.set("cond","10")
   elif npcId == 7501 and st.getInt("cond") and st.getQuestItemsCount(FIG_ID) and (st.getQuestItemsCount(LEATHER_PURSE4_ID)==0 or st.getQuestItemsCount(LEATHER_POUCH4FULL_ID)==0) :
        htmltext = "7501-02.htm"
   elif npcId == 7501 and st.getInt("cond") and st.getQuestItemsCount(FIG_ID)==0 and (st.getQuestItemsCount(LEATHER_PURSE4_ID)==1 or st.getQuestItemsCount(LEATHER_POUCH4FULL_ID)==1) :
        htmltext = "7501-03.htm"
   elif npcId == 7501 and st.getInt("cond") and st.getQuestItemsCount(SCROLL_IRON_WILL_ID) :
        htmltext = "7501-04.htm"
        st.takeItems(SCROLL_IRON_WILL_ID,1)
        st.takeItems(SCROLL_FIERY_SPIRIT_ID,1)
        st.takeItems(TORUKUS_LETTER_ID,1)
        st.giveItems(KHAVATARI_TOTEM_ID,1)
        st.set("cond","0")
        st.setState(State.COMPLETED)
        st.playSound("ItemSound.quest_finish")
   elif npcId == 7591 and st.getInt("cond") and st.getQuestItemsCount(FIG_ID) :
        htmltext = "7591-01.htm"
        st.takeItems(FIG_ID,1)
        st.giveItems(LEATHER_PURSE4_ID,1)
        st.set("cond","11")
   elif npcId == 7591 and st.getInt("cond") and st.getQuestItemsCount(LEATHER_PURSE4_ID) and st.getQuestItemsCount(LEATHER_POUCH4FULL_ID)==0 :
        htmltext = "7591-02.htm"
   elif npcId == 7591 and st.getInt("cond") and st.getQuestItemsCount(LEATHER_PURSE4_ID)==0 and st.getQuestItemsCount(LEATHER_POUCH4FULL_ID)==1 :
        htmltext = "7591-03.htm"
        st.takeItems(LEATHER_POUCH4FULL_ID,1)
        st.giveItems(SCROLL_IRON_WILL_ID,1)
        st.giveItems(TORUKUS_LETTER_ID,1)
        st.set("cond","13")
   elif npcId == 7591 and st.getInt("cond") and st.getQuestItemsCount(SCROLL_IRON_WILL_ID)==1 and st.getQuestItemsCount(TORUKUS_LETTER_ID)==1 :
        htmltext = "7591-04.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("415_PathToOrcMonk")
   if st :
      if st.getState() != State.STARTED : return
      npcId = npc.getNpcId()
      if npcId == 479 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(LEATHER_POUCH1_ID) == 1 :
          if st.getQuestItemsCount(KASHA_BEAR_CLAW_ID) == 4 :
            st.takeItems(KASHA_BEAR_CLAW_ID,st.getQuestItemsCount(KASHA_BEAR_CLAW_ID))
            st.takeItems(LEATHER_POUCH1_ID,st.getQuestItemsCount(LEATHER_POUCH1_ID))
            st.giveItems(LEATHER_POUCH1FULL_ID,1)
            st.playSound("ItemSound.quest_middle")
            st.set("cond","3")
          else:
            st.giveItems(KASHA_BEAR_CLAW_ID,1)
            st.playSound("ItemSound.quest_itemget")
      elif npcId == 415 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(LEATHER_POUCH3_ID) == 1 :
          if st.getQuestItemsCount(S_SALAMANDER_SCALE_ID) == 4 :
            st.takeItems(S_SALAMANDER_SCALE_ID,st.getQuestItemsCount(S_SALAMANDER_SCALE_ID))
            st.takeItems(LEATHER_POUCH3_ID,st.getQuestItemsCount(LEATHER_POUCH3_ID))
            st.giveItems(LEATHER_POUCH3FULL_ID,1)
            st.playSound("ItemSound.quest_middle")
            st.set("cond","7")
          else:
            st.giveItems(S_SALAMANDER_SCALE_ID,1)
            st.playSound("ItemSound.quest_itemget")
      elif npcId == 478 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(LEATHER_POUCH2_ID) == 1 :
          if st.getQuestItemsCount(KASHA_BSPIDER_TALON_ID) == 4 :
            st.takeItems(KASHA_BSPIDER_TALON_ID,st.getQuestItemsCount(KASHA_BSPIDER_TALON_ID))
            st.takeItems(LEATHER_POUCH2_ID,st.getQuestItemsCount(LEATHER_POUCH2_ID))
            st.giveItems(LEATHER_POUCH2FULL_ID,1)
            st.playSound("ItemSound.quest_middle")
            st.set("cond","5")
          else:
            st.giveItems(KASHA_BSPIDER_TALON_ID,1)
            st.playSound("ItemSound.quest_itemget")
      elif npcId == 17 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(LEATHER_PURSE4_ID) == 1 and st.getQuestItemsCount(VUKU_TUSK_ID)<3 :
          if st.getQuestItemsCount(RATMAN_FANG_ID)+st.getQuestItemsCount(LANGK_TOOTH_ID)+st.getQuestItemsCount(FELIM_TOOTH_ID)+st.getQuestItemsCount(VUKU_TUSK_ID) >= 11 :
            st.takeItems(VUKU_TUSK_ID,st.getQuestItemsCount(VUKU_TUSK_ID))
            st.takeItems(RATMAN_FANG_ID,st.getQuestItemsCount(RATMAN_FANG_ID))
            st.takeItems(LANGK_TOOTH_ID,st.getQuestItemsCount(LANGK_TOOTH_ID))
            st.takeItems(FELIM_TOOTH_ID,st.getQuestItemsCount(FELIM_TOOTH_ID))
            st.takeItems(LEATHER_PURSE4_ID,1)
            st.giveItems(LEATHER_POUCH4FULL_ID,1)
            st.playSound("ItemSound.quest_middle")
            st.set("cond","12")
          else:
            st.giveItems(VUKU_TUSK_ID,1)
            st.playSound("ItemSound.quest_itemget")
      elif npcId == 359 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(LEATHER_PURSE4_ID) == 1 and st.getQuestItemsCount(RATMAN_FANG_ID)<3 :
          if st.getQuestItemsCount(RATMAN_FANG_ID)+st.getQuestItemsCount(LANGK_TOOTH_ID)+st.getQuestItemsCount(FELIM_TOOTH_ID)+st.getQuestItemsCount(VUKU_TUSK_ID) >= 11 :
            st.takeItems(VUKU_TUSK_ID,st.getQuestItemsCount(VUKU_TUSK_ID))
            st.takeItems(RATMAN_FANG_ID,st.getQuestItemsCount(RATMAN_FANG_ID))
            st.takeItems(LANGK_TOOTH_ID,st.getQuestItemsCount(LANGK_TOOTH_ID))
            st.takeItems(FELIM_TOOTH_ID,st.getQuestItemsCount(FELIM_TOOTH_ID))
            st.takeItems(LEATHER_PURSE4_ID,1)
            st.giveItems(LEATHER_POUCH4FULL_ID,1)
            st.playSound("ItemSound.quest_middle")
            st.set("cond","12")
          else:
            st.giveItems(RATMAN_FANG_ID,1)
            st.playSound("ItemSound.quest_itemget")
      elif npcId == 24 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(LEATHER_PURSE4_ID) == 1 and st.getQuestItemsCount(LANGK_TOOTH_ID)<3 :
          if st.getQuestItemsCount(RATMAN_FANG_ID)+st.getQuestItemsCount(LANGK_TOOTH_ID)+st.getQuestItemsCount(FELIM_TOOTH_ID)+st.getQuestItemsCount(VUKU_TUSK_ID) >= 11 :
            st.takeItems(VUKU_TUSK_ID,st.getQuestItemsCount(VUKU_TUSK_ID))
            st.takeItems(RATMAN_FANG_ID,st.getQuestItemsCount(RATMAN_FANG_ID))
            st.takeItems(LANGK_TOOTH_ID,st.getQuestItemsCount(LANGK_TOOTH_ID))
            st.takeItems(FELIM_TOOTH_ID,st.getQuestItemsCount(FELIM_TOOTH_ID))
            st.takeItems(LEATHER_PURSE4_ID,1)
            st.giveItems(LEATHER_POUCH4FULL_ID,1)
            st.playSound("ItemSound.quest_middle")
            st.set("cond","12")
          else:
            st.giveItems(LANGK_TOOTH_ID,1)
            st.playSound("ItemSound.quest_itemget")
      elif npcId == 14 :
        st.set("id","0")
        if st.getInt("cond") and st.getQuestItemsCount(LEATHER_PURSE4_ID) == 1 and st.getQuestItemsCount(FELIM_TOOTH_ID)<3 :
          if st.getQuestItemsCount(RATMAN_FANG_ID)+st.getQuestItemsCount(LANGK_TOOTH_ID)+st.getQuestItemsCount(FELIM_TOOTH_ID)+st.getQuestItemsCount(VUKU_TUSK_ID) >= 11 :
            st.takeItems(VUKU_TUSK_ID,st.getQuestItemsCount(VUKU_TUSK_ID))
            st.takeItems(RATMAN_FANG_ID,st.getQuestItemsCount(RATMAN_FANG_ID))
            st.takeItems(LANGK_TOOTH_ID,st.getQuestItemsCount(LANGK_TOOTH_ID))
            st.takeItems(FELIM_TOOTH_ID,st.getQuestItemsCount(FELIM_TOOTH_ID))
            st.takeItems(LEATHER_PURSE4_ID,1)
            st.giveItems(LEATHER_POUCH4FULL_ID,1)
            st.playSound("ItemSound.quest_middle")
            st.set("cond","12")
          else:
            st.giveItems(FELIM_TOOTH_ID,1)
            st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(415,"415_PathToOrcMonk","Path To Orc Monk")
QUEST.addStartNpc(7587)

QUEST.addTalkId(7501)
QUEST.addTalkId(7587)
QUEST.addTalkId(7590)
QUEST.addTalkId(7591)

QUEST.addKillId(14)
QUEST.addKillId(17)
QUEST.addKillId(24)
QUEST.addKillId(359)
QUEST.addKillId(415)
QUEST.addKillId(478)
QUEST.addKillId(479)