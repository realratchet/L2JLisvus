# Kail's Magic Coin ver. 0.1 by DrLecter (adapted for L2JLisvus by roko91)

import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "382_KailsMagicCoin"

#Messages
default = JQuest.getNoQuestMsg()
#Quest items
ROYAL_MEMBERSHIP = 5898
#NPCs
VERGARA = 7687
#MOBs and CHANCES
MOBS={1017:[5961],1019:[5962],1020:[5963],1022:[5961,5962,5963]}
CHANCE = 10
MAX = 100

class Quest (JQuest) :

  def __init__(self,id,name,descr):
      JQuest.__init__(self,id,name,descr)
      self.questItemIds = range(5961,5964)

  def onEvent (self,event,st) :
      htmltext = event
      if event == "7687-03.htm":
         if st.getPlayer().getLevel() >= 55 and st.getQuestItemsCount(ROYAL_MEMBERSHIP) :
            st.set("cond","1")
            st.setState(State.STARTED)
            st.playSound("ItemSound.quest_accept")
         else :
            htmltext = "7687-01.htm"
            st.exitQuest(1)
      return htmltext

  def onTalk (self,npc,st):
      htmltext = default
      npcId = npc.getNpcId()
      id = st.getState()
      cond=st.getInt("cond")
      if st.getQuestItemsCount(ROYAL_MEMBERSHIP) == 0 or st.getPlayer().getLevel() < 55 :
         htmltext = "7687-01.htm"
         st.exitQuest(1)
      else :
         if cond == 0 :
            htmltext = "7687-02.htm"
         else :
            htmltext = "7687-04.htm"
      return htmltext

  def onKill(self,npc,player,isPet):
      st = player.getQuestState(qn)
      if not st : return 
      if st.getState() != State.STARTED : return 
      numItems,chance = divmod(CHANCE*Config.RATE_DROP_QUEST,MAX)
      if st.getQuestItemsCount(ROYAL_MEMBERSHIP) :
         if st.getRandom(MAX) < chance :
            numItems = numItems + 1
         npcId = npc.getNpcId()
         if numItems != 0 :
            st.giveItems(MOBS[npcId][st.getRandom(len(MOBS[npcId]))],int(numItems))
            st.playSound("ItemSound.quest_itemget")
      return

QUEST = Quest(382, qn, "Kail's Magic Coin")
QUEST.addStartNpc(VERGARA)

QUEST.addTalkId(VERGARA)

for npc in MOBS.keys():
    QUEST.addKillId(npc)