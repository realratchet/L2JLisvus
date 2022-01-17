# Made by Next - cleanup by Kerberos (adapted for L2JLisvus by roko91)
# this script is part of the Official L2J Datapack Project.
# Visit http://forum.l2jdp.com for more details.

import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "632_NecromancersRequest"

#NPC
WIZARD = 8522
#ITEMS
V_HEART = 7542
Z_BRAIN = 7543
#DROP CHANCES
V_HEART_CHANCE = 50 # in percents
Z_BRAIN_CHANCE = 33 # in percents
#REWARDS
ADENA = 57
ADENA_AMOUNT = 120000
#MOBS
VAMPIRES = [ 1568, 1573, 1582, 1585, 1586, 1587, 1588, 1589, 1590, 1591, 1592, 1593, 1594, 1595 ]
UNDEADS = [ 1547, 1548, 1549, 1551, 1552, 1555, 1556, 1562, 1571, 1576, 1577, 1579 ]

class Quest (JQuest):

    def __init__(self,id,name,descr):
        JQuest.__init__(self,id,name,descr)
        self.questItemIds = [V_HEART, Z_BRAIN]

    def onEvent (self,event,st):
        if event == "0":
           st.playSound("ItemSound.quest_finish")
           htmltext = "8522-3.htm"
           st.exitQuest(1)
        elif event == "1":
           htmltext = "8522-0.htm"
        elif event == "2":
           if st.getInt("cond") == 2:
              if st.getQuestItemsCount(V_HEART) == 200:
                 st.takeItems(V_HEART, 200)
                 st.giveItems(ADENA, ADENA_AMOUNT)
                 st.playSound("ItemSound.quest_finish")
                 st.set("cond","1")
                 htmltext = "8522-1.htm"
        elif event == "start":
           if st.getPlayer().getLevel() > 62 :
              htmltext = "8522-0.htm"
              st.set("cond","1")
              st.setState(State.STARTED)
              st.playSound("ItemSound.quest_accept")
           else:
              htmltext = "<html><body>Mysterious Wizard:<br>This quest can only be taken by characters that have a minimum level of <font color=\"LEVEL\">63</font>. Return when you are more experienced.</body></html>"
              st.exitQuest(1)
        return htmltext

    def onKill (self,npc,player,isPet):
        npcId = npc.getNpcId()
        if npcId in UNDEADS:
           partyMember = self.getRandomPartyMemberState(player, npc, State.STARTED)
           if not partyMember: return
           st = partyMember.getQuestState(qn)
           if not st: return
           chance = Z_BRAIN_CHANCE * Config.RATE_DROP_QUEST
           numItems, chance = divmod(chance,100)
           if st.getRandom(100) < chance:
              numItems += 1
           if numItems :
              st.giveItems(Z_BRAIN,int(numItems))
              st.playSound("ItemSound.quest_itemget")
        elif npcId in VAMPIRES:
           partyMember = self.getRandomPartyMember(player, npc, "cond", "1")
           if not partyMember: return                
           st = partyMember.getQuestState(qn)
           if not st: return
           chance = V_HEART_CHANCE * Config.RATE_DROP_QUEST
           numItems, chance = divmod(chance,100)
           count = st.getQuestItemsCount(V_HEART)
           if st.getRandom(100) < chance:
              numItems += 1
           if numItems :
              if count + numItems >= 200 :
                 numItems = 200 - count
                 st.playSound("ItemSound.quest_middle")
                 st.set("cond","2")
              else:
                 st.playSound("ItemSound.quest_itemget")
              st.giveItems(V_HEART, int(numItems))
        return

    def onTalk (self,npc,st):
        htmltext = JQuest.getNoQuestMsg()
        if st:
           npcId = npc.getNpcId()
           id = st.getState()
           cond = st.getInt("cond")
           if cond == 0 and id == State.CREATED:
              if npcId == WIZARD:
                 htmltext = "8522.htm"
           if cond == 1 and id == State.STARTED:
              htmltext = "8522-1.htm"
           if cond == 2 and id == State.STARTED:
              if st.getQuestItemsCount(V_HEART) == 200:
                 htmltext = "8522-2.htm"
        return htmltext

QUEST = Quest(632, qn, "Necromancer's Request")
QUEST.addStartNpc(WIZARD)

QUEST.addTalkId(WIZARD)

for i in VAMPIRES:
    QUEST.addKillId(i)
for i in UNDEADS:
    QUEST.addKillId(i)