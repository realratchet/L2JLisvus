#  Created by Skeleton (adapted for L2JLisvus by roko91)

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.network.serverpackets import NpcSay

qn = "23_LidiasHeart"

# ~~~~~~ npcId list: ~~~~~~
Innocentin          = 8328
BrokenBookshelf     = 8526
GhostofvonHellmann  = 8524
Tombstone           = 8523
Violet              = 8386
Box                 = 8530
# ~~~~~~~~~~~~~~~~~~~~~~~~~

# ~~~~~ itemId List ~~~~~
MapForestofDeadman = 7063
SilverKey          = 7149
LidiaHairPin       = 7148
LidiaDiary         = 7064
SilverSpear        = 7150
Adena              = 57
# ~~~~~~~~~~~~~~~~~~~~~~~

class Quest (JQuest) : 

    def __init__(self,id,name,descr):
       JQuest.__init__(self,id,name,descr)
       self.questItemIds = [SilverKey,LidiaHairPin,LidiaDiary,SilverSpear]

    def onAdvEvent (self,event,npc,player) :
        st = player.getQuestState(qn)
        htmltext = event
        if event == "8328-02.htm": # call 8328-03.htm
            st.giveItems(MapForestofDeadman,1)
            st.giveItems(SilverKey,1)
            st.set("cond","1")
            st.playSound("ItemSound.quest_accept")
            st.setState(State.STARTED)
        elif event == "8328-03.htm": # call 8328-05.htm and 8328-06.htm
            st.set("cond","2")
            st.playSound("ItemSound.quest_middle")
        elif event == "8526-05.htm": # called by 8526-03.htm for hairpin
            if st.getQuestItemsCount(LidiaHairPin) == 0:
                st.giveItems(LidiaHairPin,1) # give hairpin
                if st.getQuestItemsCount(LidiaDiary) != 0: # if has diary cond = 4
                    st.set("cond","4")
                    st.playSound("ItemSound.quest_middle")
        elif event == "8526-11.htm": # called by 8526-07 for diary
            if st.getQuestItemsCount(LidiaDiary) == 0:
                st.giveItems(LidiaDiary,1)
                if st.getQuestItemsCount(LidiaHairPin) != 0: # if has hairpin cond = 4
                    st.set("cond","4")
                    st.playSound("ItemSound.quest_middle")
        elif event == "8328-19.htm": # end of questions loop go to ghost
            st.set("cond","6")
            st.playSound("ItemSound.quest_middle")
        elif event == "8524-04.htm":# sends you to the tombstone to dig
            st.set("cond","7")
            st.playSound("ItemSound.quest_middle")
            st.takeItems(LidiaDiary,-1)
        elif event == "8523-02.htm":
            st.playSound("SkillSound5.horror_02")
            ghost = st.addSpawn(8524,51432,-54570,-3136,1800000)
            ghost.broadcastPacket(NpcSay(ghost.getObjectId(), 0, ghost.getNpcId(),"Who awoke me?"))
        elif event == "8523-05.htm":
            st.startQuestTimer("ghost_timer",10000)
        elif event == "ghost_timer":
            st.set("cond","8")
            htmltext = "8523-06.htm"
            st.giveItems(SilverKey,1)
        elif event == "8530-02.htm":# box gives spear takes key
            st.set("cond","10")
            st.playSound("ItemSound.quest_middle")
            st.takeItems(SilverKey,-1)
            st.giveItems(SilverSpear,1)
        return htmltext

    def onTalk (self,npc,st):
        htmltext = "<html><body>You are either not on a quest that involves this NPC, or you don't meet this NPC's minimum quest requirements.</body></html>"
        state = st.getState()
        if state == State.COMPLETED :
            htmltext = JQuest.getAlreadyCompletedMsg()
        npcId = npc.getNpcId()
        cond = st.getInt("cond")
        if npcId == Innocentin :
            if state == State.CREATED :
                st2 = st.getPlayer().getQuestState("22_TragedyInVonHellmannForest")
                if not st2 == None :
                    if st2.getState().getName() == 'Completed' and st.getPlayer().getLevel() >= 64 :
                        htmltext = "8328-01.htm" # previous quest finished, call 8328-02.htm
                    else:
                        htmltext = "8328-00.htm" # requirements not met
            elif cond == 1 :
                htmltext = "8328-03.htm"
            elif cond == 2 :
                htmltext = "8328-07.htm"
            elif cond == 4 :
                htmltext = "8328-08.htm"
            elif cond == 6 :
                htmltext = "8328-19.htm"
        elif npcId == BrokenBookshelf:
            if cond == 2 : 
                if st.getQuestItemsCount(SilverKey) != 0:
                    htmltext = "8526-00.htm"
                    st.playSound("ItemSound.quest_middle")
                    st.set("cond","3")
            elif cond == 3 :
                if st.getQuestItemsCount(SilverKey) != 0:
                    htmltext = "8526-00.htm"
                    st.playSound("ItemSound.quest_middle")
                    st.set("cond","3")
                elif st.getQuestItemsCount(LidiaHairPin) == 0 and st.getQuestItemsCount(LidiaDiary) != 0:
                    htmltext = "8526-12.htm"
                elif st.getQuestItemsCount(LidiaHairPin) != 0 and st.getQuestItemsCount(LidiaDiary) == 0:
                    htmltext = "8526-06.htm"
                elif st.getQuestItemsCount(LidiaHairPin) == 0 and st.getQuestItemsCount(LidiaDiary) == 0:
                    htmltext = "8526-02.htm"
            elif cond == 4 :
                htmltext = "8526-13.htm"
        elif npcId == GhostofvonHellmann:
            if cond == 6 :
                htmltext = "8524-01.htm" # sends you to the tombstone to dig
            elif cond == 7 :
                htmltext = "8524-05.htm"
        elif npcId == Tombstone:
            if cond == 6 :
                if st.getQuestTimer("spawn_timer") != None:
                    htmltext = "8523-03.htm"
                else:
                    htmltext = "8523-01.htm"
            if cond == 7 :
                htmltext = "8523-04.htm"
            elif cond == 8 :
                htmltext = "8523-06.htm"
        elif npcId == Violet:
            if cond == 8 :
                htmltext = "8386-01.htm" # send to box 
                st.playSound("ItemSound.quest_middle")
                st.set("cond","9")
            elif cond == 9 :
                htmltext = "8386-02.htm"
            elif cond == 10 :
                if st.getQuestItemsCount(SilverSpear) != 0:
                    htmltext = "8386-03.htm"
                    st.takeItems(SilverSpear,-1)
                    st.giveItems(Adena,350000)
                    st.addExpAndSp(456893,42112)
                    st.unset("cond")
                    st.setState(State.COMPLETED)
                    st.playSound("ItemSound.quest_finish")
                else:
                    htmltext = "You have no Silver Spear..."
        elif npcId == Box:
            if cond == 9 :
                if st.getQuestItemsCount(SilverKey) != 0:
                    htmltext = "8530-01.htm"
                else:
                    htmltext = "You have no key..."
            elif cond == 10 :
                htmltext = "8386-03.htm"
        return htmltext

QUEST = Quest(23,qn,"Lidia's Heart")
QUEST.addStartNpc(Innocentin)

QUEST.addTalkId(Innocentin)
QUEST.addTalkId(BrokenBookshelf)
QUEST.addTalkId(GhostofvonHellmann)
QUEST.addTalkId(Tombstone)
QUEST.addTalkId(Violet)
QUEST.addTalkId(Box)