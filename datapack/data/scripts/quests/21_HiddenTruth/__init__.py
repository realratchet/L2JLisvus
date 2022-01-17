# Made by Kerberos (adapted for L2JLisvus by roko91) - based on a L2Fortress script
# this script is part of the Official L2J Datapack Project.
# Visit http://forum.l2jdp.com for more details.

import sys
from net.sf.l2j.gameserver.ai import CtrlIntention
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.model import Location

qn = "21_HiddenTruth"

ROUTES={
1:[52373,-54296,-3136,0],
2:[52451,-52921,-3152,0],
3:[51909,-51725,-3125,0],
4:[52438,-51240,-3097,0],
5:[52143,-51418,-3085,0]
}

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [7140]

 def onAdvEvent (self,event,npc,player):
        st = player.getQuestState(qn)
        if not st : return
        htmltext = event
        if event == "8522-02.htm":
            st.setState(State.STARTED)
            st.playSound("ItemSound.quest_accept") 
            st.set("cond","1")
        elif event == "timer" :
            htmltext = "8328-05.htm"
        elif event == "8328-05.htm":
            st.set("cond","0")
            st.set("onlyone","1")
            st.unset("AGRIPEL")
            st.unset("DOMINIC")
            st.unset("BENEDICT")
            st.setState(State.COMPLETED)
            st.takeItems(7140,-1)
            if st.getQuestItemsCount(7141) == 0 :
                st.giveItems(7141,1)
            st.playSound("ItemSound.quest_finish")
            st.startQuestTimer("timer",1)
            htmltext = "Congratulations! You have just completed this quest!"     + \
                       " \n The Quest \"Tragedy In Von Hellmann Forest\""   + \
                       " has become available.\n Show Cross of Einhasad to High"+ \
                       " Priest Tifaren."
        elif event == "8523-03.htm" :
            st.playSound("SkillSound5.horror_02")
            st.playSound("ItemSound.quest_middle")
            st.set("cond","2")
            st.addSpawn(8524,51432,-54570,-3136,180000)
        elif event == "8524-06.htm" :
            st.set("cond","3")
            st.playSound("ItemSound.quest_middle")
            ghost = self.addSpawn(8525,npc)
            self.startQuestTimer("1",1,ghost,player)
            self.startQuestTimer("despawn",180000,ghost,player)
        elif event == "8526-03.htm" :
            st.playSound("ItemSound.item_drop_equip_armor_cloth")
        elif event == "8526-08.htm" :
            st.playSound("AmdSound.ed_chimes_05")
            st.set("cond","5")
            st.playSound("ItemSound.quest_middle")
        elif event == "8526-14.htm" :
            st.giveItems(7140,1)
            st.set("cond","6")
            st.playSound("ItemSound.quest_middle")
        elif event == "despawn" :
            npc.deleteMe()
            htmltext = None
        elif event.isdigit() :
            htmltext = None
            loc = int(event)
            x,y,z,heading=ROUTES[loc]
            if event == "1" :
                npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, Location(x,y,z,heading))
                self.startQuestTimer("2",5000,npc,player)
            elif event == "2" :
                npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, Location(x,y,z,heading))
                self.startQuestTimer("3",12000,npc,player)
            elif event == "3" :
                npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, Location(x,y,z,heading))
                self.startQuestTimer("4",15000,npc,player)
            elif event == "4" :
                npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, Location(x,y,z,heading))
                self.startQuestTimer("5",5000,npc,player)
            elif event == "5" :
                npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, Location(x,y,z,heading))
        return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   npcId = npc.getNpcId()
   cond = st.getInt("cond")
   onlyone = st.getInt("onlyone")
   state = st.getState()
   if state == State.CREATED :
     st.setState(State.STARTED)
     st.set("cond","0")
   if npcId == 8522:
     if cond == 0:
       if onlyone == 0:
         if st.getPlayer().getLevel() >= 63 :
           htmltext = "8522-01.htm"
         else:
           htmltext = "8522-03.htm"
           st.exitQuest(1)
       else:
         htmltext = JQuest.getAlreadyCompletedMsg()
     elif cond == 1:
       htmltext = "8522-05.htm"       
   elif npcId == 8523 :
     if cond == 1 :
       htmltext = "8523-01.htm"
     elif cond == 2 :
       htmltext = "8523-04.htm"
       st.playSound("SkillSound5.horror_02")
   elif npcId == 8524 :
     if cond == 2 :
       htmltext = "8524-01.htm"
     elif cond == 3 :
       htmltext = "8524-07b.htm"
       st.set("cond","4")
       st.playSound("ItemSound.quest_middle")
     elif cond == 4 :
       htmltext = "8524-07c.htm"
   elif npcId == 8525 :
     if cond == 3 :
       htmltext = "8525-01.htm"
     elif cond == 4 and id == 1 :
       htmltext = "8525-02.htm"
   elif npcId == 8526 :
     if cond in [3,4] :
       htmltext = "8526-01.htm"
     elif cond == 5 :
       htmltext = "8526-10.htm"
       st.playSound("AmdSound.ed_chimes_05")
     elif cond == 6 :
       htmltext = "8526-15.htm"
   elif npcId == 8348 and st.getQuestItemsCount(7140) == 1 :
     if cond == 6 :
       st.set("AGRIPEL","1")
       if st.getInt("AGRIPEL") == 1 and st.getInt("DOMINIC") == 1 and st.getInt("BENEDICT") == 1 :
         htmltext = "8348-02.htm"
         st.set("cond","7")
         st.playSound("ItemSound.quest_middle")
         return htmltext
       htmltext = "8348-0"+str(st.getRandom(3))+".htm"
     elif cond == 7 :
       htmltext = "8348-03.htm"
   elif npcId == 8350 and st.getQuestItemsCount(7140) == 1 :
     if cond == 6 :
       st.set("DOMINIC","1")
       if st.getInt("AGRIPEL") == 1 and st.getInt("DOMINIC") == 1 and st.getInt("BENEDICT") == 1 :
         htmltext = "8350-02.htm"
         st.set("cond","7")
         st.playSound("ItemSound.quest_middle")
         return htmltext
       htmltext = "8350-0"+str(st.getRandom(3))+".htm"
     elif cond == 7 :
       htmltext = "8350-03.htm"
   elif npcId == 8349 and st.getQuestItemsCount(7140) == 1 :
     if cond == 6 :
       st.set("BENEDICT","1")
       if st.getInt("AGRIPEL") == 1 and st.getInt("DOMINIC") == 1 and st.getInt("BENEDICT") == 1 :
         htmltext = "8349-02.htm"
         st.set("cond","7")
         st.playSound("ItemSound.quest_middle")
         return htmltext
       htmltext = "8349-0"+str(st.getRandom(3))+".htm"
     elif cond == 7 :
       htmltext = "8349-03.htm"
   elif npcId == 8328:
     if cond == 7:
       if st.getQuestItemsCount(7140) == 1:
         htmltext = "8328-01.htm"
     elif cond == 0 and onlyone == 1:
       htmltext = "8328-06.htm"
   return htmltext

QUEST = Quest(21,qn,"Hidden Truth")
QUEST.addStartNpc(8522)

for NPC in [8522,8523,8524,8525,8526,8348,8349,8350,8328]:
  QUEST.addTalkId(NPC)