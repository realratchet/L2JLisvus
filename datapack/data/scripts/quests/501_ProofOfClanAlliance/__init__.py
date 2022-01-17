# Made by QuestDevs Team: DraX, DrLecter, Rolarga
# With invaluable support from: [TI]Blue, warrax
# v0.1.r0 2005.12.05

import sys
from java.util                                		import Iterator
from net.sf.l2j.gameserver.datatables         		import SkillTable
from net.sf.l2j.gameserver.model.quest        		import State
from net.sf.l2j.gameserver.model.quest        		import QuestState
from net.sf.l2j.gameserver.model.quest.jython 		import QuestJython as JQuest
from net.sf.l2j.gameserver.network.serverpackets    import NpcSay

qn="501_ProofOfClanAlliance"
qd="Proof of Clan Alliance"

# Quest Npcs
SIR_KRISTOF_RODEMAI  = 7756
STATUE_OF_OFFERING   = 7757
WITCH_ATHREA         = 7758
WITCH_KALIS          = 7759

# Quest Items
HERB_OF_HARIT     = 3832
HERB_OF_VANOR     = 3833
HERB_OF_OEL_MAHUM = 3834
BLOOD_OF_EVA      = 3835
SYMBOL_OF_LOYALTY = 3837
PROOF_OF_ALLIANCE = 3874
VOUCHER_OF_FAITH  = 3873
ANTIDOTE_RECIPE   = 3872
POTION_OF_RECOVERY= 3889

#Quest mobs, drop, rates and prices
CHESTS=range(5173,5178)
CHEST_LOCS = [
   [102273,103433,-3512],
   [102190,103379,-3524],
   [102107,103325,-3533],
   [102024,103271,-3500],
   [102327,103350,-3511],
   [102244,103296,-3518],
   [102161,103242,-3529],
   [102078,103188,-3500],
   [102381,103267,-3538],
   [102298,103213,-3532],
   [102215,103159,-3520],
   [102132,103105,-3513],
   [102435,103184,-3515],
   [102352,103130,-3522],
   [102269,103076,-3533],
   [102186,103022,-3541]
   ]

MOBS=[[685,HERB_OF_VANOR],[644,HERB_OF_HARIT],[576,HERB_OF_OEL_MAHUM]]
RATE=35
#stackable items paid to retry chest game: (default 10k adena)
RETRY_ITEMS=57
RETRY_PRICE=10000

def leader(player) :
    leaderst = None
    clan = player.getClan()
    if clan :
       leader = clan.getLeader().getPlayerInstance()
       if leader :  
          leaderst = leader.getQuestState(qn)  
    return leaderst

def randomize_chests(leaderst) :
    chests = [ 1,0,0,1,1,0]
    for i in range(len(chests)-1, 0, -1) :
        j = leaderst.getRandom(5)
        chests[i], chests[j] = chests[j], chests[i]
    for i in range(len(chests)): chests[i]=str(chests[i])
    leaderst.set("chests"," ".join(chests))
    return

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [ANTIDOTE_RECIPE, VOUCHER_OF_FAITH, POTION_OF_RECOVERY]
     # a hashtable tracking this quest's (chest) spawns, indexed by leaderST
     self.spawn_tracker = {}

 def chest_game(self,leaderst,command) :
    if command == "start" :
       leaderst.set("chest_game","1")
       leaderst.set("chest_count","0")
       attempts = leaderst.getInt("chest_try")
       leaderst.set("chest_try",str(attempts+1))
       randomize_chests(leaderst)
       tempList = []
       for x,y,z in CHEST_LOCS :
         rand = leaderst.getRandom(5)
         tempList.append(leaderst.addSpawn(5173+rand,x,y,z,0,0,60000))
       self.spawn_tracker[leaderst]=tempList
       leaderst.startQuestTimer("chest_timer",60000)
    elif command == "stop" :
       try:
           leaderst.set("chest_game","0")
           if self.spawn_tracker.has_key(leaderst) :
               trackedSpawns = self.spawn_tracker.pop(leaderst)
               for chest in trackedSpawns :
                   chest.decayMe()
       except: pass

 def onAdvEvent (self,event,npc,player):
   leaderst = 0
   if player.isClanLeader() == 1 :
     leaderst = player.getQuestState(qn)
   else :
     leaderst = leader(player)
   if not leaderst:
     return
   htmltext = event
