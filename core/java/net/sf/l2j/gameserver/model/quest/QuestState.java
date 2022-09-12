/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.model.quest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.instancemanager.QuestManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.L2DropData;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.ExShowQuestMark;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.QuestList;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.TutorialCloseHtml;
import net.sf.l2j.gameserver.network.serverpackets.TutorialEnableClientEvent;
import net.sf.l2j.gameserver.network.serverpackets.TutorialShowHtml;
import net.sf.l2j.gameserver.network.serverpackets.TutorialShowQuestionMark;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.util.Rnd;

/**
 * @author Luis Arias
 */
public final class QuestState
{
	protected static Logger _log = Logger.getLogger(QuestState.class.getName());
	
	public static final String SOUND_ACCEPT = "ItemSound.quest_accept";
	public static final String SOUND_ITEMGET = "ItemSound.quest_itemget";
	public static final String SOUND_MIDDLE = "ItemSound.quest_middle";
	public static final String SOUND_FINISH = "ItemSound.quest_finish";
	public static final String SOUND_GIVEUP = "ItemSound.quest_giveup";
	public static final String SOUND_JACKPOT = "ItemSound.quest_jackpot";
	public static final String SOUND_FANFARE = "ItemSound.quest_fanfare_2";
	public static final String SOUND_BEFORE_BATTLE = "Itemsound.quest_before_battle";
	
	public static final byte DROP_DIVMOD = 0;
	public static final byte DROP_FIXED_RATE = 1;
	public static final byte DROP_FIXED_COUNT = 2;
	public static final byte DROP_FIXED_BOTH = 3;
	
	/** Quest associated to the QuestState */
	private final String _questName;
	
	/** Player who engaged the quest */
	private final L2PcInstance _player;
	
	/** State of the quest */
	private byte _state;
	
	/** List of couples (variable for quest,value of the variable for quest) */
	private Map<String, String> _vars;
	
	/** Boolean flag letting QuestStateManager know to exit quest when cleaning up */
	private boolean _isExitQuestOnCleanUp = false;
	
	/**
	 * Constructor of the QuestState : save the quest in the list of quests of the player.<BR/>
	 * <BR/>
	 * <U><I>Actions :</U></I><BR/>
	 * <LI>Save informations in the object QuestState created (Quest, Player, Completion, State)</LI>
	 * <LI>Add the QuestState in the player's list of quests by using setQuestState()</LI>
	 * <BR/>
	 * @param quest : quest associated with the QuestState
	 * @param player : L2PcInstance pointing out the player
	 * @param state : state of the quest
	 */
	public QuestState(Quest quest, L2PcInstance player, byte state)
	{
		_questName = quest.getName();
		_player = player;
		
		// Save the state of the quest for the player in the player's list of quest owned
		getPlayer().setQuestState(this);
		
		// Set the state of the quest
		_state = state;
	}
	
	public String getQuestName()
	{
		return _questName;
	}
	
	/**
	 * Return the quest
	 * @return Quest
	 */
	public Quest getQuest()
	{
		return QuestManager.getInstance().getQuest(_questName);
	}
	
	/**
	 * Return the L2PcInstance
	 * @return L2PcInstance
	 */
	public L2PcInstance getPlayer()
	{
		return _player;
	}
	
	/**
	 * Return the state of the quest
	 * @return
	 */
	public byte getState()
	{
		return _state;
	}
	
	/**
	 * Return true if quest completed, false otherwise
	 * @return boolean
	 */
	public boolean isCompleted()
	{
		return (getState() == State.COMPLETED);
	}
	
	/**
	 * Return true if quest started, false otherwise
	 * @return boolean
	 */
	public boolean isStarted()
	{
		return (getState() == State.STARTED);
	}
	
