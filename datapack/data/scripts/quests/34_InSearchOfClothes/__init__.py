# Made by disKret
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

SPINNERET = 7528
SUEDE = 1866
THREAD = 1868
SPIDERSILK = 1493
MYSTERIOUS_CLOTH = 7076

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [SPINNERET,SPIDERSILK]

 def onEvent (self,event,st) :
   htmltext = event
   if event == "7088-1.htm" :
     st.set("cond","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   if event == "7294-1.htm" :
     st.set("cond","2")
   if event == "7088-3.htm" :
     st.set("cond","3")
   if event == "7165-1.htm" :
     st.set("cond","4")
   if event == "7165-3.htm" :
     if st.getQuestItemsCount(SPINNERET) == 10 :
       st.takeItems(SPINNERET,10)
       st.giveItems(SPIDERSILK,1)
       st.set("cond","6")
     else :
       htmltext = "You don't have enough materials."
   if event == "7088-5.htm" :
     if st.getQuestItemsCount(SUEDE) >= 3000 and st.getQuestItemsCount(THREAD) >= 5000 and st.getQuestItemsCount(SPIDERSILK) == 1 :
       st.takeItems(SUEDE,3000)
       st.takeItems(THREAD,5000)
       st.takeItems(SPIDERSILK,1)
       st.giveItems(MYSTERIOUS_CLOTH,1)
       st.playSound("ItemSound.quest_finish")
       st.exitQuest(1)
     else :
       htmltext = "You don't have enough materials."
   return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   npcId = npc.getNpcId()
   id = st.getState()
   if id == State.CREATED :
     st.set("cond","0")
   cond = st.getInt("cond")
   if npcId == 7088 and cond == 0 and st.getQuestItemsCount(MYSTERIOUS_CLOTH) == 0 :
     fwear=st.getPlayer().getQuestState("37_PleaseMakeMeFormalWear")
     if fwear :
       if fwear.get("cond") == "6" :
         htmltext = "7088-0.htm"
       else :
         st.exitQuest(1)
     else :
       st.exitQuest(1)
   elif npcId == 7294 and cond == 1 :
     htmltext = "7294-0.htm"
   elif npcId == 7088 and cond == 2 :
     htmltext = "7088-2.htm"
   elif npcId == 7165 and cond == 3 :
     htmltext = "7165-0.htm"
   elif npcId == 7165 and cond == 5 :
     htmltext = "7165-2.htm"
   elif npcId == 7088 and cond == 6 :
      htmltext = "7088-4.htm"
   else : htmltext = JQuest.getNoQuestMsg()
   return htmltext

 def onKill (self,npc,player,isPet):
   # get 1 party member among those with cond between 1 and 4
   partyMember = 0
   j = 0
   for i in range(1,5) :  # i between 1 and 4 inclusive
       partyMember = self.getRandomPartyMember(player,npc,str(i))
       if partyMember :
           j = i
           break
   if not partyMember : return

   # if at least 1 cond exists with a party member, check if there also exist in a different cond as well
   for i in range(j+1,5) :
       partyMember2 = self.getRandomPartyMember(player,npc,str(i))
       # if a party member is found in another cond, randomly choose between
       # the new one and the previous one
       if partyMember2 :
           if Rnd.get(2) : partyMember = partyMember2

   st = partyMember.getQuestState("34_InSearchOfClothes")
   if st:
      if st.getState() != State.STARTED :
        return
      count = st.getQuestItemsCount(SPINNERET)
      if count < 10 :
        st.giveItems(SPINNERET,1)
        if count == 9 :
          st.playSound("ItemSound.quest_middle")
          st.set("cond","5")
        else :
          st.playSound("ItemSound.quest_itemget")	
   return

QUEST = Quest(34,"34_InSearchOfClothes","In Search of Clothes")
QUEST.addStartNpc(7088)

QUEST.addTalkId(7088)
QUEST.addTalkId(7165)
QUEST.addTalkId(7294)
QUEST.addKillId(560)