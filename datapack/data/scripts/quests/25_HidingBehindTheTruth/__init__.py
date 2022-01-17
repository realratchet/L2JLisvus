# Made by Kerberos v1.0 on 2009/05/10 (adapted for L2JLisvus by roko91)
# this script is part of the Official L2J Datapack Project.
# Visit http://www.l2jdp.com/forum for more details.

import sys
import time

from net.sf.l2j.gameserver.ai import CtrlIntention
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.network.serverpackets import NpcSay

qn = "25_HidingBehindTheTruth"

# Npcs
Agripel = 8348
Benedict = 8349
Wizard = 8522
Tombstone = 8531
Lidia = 8532
Bookshelf = 8533
Bookshelf2 = 8534
Bookshelf3 = 8535
Coffin = 8536
Triol = 5218

# Items
Contract = 7066
Dress = 7155
SuspiciousTotem = 7156
GemstoneKey = 7157
TotemDoll = 7158

class Quest (JQuest) :
    def __init__(self,id,name,descr):
        JQuest.__init__(self,id,name,descr)
        self.questItemIds = [SuspiciousTotem,GemstoneKey,TotemDoll,Dress]

    def onAdvEvent (self,event,npc, player) :
        st = player.getQuestState(qn)
        if not st: return
        htmltext = event
        if event == "8349-02.htm" :
            st.playSound("ItemSound.quest_accept")
            st.set("cond","1")
            st.setState(State.STARTED)
        elif event == "8349-03.htm" :
            if st.getQuestItemsCount(SuspiciousTotem) :
                htmltext = "8349-05.htm"
            else :
                st.playSound("ItemSound.quest_middle")
                st.set("cond","2")
        elif event == "8349-10.htm" :
            st.playSound("ItemSound.quest_middle")
            st.set("cond","4")
        elif event == "8348-02.htm" :
            st.takeItems(SuspiciousTotem,-1)
        elif event == "8348-07.htm" :
            st.playSound("ItemSound.quest_middle")
            st.set("cond","5")
            st.giveItems(GemstoneKey,1)
        elif event == "8522-04.htm" :
            st.playSound("ItemSound.quest_middle")
            st.set("cond","6")
        elif event == "8535-03.htm" :
            if st.getInt("step") == 0:
               st.set("step","1")
               triol = st.addSpawn(Triol,59712,-47568,-2712,300000)
               time.sleep(1)
               triol.broadcastPacket(NpcSay(triol.getObjectId(), 0, triol.getNpcId(), "That box was sealed by my master. Don't touch it!"))
               triol.setRunning()
               triol.addDamageHate(player,0,999)
               triol.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player)
               st.playSound("ItemSound.quest_middle")
               st.set("cond","7")
            elif st.getInt("step") == 2:
                htmltext = "8535-04.htm"
        elif event == "8535-05.htm" :
            st.giveItems(Contract,1)
            st.takeItems(GemstoneKey,-1)
            st.playSound("ItemSound.quest_middle")
            st.set("cond","9")
        elif event == "8532-02.htm" :
            st.takeItems(Contract,-1)
        elif event == "8532-06.htm" :
            st.playSound("ItemSound.quest_middle")
            st.set("cond","11")
        elif event == "8531-02.htm" :
            st.playSound("ItemSound.quest_middle")
            st.set("cond","12")
            st.addSpawn(Coffin,60104,-35820,-664,20000)
        elif event == "8532-18.htm" :
            st.playSound("ItemSound.quest_middle")
            st.set("cond","15")
        elif event == "8522-12.htm" :
            st.playSound("ItemSound.quest_middle")
            st.set("cond","16")
        elif event == "8348-10.htm" :
            st.takeItems(TotemDoll,-1)
        elif event == "8348-15.htm" :
            st.playSound("ItemSound.quest_middle")
            st.set("cond","17")
        elif event == "8348-16.htm" :
            st.playSound("ItemSound.quest_middle")
            st.set("cond","18")
        elif event == "8532-20.htm" :
            st.giveItems(905,2)
            st.giveItems(874,1)
            st.takeItems(7063,-1)
            st.addExpAndSp(572277,53750)
            st.unset("cond")
            st.setState(State.COMPLETED)
            st.playSound("ItemSound.quest_finish")
        elif event == "8522-15.htm" :
            st.giveItems(936,1)
            st.giveItems(874,1)
            st.takeItems(7063,-1)
            st.addExpAndSp(572277,53750)
            st.unset("cond")
            st.setState(State.COMPLETED)
            st.playSound("ItemSound.quest_finish")
        return htmltext

    def onTalk (self,npc,st):
        htmltext = "<html><body>You are either not on a quest that involves this NPC, or you don't meet this NPC's minimum quest requirements.</body></html>"
        npcId = npc.getNpcId()
        id = st.getState()
        cond = st.getInt("cond")
        if id == State.COMPLETED:
            htmltext = JQuest.getAlreadyCompletedMsg()
        elif id == State.CREATED:
            if npcId == Benedict:
                st2 = st.getPlayer().getQuestState("24_InhabitantsOfTheForrestOfTheDead")
                if not st2 == None :
                    if st2.getState().getName() == 'Completed' and st.getPlayer().getLevel() >= 66 :
                        htmltext = "8349-01.htm"
                    else :
                        htmltext = "8349-00.htm"
        elif id == State.STARTED:
            if npcId == Benedict:
                if cond == 1 :
                    htmltext = "8349-02.htm"
                elif cond in [2,3] :
                    htmltext = "8349-04.htm"
                elif cond == 4 :
                    htmltext = "8349-10.htm"
            elif npcId == Wizard:
                if cond == 2 :
                    htmltext = "8522-01.htm"
                    st.playSound("ItemSound.quest_middle")
                    st.set("cond","3")
                    st.giveItems(SuspiciousTotem,1)
                elif cond == 3 :
                    htmltext = "8522-02.htm"
                elif cond == 5 :
                    htmltext = "8522-03.htm"
                elif cond == 6 :
                    htmltext = "8522-04.htm"
                elif cond == 9 :
                    htmltext = "8522-05.htm"
                    st.playSound("ItemSound.quest_middle")
                    st.set("cond","10")
                elif cond == 10 :
                    htmltext = "8522-05.htm"
                elif cond == 15 :
                    htmltext = "8522-06.htm"
                elif cond == 16 :
                    htmltext = "8522-13.htm"
                elif cond == 17 :
                    htmltext = "8522-16.htm"
                elif cond == 18 :
                    htmltext = "8522-14.htm"
            elif npcId == Agripel:
                if cond == 4 :
                    htmltext = "8348-01.htm"
                elif cond == 5 :
                    htmltext = "8348-08.htm"
                elif cond == 16 :
                    htmltext = "8348-09.htm"
                elif cond == 17 :
                    htmltext = "8348-17.htm"
                elif cond == 18 :
                    htmltext = "8348-18.htm"
            elif npcId == Bookshelf:
                if cond == 6 :
                    htmltext = "8533-01.htm"
            elif npcId == Bookshelf2:
                if cond == 6 :
                    htmltext = "8534-01.htm"
            elif npcId == Bookshelf3:
                if cond in [6,7,8] :
                    htmltext = "8535-01.htm"
                elif cond == 9 :
                    htmltext = "8535-06.htm"
            elif npcId == Lidia:
                if cond == 10 :
                    htmltext = "8532-01.htm"
                elif cond in [11,12] :
                    htmltext = "8532-06.htm"
                elif cond == 13 :
                    htmltext = "8532-07.htm"
                    st.set("cond","14")
                    st.takeItems(Dress,-1)
                elif cond == 14 :
                    htmltext = "8532-08.htm"
                elif cond == 15 :
                    htmltext = "8532-18.htm"
                elif cond == 17 :
                    htmltext = "8532-19.htm"
                elif cond == 18 :
                    htmltext = "8532-21.htm"
            elif npcId == Tombstone:
                if cond in [11,12] :
                    htmltext = "8531-01.htm"
                elif cond == 13 :
                    htmltext = "8531-03.htm"
            elif npcId == Coffin:
                if cond == 12 :
                    htmltext = "8536-01.htm"
                    st.giveItems(Dress,1)
                    st.playSound("ItemSound.quest_middle")
                    st.set("cond","13")
                    npc.deleteMe()
        return htmltext

    def onKill(self,npc,player,isPet):
        st = player.getQuestState(qn)
        if not st : return
        if st.getState() != State.STARTED : return
        if st.getInt("cond") == 7:
            st.playSound("ItemSound.quest_itemget")
            st.set("cond","8")
            npc.broadcastPacket(NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "You've ended my immortal life! You've protected by the feudal lord, aren't you?"))
            st.giveItems(TotemDoll,1)
            st.set("step","2")
        return

QUEST = Quest(25,qn,"Hiding Behind The Truth")
QUEST.addStartNpc(Benedict)

QUEST.addTalkId(Agripel)
QUEST.addTalkId(Benedict)
QUEST.addTalkId(Bookshelf)
QUEST.addTalkId(Bookshelf2)
QUEST.addTalkId(Bookshelf3)
QUEST.addTalkId(Wizard)
QUEST.addTalkId(Lidia)
QUEST.addTalkId(Tombstone)
QUEST.addTalkId(Coffin)
QUEST.addKillId(Triol)