	/**
	 * Return state of the quest after its initialization.<BR>
	 * <BR>
	 * <U><I>Actions :</I></U>
	 * <LI>Remove drops from previous state</LI>
	 * <LI>Set new state of the quest</LI>
	 * <LI>Add drop for new state</LI>
	 * <LI>Update information in database</LI>
	 * <LI>Send packet QuestList to client</LI>
	 * @param state
	 * @return object
	 */
	public Object setState(byte state)
	{
		// set new state if it is not already in that state
		if (_state != state)
		{
			// Set new state
			_state = state;
			
			if (state == State.COMPLETED)
			{
				// Clean registered quest items
				int[] itemIdList = getQuest().getRegisteredItemIds();
				if (itemIdList != null)
				{
					for (int i = 0; i < itemIdList.length; i++)
						takeItems(itemIdList[i], -1);
				}
			}
			
			Quest.updateQuestInDb(this);
			getPlayer().sendPacket(new QuestList());
		}
		return state;
	}
	
	/**
	 * Add parameter used in quests.
	 * @param var : String pointing out the name of the variable for quest
	 * @param val : String pointing out the value of the variable for quest
	 * @return String (equal to parameter "val")
	 */
	public String setInternal(String var, String val)
	{
		if (_vars == null)
			_vars = new HashMap<>();
		
		if (val == null)
			val = "";
		
		_vars.put(var, val);
		return val;
	}
	
	/**
	 * Return value of parameter "val" after adding the couple (var,val) in class variable "vars".<BR>
	 * <BR>
	 * <U><I>Actions :</I></U><BR>
	 * <LI>Initialize class variable "vars" if is null</LI>
	 * <LI>Initialize parameter "val" if is null</LI>
	 * <LI>Add/Update couple (var,val) in class variable Map "vars"</LI>
	 * <LI>If the key represented by "var" exists in Map "vars", the couple (var,val) is updated in the database. The key is known as
	 * existing if the preceding value of the key (given as result of function put()) is not null.<BR>
	 * If the key doesn't exist, the couple is added/created in the database</LI>
	 * @param var : String indicating the name of the variable for quest
	 * @param val : String indicating the value of the variable for quest
	 * @return String (equal to parameter "val")
	 */
	public String set(String var, String val)
	{
		if (_vars == null)
			_vars = new HashMap<>();
		
		if (val == null)
			val = "";
		
		// Map.put() returns previous value associated with specified key, or null if there was no mapping for key.
		String old = _vars.put(var, val);
		
		if (old != null)
			Quest.updateQuestVarInDb(this, var, val);
		else
			Quest.createQuestVarInDb(this, var, val);
		
		if (var.equals("cond"))
		{
			QuestList ql = new QuestList();
			
			getPlayer().sendPacket(ql);
			
			int questId = getQuest().getQuestIntId();
			if (questId > 0 && questId < 999 && !val.equals("0"))
				getPlayer().sendPacket(new ExShowQuestMark(questId));
		}
		
		return val;
	}
	
	/**
	 * Remove the variable of quest from the list of variables for the quest.<BR>
	 * <BR>
	 * <U><I>Concept : </I></U>
	 * Remove the variable of quest represented by "var" from the class variable Map "vars" and from the database.
	 * @param var : String designating the variable for the quest to be deleted
	 * @return String pointing out the previous value associated with the variable "var"
	 */
	public String unset(String var)
	{
		if (_vars == null)
			return null;
		
		String old = _vars.remove(var);
		
		if (old != null)
			Quest.deleteQuestVarInDb(this, var);
		
		return old;
	}
	
	/**
	 * Return the value of the variable of quest represented by "var"
	 * @param var : name of the variable of quest
	 * @return String
	 */
	public String get(String var)
	{
		if (_vars != null && _vars.get(var) != null)
			return _vars.get(var);
		return null;
	}
	
	/**
	 * Return the value of the variable of quest represented by "var"
	 * @param var : String designating the variable for the quest
	 * @return int
	 */
	public int getInt(String var)
	{
		if (_vars == null)
		{
			return 0;
		}
		
		final String variable = _vars.get(var);
		if (variable == null || variable.isEmpty())
		{
			return 0;
		}
		
		int varint = 0;
		try
		{
			varint = Integer.parseInt(_vars.get(var));
		}
		catch (Exception e)
		{
			_log.finer(getPlayer().getName() + ": variable " + var + " isn't an integer: " + varint + e);
		}
		
		return varint;
	}
	
