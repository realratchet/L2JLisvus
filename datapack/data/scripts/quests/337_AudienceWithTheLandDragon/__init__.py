# Original code by mtrix, Updated by Emperorc
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "337_AudienceWithTheLandDragon"

#NPCS
MOKE = 7498
HELTON = 7678
CHAKIRIS = 7705
KAIENA = 7720
GABRIELLE = 7753
GILMORE = 7754
THEODRIC = 7755
KENDRA = 7851
ORVEN = 7857
NPCS = [7678, 7498, 7705, 7720, 7753, 7754, 7755, 7851, 7857]

#MOBS
HAMRUT = 649
KRANROT = 650
BLOODY_QUEEN = 12079
BLOODY_QUEEN2 = 12080
SACRIFICE_OF_THE_SACRIFICED = 5171
HARIT_LIZARDMAN_SHAMAN = 644
HARIT_LIZARDMAN_ZEALOT = 5172
MARSH_STALKER = 679
MARSH_DRAKE = 680
ABYSS_JEWEL1 = 5165
GUARDIAN1 = 5168
ABYSS_JEWEL2 = 5166
GUARDIAN2 = 5169
ABYSS_JEWEL3 = 5167
GUARDIAN3 = 5170
CAVE_KEEPER = 277
CAVE_MAIDEN = 287
CAVE_KEEPER1 = 246
CAVE_MAIDEN1 = 134
MOBS = [12079, 12080, 277, 287, 246, 134, 644, 649, 650, 679, 680] + range(5165, 5173)

FEATHER_OF_GABRIELLE,MARSH_STALKER_HORN,MARSH_DRAKE_TALONS,KRANROT_SKIN,\
HAMRUT_LEG,REMAINS_OF_SACRIFICED,TOTEM_OF_LAND_DRAGON,FIRST_FRAGMENT_OF_ABYSS_JEWEL,\
SECOND_FRAGMENT_OF_ABYSS_JEWEL,THIRD_FRAGMENT_OF_ABYSS_JEWEL,MARA_FANG,MUSFEL_FANG,\
MARK_OF_WATCHMAN,PORTAL_STONE,HERALD_OF_SLAYER = range(3852,3866)+[3890]

