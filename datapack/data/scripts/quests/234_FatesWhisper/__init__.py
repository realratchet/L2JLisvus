# Made by Fulminus, version 0.1

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

PIPETTE_KNIFE = 4665
REIRIAS_SOUL_ORB = 4666
KERMONS_INFERNIUM_SCEPTER = 4667
GOLCONDAS_INFERNIUM_SCEPTER = 4668
HALLATES_INFERNIUM_SCEPTER = 4669
REORINS_HAMMER = 4670
REORINS_MOLD = 4671
INFERNIUM_VARNISH = 4672
RED_PIPETTE_KNIFE = 4673
STAR_OF_DESTINY = 5011
CRYSTAL_B = 1460

#Reorin, Cliff, Ferris, Zenkin, Kaspar, Kernon's Chest, Golkonda's Chest, Hallate's Chest, Cabrio's "Coffer of the Dead"
NPC=[8002,7182,7847,7178,7833,8028,8029,8030,8027]

CHEST_SPAWNS = {
  10035:8027, # Shilen's Messenger Cabrio
  10054:8028, # Demon Kernon
  10126:8029, # Golkonda, the Longhorn General
  10220:8030  # Death Lord Hallate
  }

Weapons={
79:"Sword of Damascus",
2626:"Samurai Dualsword",
287:"Bow of Peril",
1303:"Lance",
175:"Art of Battle Axe",
210:"Staff of Evil Spirits",
234:"Demon Dagger" ,
268:"Bellion Cestus" ,
171:"Deadman's Glory"
}

