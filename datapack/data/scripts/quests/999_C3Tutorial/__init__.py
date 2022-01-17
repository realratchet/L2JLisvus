# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.network.serverpackets import PlaySound

RECOMMENDATION_01 = 1067
RECOMMENDATION_02 = 1068
LEAF_OF_MOTHERTREE = 1069
BLOOD_OF_JUNDIN = 1070
LICENSE_OF_MINER = 1498
VOUCHER_OF_FLAME = 1496
SOULSHOT_NOVICE = 5789
SPIRITSHOT_NOVICE = 5790
BLUE_GEM=6353

# event:[htmlfile,radarX,radarY,radarZ,item,classId1,gift1,count1,classId2,gift2,count2]
EVENTS={
"7008_02":["7008-03.htm",-84058,243239,-3730,RECOMMENDATION_01,0x00,SOULSHOT_NOVICE,200,0,0,0],
"7017_02":["7017-03.htm",-84058,243239,-3730,RECOMMENDATION_02,0x0a,SPIRITSHOT_NOVICE,100,0,0,0],
"7370_02":["7370-03.htm",45491,48359,-3086,LEAF_OF_MOTHERTREE,0x19,SPIRITSHOT_NOVICE,100,0x12,SOULSHOT_NOVICE,200],
"7129_02":["7129-03.htm",12116,16666,-4610,BLOOD_OF_JUNDIN,0x26,SPIRITSHOT_NOVICE,100,0x1f,SOULSHOT_NOVICE,200],
"7528_02":["7528-03.htm",115642,-178046,-941,LICENSE_OF_MINER,0x35,SOULSHOT_NOVICE,200,0,0,0],
"7573_02":["7573-03.htm",-45067,-113549,-235,VOUCHER_OF_FLAME,0x31,SPIRITSHOT_NOVICE,100,0x2c,SOULSHOT_NOVICE,200]
}

# npcId:[raceId,[htmlfiles],npcTyp,item]
TALKS={
7017:[0,["7017-01.htm","7017-02.htm","7017-04.htm"],0,0],
7008:[0,["7008-01.htm","7008-02.htm","7008-04.htm"],0,0],
7370:[1,["7370-01.htm","7370-02.htm","7370-04.htm"],0,0],
7129:[2,["7129-01.htm","7129-02.htm","7129-04.htm"],0,0],
7573:[3,["7573-01.htm","7573-02.htm","7573-04.htm"],0,0],
7528:[4,["7528-01.htm","7528-02.htm","7528-04.htm"],0,0],
7018:[0,["7131-01.htm",0,"7019-03a.htm","7019-04.htm",],1,RECOMMENDATION_02],
7019:[0,["7131-01.htm",0,"7019-03a.htm","7019-04.htm",],1,RECOMMENDATION_02],
7020:[0,["7131-01.htm",0,"7019-03a.htm","7019-04.htm",],1,RECOMMENDATION_02],
7021:[0,["7131-01.htm",0,"7019-03a.htm","7019-04.htm",],1,RECOMMENDATION_02],
7009:[0,["7530-01.htm","7009-03.htm",0,"7009-04.htm",],1,RECOMMENDATION_01],
7011:[0,["7530-01.htm","7009-03.htm",0,"7009-04.htm",],1,RECOMMENDATION_01],
7012:[0,["7530-01.htm","7009-03.htm",0,"7009-04.htm",],1,RECOMMENDATION_01],
7056:[0,["7530-01.htm","7009-03.htm",0,"7009-04.htm",],1,RECOMMENDATION_01],
7400:[1,["7131-01.htm","7400-03.htm","7400-03a.htm","7400-04.htm",],1,LEAF_OF_MOTHERTREE],
7401:[1,["7131-01.htm","7400-03.htm","7400-03a.htm","7400-04.htm",],1,LEAF_OF_MOTHERTREE],
7402:[1,["7131-01.htm","7400-03.htm","7400-03a.htm","7400-04.htm",],1,LEAF_OF_MOTHERTREE],
7403:[1,["7131-01.htm","7400-03.htm","7400-03a.htm","7400-04.htm",],1,LEAF_OF_MOTHERTREE],
7131:[2,["7131-01.htm","7131-03.htm","7131-03a.htm","7131-04.htm",],1,BLOOD_OF_JUNDIN],
7404:[2,["7131-01.htm","7131-03.htm","7131-03a.htm","7131-04.htm",],1,BLOOD_OF_JUNDIN],
7574:[3,["7575-01.htm","7575-03.htm","7575-03a.htm","7575-04.htm",],1,VOUCHER_OF_FLAME],
7575:[3,["7575-01.htm","7575-03.htm","7575-03a.htm","7575-04.htm",],1,VOUCHER_OF_FLAME],
7530:[4,["7530-01.htm","7530-03.htm",0,"7530-04.htm",],1,LICENSE_OF_MINER]
}

