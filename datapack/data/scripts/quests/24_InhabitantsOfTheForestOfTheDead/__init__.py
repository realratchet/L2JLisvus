#  Created by Kerberos (adapted for L2JLisvus by roko91)

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.network.serverpackets import NpcSay

qn = "24_InhabitantsOfTheForestOfTheDead"

# Npcs
Dorian = 8389
Wizard = 8522
Tombstone = 8531
MaidOfLidia = 8532

#Items
Letter = 7065
Hairpin = 7148
Totem = 7151
Flower = 7152
SilverCross = 7153
BrokenSilverCross = 7154
SuspiciousTotem = 7156

def AutoChat(npc,text) :
    chars = npc.getKnownList().getKnownPlayers().values()
    if chars != None:
        for pc in chars :
            sm = NpcSay(npc.getObjectId(), 0, npc.getNpcId(), text)
            pc.sendPacket(sm)
    return

class Quest (JQuest) : 

    def __init__(self,id,name,descr):  
        JQuest.__init__(self,id,name,descr)
        self.questItemIds = [Flower,SilverCross,BrokenSilverCross,Letter,Hairpin,Totem]

    def onAdvEvent (self,event,npc,player) :
        st = player.getQuestState(qn)
        if not st: return
        htmltext = event
        if event == "8389-02.htm":
            st.giveItems(Flower,1)
            st.set("cond","1")
            st.playSound("ItemSound.quest_accept")
            st.setState(State.STARTED)
        elif event == "8389-11.htm":
            st.set("cond","3")
            st.playSound("ItemSound.quest_middle")
            st.giveItems(SilverCross,1)
        elif event == "8389-16.htm":
            st.playSound("InterfaceSound.charstat_open_01")
        elif event == "8389-17.htm":
            st.takeItems(BrokenSilverCross,-1)
            st.giveItems(Hairpin,1)
            st.set("cond","5")
        elif event == "8522-03.htm":
            st.takeItems(Totem,-1)
        elif event == "8522-07.htm":
            st.set("cond","11")
        elif event == "8522-19.htm":
            st.giveItems(SuspiciousTotem,1)
            st.addExpAndSp(242105,22529)
            st.unset("cond")
            st.setState(State.COMPLETED)
            st.playSound("ItemSound.quest_finish")
        elif event == "8531-02.htm":
            st.playSound("ItemSound.quest_middle")
            st.set("cond","2")
            st.takeItems(Flower,-1)
        elif event == "8532-04.htm":
            st.playSound("ItemSound.quest_middle")
            st.giveItems(Letter,1)
            st.set("cond","6")
        elif event == "8532-06.htm":
            st.takeItems(Hairpin,-1)
            st.takeItems(Letter,-1)
        elif event == "8532-16.htm":
            st.playSound("ItemSound.quest_middle")
            st.set("cond","9")
        return htmltext

    def onTalk (self,npc,st):
        htmltext = "<html><body>You are either not on a quest that involves this NPC, or you don't meet this NPC's minimum quest requirements.</body></html>"
        npcId = npc.getNpcId()
        state = st.getState()
        if state == State.COMPLETED :
            if npcId == Wizard :
                htmltext = "8522-20.htm"
            else:
                htmltext = JQuest.getAlreadyCompletedMsg()
        cond = st.getInt("cond")
        if npcId == Dorian :
            if state == State.CREATED :
                st2 = st.getPlayer().getQuestState("23_LidiasHeart")
                if not st2 == None :
                    if st2.getState().getName() == 'Completed' and st.getPlayer().getLevel() >= 65 :
                        htmltext = "8389-01.htm"
                    else:
                        htmltext = "8389-00.htm"
                else:
                    htmltext = "8389-00.htm"
            elif cond == 1 :
                htmltext = "8389-03.htm"
            elif cond == 2 :
                htmltext = "8389-04.htm"
            elif cond == 3 :
                htmltext = "8389-12.htm"
            elif cond == 4 :
                htmltext = "8389-13.htm"
            elif cond == 5 :
                htmltext = "8389-18.htm"
        elif npcId == Tombstone :
            if cond == 1 :
                st.playSound("AmdSound.d_wind_loot_02")
                htmltext = "8531-01.htm"
            elif cond == 2 :
                htmltext = "8531-03.htm"
        elif npcId == MaidOfLidia :
            if cond == 5 :
                htmltext = "8532-01.htm"
            elif cond == 6 :
                if st.getQuestItemsCount(Letter) and st.getQuestItemsCount(Hairpin) :
                    htmltext = "8532-05.htm"
                else:
                    htmltext = "8532-07.htm"
            elif cond == 9 :
                htmltext = "8532-16.htm"
        elif npcId == Wizard :
            if cond == 10 :
                htmltext = "8522-01.htm"
            elif cond == 11 :
                htmltext = "8522-08.htm"
        return htmltext

    def onKill(self,npc,player,isPet):
        st = player.getQuestState(qn)
        if not st : return 
        if st.getState() != State.STARTED : return 
        npcId = npc.getNpcId()
        if not st.getQuestItemsCount(Totem) and st.getInt("cond") == 9:
            if npcId in [1557,1558,1560,1563,1564,1565,1566,1567] and st.getRandom(100) <=30:
                st.giveItems(Totem,1)
                st.set("cond","10")
                st.playSound("ItemSound.quest_middle")
        return

    def onCreatureSee(self, npc, player, isPet) : 
        if npc.getNpcId() == 10332:
            st = player.getQuestState(qn) 
            if st and st.getQuestItemsCount(SilverCross) :
                st.takeItems(SilverCross,-1)
                st.giveItems(BrokenSilverCross,1)
                st.set("cond","4")
                AutoChat(npc,"That sign!")
        return

QUEST = Quest(24, qn, "Inhabitants Of The Forest Of The Dead")
QUEST.addStartNpc(Dorian)

QUEST.addTalkId(Dorian)
QUEST.addTalkId(Tombstone)
QUEST.addTalkId(MaidOfLidia)
QUEST.addTalkId(Wizard)
QUEST.addCreatureSeeId(10332)

for mob in [1557,1558,1560,1563,1564,1565,1566,1567]:
    QUEST.addKillId(mob)