#####  Leaders area  ######
   if event == "7756-03.htm" :
     leaderst.set("cond","1")
     leaderst.setState(State.STARTED)
     leaderst.set("part","1")
     leaderst.playSound("ItemSound.quest_accept")
   elif event == "7759-03.htm" :
     leaderst.set("cond","2")
     leaderst.set("part","2")
     leaderst.set("dead_list"," ")
   elif event == "7759-07.htm" :
     leaderst.takeItems(SYMBOL_OF_LOYALTY,1)
     leaderst.takeItems(SYMBOL_OF_LOYALTY,1)
     leaderst.takeItems(SYMBOL_OF_LOYALTY,1)
     leaderst.giveItems(ANTIDOTE_RECIPE,1)
     leaderst.set("cond","3")
     leaderst.set("part","3")
     leaderst.set("ingredients","0 0 0")
     leaderst.set("chest_count","0")
     leaderst.set("chest_game","0")
     leaderst.set("chest_try","0")
     skill = SkillTable.getInstance().getInfo(4082,1)
     skill.getEffects(player, player, False)
     leaderst.addNotifyOfDeath(player)
   elif event == "chest_timer" :
     htmltext = ""
     self.chest_game(leaderst,"stop")
#####  Members area  ######
   elif event == "7757-04.htm" :
     deadlist = leaderst.get("dead_list").split()
     deadlist.append(str(player.getObjectId()))
     leaderst.set("dead_list"," ".join(deadlist))
     player.doDie(player)
     leaderst.giveItems(player,SYMBOL_OF_LOYALTY,1,0)
     leaderst.playSound(player,"ItemSound.quest_accept")
     if player.getQuestState(qn) :
        player.getQuestState(qn).exitQuest(1)
   elif event == "7758-03.htm" :
     self.chest_game(leaderst,"start")
   elif event == "7758-07.htm" :
     if leaderst.getQuestItemsCount(player,RETRY_ITEMS) < RETRY_PRICE :
        htmltext = "7758-06.htm"
     else :
        leaderst.takeItems(player,RETRY_ITEMS,RETRY_PRICE)
   return htmltext

 def onTalk (self,npc,st):
   htmltext = JQuest.getNoQuestMsg()
   leaderst = leader(st.getPlayer())
   if not leaderst : return htmltext
   id = st.getState()
   npcId = npc.getNpcId()
   part = st.getInt("part")
   if npcId == SIR_KRISTOF_RODEMAI:
     if st.getPlayer().getClan() == None or st.getPlayer().isClanLeader() == 0:
       st.exitQuest(1)
       htmltext = "7756-10.htm"
     else :
       if st.getPlayer().getClan().getLevel() <= 2 :
         st.exitQuest(1)
         htmltext =  "7756-08.htm"
       elif st.getPlayer().getClan().getLevel() >= 4 :
         st.exitQuest(1)
         htmltext =  "7756-09.htm"
       elif id == State.STARTED and part == 4 and st.getQuestItemsCount(VOUCHER_OF_FAITH):
          st.takeItems(VOUCHER_OF_FAITH,1)
          st.giveItems(PROOF_OF_ALLIANCE,1)
          st.addExpAndSp(0,120000)
          st.playSound("ItemSound.quest_finish")
          htmltext="7756-07.htm"
          st.exitQuest(1)
       elif id == State.STARTED and (part == 1 or part == 2) :
         htmltext =  "7756-06.htm"
       elif st.getQuestItemsCount(PROOF_OF_ALLIANCE) == 0 :
         st.set("cond","0")
         htmltext =  "7756-01.htm"
   elif npcId == WITCH_KALIS:
     if st.getPlayer().getClan() == None :
       st.exitQuest(1)
     else:
       if st.getPlayer().isClanLeader() == 1 :
         if id == State.STARTED and part == 1 :
           htmltext =  "7759-01.htm"
         elif id == State.STARTED and part == 2 :
           htmltext = "7759-05.htm"
           if st.getQuestItemsCount(SYMBOL_OF_LOYALTY) == 3 :
              try : deads=len(st.get("dead_list").split())
              finally :
                 if deads == 3 :
                    htmltext = "7759-06.htm"
         elif id == State.STARTED and part == 3 :
           if st.getQuestItemsCount(HERB_OF_HARIT) and \
              st.getQuestItemsCount(HERB_OF_VANOR) and \
              st.getQuestItemsCount(HERB_OF_OEL_MAHUM) and \
              st.getQuestItemsCount(BLOOD_OF_EVA) and \
              st.getQuestItemsCount(ANTIDOTE_RECIPE) and \
              st.getInt("chest_game") == 3 :
             st.takeItems(ANTIDOTE_RECIPE,1)
             st.takeItems(HERB_OF_HARIT,1)
             st.takeItems(HERB_OF_VANOR,1)
             st.takeItems(HERB_OF_OEL_MAHUM,1)
             st.takeItems(BLOOD_OF_EVA,1)
             st.set("cond","4")
             st.set("part","4")
             st.giveItems(POTION_OF_RECOVERY,1)
             st.giveItems(VOUCHER_OF_FAITH,1)
             htmltext = "7759-08.htm"
           elif st.getQuestItemsCount(VOUCHER_OF_FAITH)==0:
             htmltext = "7759-10.htm"
       else :
         if leaderst.getState() == State.STARTED and leaderst.getInt("part") == 3 :
             htmltext = "7759-11.htm"
   elif npcId == STATUE_OF_OFFERING:
     if st.getPlayer().getClan() == None :
       st.exitQuest(1)
     else :
       if st.getPlayer().isClanLeader() == 1 :
         if leaderst.getState() == State.STARTED and leaderst.getInt("part") in [1,2,3,4] :
           htmltext = "7757-03.htm"
       else :
         if st.getPlayer().getLevel() <= 39 :
           htmltext = "7757-02.htm"
           st.exitQuest(1)
         elif leaderst.getState() == State.STARTED and leaderst.getInt("part") == 2 :
           dlist=[]
           deads=3
           dlist=leaderst.get("dead_list").split()
           deads = len(dlist)
           if deads < 3 :
             if str(st.getPlayer().getObjectId()) not in dlist :
                if not leaderst.getQuestItemsCount(st.getPlayer(),SYMBOL_OF_LOYALTY) :
                   htmltext =  "7757-01.htm"
                else :
                   htmltext =  "7757-06.htm"
             else :
                 htmltext = "You cannot die again!"
                 st.exitQuest(1)
   elif npcId == WITCH_ATHREA :
     if st.getPlayer().getClan() == None :
       st.exitQuest(1)
     else :
       if st.getPlayer().isClanLeader() == 0 :
          if leaderst :
             if leaderst.getState() == State.STARTED and leaderst.getInt("part") == 3 :
                game_state=leaderst.getInt("chest_game")
                if game_state == 0 :
                   if leaderst.getInt("chest_try") == 0 :
                      htmltext="7758-01.htm"
                   else :
                      htmltext="7758-05.htm"
                elif game_state == 1 :
                   htmltext="7758-09.htm"
                elif game_state == 2 :
                   timer=leaderst.getQuestTimer("chest_timer")
                   if timer != None : timer.cancel()
                   self.chest_game(leaderst,"stop")
                   leaderst.set("chest_game","3")
                   leaderst.giveItems(st.getPlayer(),BLOOD_OF_EVA,1,0)
                   leaderst.playSound(st.getPlayer(),"ItemSound.quest_middle")
                   htmltext="7758-08.htm"
                   st.exitQuest(1)
   return htmltext

 def onKill(self,npc,player,isPet) :
   if player.isClanLeader() == 1 :
     return
   leaderst = leader(player)
   ### first part, general checking
   npcId=npc.getNpcId()
   if not leaderst :
     return "Quest clan leader not available."
   elif leaderst.getInt("cond") > 2 :
     ingredients = []
     try :
        if leaderst.get("ingredients") != None :
           ingredients = leaderst.get("ingredients").split()
     finally :
     ### second part, herbs gathering
        if len(ingredients) :
           for m in range(len(MOBS)) :
              if not int(ingredients[m]) :
                 if npcId == MOBS[m][0] :
                    if leaderst.getQuestItemsCount(player,MOBS[m][1]) == 0 :
                       if leaderst.getRandom(100) < RATE :
                          leaderst.giveItems(player,MOBS[m][1],1,0)
                          ingredients[m]='1'
                          leaderst.set("ingredients"," ".join(ingredients))
                          leaderst.playSound(player,"ItemSound.quest_middle")
                          return
     ### third part, chest game
     if npcId in CHESTS :
        timer=leaderst.getQuestTimer("chest_timer")
        #if timer == None : self.chest_game(leaderst,"stop");return "Time is up!"
        chests = leaderst.get("chests").split()
        for i in range(len(chests)) :
            if npcId == 5173+i and chests[i] == '1' :
               npc.broadcastPacket(NpcSay(npc.getObjectId(),0,npc.getNpcId(),"###### BINGO! ######"))
               count=leaderst.getInt("chest_count")
               if count < 4 :
                  count+=1
                  leaderst.set("chest_count",str(count))
                  if count == 4 :
                     leaderst.getQuestTimer("chest_timer").cancel()
                     self.chest_game(leaderst,"stop")
                     leaderst.set("chest_game","2")
                     leaderst.playSound(player,"ItemSound.quest_middle")
                  else :
                     leaderst.playSound(player,"ItemSound.quest_itemget")
   return

 def onDeath(self, npc, pc, st) :
     if st.getPlayer() == pc :
       timer=st.getQuestTimer("chest_timer")
       if timer != None : timer.cancel()
       st.exitQuest(1)

QUEST = Quest(501,qn,qd)
QUEST.addStartNpc(SIR_KRISTOF_RODEMAI)
QUEST.addStartNpc(STATUE_OF_OFFERING)
QUEST.addStartNpc(WITCH_ATHREA)

QUEST.addTalkId(SIR_KRISTOF_RODEMAI)
QUEST.addTalkId(STATUE_OF_OFFERING)
QUEST.addTalkId(WITCH_ATHREA)
QUEST.addTalkId(WITCH_KALIS)

for i in range(len(MOBS)) :
    QUEST.addKillId(MOBS[i][0])

for i in CHESTS :
    QUEST.addKillId(i)