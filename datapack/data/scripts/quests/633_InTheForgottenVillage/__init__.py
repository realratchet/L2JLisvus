### ---------------------------------------------------------------------------
###  Create by Skeleton!!! (adapted for L2JLisvus by roko91)
### ---------------------------------------------------------------------------

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "633_InTheForgottenVillage"

#NPC
MINA = 8388
#ITEMS
RIB_BONE = 7544
Z_LIVER = 7545
# Mobid : DROP CHANCES
DAMOBS = {
    1557 : 328,#Bone Snatcher
    1558 : 328,#Bone Snatcher
    1559 : 337,#Bone Maker
    1560 : 337,#Bone Shaper
    1563 : 342,#Bone Collector
    1564 : 348,#Skull Collector
    1565 : 351,#Bone Animator
    1566 : 359,#Skull Animator
    1567 : 359,#Bone Slayer
    1572 : 365,#Bone Sweeper
    1574 : 383,#Bone Grinder
    1575 : 383,#Bone Grinder
    1580 : 385,#Bone Caster
    1581 : 395,#Bone Puppeteer
    1583 : 397,#Bone Scavenger
    1584 : 401 #Bone Scavenger
    }
UNDEADS = {
    1553 : 347,#Trampled Man
    1554 : 347,#Trampled Man
    1561 : 450,#Sacrificed Man
    1578 : 501,#Behemoth Zombie
    1596 : 359,#Requiem Lord
    1597 : 370,#Requiem Behemoth
    1598 : 441,#Requiem Behemoth
    1599 : 395,#Requiem Priest
    1600 : 408,#Requiem Behemoth
    1601 : 411 #Requiem Behemoth
    }

class Quest (JQuest):

    def __init__(self,id,name,descr):
        JQuest.__init__(self,id,name,descr)
        self.questItemIds = [RIB_BONE, Z_LIVER]

    def onEvent (self,event,st):
        htmltext = event
        if event == "accept" :
            st.set("cond","1")
            st.setState(State.STARTED)
            st.playSound("ItemSound.quest_accept")
            htmltext = "8388-04.htm"
        if event == "quit":
            st.takeItems(RIB_BONE, -1)
            st.playSound("ItemSound.quest_finish")
            htmltext = "8388-10.htm"
            st.exitQuest(1)
        elif event == "stay":
            htmltext = "8388-07.htm"
        elif event == "reward":
            if st.getInt("cond") == 2:
                if st.getQuestItemsCount(RIB_BONE) >= 200:
                    st.takeItems(RIB_BONE, 200)
                    st.giveItems(57, 25000)
                    st.addExpAndSp(305235, 0)
                    st.playSound("ItemSound.quest_finish")
                    st.set("cond","1")
                    htmltext = "8388-08.htm"
                else :
                    htmltext = "8388-09.htm"
        return htmltext

    def onTalk (self,npc,st):
        htmltext = JQuest.getNoQuestMsg()
        npcId = npc.getNpcId()
        if npcId == MINA:
            id = st.getState()
            cond = st.getInt("cond")
            if id == State.CREATED:
                if st.getPlayer().getLevel() > 64:
                    htmltext = "8388-01.htm"
                else:
                    htmltext = "8388-03.htm"
                    st.exitQuest(1)
            elif cond == 1:
                htmltext = "8388-06.htm"
            elif cond == 2:
                htmltext = "8388-05.htm"
        return htmltext

    def onKill(self,npc,player,isPet):
        npcId = npc.getNpcId()
        if npcId in UNDEADS.keys():
            partyMember = self.getRandomPartyMemberState(player, npc, State.STARTED)
            if not partyMember: return
            st = partyMember.getQuestState(qn)
            if not st : return
            if st.getRandom(1000) < UNDEADS[npcId]:
                st.giveItems(Z_LIVER, 1)
                st.playSound("ItemSound.quest_itemget")
        elif npcId in DAMOBS.keys():
            partyMember = self.getRandomPartyMember(player, npc, "cond", "1")
            if not partyMember: return
            st = partyMember.getQuestState(qn)
            if not st : return
            if st.getRandom(1000) < DAMOBS[npcId]:
                st.giveItems(RIB_BONE, 1)
                if st.getQuestItemsCount(RIB_BONE) == 200:
                    st.set("cond","2")
                    st.playSound("ItemSound.quest_middle")
                else:
                    st.playSound("ItemSound.quest_itemget")
        return

QUEST = Quest(633, qn, "In The Forgotten Village")
QUEST.addStartNpc(MINA)

QUEST.addTalkId(MINA)

for i in DAMOBS.keys():
    QUEST.addKillId(i)
for i in UNDEADS.keys():
    QUEST.addKillId(i)