	/**
	 * Add player to get notification of characters death
	 * @param character : L2Character of the character to get notification of death
	 */
	public void addNotifyOfDeath(L2Character character)
	{
		if (character == null)
			return;
		
		character.addNotifyQuestOfDeath(this);
	}
	
	/**
	 * Check for an item in player's inventory.
	 * @param itemId the ID of the item to check for
	 * @return True if the item exists in player's inventory, false otherwise
	 */
	public boolean hasQuestItems(int itemId)
	{
		return _player.getInventory().getItemByItemId(itemId) != null;
	}
	
	/**
	 * Check for multiple items in player's inventory.
	 * @param itemIds a list of item IDs to check for
	 * @return True if all items exist in player's inventory, false otherwise
	 */
	public boolean hasQuestItems(int... itemIds)
	{
		final Inventory inv = _player.getInventory();
		for (int itemId : itemIds)
		{
			if (inv.getItemByItemId(itemId) == null)
				return false;
		}
		return true;
	}

	/**
	 * Check if player possesses at least one given item.
	 * @param itemIds a list of item IDs to check for
	 * @return True if at least one item exists in player's inventory, false otherwise
	 */
	public boolean hasAtLeastOneQuestItem(int... itemIds)
	{
		final Inventory inv = _player.getInventory();
		for (int itemId : itemIds)
		{
			if (inv.getItemByItemId(itemId) != null)
				return true;
		}
		return false;
	}
	
	public int getQuestItemsCount(int itemId)
	{
		return getQuestItemsCount(getPlayer(), itemId);
	}
	
	/**
	 * Return the quantity of one sort of item hold by the player
	 * @param player
	 * @param itemId : ID of the item wanted to be count
	 * @return int
	 */
	public int getQuestItemsCount(L2PcInstance player, int itemId)
	{
		int count = 0;
		
		for (L2ItemInstance item : player.getInventory().getItems())
			if (item.getItemId() == itemId)
				count += item.getCount();
			
		return count;
	}
	
	/**
	 * Return the level of enchantment on the weapon of the player(Done specifically for weapon SA's)
	 * @param itemId : ID of the item to check enchantment
	 * @return int
	 */
	public int getEnchantLevel(int itemId)
	{
		L2ItemInstance enchanteditem = getPlayer().getInventory().getItemByItemId(itemId);
		
		if (enchanteditem == null)
			return 0;
		
		return enchanteditem.getEnchantLevel();
	}
	
	/**
	 * Give item/reward to the player
	 * @param itemId
	 * @param count
	 * @return L2ItemInstance
	 */
	public L2ItemInstance giveItems(int itemId, int count)
	{
		return giveItems(itemId, count, 0);
	}
	
	public L2ItemInstance giveItems(int itemId, int count, int enchantlevel)
	{
		return giveItems(getPlayer(), itemId, count, 0);
	}
	
	public L2ItemInstance giveItems(L2PcInstance player, int itemId, int count, int enchantlevel)
	{
		if (count <= 0)
		{
			return null;
		}
		
		// Set quantity of item
		// Add items to player's inventory
		L2ItemInstance item = player.getInventory().addItem("Quest", itemId, count, player, player.getTarget());
		if (item == null)
		{
			return item;
		}
		
		if (enchantlevel > 0)
		{
			item.setEnchantLevel(enchantlevel);
		}
		
		// If item for reward is gold, send message of gold reward to client
		if (itemId == Inventory.ADENA_ID)
		{
			SystemMessage smsg = new SystemMessage(SystemMessage.EARNED_ADENA);
			smsg.addNumber(count);
			player.sendPacket(smsg);
		}
		// Otherwise, send message of object reward to client
		else
		{
			if (count > 1)
			{
				SystemMessage smsg = new SystemMessage(SystemMessage.EARNED_S2_S1_s);
				smsg.addItemName(item.getItemId());
				smsg.addNumber(count);
				player.sendPacket(smsg);
			}
			else
			{
				SystemMessage smsg = new SystemMessage(SystemMessage.EARNED_ITEM);
				smsg.addItemName(item.getItemId());
				player.sendPacket(smsg);
			}
		}
		player.sendPacket(new ItemList(player, false));
		
		StatusUpdate su = new StatusUpdate(player.getObjectId());
		su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
		player.sendPacket(su);
		
		return item;
	}
	
