# Made by Mr. Have fun! Version 0.2
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

REPORT_OF_PERRIN_ID = 2810
CRISTINAS_LETTER_ID = 2811
PICTURE_OF_WINDY_ID = 2812
GOLDEN_STATUE_ID = 2813
WINDYS_PEBBLES_ID = 2814
ORDER_OF_SORIUS_ID = 2815
SECRET_LETTER1_ID = 2816
SECRET_LETTER2_ID = 2817
SECRET_LETTER3_ID = 2818
SECRET_LETTER4_ID = 2819
MARK_OF_HEALER_ID = 2820
ADENA_ID = 57

#this handles all dropdata, npcId:[condition,maxcount,item]
DROPLIST={
5134:[2,1,0],
5123:[11,1,SECRET_LETTER1_ID],
5124:[14,1,SECRET_LETTER2_ID],
5125:[16,1,SECRET_LETTER3_ID],
5127:[18,1,SECRET_LETTER4_ID]
}

def radar2(st):
  st.addRadar(-44358,79442,-3634)
  
def radar1(st):
  st.addRadar(-59985,79234,-3502)
  
def radar3(st):
  st.addRadar(-14158,44953,-3556)

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(2810,2820)

 def onEvent (self,event,st) :
    htmltext = event
    if event == "1" :
      htmltext = "7473-04.htm"
      st.set("cond","1")
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
      st.giveItems(REPORT_OF_PERRIN_ID,1)
    elif event == "7473_1" :
          htmltext = "7473-08.htm"
    elif event == "7473_2" :
          htmltext = "7473-09.htm"
          if st.getQuestItemsCount(GOLDEN_STATUE_ID):
            st.addExpAndSp(134839,50000)
          else:
            st.addExpAndSp(118304,26250)
          st.giveItems(MARK_OF_HEALER_ID,1)
          st.takeItems(GOLDEN_STATUE_ID,1)
          st.set("cond","0")
          st.setState(State.COMPLETED)
          st.playSound("ItemSound.quest_finish")
    elif event == "7428_1" :
          htmltext = "7428-02.htm"
          st.set("cond","2")
          st.addSpawn(5134)
    elif event == "7658_1" :
          if st.getQuestItemsCount(ADENA_ID) >= 100000 :
            htmltext = "7658-02.htm"
            st.takeItems(ADENA_ID,100000)
            st.giveItems(PICTURE_OF_WINDY_ID,1)
            st.set("cond","7")
          else:
            htmltext = "7658-05.htm"
    elif event == "7658_2" :
          htmltext = "7658-03.htm"
          st.set("cond","5")
    elif event == "7658_7" :
          htmltext = "7658-07.htm"
          st.set("cond","9")
    elif event == "7660_1" :
          htmltext = "7660-02.htm"
    elif event == "7660_2" :
          htmltext = "7660-03.htm"
          st.takeItems(PICTURE_OF_WINDY_ID,1)
          st.giveItems(WINDYS_PEBBLES_ID,1)
          st.set("cond","8")
    elif event == "7674_1" :
          htmltext = "7674-02.htm"
          st.takeItems(ORDER_OF_SORIUS_ID,1)
          st.set("cond","11")
          st.addSpawn(5122,-97547,106503,-3405)
          st.addSpawn(5122,-97526,106584,-3405)
          st.addSpawn(5123,-97441,106585,-3405)
          st.playSound("Itemsound.quest_before_battle")
    elif event == "7665_1" :
          htmltext = "7665-02.htm"
          st.takeItems(SECRET_LETTER1_ID,1)
          st.takeItems(SECRET_LETTER2_ID,1)
          st.takeItems(SECRET_LETTER3_ID,1)
          st.takeItems(SECRET_LETTER4_ID,1)
          st.giveItems(CRISTINAS_LETTER_ID,1)
          st.set("cond","22")
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.COMPLETED :
      htmltext = JQuest.getAlreadyCompletedMsg()
   elif npcId == 7473 :
     if st.getInt("cond")==0 :
       if (st.getPlayer().getClassId().getId() in [0x04, 0x0f, 0x1d, 0x13]) and st.getPlayer().getLevel() > 38 :
         htmltext = "7473-03.htm"
       elif st.getPlayer().getClassId().getId() in [0x04, 0x0f, 0x1d, 0x13] :
         htmltext = "7473-01.htm"
       else:
         htmltext = "7473-02.htm"
         st.exitQuest(1)
     elif st.getInt("cond")==23 and st.getQuestItemsCount(GOLDEN_STATUE_ID) :
      htmltext = "7473-07.htm"
     else :
      htmltext = "7473-05.htm"
   elif npcId == 7428:
     if st.getInt("cond")==1 and st.getQuestItemsCount(REPORT_OF_PERRIN_ID) :
      htmltext = "7428-01.htm"
     elif npcId == 7428 and st.getInt("cond")==3 :
      htmltext = "7428-03.htm"
      st.set("cond","4")
      st.takeItems(REPORT_OF_PERRIN_ID,1)
   elif npcId == 7428 and st.getInt("cond")==3 :
      htmltext = "7428-04.htm"
   elif npcId == 7659 and st.getInt("cond")==7 :
        n = st.getRandom(5)
        if n == 0:
          htmltext = "7659-01.htm"
        if n == 1:
          htmltext = "7659-02.htm"
        if n == 2:
          htmltext = "7659-03.htm"
        if n == 3:
          htmltext = "7659-04.htm"
        if n == 4:
          htmltext = "7659-05.htm"
   elif npcId == 7424:
     if st.getInt("cond")==4 :
      htmltext = "7424-01.htm"
      st.set("cond","5")
     elif npcId == 7424 and st.getInt("cond")==5 :
      htmltext = "7424-02.htm"
   elif npcId == 7658 :
     if st.getQuestItemsCount(PICTURE_OF_WINDY_ID)==0 and st.getQuestItemsCount(WINDYS_PEBBLES_ID)==0 and st.getQuestItemsCount(GOLDEN_STATUE_ID)==0 :
      if st.getInt("cond")==5 :
        htmltext = "7658-01.htm"
        st.set("cond","6")
      elif st.getInt("cond")==6 :
        htmltext = "7658-01.htm"
     elif st.getInt("cond")==7 and st.getQuestItemsCount(PICTURE_OF_WINDY_ID) :
      htmltext = "7658-04.htm"
     elif st.getInt("cond")==8 :
      if st.getQuestItemsCount(WINDYS_PEBBLES_ID) :
        st.giveItems(GOLDEN_STATUE_ID,1)
        st.takeItems(WINDYS_PEBBLES_ID,1)
      htmltext = "7658-06.htm"
     elif st.getInt("cond")==9 :
      htmltext = "7658-07.htm"
   elif npcId == 7660:
     if st.getInt("cond")==7 and st.getQuestItemsCount(PICTURE_OF_WINDY_ID) :
      htmltext = "7660-01.htm"
     elif st.getInt("cond")==8 and st.getQuestItemsCount(WINDYS_PEBBLES_ID) :
      htmltext = "7660-04.htm"
   elif npcId == 7327 :
     if st.getInt("cond")==9 :
      htmltext = "7327-01.htm"
      st.giveItems(ORDER_OF_SORIUS_ID,1)
      st.set("cond","10")
     elif st.getInt("cond")>9 and st.getInt("cond")<22 :
      htmltext = "7327-02.htm"
     elif st.getInt("cond")==22 :
      htmltext = "7327-03.htm"
      st.takeItems(CRISTINAS_LETTER_ID,1)
      st.set("cond","23")
   elif npcId == 7674:
     if st.getInt("cond")==10 and st.getQuestItemsCount(ORDER_OF_SORIUS_ID) :
      htmltext = "7674-01.htm"
     elif st.getInt("cond")==11 and st.getQuestItemsCount(ORDER_OF_SORIUS_ID) == 0 :
      htmltext = "7674-02.htm"
     elif st.getInt("cond")==12 and st.getQuestItemsCount(SECRET_LETTER1_ID) :
      htmltext = "7674-03.htm"
      st.set("cond","13")
     elif st.getInt("cond")==13 and st.getQuestItemsCount(SECRET_LETTER1_ID) :
      htmltext = "7674-03.htm"
   elif npcId == 7662 :
     if st.getInt("cond")==13 :
      htmltext = "7662-01.htm"
      radar1(st)
     elif st.getInt("cond")==20 :
      htmltext = "7662-03.htm"
      st.set("cond","21")
      radar2(st)
     elif st.getInt("cond") > 20:
      htmltext = "7662-04.htm"
     elif st.getInt("cond") > 13 :
      htmltext = "7662-02.htm"
      radar3(st)
   elif npcId == 7663:
     if st.getInt("cond")==13 :
      htmltext = "7663-01.htm"
      radar1(st)
     elif st.getInt("cond")==20 :
      htmltext = "7663-03.htm"
      st.set("cond","21")
      radar2(st)
     elif st.getInt("cond") > 20:
      htmltext = "7663-04.htm"
     elif st.getInt("cond") > 13:
      htmltext = "7663-02.htm"
      radar3(st)
   elif npcId == 7664:
     if st.getInt("cond")==13 :
      htmltext = "7664-01.htm"
      radar1(st)
     elif st.getInt("cond")==20 :
      htmltext = "7664-03.htm"
      st.set("cond","21")
      radar2(st)
     elif st.getInt("cond") > 20:
      htmltext = "7664-04.htm"
     elif st.getInt("cond") > 13:
      htmltext = "7664-02.htm"
      radar3(st)
   elif npcId == 7661:
     if st.getInt("cond")==13 :
      htmltext = "7661-01.htm"
      st.addSpawn(5124)
      st.addSpawn(5124)
      st.addSpawn(5124)
      st.playSound("Itemsound.quest_before_battle")
      st.set("cond","14")
     elif st.getInt("cond")==15:
      htmltext = "7661-02.htm"
      st.addSpawn(5125)
      st.addSpawn(5125)
      st.addSpawn(5125)
      st.playSound("Itemsound.quest_before_battle")
      st.set("cond","16")
     elif st.getInt("cond")==17:
      htmltext = "7661-03.htm"
      st.addSpawn(5126)
      st.addSpawn(5126)
      st.addSpawn(5127)
      st.playSound("Itemsound.quest_before_battle")
      st.set("cond","18")
     elif st.getInt("cond") == 19 :
      htmltext = "7661-04.htm"
      st.set("cond","20")
   elif npcId == 7665 :
     if st.getInt("cond")==20 or st.getInt("cond")==21 :
      htmltext = "7665-01.htm"
     else :
      htmltext = "7665-03.htm"
   return htmltext

 def onKill (self,npc,player,isPet):
   st = player.getQuestState("226_TestOfHealer")
   if st :
     if st.getState() != State.STARTED : return
     npcId = npc.getNpcId()
     condition,maxcount,item=DROPLIST[npcId] 
     if st.getInt("cond")==condition and st.getQuestItemsCount(item)<maxcount:
       if item != 0:
         st.giveItems(item,1)
       if npcId == 5134 :
         st.set("cond","3")
       if npcId == 5123 :
         st.set("cond","12")
       if npcId == 5124 :
         st.set("cond","15")
       if npcId == 5125 :
         st.set("cond","17")
       if npcId == 5127 :
         st.set("cond","19")
       st.playSound("Itemsound.quest_middle")
   return

QUEST = Quest(226,"226_TestOfHealer","Test Of Healer")
QUEST.addStartNpc(7473)

for npcId in [7327,7424,7428,7473,7658,7659,7660,7661,7662,7663,7664,7665,7674]:
  QUEST.addTalkId(npcId)
 
for mobId in [150,5123,5124,5125,5127,5134]:
  QUEST.addKillId(mobId)