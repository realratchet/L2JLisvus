#Made by Emperorc
import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "611_AllianceWithVarkaSilenos"

#NPC
Naran_Ashanuk = 8378

#MOB
    #mobs for Alliance lvl 1:Ketra Orc footmen, raiders, scouts, shamans and war hounds
Ketra_One = [ 1324, 1325, 1327, 1328, 1329 ]
    #mobs for Alliance lvl 2 : Ketra Orc-warriors, Lieutenants, mediums, Elite Soldiers,\
    #White Captains, Seers, Commanders, Elite Guards
Ketra_Two = [ 1331, 1332, 1334, 1335, 1336, 1338, 1343, 1344 ]
    #mobs for Alliance lvl 3 and up SHOULD BE:Ketra Orc captains, battalion commanders, \
    #grand seers, chief shamans, chief royal guards, prophets, Prophet's Guards, and Prophet's Aides
        #AS LISTED in npc.sql: Ketra Orc-General, Battalion Commander, Grand Seer, \
        #Ketra's - Head Shaman, Head Guard, Prophet, Prophet's Guard, and Prophet's Aide
Ketra_Three = [ 1339, 1340, 1342, 1345, 1346, 1347, 1348, 1349 ]
    #All Varka Silenos mobs
Varka_Silenos = [ 1350, 1351, 1353, 1354, 1355, 1357, 1358, 1360, 1361, \
1362, 1369, 1370, 1364, 1365, 1366, 1368, 1371, 1372, 1373, 1374, 1375 ]

Chance = {
  1325:500,
  1339:500,
  1340:500,
  1324:500,
  1336:500,
  1331:500,
  1342:508,
  1327:509,
  1334:509,
  1335:518,
  1343:518,
  1329:519,
  1328:521,
  1338:527,
  1344:604,
  1346:604,
  1348:626,
  1349:626,
  1345:627,
  1332:628,
  1347:649
}

Chance_molar = {
  1339:568,
  1340:568,
  1324:500,
  1336:529,
  1331:529,
  1342:578,
  1327:510,
  1334:539,
  1343:548,
  1329:519,
  1328:522,
  1338:558,
  1345:713,
  1332:664,
  1347:638
}

#Quest Items
Varka_Badge_Soldier, Varka_Badge_Officer, Varka_Badge_Captain = [7216, 7217, 7218]
Ketra_Alliance_One, Ketra_Alliance_Two, Ketra_Alliance_Three, \
Ketra_Alliance_Four, Ketra_Alliance_Five = [7211, 7212, 7213, 7214, 7215]
Varka_Alliance_One, Varka_Alliance_Two, Varka_Alliance_Three, \
Varka_Alliance_Four, Varka_Alliance_Five  = [7221, 7222, 7223, 7224, 7225]
Ketra_Badge_Soldier, Ketra_Badge_Officer, Ketra_Badge_Captain  = [7226, 7227,7228]
Valor_Feather, Wisdom_Feather = [ 7229, 7230 ]
Molar = 7234

#drop system - cond:[item_id,max,drop_id]
One ={
  1:[57,100,Ketra_Badge_Soldier],
  2:[Varka_Alliance_One,200,Ketra_Badge_Soldier],
  3:[Varka_Alliance_Two,300,Ketra_Badge_Soldier],
  4:[Varka_Alliance_Three,300,Ketra_Badge_Soldier],
  5:[Varka_Alliance_Four,400,Ketra_Badge_Soldier]
}
Two ={   
  2:[Varka_Alliance_One,100,Ketra_Badge_Officer],
  3:[Varka_Alliance_Two,200,Ketra_Badge_Officer],
  4:[Varka_Alliance_Three,300,Ketra_Badge_Officer],
  5:[Varka_Alliance_Four,400,Ketra_Badge_Officer]
}
Three ={   
  3:[Varka_Alliance_Two,100,Ketra_Badge_Captain],
  4:[Varka_Alliance_Three,200,Ketra_Badge_Captain],
  5:[Varka_Alliance_Four,200,Ketra_Badge_Captain]
}