	/**
	 * Drop items to the player's inventory. Rate is 100%, amount is affected by Config.RATE_QUEST_DROP.
	 * @param itemId : Identifier of the item to be dropped.
	 * @param count : Quantity of items to be dropped.
	 * @param neededCount : Quantity of items needed to complete the task. If set to 0, unlimited amount is collected.
	 * @return boolean : Indicating whether item quantity has been reached.
	 */
	public boolean dropItemsAlways(int itemId, int count, int neededCount)
	{
		return dropItems(itemId, count, neededCount, L2DropData.MAX_CHANCE, DROP_FIXED_RATE);
	}
	
	/**
	 * Drop items to the player's inventory. Rate and amount is affected by DIVMOD of Config.RATE_QUEST_DROP.
	 * @param itemId : Identifier of the item to be dropped.
	 * @param count : Quantity of items to be dropped.
	 * @param neededCount : Quantity of items needed to complete the task. If set to 0, unlimited amount is collected.
	 * @param dropChance : Item drop rate (100% chance is defined by the L2DropData.MAX_CHANCE = 1.000.000).
	 * @return boolean : Indicating whether item quantity has been reached.
	 */
	public boolean dropItems(int itemId, int count, int neededCount, int dropChance)
	{
		return dropItems(itemId, count, neededCount, dropChance, DROP_DIVMOD);
	}
	
	/**
	 * Drop items to the player's inventory.
	 * @param itemId : Identifier of the item to be dropped.
	 * @param count : Quantity of items to be dropped.
	 * @param neededCount : Quantity of items needed to complete the task. If set to 0, unlimited amount is collected.
	 * @param dropChance : Item drop rate (100% chance is defined by the L2DropData.MAX_CHANCE = 1.000.000).
	 * @param type : Item drop behavior: DROP_DIVMOD (rate and), DROP_FIXED_RATE, DROP_FIXED_COUNT or DROP_FIXED_BOTH
	 * @return boolean : Indicating whether item quantity has been reached.
	 */
	public boolean dropItems(int itemId, int count, int neededCount, int dropChance, byte type)
	{
		// Get current amount of item
		final int currentCount = getQuestItemsCount(itemId);
		
		// Check if required amount has already been reached
		if (neededCount > 0 && currentCount >= neededCount)
			return true;
		
		int amount = 0;
		switch (type)
		{
			case DROP_DIVMOD:
				dropChance *= Config.RATE_DROP_QUEST;
				amount = count * (dropChance / L2DropData.MAX_CHANCE);
				if (Rnd.get(L2DropData.MAX_CHANCE) < dropChance % L2DropData.MAX_CHANCE)
					amount += count;
				break;
			
			case DROP_FIXED_RATE:
				if (Rnd.get(L2DropData.MAX_CHANCE) < dropChance)
					amount = (int) (count * Config.RATE_DROP_QUEST);
				break;
			
			case DROP_FIXED_COUNT:
				if (Rnd.get(L2DropData.MAX_CHANCE) < dropChance * Config.RATE_DROP_QUEST)
					amount = count;
				break;
			
			case DROP_FIXED_BOTH:
				if (Rnd.get(L2DropData.MAX_CHANCE) < dropChance)
					amount = count;
				break;
		}
		
		boolean reached = false;
		if (amount > 0)
		{
			// Limit count to reach required amount
			if (neededCount > 0)
			{
				reached = (currentCount + amount) >= neededCount;
				amount = (reached) ? neededCount - currentCount : amount;
			}
			
			// Inventory slot check
			if (!_player.getInventory().validateCapacityByItemId(itemId))
				return false;
			
			// Give items to the player
			giveItems(itemId, amount, 0);
			
			playSound(reached ? SOUND_MIDDLE : SOUND_ITEMGET);
		}
		
		return neededCount > 0 && reached;
	}
	
