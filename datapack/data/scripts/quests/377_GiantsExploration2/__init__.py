# Exploration of Giants Cave, part 2 version 0.1 
# by DrLecter
import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

#Quest info
QUEST_NUMBER,QUEST_NAME,QUEST_DESCRIPTION = 377,"GiantsExploration2","Exploration of Giants Cave, part 2"

#Variables
#Titan Ancient Books drop rate in %
DROP_RATE=15*Config.RATE_DROP_QUEST
MAX = 100

#Quest items
ANC_BOOK = 5955
DICT2    = 5892

#Items
BOOK1,BOOK2,BOOK3,BOOK4,BOOK5 = [5945, 5946, 5947, 5948, 5949]
ALMANAC1,ALMANAC2,ALMANAC3,ALMANAC4,ALMANAC5 = [5950, 5951, 5952, 5953, 5954]

#Messages
default = JQuest.getNoQuestMsg()
error_1   = "<html><body>Head Researcher Sobling:<br><br>I think it is too early for you to help me. Come back after you have gained some more experience. <br><font color=\"LEVEL\">(Quest for characters level 57 and above.)</font></body></html>"
start     = "<html><body>Head Researcher Sobling:<br><br>So Cliff sent us this dictionary, i can see clearly now. It's very impressive... There are more relics for we to find out and maybe you will help us as a future member of our excavation team. We should look for <font color=\"LEVEL\">The book of the Titan's science, and the Book of the Titan's Culture.</font><br><br>Our payment for such a discovery cannot be rejected so easily, <font color=\"LEVEL\">A grade recipes</font> used in the manufacture of top level armors... Of course i won't give you anything just for fragments, you will have to gather every piece of a given book.<br><br><a action=\"bypass -h Quest 377_GiantsExploration2 yes\">I will search for ancient books</a><br><a action=\"bypass -h Quest 377_GiantsExploration2 0\">I won't help you this time.</a><br></body></html>"
starting  = "Starting.htm"
checkout  = "<html><body>Head Researcher Sobling:<br><br>Excellent! You came back! Was it difficult to collect ancient books?<br><br>Let me see what you've found thus far...<br><br><a action=\"bypass -h Quest 377_GiantsExploration2 show\">Show him the books you collected.</a></body></html>"
checkout2 = "<html><body>Head Researcher Sobling:<br><br>Excellent! You came back! Was it difficult to collect ancient books?<br><br>Hum... what is it? You have some untranslated book fragments here, but those are of no use to me until you translate its contents, i have no time to do it on my own and that's why i gave you the dictionary Cliff sent to me. Anyway i can check any other translated fragments you may have...<br><br><a action=\"bypass -h Quest 377_GiantsExploration2 show\">Show him the fragments.</a></body></html>"
no_items  = "<html><body>Head Researcher Sobling:<br><br>Hum... I don't see any valuable or complete book here, you should continue your research. I'm pretty sure you can do it better if you put more effort... what you think?<br><br><a action=\"bypass -h Quest 377_GiantsExploration2 Starting.htm\">I will continue.</a><br><a action=\"bypass -h Quest 377_GiantsExploration2 0\">I will quit.</a><br></body></html>"
tnx4items = "<html><body>Head Researcher Sobling:<br><br>Amazing! These are the sort of items i was looking for... Take this rare recipes as a proof of my gratitude. Anyhow, I'm sure there are more ancient relics guarded by those monsters, would you like to search some more?<br><br><a action=\"bypass -h Quest 377_GiantsExploration2 Starting.htm\">I will continue.</a><br><a action=\"bypass -h Quest 377_GiantsExploration2 0\">I will quit.</a><br></body></html>"
ext_msg   = "Quest aborted"

#NPCs
HR_SOBLING = 8147

#Mobs
MOBS = [ 654,656,657,658 ]

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [DICT2]

 def onEvent (self,event,st) :
    id = st.getState()
    htmltext = event
    if event == "yes" :
       htmltext = starting
       st.setState(State.STARTED)
       st.set("cond","1")
       st.playSound("ItemSound.quest_accept")
    elif event == "0" :
       htmltext = ext_msg
       st.playSound("ItemSound.quest_finish")
       st.takeItems(DICT2,1)
       st.exitQuest(1)
    elif event == "show" :
       htmltext = no_items
       if st.getQuestItemsCount(BOOK1) > 0 and st.getQuestItemsCount(BOOK2) > 0 and st.getQuestItemsCount(BOOK3) > 0 and st.getQuestItemsCount(BOOK4) > 0 and st.getQuestItemsCount(BOOK5) > 0 :
          st.takeItems(BOOK1,1)
          st.takeItems(BOOK2,1)
          st.takeItems(BOOK3,1)
          st.takeItems(BOOK4,1)
          st.takeItems(BOOK5,1)
          luck = st.getRandom(MAX)
          if luck > 50 :
             st.giveItems(5336,1) #nightmare leather 60%
          else :
             st.giveItems(5338,1) #majestic leather 60%
          htmltext = tnx4items
       if st.getQuestItemsCount(ALMANAC1) > 0 and st.getQuestItemsCount(ALMANAC2) > 0 and st.getQuestItemsCount(ALMANAC3) > 0 and st.getQuestItemsCount(ALMANAC4) > 0 and st.getQuestItemsCount(ALMANAC5) > 0 :
          st.takeItems(ALMANAC1,1)
          st.takeItems(ALMANAC2,1)
          st.takeItems(ALMANAC3,1)
          st.takeItems(ALMANAC4,1)
          st.takeItems(ALMANAC5,1)
          luck = st.getRandom(MAX)
          if luck > 50 :
             st.giveItems(5422,1) #majestic plate 60%
          else :
             st.giveItems(5420,1) #nightmare armor 60%
          htmltext = tnx4items
    return htmltext

 def onTalk (self,npc,st):
   htmltext = default
   id = st.getState()
   if st.getQuestItemsCount(DICT2) != 1 :
      st.exitQuest(1) 
   elif id == State.CREATED :
      st.set("cond","0")
      htmltext = start
      if st.getPlayer().getLevel() < 57 :
         st.exitQuest(1)
         htmltext = error_1
   elif id == State.STARTED :
      if st.getQuestItemsCount(ANC_BOOK) == 0 :
         htmltext = checkout
      else :
         htmltext = checkout2
   return htmltext

 def onKill (self,npc,player,isPet) :
   partyMember = self.getRandomPartyMemberState(player,npc,State.STARTED)
   if not partyMember : return
   st = partyMember.getQuestState(str(QUEST_NUMBER)+"_"+QUEST_NAME)
   if st :
     numItems, chance = divmod(DROP_RATE,MAX)
     drop = st.getRandom(MAX)
     if drop < chance :
        numItems = numItems + 1
     if int(numItems) != 0 :
        st.giveItems(ANC_BOOK,int(numItems))
        st.playSound("ItemSound.quest_itemget")
   return  

# Quest class and state definition
QUEST = Quest(QUEST_NUMBER, str(QUEST_NUMBER)+"_"+QUEST_NAME, QUEST_DESCRIPTION)
# Quest NPC starter initialization
QUEST.addStartNpc(HR_SOBLING)

# Quest initialization
QUEST.addTalkId(HR_SOBLING)

for i in MOBS :
  QUEST.addKillId(i)