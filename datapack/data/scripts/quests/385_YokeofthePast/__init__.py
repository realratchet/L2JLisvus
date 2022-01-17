import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

ANCIENT_SCROLL = 5902

class Quest (JQuest) :

 def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

 def onEvent (self,event,st) :
    htmltext = event
    if event == "14.htm" :
      st.setState(State.STARTED)
      st.playSound("ItemSound.quest_accept")
      st.set("cond","1")
    elif event == "16.htm" :
      st.playSound("ItemSound.quest_finish")
      st.exitQuest(1)
    return htmltext

 def onTalk (self,npc,st):
   npcId = npc.getNpcId()
   htmltext = JQuest.getNoQuestMsg()
   id = st.getState()
   if id == State.CREATED :
       htmltext = "10.htm"
       st.set("cond","0")
   elif st.getInt("cond") == 1 and st.getQuestItemsCount(ANCIENT_SCROLL) == 0 :
       htmltext = "17.htm"
   elif st.getInt("cond") == 1 and st.getQuestItemsCount(ANCIENT_SCROLL):
        htmltext = "16.htm"
        numancientscrolls = st.getQuestItemsCount(ANCIENT_SCROLL)
        st.giveItems(5965,numancientscrolls)
        st.takeItems(ANCIENT_SCROLL,-1)
   else:
     st.exitQuest(1)  # cond is always 1 if he accepted the quest, but we have no way to check if he hasnt the quest, so we delete it if he didnt accept by first talk
   return htmltext

 def onKill (self,npc,player,isPet):
    partyMember = self.getRandomPartyMemberState(player, npc, State.STARTED)
    if not partyMember : return
    st = partyMember.getQuestState("385_YokeofthePast")
    if st :
      npcId = npc.getNpcId()
      if st.getRandom(10)<6 :
        st.giveItems(ANCIENT_SCROLL,1)
        st.playSound("ItemSound.quest_itemget")
    return

QUEST = Quest(385,"385_YokeofthePast","Yoke of the Past")

for npcId in range(8095,8126):
    if npcId in [8111,8112,8113]:
        continue
    QUEST.addTalkId(npcId)
    QUEST.addStartNpc(npcId)

for mobs in range(1208,1256):
    QUEST.addKillId(mobs)