	/**
	 * Drop multiple items to the player's inventory. Rate and amount is affected by DIVMOD of Config.RATE_QUEST_DROP.
	 * @param rewardsInfos : Infos regarding drops (itemId, count, neededCount, dropChance).
	 * @return boolean : Indicating whether item quantity has been reached.
	 */
	public boolean dropMultipleItems(int[][] rewardsInfos)
	{
		return dropMultipleItems(rewardsInfos, DROP_DIVMOD);
	}
	
	/**
	 * Drop items to the player's inventory.
	 * @param rewardsInfos : Infos regarding drops (itemId, count, neededCount, dropChance).
	 * @param type : Item drop behavior: DROP_DIVMOD (rate and), DROP_FIXED_RATE, DROP_FIXED_COUNT or DROP_FIXED_BOTH
	 * @return boolean : Indicating whether item quantity has been reached.
	 */
	public boolean dropMultipleItems(int[][] rewardsInfos, byte type)
	{
		// Used for the sound
		boolean sendSound = false;
		
		// Used for the reached state
		boolean reached = true;
		
		// For each reward type, calculate the probability of drop
		for (int[] info : rewardsInfos)
		{
			final int itemId = info[0];
			final int currentCount = getQuestItemsCount(itemId);
			final int neededCount = info[2];
			
			// Check if required amount has already been reached
			if (neededCount > 0 && currentCount >= neededCount)
				continue;
			
			final int count = info[1];
			
			int dropChance = info[3];
			int amount = 0;
			
			switch (type)
			{
				case DROP_DIVMOD:
					dropChance *= Config.RATE_DROP_QUEST;
					amount = count * (dropChance / L2DropData.MAX_CHANCE);
					if (Rnd.get(L2DropData.MAX_CHANCE) < dropChance % L2DropData.MAX_CHANCE)
						amount += count;
					break;
				
				case DROP_FIXED_RATE:
					if (Rnd.get(L2DropData.MAX_CHANCE) < dropChance)
						amount = (int) (count * Config.RATE_DROP_QUEST);
					break;
				
				case DROP_FIXED_COUNT:
					if (Rnd.get(L2DropData.MAX_CHANCE) < dropChance * Config.RATE_DROP_QUEST)
						amount = count;
					break;
				
				case DROP_FIXED_BOTH:
					if (Rnd.get(L2DropData.MAX_CHANCE) < dropChance)
						amount = count;
					break;
			}
			
			if (amount > 0)
			{
				// Limit count to reach required amount
				if (neededCount > 0)
					amount = ((currentCount + amount) >= neededCount) ? neededCount - currentCount : amount;
				
				// Inventory slot check
				if (!_player.getInventory().validateCapacityByItemId(itemId))
					continue;
				
				// Give items to the player
				giveItems(itemId, amount, 0);
				
				// Send sound
				sendSound = true;
			}
			
			// Unlimited needed count or current count being inferior to needed count means the state isn't reached
			if (neededCount <= 0 || ((currentCount + amount) < neededCount))
				reached = false;
		}
		
		// Play the sound
		if (sendSound)
			playSound((reached) ? SOUND_MIDDLE : SOUND_ITEMGET);
		
		return reached;
	}
	
	/**
	 * Reward player with items. The amount is affected by Config.RATE_QUEST_REWARD or Config.RATE_QUEST_REWARD_ADENA.
	 * @param itemId : Identifier of the item.
	 * @param itemCount : Quantity of item to reward before applying multiplier.
	 */
	public void rewardItems(int itemId, int itemCount)
	{
		if (itemId == 57)
			giveItems(itemId, (int) (itemCount * Config.RATE_QUEST_REWARD_ADENA), 0);
		else
			giveItems(itemId, (int) (itemCount * Config.RATE_QUEST_REWARD), 0);
	}
	