def decreaseAlliance(st) :
  if st.getPlayer().isAlliedWithVarka() :
    cond = st.getInt("cond")
    id = st.getInt("id")
    st.takeItems(Ketra_Badge_Soldier,-1)
    st.takeItems(Ketra_Badge_Officer,-1)
    st.takeItems(Ketra_Badge_Captain,-1)
    st.takeItems(Valor_Feather,-1)
    st.takeItems(Wisdom_Feather,-1)
    st.getPlayer().setAllianceWithVarkaKetra(0)
    st.exitQuest(1)
    if cond == 2 :
      if id == 2 :
        st.takeItems(Varka_Alliance_One,-1)
      else :
        st.takeItems(Varka_Alliance_Two,-1)
        st.giveItems(Varka_Alliance_One,1)
    elif cond == 3 :
      if id == 2:
        st.takeItems(Varka_Alliance_Two,-1)
        st.giveItems(Varka_Alliance_One,1)
      else :
         st.takeItems(Varka_Alliance_Three,-1)
         st.giveItems(Ketra_Alliance_Two,1)
    elif cond == 4 :
      if id == 2 :
        st.takeItems(Varka_Alliance_Three,-1)
        st.giveItems(Varka_Alliance_Two,1)
      else :
        st.takeItems(Varka_Alliance_Four,-1)
        st.giveItems(Varka_Alliance_Three,1)
    elif cond == 5 :
      if id == 2 :
        st.takeItems(Varka_Alliance_Four,-1)
        st.giveItems(Varka_Alliance_Three,1)
      else :
        st.takeItems(Varka_Alliance_Five,-1)
        st.giveItems(Varka_Alliance_Four,1)
    elif cond == 6 :
      st.takeItems(Varka_Alliance_Five,-1)
      st.giveItems(Varka_Alliance_Four,1)