class Quest (JQuest) :

 def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

 def onEvent (self,event,st):
    htmltext = event
    qs = st.getPlayer().getQuestState("255_Tutorial")
    if not qs: return
    if qs != None :
       Ex = qs.getInt("Ex")
       classId = int(st.getPlayer().getClassId().getId())
       if event == "TimerEx_NewbieHelper" :
          if Ex == 0 :
             if st.getPlayer().getClassId().isMage() :
                st.playTutorialVoice("tutorial_voice_009b")
             else:
                st.playTutorialVoice("tutorial_voice_009a")
             qs.set("Ex","1")
          elif Ex == 3 :
             st.playTutorialVoice("tutorial_voice_010a")
             qs.set("Ex","4")
          return
       elif event == "TimerEx_GrandMaster" :
          if Ex >= 4 :
             st.showQuestionMark(7)
             st.playSound("ItemSound.quest_tutorial")
             st.playTutorialVoice("tutorial_voice_025")
          return
       else:
          htmlfile,radarX,radarY,radarZ,item,classId1,gift1,count1,classId2,gift2,count2 = EVENTS[event]
          st.addRadar(radarX,radarY,radarZ);
          htmltext=htmlfile
          if st.getQuestItemsCount(item) and st.getInt("onlyone") == 0:
             st.addExpAndSp(0,50)
             st.startQuestTimer("TimerEx_GrandMaster",60000)
             st.takeItems(item,1)
             if Ex <= 3 :
                qs.set("Ex","4")
             if st.getPlayer().getClassId().getId() == classId1 :
                st.giveItems(gift1,count1)
                if gift1 == SPIRITSHOT_NOVICE :
                   st.playTutorialVoice("tutorial_voice_027")
                else:
                   st.playTutorialVoice("tutorial_voice_026")
             elif st.getPlayer().getClassId().getId() == classId2 :
                if gift2:
                   st.giveItems(gift2,count2)
                   st.playTutorialVoice("tutorial_voice_026")
             st.unset("step")
             st.set("onlyone","1")
    return htmltext

 def onFirstTalk (self,npc,player):
   qs = player.getQuestState("255_Tutorial")
   if not qs :
     npc.showChatWindow(player)
     return None
   st = player.getQuestState("999_C3Tutorial")
   if not st :
     st = self.newQuestState(player)
   htmltext = ""
   Ex = qs.getInt("Ex")
   npcId = npc.getNpcId()
   step=st.getInt("step")
   onlyone=st.getInt("onlyone")
   npcTyp=0
   if npcId in TALKS.keys():
     raceId,htmlfiles,npcTyp,item = TALKS[npcId]
   if (player.getLevel() >= 10 or onlyone) and npcTyp == 1:
     htmltext = "7575-05.htm"
   elif npcId in [7600, 7601, 7602, 7598, 7599]:
     reward=qs.getInt("reward")
     if reward == 0:
       if player.getClassId().isMage() :
         st.playTutorialVoice("tutorial_voice_027")
         st.giveItems(SPIRITSHOT_NOVICE,100)
       else:
         st.playTutorialVoice("tutorial_voice_026")
         st.giveItems(SOULSHOT_NOVICE,200)
       qs.set("reward","1")
       st.setState(State.COMPLETED)
     npc.showChatWindow(player)
     return None
   elif onlyone == 0 and player.getLevel() < 10 :
    if player.getRace().ordinal() == raceId :
      htmltext=htmlfiles[0]
      if npcTyp==1:
       if step==0 and Ex < 0:
        qs.set("Ex","0")
        st.startQuestTimer("TimerEx_NewbieHelper",30000)
        if player.getClassId().isMage() :
         st.set("step","1")
         st.setState(State.STARTED)
         st.playSound("ItemSound.quest_tutorial")
        else:
         htmltext="7530-01.htm"
         st.set("step","1")
         st.setState(State.STARTED)
         st.playSound("ItemSound.quest_tutorial")
       elif step==1 and st.getQuestItemsCount(item)==0 :
         if st.getQuestItemsCount(BLUE_GEM) :
           st.takeItems(BLUE_GEM,st.getQuestItemsCount(BLUE_GEM))
           st.giveItems(item,1)
           st.set("step","2")
           qs.set("Ex","3")
           st.startQuestTimer("TimerEx_NewbieHelper",30000)
           qs.set("ucMemo","3")
           st.playSound("ItemSound.quest_middle")
           if player.getClassId().isMage() :
             st.playTutorialVoice("tutorial_voice_027")
             st.giveItems(SPIRITSHOT_NOVICE,100)
             htmltext = htmlfiles[2]
             if htmltext == 0 :
               htmltext = "<html><body>I am sorry. I only help warriors. Please go to another Newbie Helper who may assist you.</body></html>"
           else:
             st.playTutorialVoice("tutorial_voice_026")
             st.giveItems(SOULSHOT_NOVICE,200)
             htmltext = htmlfiles[1]
             if htmltext == 0 :
               htmltext = "<html><body>I am sorry. I only help mystics. Please go to another Newbie Helper who may assist you.</body></html>"
         else:
           if player.getClassId().isMage() :
             htmltext = "7131-02.htm"
             if player.getRace().ordinal() == 3 :
              htmltext = "7575-02.htm"
           else:
             htmltext = "7530-02.htm"
       elif step==2 :
        htmltext = htmlfiles[3]
      elif npcTyp == 0 :
        if step==1 :
          htmltext = htmlfiles[0]
        elif step==2 :
          htmltext = htmlfiles[1]
        elif step==3 :
          htmltext = htmlfiles[2]
   elif st.getState() != State.COMPLETED and npcTyp == 0:
     htmltext = str(npc.getNpcId())+"-04.htm"
   if htmltext == None or htmltext == "":
     npc.showChatWindow(player)
   return htmltext

 def onTalk (self,npc,st):
   return JQuest.getNoQuestMsg()

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("999_C3Tutorial")
   if st :
      qs = player.getQuestState("255_Tutorial")
      if not qs : return
      Ex = qs.getInt("Ex")
      if qs != None :
         if Ex in [0,1] :
            st.playTutorialVoice("tutorial_voice_011")
            st.showQuestionMark(3)
            qs.set("Ex","2")
         if Ex in [0,1,2] and st.getQuestItemsCount(BLUE_GEM) == 0 :
            if st.getRandom(100) < 50 :
               st.dropItem(npc,st.getPlayer(),BLUE_GEM,1)
               st.playSound("ItemSound.quest_tutorial")
   return

QUEST = Quest(999,"999_C3Tutorial","C3 Tutorial")

for startNpc in [7008,7009,7017,7019,7129,7131,7404,7056,7011,7012,7401,7403,7402,7018,7021,7020,7574,7370,7400,7528,7530,7573,7575,7600,7601,7602,7598,7599]:
  QUEST.addStartNpc(startNpc)
  QUEST.addFirstTalkId(startNpc)
  QUEST.addTalkId(startNpc)

QUEST.addKillId(1)
QUEST.addKillId(5198)