	// TODO: More radar functions need to be added when the radar class is complete.
	// BEGIN STUFF THAT WILL PROBABLY BE CHANGED
	public void addRadar(int x, int y, int z)
	{
		getPlayer().getRadar().addMarker(x, y, z);
	}
	
	public void removeRadar(int x, int y, int z)
	{
		getPlayer().getRadar().removeMarker(x, y, z);
	}
	
	public void clearRadar()
	{
		getPlayer().getRadar().removeAllMarkers();
	}
	// END STUFF THAT WILL PROBABLY BE CHANGED
	
	/**
	 * Remove items from player's inventory when talking to NPC in order to have rewards.<BR>
	 * <BR>
	 * <U><I>Actions :</I></U>
	 * <LI>Destroy quantity of items wanted</LI>
	 * <LI>Send new inventory list to player</LI>
	 * @param itemId : Identifier of the item
	 * @param count : Quantity of items to destroy
	 * @return L2ItemInstance
	 */
	public L2ItemInstance takeItems(int itemId, int count)
	{
		return takeItems(getPlayer(), itemId, count);
	}
	
	public L2ItemInstance takeItems(L2PcInstance player, int itemId, int count)
	{
		// Get object item from player's inventory list
		L2ItemInstance item = player.getInventory().getItemByItemId(itemId);
		if (item == null)
		{
			return item;
		}
		
		// Tests on count value in order not to have negative value
		if (count < 0 || count > item.getCount())
			count = item.getCount();
		
		// Destroy the quantity of items wanted
		if (itemId == Inventory.ADENA_ID)
			player.reduceAdena("Quest", count, player, true);
		else
		{
			if (item.isEquipped())
				player.getInventory().unEquipItemInBodySlotAndRecord(item.getItem().getBodyPart());
			
			player.destroyItemByItemId("Quest", itemId, count, player, true);
		}
		return item;
	}
	
	/**
	 * Send a packet in order to play sound at client terminal
	 * @param sound
	 */
	public void playSound(String sound)
	{
		playSound(getPlayer(), sound);
	}
	
	public void playSound(L2PcInstance player, String sound)
	
	{
		player.sendPacket(new PlaySound(sound));
		
	}
	
	/**
	 * Return random value
	 * @param max : max value for randomization
	 * @return int
	 */
	public int getRandom(int max)
	{
		return Rnd.get(max);
	}
	
	/**
	 * Add XP and SP as quest reward
	 * @param exp
	 * @param sp
	 */
	public void addExpAndSp(long exp, int sp)
	{
		getPlayer().addExpAndSp((long) getPlayer().calcStat(Stats.EXPSP_RATE, exp * Config.RATE_QUEST_REWARD_XP, null, null), (int) getPlayer().calcStat(Stats.EXPSP_RATE, sp * Config.RATE_QUEST_REWARD_SP, null, null));
	}
	
	/**
	 * Return number of ticks from GameTimeController
	 * @param loc
	 * @return int
	 */
	public int getItemEquipped(int loc)
	{
		return getPlayer().getInventory().getPaperdollItemId(loc);
	}
	
	/**
	 * Return the number of ticks from the GameTimeController
	 * @return int
	 */
	public int getGameTicks()
	{
		return GameTimeController.getInstance().getGameTicks();
	}
	
	/**
	 * Return true if quest is to exited on clean up by QuestStateManager
	 * @return boolean
	 */
	public final boolean isExitQuestOnCleanUp()
	{
		return _isExitQuestOnCleanUp;
	}
	
	/**
	 * Return the QuestTimer object with the specified name
	 * @param isExitQuestOnCleanUp
	 */
	public void setIsExitQuestOnCleanUp(boolean isExitQuestOnCleanUp)
	{
		_isExitQuestOnCleanUp = isExitQuestOnCleanUp;
	}
	
