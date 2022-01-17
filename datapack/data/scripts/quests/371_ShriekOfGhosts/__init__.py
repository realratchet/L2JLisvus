# Contributed by t0rm3nt0r to the Official L2J Datapack Project (adapted for L2JLisvus by roko91).

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "371_ShriekOfGhosts"

#NPC
PATRIN = 7929
REVA = 7867
#Quest items
URN = 5903
PORCELAIN = 6002
# item : [chance, html]
PORC = {
    6003 : [ 2 , "7929-03.htm"],
    6004 : [ 32, "7929-04.htm"],
    6005 : [ 62, "7929-05.htm"],
    6006 : [ 77, "7929-06.htm"]
    }
# mobid : [urn chance, porcelain chance]
MOBS = {
    818 : [38, 43],
    820 : [48, 56],
    824 : [50, 58]
    }

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = PORC.keys()
 
 def onEvent (self,event,st) :
     htmltext = event
     urn = st.getQuestItemsCount(URN)
     porcelain = st.getQuestItemsCount(PORCELAIN)
     if event == "7867-03.htm" :
       st.set("cond","1")
       st.setState(State.STARTED)
       st.playSound("ItemSound.quest_accept")
     elif event == "TRADE" :
       if urn == 0 :
         htmltext = "7867-06.htm"
       elif urn <= 100 :
         st.takeItems(URN,-1)
         st.giveItems(57,7000+urn*1000)
         htmltext = "7867-07.htm"
       elif urn > 100 :
         st.takeItems(URN,-1)
         st.giveItems(57,13000+urn*1000)
         htmltext = "7867-08.htm"
     elif event == "7867-10.htm" :
       htmltext = "7867-10.htm"
       if urn > 0:
           st.takeItems(URN,-1)
           st.giveItems(57,urn*1000)
       st.exitQuest(1)
     elif event == "APPR" :
       if not porcelain :
         htmltext = "7929-02.htm"
       else :
           test = st.getRandom(100)
           st.takeItems(PORCELAIN,1)
           htmltext = "7929-07.htm"
           for item in PORC.keys():
               chance, html = PORC[item]
               if test < chance :
                   st.giveItems(item,1)
                   htmltext = html
                   break
     return htmltext

 def onTalk (self,npc,st):
     npcId = npc.getNpcId()
     htmltext = JQuest.getNoQuestMsg()
     id = st.getState()
     cond = st.getInt("cond")
     urn = st.getQuestItemsCount(URN)
     porcelain = st.getQuestItemsCount(PORCELAIN)
     if id == State.CREATED and npcId == REVA :
       if st.getPlayer().getLevel() < 59 :
         htmltext = "7867-01.htm"
         st.exitQuest(1)
       else :
         htmltext = "7867-02.htm"
     elif id == State.STARTED :
       if npcId == REVA :
         if not porcelain :
           htmltext = "7867-04.htm"
         else :
           htmltext = "7867-05.htm"
       elif npcId == PATRIN :
           htmltext = "7929-01.htm"
     return htmltext
    
 def onKill(self,npc,player,isPet) :
     partyMember = self.getRandomPartyMemberState(player, npc, State.STARTED)
     if not partyMember: return
     st = partyMember.getQuestState(qn)
     if not st : return
     npcId = npc.getNpcId()
     chance = st.getRandom(100)
     if npcId in MOBS.keys() :
         urnchance, porcchance = MOBS[npcId]
         if chance < urnchance :
             st.giveItems(URN,1)
             st.playSound("ItemSound.quest_itemget")
         elif chance < porcchance :
             st.giveItems(PORCELAIN,1)
             st.playSound("ItemSound.quest_itemget")
     return

QUEST = Quest(371, qn, "Shriek Of Ghosts")
QUEST.addStartNpc(REVA)

QUEST.addTalkId(REVA)
QUEST.addTalkId(PATRIN)

for mob in MOBS.keys() :
    QUEST.addKillId(mob)