# Upgrade your Hatchling to Strider version 0.2
# by DrLecter & DraX_
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.network.serverpackets import NpcSay

#Quest info
QUEST_NUMBER      = 421
QUEST_NAME        = "LittleWingAdventures"
QUEST_DESCRIPTION = "Little Wing's Big Adventures"

#Configuration

#Minimum pet and player levels required to complete the quest (defaults 55 and 45)
MIN_PET_LEVEL = 55
MIN_PLAYER_LEVEL = 45
# Maximum distance allowed between pet and owner; if it's reached while talking to any NPC, quest is aborted
MAX_DISTANCE = 100

#Messages
default = JQuest.getNoQuestMsg()
event_1 = "<html><body>Sage Cronos:<br>Then go and see <font color=\"LEVEL\">Fairy Mymyu</font>, she will help you</body></html>"
error_1 = "<html><body>You're supposed to own a hatchling and have it summoned in order to complete this quest.</body></html>"
error_2 = "<html><body>Hey! What happened with the other hatchling you had? This one is different.</body></html>"
error_3 = "<html><body>Sage Cronos:<br>You need to be level "+str(MIN_PLAYER_LEVEL)+" to complete this quest.</body></html>"
error_4 = "<html><body>Sage Cronos:<br>Your pet needs to be level "+str(MIN_PET_LEVEL)+" in order to complete this quest.</body></html>"
error_5 = "Your pet is not a hatchling."
error_6 = "Your pet should be nearby."
qston_1 = "<html><body>Sage Cronos:<br>So, you want to turn your hatchling into a more powerful creature?<br><br><a action=\"bypass -h Quest "+str(QUEST_NUMBER)+"_"+QUEST_NAME+" 16\">Yes, please tell me how</a><br></body></html>"
qston_2 = "<html><body>Sage Cronos:<br>I've said you need to talk to <font color=\"LEVEL\">Fairy Mymyu</font>!!!. Am i clear???</body></html>"
qston_3 = "<html><body>Fairy Mymyu:<br>You weren't yet able to find the <font color=\"LEVEL\">Fairy Trees of Wind, Star, Twilight and Abyss</font>? Don't give up! They are all in <font color=\"LEVEL\">Hunter's Valley</font></body></html>"
order_1 = "<html><body>Fairy Mymyu:<br>Your pet must drink the sap of <font color=\"LEVEL\">Fairy Trees of Wind, Star, Twilight and Abyss</font> to grow up. The trees will probably agree but as we don't want to hurt them, take that leafs to heal any wound your hatchling could cause them</body></html>"
end_msg = "<html><body>Fairy Mymyu:<br>Great job, your hatchling"
end_msg2= "has become a strider, enjoy!</body></html>"

#Quest items
FT_LEAF = 4325
CONTROL_ITEMS = { 3500:4422, 3501:4423, 3502:4424 }

#NPCs
SG_CRONOS = 7610
FY_MYMYU  = 7747

def get_control_item(st) :
  item = st.getPlayer().getPet().getControlItemObjectId()
  if st.getState() == State.CREATED :
      st.set("item",str(item))
  else :
      if  st.getInt("item") != item : item = 0
  return item  

def get_distance(st) :
    is_far = False
    if abs(st.getPlayer().getPet().getX() - st.getPlayer().getX()) > MAX_DISTANCE :
        is_far = True
    if abs(st.getPlayer().getPet().getY() - st.getPlayer().getY()) > MAX_DISTANCE :
        is_far = True
    if abs(st.getPlayer().getPet().getZ() - st.getPlayer().getZ()) > MAX_DISTANCE :
        is_far = True
    return is_far

class Quest (JQuest) :

 def __init__(self,id,name,descr):
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = [FT_LEAF]

 def onEvent (self,event,st) :
    htmltext = event
    if event == "16" :
       htmltext = event_1
       st.setState(State.STARTED)
       st.set("cond","1")
       st.playSound("ItemSound.quest_accept")
    return htmltext

 def onTalk (self,npc,st):
   htmltext = default
   id = st.getState()
   npcid = npc.getNpcId()
   if st.getPlayer().getPet() == None :
       return error_1
   elif st.getPlayer().getPet().getTemplate().npcId not in [12311,12312,12313] : #npcids for hatchlings
       return error_5
   elif st.getPlayer().getPet().getLevel() < MIN_PET_LEVEL :
       return error_4
   elif get_distance(st) :
       return error_6
   elif get_control_item(st) == 0 :
       return error_2
   elif npcid == SG_CRONOS :
      if id == State.CREATED :
         if st.getPlayer().getLevel() < MIN_PLAYER_LEVEL :
            return error_3
         else :   
            htmltext = qston_1
      else :
         htmltext = qston_2
   elif npcid == FY_MYMYU :
     if id == State.STARTED and st.getInt("cond") < 3 :
        if st.getQuestItemsCount(FT_LEAF) == 0 :
           st.set("cond","2")
           st.giveItems(FT_LEAF,4)
           st.set("windTree","0")
           st.set("starTree","0")
           st.set("twilightTree","0")
           st.set("abyssTree","0")
           st.playSound("ItemSound.quest_itemget")
           htmltext = order_1
        else :
            htmltext = qston_3
     elif id == State.STARTED and st.getInt("cond") >= 3 :
        name = st.getPlayer().getPet().getName()
        if name == None : name = " "
        else : name = " "+name+" "
        htmltext = end_msg+name+end_msg2
        item = CONTROL_ITEMS[st.getPlayer().getInventory().getItemByObjectId(st.getPlayer().getPet().getControlItemObjectId()).getItemId()]
        st.getPlayer().getPet().deleteMe(st.getPlayer()) #both despawn pet and delete control item
        st.giveItems(item,1)
        st.exitQuest(1)
        st.playSound("ItemSound.quest_finish")
   return htmltext

 def onAttack(self, npc, player, damage, isPet) :
   st = player.getQuestState(str(QUEST_NUMBER)+"_"+QUEST_NAME)
   if not st:
     return
   if st.getInt("cond") == 2 :
     pet = player.getPet()
     if isPet and pet.getTemplate().npcId in [12311,12312,12313] : #npcids for hatchlings
       if st.getRandom(100) <= 2 and st.getQuestItemsCount(FT_LEAF) > 0 :
         npcId = npc.getNpcId()
         if npcId == 5185 and st.getInt("windTree") != 1 :
           st.set("windTree","1")
         elif npcId == 5186 and st.getInt("starTree") != 1 :
           st.set("starTree","1")
         elif npcId == 5187 and st.getInt("twilightTree") != 1 :
           st.set("twilightTree","1")
         elif npcId == 5188 and st.getInt("abyssTree") != 1 :
           st.set("abyssTree","1")
         else :
           return
         st.takeItems(FT_LEAF,1)
         st.playSound("ItemSound.quest_middle")
         npc.broadcastPacket(NpcSay(npc.getObjectId(),0,npc.getNpcId(),"gives me spirit leaf...!"))
         if st.getInt("windTree") == 1 and st.getInt("starTree") == 1 and st.getInt("twilightTree") == 1 and st.getInt("abyssTree") == 1 :
           st.set("cond","3")
   return

# Quest class and state definition
QUEST = Quest(QUEST_NUMBER, str(QUEST_NUMBER)+"_"+QUEST_NAME, QUEST_DESCRIPTION)
# Quest NPC starter initialization
QUEST.addStartNpc(SG_CRONOS)

# Quest initialization
QUEST.addTalkId(SG_CRONOS)
QUEST.addTalkId(FY_MYMYU)

for i in range(5185,5189):
  QUEST.addAttackId(i)