	public void startQuestTimer(String name, long time)
	{
		getQuest().startQuestTimer(name, time, null, getPlayer(), false);
	}
	
	public void startQuestTimer(String name, long time, L2NpcInstance npc)
	{
		getQuest().startQuestTimer(name, time, npc, getPlayer(), false);
	}
	
	public void startRepeatingQuestTimer(String name, long time)
	{
		getQuest().startQuestTimer(name, time, null, getPlayer(), true);
	}
	
	public void startRepeatingQuestTimer(String name, long time, L2NpcInstance npc)
	{
		getQuest().startQuestTimer(name, time, npc, getPlayer(), true);
	}
	
	public final QuestTimer getQuestTimer(String name)
	{
		return getQuest().getQuestTimer(name, null, getPlayer());
	}
	
	/**
	 * Add spawn for player instance
	 * Return object id of newly spawned npc
	 * @param npcId
	 * @return
	 */
	public L2NpcInstance addSpawn(int npcId)
	{
		return addSpawn(npcId, getPlayer().getX(), getPlayer().getY(), getPlayer().getZ(), 0, false, 0);
	}
	
	public L2NpcInstance addSpawn(int npcId, int despawnDelay)
	{
		return addSpawn(npcId, getPlayer().getX(), getPlayer().getY(), getPlayer().getZ(), 0, false, despawnDelay);
	}
	
	public L2NpcInstance addSpawn(int npcId, int x, int y, int z)
	{
		return addSpawn(npcId, x, y, z, 0, false, 0);
	}
	
	/**
	 * Add spawn for player instance
	 * Will despawn after the spawn length expires
	 * Uses player's coords and heading.
	 * Adds a little randomization in the x y coords
	 * Return object id of newly spawned npc
	 * @param npcId
	 * @param cha
	 * @return
	 */
	public L2NpcInstance addSpawn(int npcId, L2Character cha)
	{
		return addSpawn(npcId, cha, true, 0);
	}
	
	public L2NpcInstance addSpawn(int npcId, L2Character cha, int despawnDelay)
	{
		return addSpawn(npcId, cha.getX(), cha.getY(), cha.getZ(), cha.getHeading(), true, despawnDelay);
	}
	
	/**
	 * Add spawn for player instance
	 * Will despawn after the spawn length expires
	 * Return object id of newly spawned npc
	 * @param npcId
	 * @param x
	 * @param y
	 * @param z
	 * @param despawnDelay
	 * @return
	 */
	public L2NpcInstance addSpawn(int npcId, int x, int y, int z, int despawnDelay)
	{
		return addSpawn(npcId, x, y, z, 0, false, despawnDelay);
	}
	
	/**
	 * Add spawn for player instance
	 * Inherits coords and heading from specified L2Character instance.
	 * It could be either the player, or any killed/attacked mob
	 * Return object id of newly spawned npc
	 * @param npcId
	 * @param cha
	 * @param randomOffset
	 * @param despawnDelay
	 * @return
	 */
	public L2NpcInstance addSpawn(int npcId, L2Character cha, boolean randomOffset, int despawnDelay)
	{
		return addSpawn(npcId, cha.getX(), cha.getY(), cha.getZ(), cha.getHeading(), randomOffset, despawnDelay);
	}
	
	/**
	 * Add spawn for player instance
	 * Return object id of newly spawned npc
	 * @param npcId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @param randomOffset
	 * @param despawnDelay
	 * @return
	 */
	public L2NpcInstance addSpawn(int npcId, int x, int y, int z, int heading, boolean randomOffset, int despawnDelay)
	{
		return getQuest().addSpawn(npcId, x, y, z, heading, randomOffset, despawnDelay);
	}
	
	/**
	 * Show HTML file to client
	 * @param fileName
	 * @return String : message sent to client
	 */
	public String showHtmlFile(String fileName)
	{
		return getQuest().showHtmlFile(getPlayer(), fileName);
	}
	
