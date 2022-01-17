# Linnaeus Special Bait - a seamless merge from Next and DooMita contributions(adapted for L2JLisvus by roko91)

import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "53_LinnaeusSpecialBait"

LINNAEUS = 8577
CRIMSON_DRAKE = 670
CRIMSON_DRAKE_HEART = 7624
FLAMING_FISHING_LURE = 7613
#Drop chance
CHANCE = 50
#Custom setting: whether or not to check for fishing skill level?
#default False to require fishing skill level, any other value to ignore fishing
#and evaluate char level only.
ALT_IGNORE_FISHING=False

def fishing_level(player):
    if ALT_IGNORE_FISHING :
       level=20
    else :
       level = player.getSkillLevel(1315)
       for effect in player.getAllEffects():
          if effect.getSkill().getId() == 2274:
            level = int(effect.getSkill().getPower(player))
            break
    return level

class Quest (JQuest):

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [CRIMSON_DRAKE_HEART]

 def onEvent (self,event,st):
     htmltext = event
     if event == "8577-1.htm":
        st.setState(State.STARTED)
        st.set("cond","1")
        st.playSound("ItemSound.quest_accept")
     elif event == "8577-3.htm":
        cond = st.getInt("cond")
        if cond == 2 and st.getQuestItemsCount(CRIMSON_DRAKE_HEART) == 100:
           st.giveItems(FLAMING_FISHING_LURE, 4)
           st.takeItems(CRIMSON_DRAKE_HEART, 100)                
           st.setState(State.COMPLETED)
           st.unset("cond") # we dont need it in db if quest is already completed
           st.playSound("ItemSound.quest_finish")
        else :
           htmltext = "8577-5.htm"
     return htmltext

 def onTalk (self,npc,st):
     htmltext=JQuest.getNoQuestMsg()
     id = st.getState()
     if id == State.COMPLETED:
        htmltext = JQuest.getAlreadyCompletedMsg()           
     elif id == State.CREATED :
        if st.getPlayer().getLevel() > 59 and fishing_level(st.getPlayer()) > 19 :
           htmltext= "8577-0.htm"
        else:
           st.exitQuest(1)
           htmltext= "8577-0a.htm"
     elif id == State.STARTED:
        if st.getInt("cond") == 1:
            htmltext = "8577-4.htm"
        else :
            htmltext = "8577-2.htm"
     return htmltext

 def onKill(self,npc,player,isPet):
     partyMember = self.getRandomPartyMember(player,npc,"1")
     if not partyMember : return
     st = partyMember.getQuestState(qn)
     if st :
        count = st.getQuestItemsCount(CRIMSON_DRAKE_HEART)
        if st.getInt("cond") == 1 and count < 100 :
           chance = 33 * Config.RATE_DROP_QUEST
           numItems, chance = divmod(chance,100)
           if st.getRandom(100) < chance :
              numItems += 1
           if numItems :
              if count + numItems >= 100 :
                 numItems = 100 - count
                 st.playSound("ItemSound.quest_middle")
                 st.set("cond","2")
              else:
                 st.playSound("ItemSound.quest_itemget")
              st.giveItems(CRIMSON_DRAKE_HEART,1)
     return

QUEST = Quest(53, qn, "Linnaeus Special Bait")
QUEST.addStartNpc(LINNAEUS)

QUEST.addTalkId(LINNAEUS)

QUEST.addKillId(CRIMSON_DRAKE)