def checkCond(st) :
    if st.getInt("orven")== 1 and st.getInt("kendra")==1 and st.getInt("chakiris")==1 and st.getInt("kaiena")==1 :
        st.set("all","1")

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = range(3852,3865)+[3890]

 def onAdvEvent (self,event,npc,player):
     if player :
         st = player.getQuestState(qn)
         if not st : return
         htmltext = event
         if event == "30753-02.htm" :
             st.exitQuest(1)
         elif event == "7753-06.htm" :
             st.setState(State.STARTED)
             st.set("cond","1")
             st.set("all","0")
             st.set("orven","0")
             st.set("kendra","0")
             st.set("chakiris","0")
             st.set("kaiena","0")
             st.set("moke","0")
             st.set("helton","0")
             st.giveItems(FEATHER_OF_GABRIELLE,1)
             st.playSound("ItemSound.quest_accept")
         elif event == "7753-10.htm" :
             st.set("cond","2")
             st.takeItems(MARK_OF_WATCHMAN,-1)
         elif event == "7754-03.htm" :
             st.set("cond","4")
         elif event == "7755-05.htm" :
             st.giveItems(PORTAL_STONE,1)
             st.takeItems(HERALD_OF_SLAYER,-1)
             st.takeItems(THIRD_FRAGMENT_OF_ABYSS_JEWEL,-1)
             st.playSound("ItemSound.quest_finish")
             st.exitQuest(1)
         return htmltext
     elif event == "Jewel1_Timer1" :
         npc.doDie(npc)
         self.cancelQuestTimer("Jewel1_Timer2",npc,None)
     elif event == "Jewel1_Timer2" :
         npc.doDie(npc)
         self.cancelQuestTimer("Jewel1_Timer1",npc,None)
     elif event == "Jewel2_Timer1" :
         npc.reduceCurrentHp(9999999,npc)
         self.cancelQuestTimer("Jewel2_Timer2",npc,None)
     elif event == "Jewel2_Timer2" :
         npc.doDie(npc)
         self.cancelQuestTimer("Jewel2_Timer1",npc,None)
     return

 def onTalk (self,npc,st):
    htmltext = JQuest.getNoQuestMsg()
    npcId = npc.getNpcId()
    cond = st.getInt("cond")
    id = st.getState()
    level = st.getPlayer().getLevel()
    if npcId == GABRIELLE :
         if id == State.CREATED :
             if level>=50 :
                 htmltext = "7753-03.htm"
             else :
                 htmltext = "7753-01.htm"
         elif cond == 1 :
             if st.getInt("all") == 0 :
                 htmltext = "7753-07.htm"
             else :
                 htmltext = "7753-09.htm"
         elif cond == 2 :
             if st.getInt("all") < 2 :
                 htmltext = "7753-11.htm"
             else :
                 htmltext = "7753-12.htm"
                 st.takeItems(MARK_OF_WATCHMAN,-1)
                 st.takeItems(FEATHER_OF_GABRIELLE,-1)
                 st.giveItems(HERALD_OF_SLAYER,1)
                 st.set("cond","3")
         elif cond == 3 :
             htmltext = "7753-13.htm"
         elif cond == 4 :
             htmltext = "7753-14.htm"
    if npcId == CHAKIRIS :
         if st.getInt("all") == 1 : #if all 4 tasks have been done
             htmltext = "7705-04.htm"
         elif st.getInt("chakiris")== 1 : #if not all 4 are done
             htmltext = "7705-03.htm"
         elif cond == 1:
             if st.getQuestItemsCount(HAMRUT_LEG)==0 or st.getQuestItemsCount(KRANROT_SKIN)==0 :
                 htmltext = "7705-01.htm"
             else :
                 st.giveItems(MARK_OF_WATCHMAN,1)
                 st.takeItems(HAMRUT_LEG,-1)
                 st.takeItems(KRANROT_SKIN,-1)
                 htmltext = "7705-02.htm"
                 st.set("chakiris","1")
                 checkCond(st)
    if npcId == KAIENA :
         if st.getInt("all") == 1 : #if all 4 tasks have been done
             htmltext = "7720-04.htm"
         elif st.getInt("kaiena")== 1 : #if not all 4 are done
             htmltext = "7720-03.htm"
         elif cond == 1:
             if st.getQuestItemsCount(MARSH_STALKER_HORN)==0 or st.getQuestItemsCount(MARSH_DRAKE_TALONS)==0 :
                 htmltext = "7720-01.htm"
             else :
                 st.giveItems(MARK_OF_WATCHMAN,1)
                 st.takeItems(MARSH_STALKER_HORN,-1)
                 st.takeItems(MARSH_DRAKE_TALONS,-1)
                 htmltext = "7720-02.htm"
                 st.set("kaiena","1")
                 checkCond(st)
    if npcId == KENDRA :
         if st.getInt("all") == 1 : #if all 4 tasks have been done
             htmltext = "7851-04.htm"
         elif st.getInt("kendra")== 1 : #if not all 4 are done
             htmltext = "7851-03.htm"
         elif cond == 1:
             if st.getQuestItemsCount(TOTEM_OF_LAND_DRAGON)==0 :
                 htmltext = "7851-01.htm"
             else :
                 st.giveItems(MARK_OF_WATCHMAN,1)
                 st.takeItems(TOTEM_OF_LAND_DRAGON,-1)
                 htmltext = "7851-02.htm"
                 st.set("kendra","1")
                 checkCond(st)
    if npcId == ORVEN :
         if st.getInt("all") == 1 : #if all 4 tasks have been done
             htmltext = "7857-04.htm"
         elif st.getInt("orven")== 1 : #if not all 4 are done
             htmltext = "7857-03.htm"
         elif cond == 1:
             if st.getQuestItemsCount(REMAINS_OF_SACRIFICED)==0 :
                 htmltext = "7857-01.htm"
             else :
                 st.giveItems(MARK_OF_WATCHMAN,1)
                 st.takeItems(REMAINS_OF_SACRIFICED,-1)
                 htmltext = "7857-02.htm"
                 st.set("orven","1")
                 checkCond(st)
    if npcId == MOKE :
         if st.getInt("all") == 2 :
             htmltext = "7498-05.htm"
         elif st.getInt("moke") == 1 :
             htmltext = "7498-04.htm"
         elif cond == 2 :
             if st.getQuestItemsCount(MARA_FANG) == 0 or st.getQuestItemsCount(FIRST_FRAGMENT_OF_ABYSS_JEWEL) == 0 :
                 htmltext = "7498-01.htm"
             else :
                 htmltext = "7498-03.htm"
                 st.giveItems(MARK_OF_WATCHMAN,1)
                 st.takeItems(MARA_FANG,-1)
                 st.takeItems(FIRST_FRAGMENT_OF_ABYSS_JEWEL,-1)
                 if st.getInt("helton") == 1 :
                     st.set("all","2")
                 else :
                     st.set("moke","1")
    if npcId == HELTON :
         if st.getInt("all") == 2 :
             htmltext = "7678-05.htm"
         elif st.getInt("helton") == 1 :
             htmltext = "7678-04.htm"
         elif cond == 2 :
             if st.getQuestItemsCount(MUSFEL_FANG) == 0 or st.getQuestItemsCount(SECOND_FRAGMENT_OF_ABYSS_JEWEL) == 0 :
                 htmltext = "7678-01.htm"
             else :
                 htmltext = "7678-03.htm"
                 st.giveItems(MARK_OF_WATCHMAN,1)
                 st.takeItems(MUSFEL_FANG,-1)
                 st.takeItems(SECOND_FRAGMENT_OF_ABYSS_JEWEL,-1)
                 if st.getInt("moke") == 1 :
                     st.set("all","2")
                 else :
                     st.set("helton","1")
    if npcId == GILMORE :
         if cond < 3 :
             htmltext = "7754-01.htm"
         elif cond == 3 and st.getQuestItemsCount(HERALD_OF_SLAYER)==1 :
             htmltext = "7754-02.htm"
         elif cond==4 :
             if st.getQuestItemsCount(THIRD_FRAGMENT_OF_ABYSS_JEWEL)==1 :
                 htmltext = "7754-05.htm"
             else :
                 htmltext = "7754-04.htm"
    if npcId == THEODRIC :
         if cond<3 :
             htmltext = "7755-01.htm"
         elif cond==3 and st.getQuestItemsCount(HERALD_OF_SLAYER)==1 :
             htmltext = "7755-02.htm"
         elif cond==4 :
             if st.getQuestItemsCount(THIRD_FRAGMENT_OF_ABYSS_JEWEL) == 0 :
                 htmltext = "7755-03.htm"
             else :
                 htmltext = "7755-04.htm"
    return htmltext

 def onAttack (self, npc, player, damage, isPet):
   st = player.getQuestState(qn)
   if st :
     npcId = npc.getNpcId()
     maxHp = npc.getMaxHp()
     nowHp = npc.getCurrentHp()
     cond = st.getInt("cond")
     if npcId == ABYSS_JEWEL1 :
         if cond == 2 and st.getInt("moke")<>1:
             if nowHp < maxHp*0.8 and st.getInt("aspawned")<>1 :
                 for i in range(0,70,7):
                    st.addSpawn(GUARDIAN1,-81260,75639+i,-3300,180000)
                    st.addSpawn(GUARDIAN1,-81240,75639+i,-3300,180000)
                 st.set("aspawned","1")
                 self.startQuestTimer("Jewel1_Timer1",900000,npc,None)
             elif nowHp < maxHp*0.4 and st.getQuestItemsCount(FIRST_FRAGMENT_OF_ABYSS_JEWEL)==0 :
                 st.giveItems(FIRST_FRAGMENT_OF_ABYSS_JEWEL,1)
                 st.playSound("ItemSound.quest_itemget")
                 self.startQuestTimer("Jewel1_Timer2",240000,npc,None)
         if nowHp < maxHp*0.1 :
             npc.doDie(npc)
             self.cancelQuestTimer("Jewel1_Timer1",npc,None)
             self.cancelQuestTimer("Jewel1_Timer2",npc,None)
             st.set("aspawned","0")
     if npcId == ABYSS_JEWEL2 :
         if cond == 2 and st.getInt("helton")<>1:
             if nowHp < maxHp*0.8 and st.getInt("bspawned")<>1 :
                 for i in range(0,70,7) :
                    st.addSpawn(GUARDIAN2,63766+i,31139,-3400,180000)
                    st.addSpawn(GUARDIAN2,63706,31139+i,-3400,180000)
                 st.set("bspawned","1")
                 self.startQuestTimer("Jewel2_Timer1",900000,npc,None)
             elif nowHp < maxHp*0.4 and st.getQuestItemsCount(SECOND_FRAGMENT_OF_ABYSS_JEWEL)==0 :
                 st.giveItems(SECOND_FRAGMENT_OF_ABYSS_JEWEL,1)
                 st.playSound("ItemSound.quest_itemget")
                 self.startQuestTimer("Jewel2_Timer2",240000,npc,None)
         if nowHp < maxHp*0.1 :
             npc.doDie(npc)
             self.cancelQuestTimer("Jewel2_Timer1",npc,None)
             self.cancelQuestTimer("Jewel2_Timer2",npc,None)
             st.set("bspawned","0")
     if npcId == ABYSS_JEWEL3 :
         if cond == 4 :
             if nowHp < maxHp*0.8 and st.getInt("cspawned")<>1 :
                 for i in range(1,5) :
                    st.addSpawn(GUARDIAN3,180000)
                 st.set("cspawned","1")
             elif nowHp < maxHp*0.4 and st.getQuestItemsCount(THIRD_FRAGMENT_OF_ABYSS_JEWEL)==0 :
                 st.giveItems(THIRD_FRAGMENT_OF_ABYSS_JEWEL,1)
                 st.playSound("ItemSound.quest_itemget")
         if nowHp < maxHp*0.1 :
             npc.decayMe()
     return

 def onKill(self,npc,player,isPet):
    npcId = npc.getNpcId()
    st = player.getQuestState(qn)
    if st :
        cond = st.getInt("cond")
        if cond == 1 :
            if npcId == HAMRUT and st.getQuestItemsCount(HAMRUT_LEG)==0 and st.getInt("chakiris") == 0 :
                st.giveItems(HAMRUT_LEG,1)
                st.playSound("ItemSound.quest_itemget")
            elif npcId == KRANROT and st.getQuestItemsCount(KRANROT_SKIN)==0 and st.getInt("chakiris") == 0 :
                st.giveItems(KRANROT_SKIN,1)
                st.playSound("ItemSound.quest_itemget")
            elif npcId == MARSH_STALKER and st.getQuestItemsCount(MARSH_STALKER_HORN)==0 and st.getInt("kaiena") == 0 :
                st.giveItems(MARSH_STALKER_HORN,1)
                st.playSound("ItemSound.quest_itemget")
            elif npcId == MARSH_DRAKE and st.getQuestItemsCount(MARSH_DRAKE_TALONS)==0 and st.getInt("kaiena") == 0 :
                st.giveItems(MARSH_DRAKE_TALONS,1)
                st.playSound("ItemSound.quest_itemget")
            elif npcId in (BLOODY_QUEEN, BLOODY_QUEEN2) and st.getQuestItemsCount(REMAINS_OF_SACRIFICED)==0 and st.getInt("orven")== 0 :
                for i in range(8) :
                    st.addSpawn(SACRIFICE_OF_THE_SACRIFICED,180000)
            elif npcId == SACRIFICE_OF_THE_SACRIFICED and st.getQuestItemsCount(REMAINS_OF_SACRIFICED)==0 and st.getInt("orven")== 0 :
                st.giveItems(REMAINS_OF_SACRIFICED,1)
                st.playSound("ItemSound.quest_itemget")
            elif npcId == HARIT_LIZARDMAN_SHAMAN and st.getRandom(5) == 0 and st.getQuestItemsCount(TOTEM_OF_LAND_DRAGON)==0 and st.getInt("kendra")== 0 :
                for i in range(3) :
                    st.addSpawn(HARIT_LIZARDMAN_ZEALOT,180000)
            elif npcId == HARIT_LIZARDMAN_ZEALOT and st.getQuestItemsCount(TOTEM_OF_LAND_DRAGON)==0 and st.getInt("kendra")== 0 :
                st.giveItems(TOTEM_OF_LAND_DRAGON,1)
                st.playSound("ItemSound.quest_itemget")
        elif cond == 2 :
            if npcId == GUARDIAN1 and st.getQuestItemsCount(MARA_FANG)==0 and st.getInt("moke")<>1 :
                st.giveItems(MARA_FANG,1)
                st.set("aspawned","1")
                st.playSound("ItemSound.quest_itemget")
            elif npcId == GUARDIAN2 and st.getQuestItemsCount(MUSFEL_FANG)==0 and st.getInt("helton")<>1 :
                st.giveItems(MUSFEL_FANG,1)
                st.set("bspawned","1")
                st.playSound("ItemSound.quest_itemget")
        elif cond == 4:
            if npcId in (CAVE_MAIDEN, CAVE_KEEPER, CAVE_KEEPER1, CAVE_MAIDEN1) and st.getQuestItemsCount(THIRD_FRAGMENT_OF_ABYSS_JEWEL)==0 and st.getRandom(5) == 0 :
                mob = st.addSpawn(ABYSS_JEWEL3,180000)
    elif npcId == ABYSS_JEWEL1 :
        self.cancelQuestTimer("Jewel1_Timer1",npc,None)
        self.cancelQuestTimer("Jewel1_Timer2",npc,None)
    elif npcId == ABYSS_JEWEL2 :
        self.cancelQuestTimer("Jewel2_Timer1",npc,None)
        self.cancelQuestTimer("Jewel2_Timer2",npc,None)
    return

QUEST = Quest(337,qn,"Audience With The Land Dragon")
QUEST.addStartNpc(GABRIELLE)

QUEST.addAttackId(ABYSS_JEWEL1)
QUEST.addAttackId(ABYSS_JEWEL2)
QUEST.addAttackId(ABYSS_JEWEL3)

for npc in NPCS :
    QUEST.addTalkId(npc)

for mob in MOBS :
    QUEST.addKillId(mob)