	/**
	 * Destroy element used by quest when quest is exited
	 * @param repeatable
	 * @return QuestState
	 */
	public QuestState exitQuest(boolean repeatable)
	{
		if (isCompleted())
			return this;
		
		// Say quest is completed
		setState(State.COMPLETED);
		
		// If quest is repeatable, delete quest from list of quest of the player and from database (quest CAN be created again => repeatable)
		if (repeatable)
		{
			getPlayer().delQuestState(getQuestName());
			Quest.deleteQuestInDb(this);
		}
		else
		{
			// Otherwise, delete variables for quest and update database (quest CANNOT be created again => not repeatable)
			if (_vars != null)
			{
				for (String var : _vars.keySet())
				{
					Quest.deleteQuestVarInDb(this, var);
				}
			}
			Quest.updateQuestInDb(this);
		}
		
		if (_vars != null)
		{
			_vars.clear();
			_vars = null;
		}
		
		return this;
	}
	
	public void showQuestionMark(int number)
	{
		getPlayer().sendPacket(new TutorialShowQuestionMark(number));
	}
	
	public void playTutorialVoice(String voice)
	{
		getPlayer().sendPacket(new PlaySound(2, voice, 0, 0, getPlayer().getX(), getPlayer().getY(), getPlayer().getZ()));
	}
	
	public void showTutorialHTML(String html)
	{
		String text = HtmCache.getInstance().getHtm("data/scripts/quests/255_Tutorial/" + html);
		if (text == null)
		{
			_log.warning("missing html page data/scripts/quests/255_Tutorial/" + html);
			text = "<html><body>File data/scripts/quests/255_Tutorial/" + html + " not found or file is empty.</body></html>";
		}
		getPlayer().sendPacket(new TutorialShowHtml(text));
	}
	
	public void closeTutorialHtml()
	{
		getPlayer().sendPacket(new TutorialCloseHtml());
	}
	
	public void onTutorialClientEvent(int number)
	{
		getPlayer().sendPacket(new TutorialEnableClientEvent(number));
	}
	
	public void dropItem(L2MonsterInstance npc, L2PcInstance player, int itemId, int count)
	{
		npc.dropItem(player, itemId, count);
	}
	
	/**
	 * Quest: Pursuit of Clan Ambition
	 */
	public void exitMembers()
	{
		L2Clan clan = getPlayer().getClan();
		if (clan == null)
			return;
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			for (L2ClanMember member : clan.getMembers())
			{
				if (member == null)
					continue;
				
				if (member.isOnline() && member.getPlayerInstance() != null)
				{
					QuestState memberState = member.getPlayerInstance().getQuestState(getQuest().getName());
					if (memberState != null)
						memberState.exitQuest(true);
				}
				else
				{
					try (PreparedStatement statement = con.prepareStatement("DELETE FROM character_quests WHERE name = ? and char_id = ?"))
					{
						statement.setString(1, getQuest().getName());
						statement.setInt(2, member.getObjectId());
						statement.executeUpdate();
					}
				}
			}
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * Quest: Pursuit of Clan Ambition
	 * @param var
	 * @return
	 */
	public int getLeaderState(String var)
	{
		L2Clan clan = getPlayer().getClan();
		if (clan == null)
			return -1;
		
		L2PcInstance leader = clan.getLeader().getPlayerInstance();
		if (leader != null)
		{
			if (leader.getQuestState(getQuest().getName()) != null)
				return leader.getQuestState(getQuest().getName()).getInt(var);
		}
		
		int val = -1;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT value FROM character_quests WHERE char_id=? AND var=? AND name=?"))
		{
			statement.setInt(1, clan.getLeaderId());
			statement.setString(2, var);
			statement.setString(3, getQuest().getName());
			try (ResultSet rs = statement.executeQuery())
			{
				if (rs.next())
					val = rs.getInt("value");
			}
		}
		catch (Exception e)
		{
			return -1;
		}
		
		return val;
	}
}