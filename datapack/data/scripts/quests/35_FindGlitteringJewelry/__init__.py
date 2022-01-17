# Made by disKret
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

ROUGH_JEWEL = 7162
ORIHARUKON = 1893
SILVER_NUGGET = 1873
THONS = 4044
JEWEL_BOX = 7077

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [ROUGH_JEWEL]

 def onEvent (self,event,st) :
   htmltext = event
   if event == "7091-1.htm" :
     st.set("cond","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   if event == "7879-1.htm" :
     st.set("cond","2")
   if event == "7091-3.htm" :
     st.takeItems(ROUGH_JEWEL,10)
     st.set("cond","4")
   if event == "7091-5.htm" :
     if st.getQuestItemsCount(ORIHARUKON) >= 5 and st.getQuestItemsCount(SILVER_NUGGET) >= 500 and st.getQuestItemsCount(THONS) >= 150 :
       st.takeItems(ORIHARUKON,5)
       st.takeItems(SILVER_NUGGET,500)
       st.takeItems(THONS,150)
       st.giveItems(JEWEL_BOX,1)
       st.playSound("ItemSound.quest_finish")
       st.exitQuest(1)
     else :
       htmltext = "You don't have enough materials."
   return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   if npcId == 7091 and st.getInt("cond") == 0 and st.getQuestItemsCount(JEWEL_BOX) == 0 :
     fwear=st.getPlayer().getQuestState("37_PleaseMakeMeFormalWear")
     if not fwear is None :
       if fwear.get("cond") == "6" :
         htmltext = "7091-0.htm"
         return htmltext
       else:
         htmltext = JQuest.getNoQuestMsg()
         st.exitQuest(1)
     else:
       htmltext = JQuest.getNoQuestMsg()
       st.exitQuest(1)
   elif npcId == 7879 and st.getInt("cond") == 1 :
     htmltext = "7879-0.htm"
   elif npcId == 7091 and st.getQuestItemsCount(ROUGH_JEWEL) == 10 :
     htmltext = "7091-2.htm"
   elif npcId == 7091 and st.getInt("cond") == 4 and st.getQuestItemsCount(ORIHARUKON) >= 5 and st.getQuestItemsCount(SILVER_NUGGET) >= 500 and st.getQuestItemsCount(THONS) >= 150 :
     htmltext = "7091-4.htm"
   else :
     htmltext = JQuest.getNoQuestMsg()
   return htmltext

 def onKill (self,npc,player,isPet):
   partyMember1 = self.getRandomPartyMember(player,npc,"1")
   partyMember2 = self.getRandomPartyMember(player,npc,"2")
   partyMember = partyMember1 # initialize
   if not partyMember1 and not partyMember2: return
   elif not partyMember2 : partyMember = partyMember1
   elif not partyMember1 : partyMember = partyMember2
   else :
      if Rnd.get(2):
        partyMember = partyMember2
   if not partyMember : return
   st = partyMember.getQuestState("35_FindGlitteringJewelry")
   if st :
      count = st.getQuestItemsCount(ROUGH_JEWEL)
      if count<10 :
        st.giveItems(ROUGH_JEWEL,1)
        if count == 9 :
          st.playSound("ItemSound.quest_middle")
          st.set("cond","3")
        else:
          st.playSound("ItemSound.quest_itemget")	
   return

QUEST = Quest(35,"35_FindGlitteringJewelry","Find Glittering Jewelry")
QUEST.addStartNpc(7091)

QUEST.addTalkId(7091)
QUEST.addTalkId(7879)
QUEST.addKillId(135)