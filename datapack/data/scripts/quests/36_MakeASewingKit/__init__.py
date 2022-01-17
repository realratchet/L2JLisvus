# Made by disKret
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

REINFORCED_STEEL = 7163
ARTISANS_FRAME = 1891
ORIHARUKON = 1893
SEWING_KIT = 7078

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [REINFORCED_STEEL]

 def onEvent (self,event,st) :
   htmltext = event
   if event == "7847-1.htm" :
     st.set("cond","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   if event == "7847-3.htm" :
     st.takeItems(REINFORCED_STEEL,5)
     st.set("cond","3")
   return htmltext

 def onTalk (self,npc,st) :
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   cond = st.getInt("cond")
   if cond == 0 and st.getQuestItemsCount(SEWING_KIT) == 0 :
     fwear=st.getPlayer().getQuestState("37_PleaseMakeMeFormalWear")
     if fwear:
         if fwear.get("cond") == "6" :
           htmltext = "7847-0.htm"
         else:
           st.exitQuest(1)
     else:
       st.exitQuest(1)
   elif st.getQuestItemsCount(REINFORCED_STEEL) == 5 :
     htmltext = "7847-2.htm"
   elif cond == 3 and st.getQuestItemsCount(ORIHARUKON) >= 10 and st.getQuestItemsCount(ARTISANS_FRAME) >= 10 :
     st.takeItems(ORIHARUKON,10)
     st.takeItems(ARTISANS_FRAME,10)
     st.giveItems(SEWING_KIT,1)
     st.playSound("ItemSound.quest_finish")
     htmltext = "7847-4.htm"
     st.exitQuest(1)
   return htmltext

 def onKill (self,npc,player,isPet):
   partyMember = self.getRandomPartyMember(player,npc,"1")
   if not partyMember : return
   st = partyMember.getQuestState("36_MakeASewingKit")
   if st :
      count = st.getQuestItemsCount(REINFORCED_STEEL)
      if count < 5 :
        st.giveItems(REINFORCED_STEEL,1)
        if count == 4 :
          st.playSound("ItemSound.quest_middle")
          st.set("cond","2")
        else:
          st.playSound("ItemSound.quest_itemget")
   return

QUEST = Quest(36,"36_MakeASewingKit","Make A Sewing Kit")
QUEST.addStartNpc(7847)

QUEST.addTalkId(7847)
QUEST.addKillId(566)