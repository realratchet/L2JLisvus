# Made by mtrix
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

BLADE_MOLD,TYRAS_BILL,RANGERS_REPORT1,RANGERS_REPORT2,RANGERS_REPORT3,RANGERS_REPORT4,\
WEAPON_TRADE_CONTRACT,ATTACK_DIRECTIVES,CERTIFICATE,CARGOBOX,OL_MAHUM_HEAD = range(4239,4250)

ADENA = 57

CHANCE = 400000
CHANCE2 = 300000
CHANCE21 = 300000
CHANCE22 = 300000
CHANCE23 = 300000
CHANCE24 = 200000
CHANCE25 = 200000
CHANCE3 = 150000

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [RANGERS_REPORT1, RANGERS_REPORT2, RANGERS_REPORT3, RANGERS_REPORT4, OL_MAHUM_HEAD, CARGOBOX, TYRAS_BILL, CERTIFICATE, BLADE_MOLD, WEAPON_TRADE_CONTRACT]

 def onEvent (self,event,st) :
     htmltext = event
     if event == "7381-02.htm" :
         st.setState(State.STARTED)
         st.set("cond","1")
         st.playSound("ItemSound.quest_accept")
     elif event == "7207-02.htm" :
         st.set("cond","2")
     elif event == "7381-04.htm" :
         st.set("cond","5")
     elif event == "7381-07.htm" :
         st.set("cond","7")
         st.takeItems(WEAPON_TRADE_CONTRACT,-1)
         st.playSound("ItemSound.quest_middle")
     elif event == "7437-03.htm" :
         st.giveItems(CARGOBOX,1)
         st.giveItems(CERTIFICATE,1)
         st.set("cond","9")
     elif event == "7617-04.htm" :
         st.takeItems(CERTIFICATE,-1)
         st.takeItems(ATTACK_DIRECTIVES,-1)
         st.takeItems(CARGOBOX,-1)
         st.set("cond","10")
     return htmltext

 def onTalk (self,npc,st):
     npcId = npc.getNpcId()
     htmltext = JQuest.getNoQuestMsg()
     id = st.getState()
     level = st.getPlayer().getLevel()
     cond = st.getInt("cond")
     if npcId==7381 :
         if id == State.CREATED :
            if level > 26:
               htmltext = "7381-01.htm"
            else :
               htmltext = "7381-01a.htm"
               st.exitQuest(1)
         elif id == State.COMPLETED :
             htmltext = JQuest.getAlreadyCompletedMsg()
         elif cond==1 :
             htmltext = "7381-02a.htm"
         elif cond==4 :
             htmltext = "7381-03.htm"
         elif cond==5 :
             if st.getQuestItemsCount(RANGERS_REPORT1) and st.getQuestItemsCount(RANGERS_REPORT2) and st.getQuestItemsCount(RANGERS_REPORT3) and st.getQuestItemsCount(RANGERS_REPORT4) :
                 htmltext = "7381-05.htm"
                 st.takeItems(RANGERS_REPORT1,-1)
                 st.takeItems(RANGERS_REPORT2,-1)
                 st.takeItems(RANGERS_REPORT3,-1)
                 st.takeItems(RANGERS_REPORT4,-1)
                 st.set("cond","6")
             else :
                 htmltext = "7381-04a.htm"
         elif cond==6 :
             if st.getQuestItemsCount(WEAPON_TRADE_CONTRACT) and st.getQuestItemsCount(ATTACK_DIRECTIVES) :
                 htmltext = "7381-06.htm"
             else :
                 htmltext = "7381-05a.htm"
         elif cond==7 :
             htmltext = "7381-07a.htm"
         elif cond==11 :
             htmltext = "7381-08.htm"
             st.giveItems(ADENA,90000)
             st.playSound("ItemSound.quest_finish")
             st.setState(State.COMPLETED)
     elif npcId==7207 :
         if cond==1 :
             htmltext = "7207-01.htm"
         elif cond==2 :
             htmltext = "7207-01a.htm"
         elif cond==3 :
             if st.getQuestItemsCount(TYRAS_BILL) :
                 st.takeItems(TYRAS_BILL,-1)
                 htmltext = "7207-03.htm"
                 st.set("cond","4")
             else :
                 htmltext = "7207-01a.htm"
         elif cond==4 :
             htmltext = "7207-03a.htm"
     elif npcId==7420 :
         if cond==2 and st.getQuestItemsCount(BLADE_MOLD)>=20 :
            st.takeItems(BLADE_MOLD,-1)
            st.giveItems(TYRAS_BILL,1)
            htmltext = "7420-01.htm"
            st.set("cond","3")
            #mmh.. we're lacking a some text here in case you dont have 20 molds?
         if cond==3 :
             htmltext = "7420-01a.htm"
     elif npcId==7425 :
         if cond==7 :
             htmltext = "7425-01.htm"
             st.set("cond","8")
         elif cond==8 :
             htmltext = "7425-02.htm"
     elif npcId==7437 :
         if cond==8 :
             htmltext = "7437-01.htm"
         elif cond==9 :
             htmltext = "7437-03a.htm"
     elif npcId==7617 :
         if cond==9 and st.getQuestItemsCount(CERTIFICATE) and st.getQuestItemsCount(CARGOBOX) and st.getQuestItemsCount(ATTACK_DIRECTIVES) :
             htmltext = "7617-01.htm"
         if cond==10 :
             if st.getQuestItemsCount(OL_MAHUM_HEAD)>=30 :
                 htmltext = "7617-05.htm"
                 st.giveItems(ADENA,8000)
                 st.takeItems(OL_MAHUM_HEAD,-1)
                 st.set("cond","11")
                 st.playSound("ItemSound.quest_itemget")
             else :
                 htmltext = "7617-04a.htm"
     return htmltext

 def onKill (self,npc,player,isPet):
     st = player.getQuestState("171_ActsOfEvil")
     if st :
         if st.getState() != State.STARTED : return
         npcId = npc.getNpcId()
         cond = st.getInt("cond")
         if cond==2 :
             if npcId in range(496,500) and st.getQuestItemsCount(BLADE_MOLD)<20 :
                 st.addSpawn(5190)
             elif npcId==5190 :
                 st.dropItems(BLADE_MOLD,1,20,CHANCE,1)
         elif cond==5 and npcId==62 :
             st.dropItems(RANGERS_REPORT1,1,1,CHANCE2,0)
             st.dropItems(RANGERS_REPORT2,1,1,CHANCE21,0)
             st.dropItems(RANGERS_REPORT3,1,1,CHANCE22,0)
             st.dropItems(RANGERS_REPORT4,1,1,CHANCE23,0)
         elif cond==6 and npcId==438 :
             st.dropItems(WEAPON_TRADE_CONTRACT,1,1,CHANCE24,0)
             st.dropItems(ATTACK_DIRECTIVES,1,1,CHANCE25,0)
         elif cond==10 and npcId==66 :
             st.dropItems(OL_MAHUM_HEAD,1,30,CHANCE3,1)
     return

QUEST = Quest(171,"171_ActsOfEvil","Acts of Evil")
QUEST.addStartNpc(7381)

QUEST.addTalkId(7381)
QUEST.addTalkId(7207)
QUEST.addTalkId(7420)
QUEST.addTalkId(7425)
QUEST.addTalkId(7437)
QUEST.addTalkId(7617)

for i in range(494,500)+[62]+[66]+[5190]+[438] :
    QUEST.addKillId(i)