class Quest (JQuest) :

	def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

	def onEvent (self,event,st) :
		htmltext = event
		#accept quest
		if event == "1" :
			st.set("cond","1")
			st.setState(State.STARTED)
			htmltext = "8002-03.htm"
		# talking with cliff...last dialog to get the Infernium Varnish
		elif event == "7182_01" :
			htmltext = "7182-01c.htm"
			st.giveItems(INFERNIUM_VARNISH,1)
		# open Kernon's Chest
		elif event == "8028_01" :
			htmltext = "8028-02.htm"
			st.giveItems(KERMONS_INFERNIUM_SCEPTER,1)
		# open Hallate's Chest
		elif event == "8029_01" :
			htmltext = "8029-02.htm"
			st.giveItems(GOLCONDAS_INFERNIUM_SCEPTER,1)
		# open Golkonda's Chest
		elif event == "8030_01" :
			htmltext = "8030-02.htm"
			st.giveItems(HALLATES_INFERNIUM_SCEPTER,1)
		# dialog with Zenkin
		elif event == "7178_01" :
			st.set("cond","6")
			htmltext = "7178-01a.htm"
		# dialog with Kaspar - go to baium
		elif event == "7833_01a" :
			htmltext = "7833-01b.htm"
			st.giveItems(PIPETTE_KNIFE,1)
			st.set("cond","7")
		## FINAL ITEM EXCHANGE SECTION
		elif event.startswith("selectBGrade_"):
			if st.getInt("bypass") :
                            return
			bGradeId = event.replace("selectBGrade_", "")
			st.set("weaponId", bGradeId)
                        htmltext = st.showHtmlFile("8002-13.htm").replace("%weaponname%",Weapons[int(bGradeId)])
                elif event.startswith("confirmWeapon"):
                        st.set("bypass","1")
                        htmltext = st.showHtmlFile("8002-14.htm").replace("%weaponname%",Weapons[st.getInt("weaponId")])
		elif event.startswith("selectAGrade_"):
			if st.getInt("bypass"):
				if st.getQuestItemsCount(st.getInt("weaponId")) > 0 :
                                        aGradeItemId = int(event.replace("selectAGrade_", ""))
                                        htmltext = "8002-12.htm"
                                        st.takeItems(st.getInt("weaponId"),1)
                                        st.giveItems(aGradeItemId,1)
                                        st.giveItems(STAR_OF_DESTINY,1)
                                        st.setState(State.COMPLETED)
                                        st.unset("cond")
                                        st.unset("bypass")
                                        st.unset("weaponId")
                                else:
                                        htmltext = st.showHtmlFile("8002-15.htm").replace("%weaponname%",Weapons[st.getInt("weaponId")])
			else:
				htmltext="<html><body>Maestro Reorin:<br>Are you trying to cheat me?! What happenned to the weapon you were about to give me for the neutralization of Infernum's evil aura?</body></html>"
				#st.exitQuest(1)
		return htmltext

	def onTalk(self,npc,st):
		npcId=npc.getNpcId()
		id =  st.getState()
		htmltext = JQuest.getNoQuestMsg()
		# first time when a player join the quest
		if id == State.CREATED:
			if st.getPlayer().getLevel() >= 75:
				htmltext = "8002-02.htm"
			else:
				htmltext = "8002-01.htm"
				st.exitQuest(1)
			return htmltext
		# if quest is already completed
		elif id == State.COMPLETED:
			return JQuest.getAlreadyCompletedMsg()
		# if quest is accepted and in progress
		elif id == State.STARTED:
			cond =st.getInt("cond")
			if npcId == NPC[0] :
				if cond == 1 and not st.getQuestItemsCount(REIRIAS_SOUL_ORB) :  # waiting for the orb
					htmltext = "8002-04b.htm"
				elif cond == 1 :	#got the orb!  Go to the next step (infernium scepter pieces)
					st.takeItems(REIRIAS_SOUL_ORB,1)
					htmltext = "8002-05.htm"
					st.set("cond","2")
				# waiting for infernium scepter pieces
				elif cond == 2 and (st.getQuestItemsCount(KERMONS_INFERNIUM_SCEPTER)+st.getQuestItemsCount(GOLCONDAS_INFERNIUM_SCEPTER)+st.getQuestItemsCount(HALLATES_INFERNIUM_SCEPTER) < 3) :
					htmltext = "8002-05c.htm"
				elif cond == 2 :	#got the infernium scepter pieces!  Go to the next step (infernium Varnish)
					st.takeItems(KERMONS_INFERNIUM_SCEPTER,1)
					st.takeItems(GOLCONDAS_INFERNIUM_SCEPTER,1)
					st.takeItems(HALLATES_INFERNIUM_SCEPTER,1)
					htmltext = "8002-06.htm"
					st.set("cond","3")
				# waiting for infernium varnish
				elif cond == 3 and not st.getQuestItemsCount(INFERNIUM_VARNISH) :
					htmltext = "8002-06b.htm"
				elif cond == 3 :	#got the infernium varnish!  Go to the next step (Reorin's Hammer)
					st.takeItems(INFERNIUM_VARNISH,1)
					htmltext = "8002-07.htm"
					st.set("cond","4")
				# waiting for Reorin's Hammer
				elif cond == 4 and not st.getQuestItemsCount(REORINS_HAMMER) :
					htmltext = "8002-07b.htm"
				elif cond == 4 :	# got Reorin's Hammer!  Go to the next step (Reorin's Mold)
					st.takeItems(REORINS_HAMMER,1)
					htmltext = "8002-08.htm"
					st.set("cond","5")
				elif cond < 8 :	 	# waiting for Reorin's Mold
					htmltext = "8002-08b.htm"
				elif cond == 8 :	# got Reorin's Mold!  Go to the next step (B Crystals)
					st.takeItems(REORINS_MOLD,1)
					htmltext = "8002-09.htm"
					st.set("cond","9")
				# waiting for 984 B Grade Crystals
				elif cond == 9 and (st.getQuestItemsCount(CRYSTAL_B) < 984) :
					htmltext = "8002-09a.htm"
				elif cond == 9 : # got the crystals
					st.takeItems(CRYSTAL_B,984)
					htmltext = "8002-BGradeList.htm"
					st.set("cond","10")
				# all is ready.  Now give a menu to trade the B weapon for the player's choice of A Weapon.
				elif cond == 10:
					if st.getInt("bypass") :
                                                if st.getQuestItemsCount(st.getInt("weaponId")) > 0 :
                                                        htmltext = st.showHtmlFile("8002-AGradeList.htm").replace("%weaponname%",Weapons[st.getInt("weaponId")])
						else :
                                                        htmltext = st.showHtmlFile("8002-15.htm").replace("%weaponname%",Weapons[st.getInt("weaponId")])
					else :
						htmltext = "8002-BGradeList.htm"
			## CLIFF.
			# came to take the varnish
			elif npcId == NPC[1] and cond==3 and not st.getQuestItemsCount(INFERNIUM_VARNISH) :
				htmltext = "7182-01.htm"
			# you already got the varnish...why are you back?
			elif npcId == NPC[1] and (cond>=3 or st.getQuestItemsCount(INFERNIUM_VARNISH)) :
				htmltext = "7182-02.htm"
			## FERRIS
			# go to take the mold			
			elif npcId == NPC[2] and cond==4 and not st.getQuestItemsCount(REORINS_HAMMER) :
				htmltext = "7847-01.htm"	# go to trader Zenkin
				st.giveItems(REORINS_HAMMER,1)
			# I already told you I don't have it!  
			elif npcId == NPC[2] and cond>=4 :
				htmltext = "7847-02.htm"	# go to trader Zenkin
			## ZENKIN
			# go to take mold
			elif npcId == NPC[3] and cond==5 :
				htmltext = "7178-01.htm"	# go to Magister Kaspar
			# I already told you I don't have it!  
			elif npcId == NPC[3] and cond>5 :
				htmltext = "7178-02.htm"	# go to Magister Kaspar
			## KASPAR
			elif npcId == NPC[4]:
				# first visit: You have neither plain nor blooded knife.
				if cond==6 :
					htmltext = "7833-01.htm"	# go to Magister Hanellin,etc. Get Baium's Blood with the pipette
				# revisit before getting the blood: remind "go get the blood"
				if cond==7 and st.getQuestItemsCount(PIPETTE_KNIFE) and not st.getQuestItemsCount(RED_PIPETTE_KNIFE) :
					htmltext = "7833-02.htm"	# go to Magister Hanellin,etc. Get Baium's Blood with the pipette
				# got the blood and I'm ready to proceed
				if cond==7 and not st.getQuestItemsCount(PIPETTE_KNIFE) and st.getQuestItemsCount(RED_PIPETTE_KNIFE) :
					htmltext = "7833-03.htm"	# great! Here is your mold for Reorin
					st.takeItems(RED_PIPETTE_KNIFE,1)
					st.giveItems(REORINS_MOLD,1)
					st.set("cond","8")
				#revisit after you've gotten the mold: What are you still doing here?
				if st.getInt("cond") > 7 :
					htmltext = "7833-04.htm"	# Have you given the mold to Reorin, yet?
			## CHESTS FROM RAIDBOSSES
			elif cond==1 :
				if npcId ==NPC[8] and st.getQuestItemsCount(REIRIAS_SOUL_ORB)==0 :
					htmltext = "8027-01.htm"
					st.giveItems(REIRIAS_SOUL_ORB,1)
					st.playSound("Itemsound.quest_itemget")
			elif cond==2 :
				# Kernon's Chest
				if npcId == NPC[5] and st.getQuestItemsCount(KERMONS_INFERNIUM_SCEPTER)==0 :
					htmltext = "8028-01.htm"
				elif npcId == NPC[5] :
					htmltext = "<html><body>This chest looks empty.</body></html>"
				# Golkonda's Chest
				elif npcId == NPC[6] and st.getQuestItemsCount(GOLCONDAS_INFERNIUM_SCEPTER)==0 :
					htmltext = "8029-01.htm"
				elif npcId == NPC[6] :
					htmltext = "<html><body>This chest looks empty.</body></html>"
				# Hallate's Chest 
				elif npcId == NPC[7] and st.getQuestItemsCount(HALLATES_INFERNIUM_SCEPTER)==0 :
					htmltext = "8030-01.htm"
				elif npcId == NPC[7] :
					htmltext = "<html><body>This chest looks empty.</body></html>"
		return htmltext		

	def onAttack (self, npc, player, damage, isPet):
                st = player.getQuestState("234_FatesWhisper")
                if st:
                    if st.getState() != State.STARTED : return
		    npcId = npc.getNpcId()
		    if st.getInt("cond") == 7 and npcId == 12372 :
			if player.getActiveWeaponItem() != None and player.getActiveWeaponItem().getItemId() == PIPETTE_KNIFE and st.getQuestItemsCount(RED_PIPETTE_KNIFE) == 0:
                            st.giveItems(RED_PIPETTE_KNIFE,1)
                            st.takeItems(PIPETTE_KNIFE,1)
                            st.playSound("Itemsound.quest_itemget")
		return

        def onKill (self, npc, player, isPet):
                npcId=npc.getNpcId()
                # the chests always spawn, even if the RB is killed with nobody nearby doing the quest.
                if npcId in CHEST_SPAWNS.keys() :
                    self.addSpawn(CHEST_SPAWNS[npcId], npc.getX(), npc.getY(), npc.getZ(),npc.getHeading(), True, 60000)
                return

QUEST = Quest(234,"234_FatesWhisper","Fate's Whisper")
QUEST.addStartNpc(NPC[0])

for npcId in NPC:
  QUEST.addTalkId(npcId)

for mobId in CHEST_SPAWNS.keys():
  QUEST.addKillId(mobId)

QUEST.addAttackId(12372)