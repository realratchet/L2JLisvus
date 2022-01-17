# Made by Renji v0.1 (adapted for L2JLisvus by roko91)

import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "631_DeliciousTopChoiceMeat"

#NPC
TUNATUN = 8537

#ITEMS
TOP_QUALITY_MEAT = 7546

#REWARDS
MOLD_GLUE,MOLD_LUBRICANT,MOLD_HARDENER,ENRIA,ASOFE,THONS = 4039,4040,4041,4042,4043,4044
REWARDS={"1":[MOLD_GLUE,15],"2":[ASOFE,15],"3":[THONS,15],"4":[MOLD_LUBRICANT,10],"5":[ENRIA,10],"6":[MOLD_HARDENER,5]}


class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [TOP_QUALITY_MEAT]

 def onEvent (self,event,st) :
   htmltext = event
   if event == "8537-03.htm" :
     st.set("cond","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
   elif event == "8537-05.htm" and st.getQuestItemsCount(TOP_QUALITY_MEAT) == 120 :
     st.set("cond","3")
   elif event in REWARDS.keys() :
     htmltext = "8537-07.htm"
     item,qty=REWARDS[event]
     if st.getQuestItemsCount(TOP_QUALITY_MEAT) == 120 and st.getInt("cond") == 3:
       htmltext = "8537-06.htm"
       st.takeItems(TOP_QUALITY_MEAT,120)
       st.giveItems(item,int(qty*Config.RATE_QUEST_REWARD))
       st.playSound("ItemSound.quest_finish")
       st.exitQuest(1)
   return htmltext

 def onTalk (self,npc,st):
   if st :
        npcId = npc.getNpcId()
        htmltext = JQuest.getNoQuestMsg()
        id = st.getState()
        cond = st.getInt("cond")
        if cond == 0 :
            if st.getPlayer().getLevel() >= 65 :
                htmltext = "8537-01.htm"
            else:
                htmltext = "8537-02.htm"
                st.exitQuest(1)
        elif id == State.STARTED :
            if cond == 1 :
                htmltext = "8537-01a.htm"
            elif cond == 2 and st.getQuestItemsCount(TOP_QUALITY_MEAT) == 120 :
                htmltext = "8537-04.htm"
            elif cond == 3 :
                htmltext = "8537-05.htm"
   return htmltext

 def onKill(self,npc,player,isPet):
   partyMember = self.getRandomPartyMember(player, npc, "1")
   if not partyMember: return
   st = partyMember.getQuestState(qn)
   if st :
      if st.getState() == State.STARTED :
         count = st.getQuestItemsCount(TOP_QUALITY_MEAT)
         if st.getInt("cond") == 1 and count < 120 :
            chance = 100 * Config.RATE_DROP_QUEST
            numItems, chance = divmod(chance,100)
            if st.getRandom(100) < chance : 
               numItems += 1
            if numItems :
               if count + numItems >= 120 :
                  numItems = 120 - count
                  st.playSound("ItemSound.quest_middle")
                  st.set("cond","2")
               else:
                  st.playSound("ItemSound.quest_itemget")
               st.giveItems(TOP_QUALITY_MEAT,int(numItems))
   return

QUEST = Quest(631,qn,"Delicious Top Choice Meat")
QUEST.addStartNpc(TUNATUN)

QUEST.addTalkId(TUNATUN)

for npcId in range(1460,1468)+ range(1479,1487)+range(1498,1506) :
    QUEST.addKillId(npcId)