def giveReward(st,item,chance,MAX,drop) :
  if st.getQuestItemsCount(item) > 0 :
    count = st.getQuestItemsCount(drop)
    if count < MAX or drop == Molar :
      numItems,chance = divmod(chance*Config.RATE_DROP_QUEST,1000)
      if st.getRandom(1000) < chance :
        numItems += 1
      numItems = int(numItems)
      if numItems != 0 :
        if count + numItems >= MAX and drop != Molar :
          numItems = MAX - count
          st.playSound("ItemSound.quest_middle")
        elif drop == Molar and int((count+numItems)/100) > int(count/100) :
          st.playSound("ItemSound.quest_middle")
        else :
          st.playSound("ItemSound.quest_itemget")
        st.giveItems(drop,numItems)

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [Ketra_Badge_Soldier, Ketra_Badge_Officer, Ketra_Badge_Captain]

 def onEvent (self,event,st) :
   cond = st.getInt("cond")
   id = st.getInt("id")
   htmltext = event
   player = st.getPlayer()
   if event == "8378-03a.htm" :
       if player.getLevel() >= 74 :
            st.set("cond","1")
            st.set("id","2")
            st.setState(State.STARTED)
            st.playSound("ItemSound.quest_accept")
            htmltext = "8378-03a.htm"
       else :
            htmltext = "8378-02b.htm"
            player.setAllianceWithVarkaKetra(0)
            st.exitQuest(1)
   elif event == "8378-10-1.htm" :
       htmltext = "8378-10-1.htm"
       st.set("id","3")
       st.takeItems(Ketra_Badge_Soldier, 100)
       st.giveItems(Varka_Alliance_One, 1)
       player.setAllianceWithVarkaKetra(-1)
       st.playSound("ItemSound.quest_middle")
   elif event == "8378-10-2.htm" :
       htmltext = "8378-10-2.htm"
       st.set("id","3")
       st.takeItems(Ketra_Badge_Soldier, 200)
       st.takeItems(Ketra_Badge_Officer, 100)
       st.takeItems(Varka_Alliance_One, -1)
       st.giveItems(Varka_Alliance_Two, 1)
       player.setAllianceWithVarkaKetra(-2)
       st.playSound("ItemSound.quest_middle")
   elif event == "8378-10-3.htm" :
       htmltext = "8378-10-3.htm"
       st.set("id","3")
       st.takeItems(Ketra_Badge_Soldier, 300)
       st.takeItems(Ketra_Badge_Officer, 200)
       st.takeItems(Ketra_Badge_Captain, 100)
       st.takeItems(Varka_Alliance_Two, -1)
       st.giveItems(Varka_Alliance_Three, 1)
       player.setAllianceWithVarkaKetra(-3)
       st.playSound("ItemSound.quest_middle")
   elif event == "8378-10-4.htm" :
       htmltext = "8378-10-4.htm"
       st.set("id","3")
       st.takeItems(Ketra_Badge_Soldier, 300)
       st.takeItems(Ketra_Badge_Officer, 300)
       st.takeItems(Ketra_Badge_Captain, 200)
       st.takeItems(Varka_Alliance_Three, -1)
       st.takeItems(Valor_Feather,-1)
       st.giveItems(Varka_Alliance_Four, 1)
       player.setAllianceWithVarkaKetra(-4)
       st.playSound("ItemSound.quest_middle")
   elif event == "8378-11a.htm" :
       htmltext = "8378-11a.htm"
   elif event == "8378-19.htm" :
       htmltext = "8378-19.htm"
   elif event == "8378-11b.htm" :
       htmltext = "8378-11b.htm"
   elif event == "8378-20.htm" :
       htmltext = "8378-20.htm"
       st.takeItems(Ketra_Badge_Soldier, -1)
       st.takeItems(Ketra_Badge_Officer, -1)
       st.takeItems(Ketra_Badge_Captain, -1)
       st.takeItems(Varka_Alliance_One, -1)
       st.takeItems(Varka_Alliance_Two, -1)
       st.takeItems(Varka_Alliance_Three, -1)
       st.takeItems(Varka_Alliance_Four, -1)
       st.takeItems(Varka_Alliance_Five, -1)
       st.takeItems(Valor_Feather,-1)
       st.takeItems(Wisdom_Feather,-1)
       player.setAllianceWithVarkaKetra(0)
       st.exitQuest(1)
   return htmltext

 def onTalk (self,npc,st):
    htmltext = JQuest.getNoQuestMsg()
    if st :
      npcId = npc.getNpcId()
      cond = st.getInt("cond")
      id = st.getInt("id")
      KBadgeS = st.getQuestItemsCount(Ketra_Badge_Soldier)
      KBadgeO = st.getQuestItemsCount(Ketra_Badge_Officer)
      KBadgeC = st.getQuestItemsCount(Ketra_Badge_Captain)
      VAlliance1 = st.getQuestItemsCount(Varka_Alliance_One)
      VAlliance2 = st.getQuestItemsCount(Varka_Alliance_Two)
      VAlliance3 = st.getQuestItemsCount(Varka_Alliance_Three)
      VAlliance4 = st.getQuestItemsCount(Varka_Alliance_Four)
      VAlliance5 = st.getQuestItemsCount(Varka_Alliance_Five)
      VAlliance = VAlliance1 + VAlliance2 + VAlliance3 + VAlliance4 + VAlliance5
      KAlliance = st.getQuestItemsCount(Ketra_Alliance_One) + \
       st.getQuestItemsCount(Ketra_Alliance_Two) + st.getQuestItemsCount(Ketra_Alliance_Three) + \
       st.getQuestItemsCount(Ketra_Alliance_Four) + st.getQuestItemsCount(Ketra_Alliance_Five)
      Valor = st.getQuestItemsCount(Valor_Feather)
      Wisdom = st.getQuestItemsCount(Wisdom_Feather)
      if npcId == Naran_Ashanuk :
          st.set("id","1")
          if st.getPlayer().isAlliedWithKetra() or KAlliance :
              htmltext= "8378-02a.htm"
              st.exitQuest(1)
          elif VAlliance == 0 :
              if cond != 1 :
                  htmltext = "8378-01.htm"
              else :
                  st.set("id","2")
                  if KBadgeS < 100 :
                      htmltext= "8378-03b.htm"
                  elif KBadgeS >= 100 :
                      htmltext = "8378-09.htm"
          elif VAlliance :
              st.setState(State.STARTED)
              st.set("id","2")
              if VAlliance1 :
                  if cond != 2 :
                      htmltext = "8378-04.htm"
                      st.set("cond","2")
                      st.getPlayer().setAllianceWithVarkaKetra(-1)
                  else :
                      if KBadgeS < 200 or KBadgeO < 100 :
                          htmltext = "8378-12.htm"
                      elif KBadgeS >= 200 and KBadgeO >= 100 :
                          htmltext = "8378-13.htm"
              elif VAlliance2 :
                  if cond != 3 :
                      htmltext = "8378-05.htm"
                      st.set("cond","3")
                      st.getPlayer().setAllianceWithVarkaKetra(-2)
                  else :
                      if KBadgeS < 300 or KBadgeO < 200 or KBadgeC < 100 :
                          htmltext = "8378-15.htm"
                      elif KBadgeS >= 300 and KBadgeO >= 200 and KBadgeC >= 100 :
                          htmltext = "8378-16.htm"
              elif VAlliance3 :
                  if cond != 4 :
                      htmltext = "8378-06.htm"
                      st.set("cond","4")
                      st.getPlayer().setAllianceWithVarkaKetra(-3)
                  else:
                      if KBadgeS < 300 or KBadgeO < 300 or KBadgeC < 200 or Valor == 0 :
                          htmltext = "8378-21.htm"
                      elif KBadgeS >= 300 and KBadgeO >= 300 and KBadgeC >= 200 and Valor > 0 :
                          htmltext = "8378-22.htm"
              elif VAlliance4 :
                  if cond != 5 :
                      htmltext = "8378-07.htm"
                      st.set("cond","5")
                      st.getPlayer().setAllianceWithVarkaKetra(-4)
                  else :
                      if KBadgeS < 400 or KBadgeO < 400 or KBadgeC < 200 or Wisdom == 0 :
                          htmltext = "8378-17.htm"
                      elif KBadgeS >= 400 and KBadgeO >= 400 and KBadgeC >= 200 and Wisdom > 0 :
                          htmltext = "8378-10-5.htm"
                          st.takeItems(Ketra_Badge_Soldier, 400)
                          st.takeItems(Ketra_Badge_Officer, 400)
                          st.takeItems(Ketra_Badge_Captain, 200)
                          st.takeItems(Varka_Alliance_Four, -1)
                          st.takeItems(Wisdom_Feather,-1)
                          st.giveItems(Varka_Alliance_Five, 1)
                          st.getPlayer().setAllianceWithVarkaKetra(-5)
                          st.set("id","3")
                          st.playSound("ItemSound.quest_middle")
              elif VAlliance5 :
                  if cond != 6 :
                      htmltext = "8378-18.htm"
                      st.set("cond","6")
                      st.getPlayer().setAllianceWithVarkaKetra(-5)
                  else:
                      htmltext = "8378-08.htm"
    return htmltext

 def onKill(self,npc,player,isPet):
   partyMember = self.getRandomPartyMemberState(player,npc,State.STARTED)
   if not partyMember: return
   st = partyMember.getQuestState(qn)
   if st :
     if st.getState() == State.STARTED :
          npcId = npc.getNpcId()
          cond = st.getInt("cond")
          id = st.getInt("id")
          st2 = partyMember.getQuestState("612_WarWithKetraOrcs")
          if not player.isAlliedWithKetra() :
              if (npcId in Ketra_One) or (npcId in Ketra_Two) or (npcId in Ketra_Three):
                  item = 0
                  if cond <= 5 :
                    if npcId in Ketra_One :
                      item,MAX,drop = One[cond]
                    elif npcId in Ketra_Two and cond > 1:
                      item,MAX,drop = Two[cond]
                    elif npcId in Ketra_Three and cond > 2 :
                      item,MAX,drop = Three[cond]
                  if item != 0 :
                    if st.getQuestItemsCount(drop) == MAX :
                      item = 0
                  chance = Chance[npcId]
                  #This is support for quest 612: War With Ketra Orcs. Basically, if the person has both this quest and 612, then they only get one quest item, 50% chance for 612 quest item and 50% chance for this quest's item
                  if st2 :
                      if (st.getRandom(2) == 1 or item == 0) and npcId in Chance_molar.keys() :
                          item = 57
                          MAX = 100
                          drop = Molar
                          chance = Chance_molar[npcId]
                          giveReward(st,item,chance,MAX,drop)
                      elif id == 2 and item != 0 :
                          giveReward(st,item,chance,MAX,drop)
                  elif id == 2 and item != 0 :
                      giveReward(st,item,chance,MAX,drop)
              elif npcId in Varka_Silenos :
                  decreaseAlliance(st)
                  party = player.getParty()
                  if party :
                      for player1 in player.getParty().getPartyMembers() :
                          pst = player1.getQuestState(qn)
                          if pst :
                              decreaseAlliance(pst)
   return

QUEST = Quest(611,qn,"Alliance With Varka Silenos")
QUEST.addStartNpc(Naran_Ashanuk)

QUEST.addTalkId(Naran_Ashanuk)

for mobId in Chance.keys() :
    QUEST.addKillId(mobId)

for mobId in Varka_Silenos :
    QUEST.addKillId(mobId)