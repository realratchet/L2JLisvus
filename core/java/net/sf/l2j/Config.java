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
package net.sf.l2j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.base.Experience;
import net.sf.l2j.gameserver.util.FloodProtectorConfig;
import net.sf.l2j.util.StringUtil;

/**
 * This class contains global server configuration.<br>
 * It has static final fields initialized from configuration files.<br>
 * It's initialized at the very begin of startup, and later JIT will optimize away debug/unused code.
 * @author mkizub
 */
public final class Config
{
	private final static Logger _log = Logger.getLogger(Config.class.getName());
	
	/** Properties file for access level configurations */
	public static final String ACCESS_LEVELS_FILE = "./config/AccessLevels.properties";
	/** Properties file for alternative configurations */
	public static final String ALT_SETTINGS_FILE = "./config/AltSettings.properties";
	/** Properties file for custom configurations */
	public static final String CUSTOM_FILE = "./config/Custom.properties";
	/** Properties file for events configurations */
	public static final String EVENTS_FILE = "./config/Events.properties";
	/** Properties file for extensions */
	public static final String EXTENSIONS_FILE = "./config/Extensions.properties";
	/** Properties file for feature configurations */
	public static final String FEATURE_FILE = "./config/Feature.properties";
	/** Properties file for Flood Protector configurations */
	public static final String FLOOD_PROTECTOR_FILE = "./config/FloodProtector.properties";
	/** Properties file for game server (connection and ingame) configurations */
	public static final String GAME_SERVER_FILE = "./config/GameServer.properties";
	/** Properties file for geodata configurations */
	public static final String GEODATA_FILE = "./config/GeoData.properties";
	/** Properties file for grand bosses configurations */
	public static final String GRAND_BOSS_FILE = "./config/GrandBoss.properties";
	/** Properties file for the ID factory */
	public static final String ID_FACTORY_FILE = "./config/IdFactory.properties";
	/** Properties file for login server configurations */
	public static final String LOGIN_SERVER_FILE = "./config/LoginServer.properties";
	/** Properties file for MMO configurations */
	public static final String MMO_FILE = "./config/MMO.properties";
	/** Properties file for olympiad configurations */
	public static final String OLYMPIAD_FILE = "./config/Olympiad.properties";
	/** Properties file for game server options */
	public static final String OPTIONS_FILE = "./config/Options.properties";
	/** Properties file for other configurations */
	public static final String OTHER_FILE = "./config/Other.properties";
	/** Properties file for PVP configurations */
	public static final String PVP_FILE = "./config/PVP.properties";
	/** Properties file for rates configurations */
	public static final String RATES_FILE = "./config/Rates.properties";
	/** Properties file for server version configurations */
	public static final String SERVER_VERSION_FILE = "./config/l2j-version.properties";
	/** Properties file for siege configurations */
	public static final String SIEGE_FILE = "./config/Siege.properties";
	/** Properties file for telnet configurations */
	public static final String TELNET_FILE = "./config/Telnet.properties";
	
	/** Text file containing hexadecimal value of server ID */
	public static final String HEXID_FILE = "./config/hexid.txt";
	
	/** Debug/release mode */
	public static boolean DEBUG;
	/** Enable/disable assertions */
	public static boolean ASSERT;
	/** Enable/disable code 'in progress' */
	public static boolean DEVELOPER;
	public static boolean ACCEPT_GEOEDITOR_CONN;
	
	/** Set if this server is a test server used for development */
	public static boolean TEST_SERVER;
	
	/** Enable upnp service */
	public static boolean ENABLE_UPNP;
	
	/** Game Server ports */
	public static int PORT_GAME;
	/** Login Server port */
	public static int PORT_LOGIN;
	/** Login Server bind ip */
	public static String LOGIN_BIND_ADDRESS;
	/** Number of tries of login before ban */
	public static int LOGIN_TRY_BEFORE_BAN;
	/** Number of seconds the IP ban will last, default 10 minutes */
	public static int LOGIN_BLOCK_AFTER_BAN;
	
	/** Hostname of the Game Server */
	public static String GAMESERVER_HOSTNAME;
	
	// Access to database
	/** Driver to access to database */
	public static String DATABASE_DRIVER;
	/** Path to access to database */
	public static String DATABASE_URL;
	/** Database login */
	public static String DATABASE_LOGIN;
	/** Database password */
	public static String DATABASE_PASSWORD;
	/** Maximum number of connections to the database */
	public static int DATABASE_MAX_CONNECTIONS;
	/** Maximum idle time of connections to the database */
	public static int DATABASE_MAX_IDLE_TIME;
	
	/** Maximum number of players allowed to play simultaneously on server */
	public static int MAXIMUM_ONLINE_USERS;
	
	// Setting for serverList
	/** Displays [] in front of server name ? */
	public static boolean SERVER_LIST_BRACKET;
	/** Displays a clock next to the server name ? */
	public static boolean SERVER_LIST_CLOCK;
	/** Display test server in the list of servers ? */
	public static boolean SERVER_LIST_TESTSERVER;
	/** Set the server as gm only at startup ? */
	public static boolean SERVER_GMONLY;
	
	// Thread pools size
	/** Thread pool size effect */
	public static int THREAD_P_EFFECTS;
	/** Thread pool size general */
	public static int THREAD_P_GENERAL;
	/** Packet max thread */
	public static int GENERAL_PACKET_THREAD_CORE_SIZE;
	public static int IO_PACKET_THREAD_CORE_SIZE;
	/** General max thread */
	public static int GENERAL_THREAD_CORE_SIZE;
	/** AI max thread */
	public static int AI_MAX_THREAD;
	
	/** Accept auto-loot ? */
	public static boolean AUTO_LOOT;
	/** Accept auto-loot for RBs ? */
	public static boolean AUTO_LOOT_RAIDS;
	
	public static boolean LEVEL_UP_SOUL_CRYSTAL_WHEN_HAS_MANY;
	
	/** Character name template */
	public static String CHAR_NAME_TEMPLATE;
	/** Pet name template */
	public static String PET_NAME_TEMPLATE;
	/** Clan & Alliance name template */
	public static String CLAN_ALLY_NAME_TEMPLATE;
	/** Maximum number of characters per account */
	public static int MAX_CHARACTERS_NUMBER_PER_ACCOUNT;
	
	/** Global chat state */
	public static String DEFAULT_GLOBAL_CHAT;
	/** Trade chat state */
	public static String DEFAULT_TRADE_CHAT;
	/** For test servers - everybody has admin rights */
	public static boolean EVERYBODY_HAS_ADMIN_RIGHTS;
	/** Display server version */
	public static boolean DISPLAY_SERVER_VERSION;
	/** Alternative game crafting */
	public static boolean ALT_GAME_CREATION;
	/** Alternative game crafting speed mutiplier - default 0 (fastest but still not instant) */
	public static double ALT_GAME_CREATION_SPEED;
	/** Alternative game crafting XP rate multiplier - default 1 */
	public static double ALT_GAME_CREATION_XP_RATE;
	/** Alternative game crafting SP rate multiplier - default 1 */
	public static double ALT_GAME_CREATION_SP_RATE;
	/** Blacksmith NPC uses recipes on craft - default true */
	public static boolean ALT_BLACKSMITH_USE_RECIPES;
	/** Check if skills learned by a character are legal */
	public static boolean SKILL_CHECK_ENABLE;
	
	/** Block exp/sp command */
	public static boolean Boost_EXP_COMMAND;
	/** Enable Auto NPC target */
	public static boolean AUTO_TARGET_NPC;
	/** Show L2Npc crest ? */
	public static boolean SHOW_NPC_CREST;
	/** Enable Real Time */
	public static boolean ENABLE_REAL_TIME;
	
	/** Alternative auto skill learning */
	public static boolean AUTO_LEARN_SKILLS;
	/** Alternative auto skill learning for 3rd class */
	public static boolean AUTO_LEARN_3RD_SKILLS;
	/** Cancel attack bow by hit */
	public static boolean ALT_GAME_CANCEL_BOW;
	/** Cancel cast by hit */
	public static boolean ALT_GAME_CANCEL_CAST;
	
	/** Alternative game - use tiredness, instead of CP */
	public static boolean ALT_GAME_TIREDNESS;
	
	/** Party Range */
	public static int ALT_PARTY_RANGE;
	public static int ALT_PARTY_RANGE2;
	
	/** Alternative Perfect shield defense rate */
	public static int ALT_PERFECT_SHLD_BLOCK;
	
	/** Alternative mob aggro in peaceful zone */
	public static boolean ALT_MOB_AGGRO_IN_PEACEZONE;
	
	/** Alternative freight modes - Freights can be withdrawn from any village */
	public static boolean ALT_GAME_FREIGHTS;
	/** Alternative freight modes - Sets the price value for each freightened item */
	public static int ALT_GAME_FREIGHT_PRICE;
	
	/** Alternative gaming - loss of XP on death */
	public static boolean ALT_GAME_DELEVEL;
	
	/** Alternative Weight Limit */
	public static double ALT_WEIGHT_LIMIT;
	
	/** Alternative gaming - magic dmg failures */
	public static boolean ALT_GAME_MAGICFAILURES;
	
	/** Alternative gaming - player must be in a castle-owning clan or ally to sign up for Dawn. */
	public static boolean ALT_GAME_REQUIRE_CASTLE_DAWN;
	
	/** Alternative gaming - allow clan-based castle ownage check rather than ally-based. */
	public static boolean ALT_GAME_REQUIRE_CLAN_CASTLE;
	
	/** Alternative gaming - allow free teleporting around the world. */
	public static boolean ALT_GAME_FREE_TELEPORT;
	
	/** Disallow recommend character twice or more a day ? */
	public static boolean ALT_RECOMMEND;
	
	/** Alternative gaming - add more or less than 3 sub-classes. */
	public static int ALT_MAX_SUBCLASS;
	
	/** Alternative gaming - allow sub-class addition without quest completion. */
	public static boolean ALT_GAME_SUBCLASS_WITHOUT_QUESTS;
	
	/** Alternative gaming - allow/disallow tutorial. */
	public static boolean ALT_ENABLE_TUTORIAL;
	
	/** View npc stats/drop by shift-cliking it for nongm-players */
	public static boolean ALT_GAME_VIEWNPC;
	
	/** Minimum number of player to participate in SevenSigns Festival */
	public static int ALT_FESTIVAL_MIN_PLAYER;
	
	/** Maximum of player contrib during Festival */
	public static int ALT_MAXIMUM_PLAYER_CONTRIB;
	
	/** Festival Manager start time. */
	public static long ALT_FESTIVAL_MANAGER_START;
	
	/** Festival Length */
	public static long ALT_FESTIVAL_LENGTH;
	
	/** Festival Cycle Length */
	public static long ALT_FESTIVAL_CYCLE_LENGTH;
	
	/** Festival First Spawn */
	public static long ALT_FESTIVAL_FIRST_SPAWN;
	
	/** Festival First Swarm */
	public static long ALT_FESTIVAL_FIRST_SWARM;
	
	/** Festival Second Spawn */
	public static long ALT_FESTIVAL_SECOND_SPAWN;
	
	/** Festival Second Swarm */
	public static long ALT_FESTIVAL_SECOND_SWARM;
	
	/** Festival Chest Spawn */
	public static long ALT_FESTIVAL_CHEST_SPAWN;
	
	/** Number of members needed to request a clan war */
	public static int ALT_CLAN_MEMBERS_FOR_WAR;
	
	/** Number of days before joining a new clan */
	public static int ALT_CLAN_JOIN_DAYS;
	/** Number of days before creating a new clan */
	public static int ALT_CLAN_CREATE_DAYS;
	/** Number of days it takes to dissolve a clan */
	public static int ALT_CLAN_DISSOLVE_DAYS;
	/** Number of days it takes to dissolve a clan again */
	public static int ALT_RECOVERY_PENALTY;
	
	/** Number of days before joining a new alliance when clan voluntarily leave an alliance */
	public static int ALT_ALLY_JOIN_DAYS_WHEN_LEAVED;
	/** Number of days before joining a new alliance when clan was dismissed from an alliance */
	public static int ALT_ALLY_JOIN_DAYS_WHEN_DISMISSED;
	/** Number of days before accepting a new clan for alliance when clan was dismissed from an alliance */
	public static int ALT_ACCEPT_CLAN_DAYS_WHEN_DISMISSED;
	/** Number of days before creating a new alliance when dissolved an alliance */
	public static int ALT_CREATE_ALLY_DAYS_WHEN_DISSOLVED;
	/** Maximum number of clans in ally */
	public static int ALT_MAX_NUM_OF_CLANS_IN_ALLY;
	/** Minimum number of parties to activate command channel */
	public static int ALT_CHANNEL_ACTIVATION_COUNT;
	
	/** Alternative gaming - all new characters always are newbies. */
	public static boolean ALT_GAME_NEW_CHAR_ALWAYS_IS_NEWBIE;
	/** Newbie level range. */
	public static byte ALT_MIN_NEWBIE_LEVEL;
	public static byte ALT_MAX_NEWBIE_LEVEL;
	
	/** Spell Book needed to learn skill */
	public static boolean SP_BOOK_NEEDED;
	/** Spell Book needet to enchant skill */
	public static boolean ES_SP_BOOK_NEEDED;
	/** Logging Chat Window */
	public static boolean LOG_CHAT;
	/** Logging Item Window */
	public static boolean LOG_ITEMS;
	
	/** Olympiad Competition Starting time */
	public static int ALT_OLY_START_TIME;
	
	/** Olympiad Minutes */
	public static int ALT_OLY_MIN;
	
	/** Olympiad Competition Period */
	public static long ALT_OLY_CPERIOD;
	
	/** Olympiad Battle Period */
	public static long ALT_OLY_BATTLE;
	
	/** Olympiad Battle Wait */
	public static long ALT_OLY_BWAIT;
	
	/** Olympiad Inital Wait */
	public static long ALT_OLY_IWAIT;
	
	/** Olympiad Weekly Period */
	public static long ALT_OLY_WPERIOD;
	
	/** Olympiad Validation Period */
	public static long ALT_OLY_VPERIOD;
	
	/** Olympiad Base Class */
	public static int ALT_OLY_CLASSED;
	
	/** Olympiad Non Base Class */
	public static int ALT_OLY_NONCLASSED;
	
	public static int ALT_OLY_REG_DISPLAY;
	
	/** Olympiad Battle Reward */
	public static int ALT_OLY_BATTLE_REWARD_ITEM;
	
	/** Olympiad Class Based Reward Count */
	public static int ALT_OLY_CLASSED_RITEM_C;
	
	/** Olympiad Non Base Reward Count */
	public static int ALT_OLY_NONCLASSED_RITEM_C;
	
	/** Olympiad Competition Reward */
	public static int ALT_OLY_COMP_RITEM;
	
	/** Olympiad Item Reward */
	public static int ALT_OLY_GP_PER_POINT;
	
	/** Olympiad Hero Points */
	public static int ALT_OLY_HERO_POINTS;
	public static int ALT_OLY_RANK1_POINTS;
	public static int ALT_OLY_RANK2_POINTS;
	public static int ALT_OLY_RANK3_POINTS;
	public static int ALT_OLY_RANK4_POINTS;
	public static int ALT_OLY_RANK5_POINTS;
	public static int ALT_OLY_MAX_POINTS;
	
	/** Both players lose olympiad points on tie */
	public static boolean ALT_OLY_LOSE_POINTS_ON_TIE;
	
	/** Olympiad Show Monthly Winners */
	public static boolean ALT_OLY_SHOW_MONTHLY_WINNERS;
	
	/** Enchant limit for player items in Grand Olympiad games */
	public static int ALT_OLY_ENCHANT_LIMIT;
	
	/** Manor Refresh Starting time */
	public static int ALT_MANOR_REFRESH_TIME;
	
	/** Manor Refresh Min */
	public static int ALT_MANOR_REFRESH_MIN;
	
	/** Manor Next Period Approve Starting time */
	public static int ALT_MANOR_APPROVE_TIME;
	
	/** Manor Next Period Approve Min */
	public static int ALT_MANOR_APPROVE_MIN;
	
	/** Manor Maintenance Time */
	public static int ALT_MANOR_MAINTENANCE_PERIOD;
	
	/** Manor Save All Actions */
	public static boolean ALT_MANOR_SAVE_ALL_ACTIONS;
	
	/** Manor Save Period Rate */
	public static int ALT_MANOR_SAVE_PERIOD_RATE;
	
	/** Initial Lottery prize */
	public static int ALT_LOTTERY_PRIZE;
	
	/** Lottery Ticket Price */
	public static int ALT_LOTTERY_TICKET_PRICE;
	
	/** What part of jackpot amount should receive characters who pick 5 wining numbers */
	public static float ALT_LOTTERY_5_NUMBER_RATE;
	
	/** What part of jackpot amount should receive characters who pick 4 wining numbers */
	public static float ALT_LOTTERY_4_NUMBER_RATE;
	
	/** What part of jackpot amount should receive characters who pick 3 wining numbers */
	public static float ALT_LOTTERY_3_NUMBER_RATE;
	
	/** How much adena receive characters who pick two or less of the winning number */
	public static int ALT_LOTTERY_2_AND_1_NUMBER_PRIZE;
	
	/** Four Sepulcher Settings */
	public static int FS_PENDING_TIME;
	public static int FS_ENTRY_TIME;
	public static int FS_PARTY_MEMBER_COUNT;
	
	/** Minimum size of a party that may enter dimensional rift */
	public static int RIFT_MIN_PARTY_SIZE;
	
	/** Time in ms the party has to wait until the mobs spawn when entering a room */
	public static int RIFT_SPAWN_DELAY;
	
	/** Amount of random rift jumps before party is ported back */
	public static int RIFT_MAX_JUMPS;
	
	/** Random time between two jumps in dimensional rift - in seconds */
	public static int RIFT_AUTO_JUMPS_TIME_MIN;
	public static int RIFT_AUTO_JUMPS_TIME_MAX;
	
	/** Dimensional Fragment cost for entering rift */
	public static int RIFT_ENTER_COST_RECRUIT;
	public static int RIFT_ENTER_COST_SOLDIER;
	public static int RIFT_ENTER_COST_OFFICER;
	public static int RIFT_ENTER_COST_CAPTAIN;
	public static int RIFT_ENTER_COST_COMMANDER;
	public static int RIFT_ENTER_COST_HERO;
	
	/** Time multiplier for boss room */
	public static float RIFT_BOSS_ROOM_TIME_MULTIPLIER;
	
	/** The highest access level */
	public static int MASTER_ACCESS_LEVEL;
	/** General GM AccessLevel to unstuck without 5min delay */
	public static int GM_ESCAPE;
	/** General GM AccessLevel to resurrect fixed after death */
	public static int GM_FIXED;
	/** General GM AccessLevel to attack in the peace zone */
	public static int GM_PEACE_ATTACK;
	/** General GM AccessLevel for performing transactions */
	public static int GM_TRANSACTION;
	/** General GM AccessLevel for disregarding item use restrictions */
	public static int GM_ITEM_RESTRICTION;
	/** General GM AccessLevel for disregarding skill use restrictions */
	public static int GM_SKILL_RESTRICTION;
	
	/* Rate control */
	/** Rate for eXperience Point rewards */
	public static float RATE_XP;
	/** Rate for Skill Point rewards */
	public static float RATE_SP;
	/** Rate for party eXperience Point rewards */
	public static float RATE_PARTY_XP;
	/** Rate for party Skill Point rewards */
	public static float RATE_PARTY_SP;
	/** Rate for Quest item rewards */
	public static float RATE_QUEST_REWARD;
	/** Rate for Quest adena rewards */
	public static float RATE_QUEST_REWARD_ADENA;
	/** Rate for Quest xp rewards */
	public static float RATE_QUEST_REWARD_XP;
	/** Rate for Quest sp rewards */
	public static float RATE_QUEST_REWARD_SP;
	/** Rate for drop adena */
	public static float RATE_DROP_ADENA;
	/** Rate for cost of consumable */
	public static float RATE_CONSUMABLE_COST;
	/** Rate for dropped items */
	public static float RATE_DROP_ITEMS;
	/** Rate for dropped items for bosses */
	public static float RATE_BOSS_DROP_ITEMS;
	/** Rate for spoiled items */
	public static float RATE_DROP_SPOIL;
	/** Rate for manor items */
	public static int RATE_DROP_MANOR;
	/** Rate for extracting fish */
	public static float RATE_EXTRACT_FISH;
	/** Rate for quest items */
	public static float RATE_DROP_QUEST;
	/** Rate for karma and experience lose */
	public static float RATE_KARMA_EXP_LOST;
	/** Rate siege guards prices */
	public static float RATE_SIEGE_GUARDS_PRICE;
	/**
	 * Alternative Xp/Sp rewards, if not 0, then calculated as 2^((mob.level-player.level) / coef), A few examples for "AltGameExponentXp = 5." and "AltGameExponentSp = 3." diff = 0 (player and mob has the same level), XP bonus rate = 1, SP bonus rate = 1 diff = 3 (mob is 3 levels above), XP bonus
	 * rate = 1.52, SP bonus rate = 2 diff = 5 (mob is 5 levels above), XP bonus rate = 2, SP bonus rate = 3.17 diff = -8 (mob is 8 levels below), XP bonus rate = 0.4, SP bonus rate = 0.16
	 */
	/** Alternative experience Point rewards */
	public static float ALT_GAME_EXPONENT_XP;
	/** Alternative Spirit Point rewards */
	public static float ALT_GAME_EXPONENT_SP;
	
	// Player Drop Rate control
	/** Limit for player drop */
	public static int PLAYER_DROP_LIMIT;
	/** Rate for drop */
	public static int PLAYER_RATE_DROP;
	/** Rate for player's item drop */
	public static int PLAYER_RATE_DROP_ITEM;
	/** Rate for player's equipment drop */
	public static int PLAYER_RATE_DROP_EQUIP;
	/** Rate for player's equipment and weapon drop */
	public static int PLAYER_RATE_DROP_EQUIP_WEAPON;
	
	// Pet Rates (Multipliers)
	/** Rate for experience rewards of the pet */
	public static float PET_XP_RATE;
	/** Rate for food consumption of the pet */
	public static int PET_FOOD_RATE;
	/** Rate for experience rewards of the Sin Eater */
	public static float SINEATER_XP_RATE;
	
	// Karma Drop Rate control
	/** Karma drop limit */
	public static int KARMA_DROP_LIMIT;
	/** Karma drop rate */
	public static int KARMA_RATE_DROP;
	/** Karma drop rate for item */
	public static int KARMA_RATE_DROP_ITEM;
	/** Karma drop rate for equipment */
	public static int KARMA_RATE_DROP_EQUIP;
	/** Karma drop rate for equipment and weapon */
	public static int KARMA_RATE_DROP_EQUIP_WEAPON;
	
	/** Time after which item will auto-destroy */
	public static int AUTODESTROY_ITEM_AFTER;
	/** List of items that will not be destroyed (separated by ",") */
	public static String PROTECTED_ITEMS;
	/** List of items that will not be destroyed */
	public static List<Integer> LIST_PROTECTED_ITEMS;
	
	/** Update itens owned by this char when storing the char on DB */
	public static boolean UPDATE_ITEMS_ON_CHAR_STORE;
	/** Update itens only when strictly necessary */
	public static boolean LAZY_ITEMS_UPDATE;
	/** Auto destroy nonequipable items dropped by players */
	public static boolean DESTROY_DROPPED_PLAYER_ITEM;
	/** Auto destroy equipable items dropped by players */
	public static boolean DESTROY_EQUIPABLE_PLAYER_ITEM;
	/** Save items on ground for restoration on server restart */
	public static boolean SAVE_DROPPED_ITEM;
	/** Empty table ItemsOnGround after load all items */
	public static boolean EMPTY_DROPPED_ITEM_TABLE_AFTER_LOAD;
	/** Time interval to save into db items on ground */
	public static int SAVE_DROPPED_ITEM_INTERVAL;
	/** Clear all items stored in ItemsOnGround table */
	public static boolean CLEAR_DROPPED_ITEM_TABLE;
	
	/** Accept precise drop calculation ? */
	public static boolean PRECISE_DROP_CALCULATION;
	/** Accept multi-items drop ? */
	public static boolean MULTIPLE_ITEM_DROP;
	
	/** Coord Synchronization */
	public static int COORD_SYNCHRONIZE;
	/** Falling Damage */
	public static boolean ENABLE_FALLING_DAMAGE;
	
	/** Period in days after which character is deleted */
	public static int DELETE_DAYS;
	
	/** Datapack root directory */
	public static File DATAPACK_ROOT;
	
	/** Maximum range mobs can randomly go from spawn point */
	public static int MAX_DRIFT_RANGE;
	
	/** Allow fishing ? */
	public static boolean ALLOW_FISHING;
	
	/** Jail config **/
	public static boolean JAIL_IS_PVP;
	public static boolean JAIL_DISABLE_CHAT;
	
	/** Allow L2Walker */
	public static boolean ALLOW_L2WALKER;
	
	/** Allow Manor system */
	public static boolean ALLOW_MANOR;
	
	/** Allow NPC walkers */
	public static boolean ALLOW_NPC_WALKERS;
	
	/** Allow Pet walkers */
	public static boolean ALLOW_PET_WALKERS;
	
	/** Allow Discard item ? */
	public static boolean ALLOW_DISCARDITEM;
	/** Allow freight ? */
	public static boolean ALLOW_FREIGHT;
	/** Allow warehouse ? */
	public static boolean ALLOW_WAREHOUSE;
	/** Allow warehouse cache? */
	public static boolean WAREHOUSE_CACHE;
	/** How long store WH datas */
	public static int WAREHOUSE_CACHE_TIME;
	/** Allow wear ? (try on in shop) */
	public static boolean ALLOW_WEAR;
	/** Duration of the try on after which items are taken back */
	public static int WEAR_DELAY;
	/** Price of the try on of one item */
	public static int WEAR_PRICE;
	/** Allow lottery ? */
	public static boolean ALLOW_LOTTERY;
	/** Allow race ? */
	public static boolean ALLOW_RACE;
	/** Allow water ? */
	public static boolean ALLOW_WATER;
	/** Allow rent pet ? */
	public static boolean ALLOW_RENTPET;
	/** Allow boat ? */
	public static boolean ALLOW_BOAT;
	
	/** Time after which a packet is considered as lost */
	public static int PACKET_LIFETIME;
	
	/** Detects server deadlocks */
	public static boolean DEADLOCK_DETECTOR;
	/** Check interval in seconds */
	public static int DEADLOCK_CHECK_INTERVAL;
	/** Restarts server to remove deadlocks */
	public static boolean RESTART_ON_DEADLOCK;
	
	/** Allow Wyvern Upgrader ? */
	public static boolean ALLOW_WYVERN_UPGRADER;
	
	// protocol revision
	/** Minimal protocol revision */
	public static int MIN_PROTOCOL_REVISION;
	/** Maximal protocol revision */
	public static int MAX_PROTOCOL_REVISION;
	
	public static boolean LOG_GAME_DAMAGE;
	
	// random animation interval
	/** Minimal time between 2 animations of a NPC */
	public static int MIN_NPC_ANIMATION;
	/** Maximal time between 2 animations of a NPC */
	public static int MAX_NPC_ANIMATION;
	/** Minimal time between animations of a monster */
	public static int MIN_MONSTER_ANIMATION;
	/** Maximal time between animations of a monster */
	public static int MAX_MONSTER_ANIMATION;
	
	/** Enable move-based knownlist */
	public static boolean MOVE_BASED_KNOWNLIST;
	/** Knownlist update time interval */
	public static long KNOWNLIST_UPDATE_INTERVAL;
	public static boolean DEBUG_KNOWNLIST;
	
	// Community Board
	/** Type of community */
	public static int COMMUNITY_TYPE;
	public static boolean BBS_SHOW_PLAYERLIST;
	public static String BBS_DEFAULT;
	/** Show level of the community board ? */
	public static boolean SHOW_LEVEL_COMMUNITYBOARD;
	/** Show status of the community board ? */
	public static boolean SHOW_STATUS_COMMUNITYBOARD;
	/** Size of the name page on the community board */
	public static int NAME_PAGE_SIZE_COMMUNITYBOARD;
	/** Name per row on community board */
	public static int NAME_PER_ROW_COMMUNITYBOARD;
	
	public static int MAX_ITEM_IN_PACKET;
	
	/** Game Server login port */
	public static int GAME_SERVER_LOGIN_PORT;
	/** Game Server login Host */
	public static String GAME_SERVER_LOGIN_HOST;
	/** Internal Hostname */
	public static String INTERNAL_HOSTNAME;
	/** External Hostname */
	public static String EXTERNAL_HOSTNAME;
	
	/** Show L2Monster level and aggro ? */
	public static boolean SHOW_NPC_LVL;
	
	/**
	 * Force full item inventory packet to be sent for any item change ?<br>
	 * <u><i>Note:</i></u> This can increase network traffic
	 */
	public static boolean FORCE_INVENTORY_UPDATE;
	/** Disable the use of guards against aggressive monsters ? */
	public static boolean GUARD_ATTACK_AGGRO_MOB;
	
	public static boolean SEVEN_SIGNS_DUNGEON_NPC_ACCESS;
	
	/** Allow use of AIO Buffer ? */
	public static boolean AIO_BUFFER_ENABLED;
	/** Allow use of NPC Buffer ? */
	public static boolean NPC_BUFFER_ENABLED;
	/** Schemes max amount */
	public static int SCHEMES_MAX_AMOUNT;
	/** Scheme name template */
	public static String SCHEME_NAME_TEMPLATE;
	/** Allow name prefix for AIO Buffer ? */
	public static String AIO_BUFFER_NAME_PREFIX;
	/** Allow color for AIO Buffer ? */
	public static boolean AIO_BUFFER_SET_NAME_COLOR;
	/** Color for AIO Buffer */
	public static int AIO_BUFFER_NAME_COLOR;
	/** Base maximum MP for buffers */
	public static float BUFFER_BASE_MP_MAX;
	/** Enable modifying skill duration */
	public static boolean ENABLE_MODIFY_SKILL_DURATION;
	/** Skill duration list */
	public static Map<Integer, Integer> SKILL_DURATION_LIST;
	
	/** Wedding System */
	public static boolean ALLOW_WEDDING;
	public static int WEDDING_PRICE;
	public static boolean WEDDING_TELEPORT;
	public static int WEDDING_TELEPORT_PRICE;
	public static int WEDDING_TELEPORT_DURATION;
	public static boolean WEDDING_SAME_SEX;
	public static boolean WEDDING_FORMAL_WEAR;
	
	/** Allow Offline Trade ? */
	public static boolean OFFLINE_TRADE_ENABLE;
	/** Allow Offline Craft ? */
	public static boolean OFFLINE_CRAFT_ENABLE;
	/** Restore Offliners ? */
	public static boolean RESTORE_OFFLINERS;
	/** Max Days for Offline Stores ? */
	public static int OFFLINE_MAX_DAYS;
	/** Disconnect shops that finished selling ? */
	public static boolean OFFLINE_DISCONNECT_FINISHED;
	/** Allow color for offline mode ? */
	public static boolean OFFLINE_SET_NAME_COLOR;
	/** Color for offline mode */
	public static int OFFLINE_NAME_COLOR;
	/** Allow teleporting to towns that are under siege ? */
	public static boolean ALLOW_SIEGE_TELEPORT;
	
	/** Allow players to keep subclass skills ? */
	public static boolean KEEP_SUBCLASS_SKILLS;
	
	/** Allow use Event Managers for change occupation ? */
	public static boolean ALLOW_CLASS_MASTERS;
	public static ClassMasterSettings CLASS_MASTER_SETTINGS;
	public static boolean ALLOW_ENTIRE_TREE;
	public static boolean ALTERNATE_CLASS_MASTER;
	
	/** Auto rewarding players */
	public static boolean ALLOW_AUTO_REWARDER;
	public static int AUTO_REWARD_DELAY;
	public static int AUTO_REWARD_ID;
	public static int AUTO_REWARD_COUNT;
	
	/** Custom starting spawn for new characters */
	public static boolean CUSTOM_STARTING_SPAWN;
	public static int CUSTOM_SPAWN_X;
	public static int CUSTOM_SPAWN_Y;
	public static int CUSTOM_SPAWN_Z;
	
	/** Allow players to view all available classes to the same village master */
	public static boolean CHANGE_SUBCLASS_EVERYWHERE;
	
	/** Auto Noblesse status at login */
	public static boolean AUTO_NOBLE_STATUS;
	
	/** Allow enchanting hero items */
	public static boolean ALLOW_HERO_ENCHANT;
	
	/** Maximum clients per IP */
	public static int MAX_CLIENTS_PER_IP;
	
	/** Use /block command as an AntiBuff shield */
	public static boolean ANTIBUFF_SHIELD_ENABLE;
	
	/** Allow atk/casting spd affect skill reuse delay */
	public static boolean SKILL_REUSE_INDEPENDENT;
	
	/** Allow password change using .changepassword voiced command */
	public static boolean PASSWORD_CHANGE_ENABLE;

	/** Enable custom data tables ? */
	public static boolean CUSTOM_SPAWNLIST_TABLE;
	public static boolean SAVE_GMSPAWN_ON_CUSTOM;
	public static boolean CUSTOM_NPC_TABLE;
	public static boolean CUSTOM_NPC_SKILLS_TABLE;
	public static boolean CUSTOM_MINIONS_TABLE;
	public static boolean CUSTOM_ITEM_TABLES;
	public static boolean CUSTOM_ARMORSETS_TABLE;
	public static boolean CUSTOM_TELEPORT_TABLE;
	public static boolean CUSTOM_DROPLIST_TABLE;
	public static boolean CUSTOM_MERCHANT_TABLES;
	public static boolean CUSTOM_MULTISELL_LOAD;
	
	/** Champion Mod */
	public static boolean CHAMPION_ENABLE;
	public static boolean CHAMPION_PASSIVE;
	public static int CHAMPION_FREQUENCY;
	public static String CHAMPION_TITLE;
	public static int CHAMP_MIN_LVL;
	public static int CHAMP_MAX_LVL;
	public static int CHAMPION_HP;
	public static float CHAMPION_REWARDS;
	public static float CHAMPION_ADENAS_REWARDS;
	public static float CHAMPION_HP_REGEN;
	public static float CHAMPION_ATK;
	public static float CHAMPION_SPD_ATK;
	public static int CHAMPION_REWARD_LOWER_CHANCE;
	public static int CHAMPION_REWARD_HIGHER_CHANCE;
	public static int CHAMPION_REWARD_ID;
	public static int CHAMPION_REWARD_QTY;
	
	// --------------------------------------------------
	// FloodProtector Settings
	// --------------------------------------------------
	public static FloodProtectorConfig FLOOD_PROTECTOR_USE_ITEM;
	public static FloodProtectorConfig FLOOD_PROTECTOR_ROLL_DICE;
	public static FloodProtectorConfig FLOOD_PROTECTOR_FIREWORK;
	public static FloodProtectorConfig FLOOD_PROTECTOR_ITEM_PET_SUMMON;
	public static FloodProtectorConfig FLOOD_PROTECTOR_HERO_VOICE;
	public static FloodProtectorConfig FLOOD_PROTECTOR_GLOBAL_CHAT;
	public static FloodProtectorConfig FLOOD_PROTECTOR_SUBCLASS;
	public static FloodProtectorConfig FLOOD_PROTECTOR_DROP_ITEM;
	public static FloodProtectorConfig FLOOD_PROTECTOR_SERVER_BYPASS;
	public static FloodProtectorConfig FLOOD_PROTECTOR_MULTISELL;
	public static FloodProtectorConfig FLOOD_PROTECTOR_TRANSACTION;
	public static FloodProtectorConfig FLOOD_PROTECTOR_MANUFACTURE;
	public static FloodProtectorConfig FLOOD_PROTECTOR_MANOR;
	public static FloodProtectorConfig FLOOD_PROTECTOR_CHARACTER_SELECT;
	
	// --------------------------------------------------
	// MMO Settings
	// --------------------------------------------------
	public static int MMO_SELECTOR_SLEEP_TIME;
	public static int MMO_MAX_SEND_PER_PASS;
	public static int MMO_MAX_READ_PER_PASS;
	public static int MMO_HELPER_BUFFER_COUNT;
	
	/** Project tag */
	public static String PROJECT_TAG;
	/** Date of server build */
	public static String SERVER_BUILD_DATE;
	
	/** Zone Setting */
	public static int ZONE_TOWN;
	
	/** Crafting Enabled? */
	public static boolean IS_CRAFTING_ENABLED;
	
	// Inventory slots limit
	/** Maximum inventory slots limits for non dwarf characters */
	public static int INVENTORY_MAXIMUM_NO_DWARF;
	/** Maximum inventory slots limits for dwarf characters */
	public static int INVENTORY_MAXIMUM_DWARF;
	/** Maximum inventory slots limits for GM */
	public static int INVENTORY_MAXIMUM_GM;
	/** Maximum inventory slots limits for pet */
	public static int INVENTORY_MAXIMUM_PET;
	
	// Warehouse slots limits
	/** Maximum inventory slots limits for non dwarf warehouse */
	public static int WAREHOUSE_SLOTS_NO_DWARF;
	/** Maximum inventory slots limits for dwarf warehouse */
	public static int WAREHOUSE_SLOTS_DWARF;
	/** Maximum inventory slots limits for clan warehouse */
	public static int WAREHOUSE_SLOTS_CLAN;
	/** Maximum inventory slots limits for freight */
	public static int FREIGHT_SLOTS;
	
	// Karma System Variables
	/** Minimum karma gain/loss */
	public static int KARMA_MIN_KARMA;
	/** Maximum karma gain/loss */
	public static int KARMA_MAX_KARMA;
	/** Number to divide the xp recieved by, to calculate karma lost on xp gain/lost */
	public static int KARMA_XP_DIVIDER;
	/** The Minimum Karma lost if 0 karma is to be removed */
	public static int KARMA_LOST_BASE;
	/** Can a GM drop items on death ? */
	public static boolean GM_ON_DIE_DROP_ITEM;
	/** Should award a pvp point for killing a player with karma ? */
	public static boolean KARMA_AWARD_PK_KILL;
	/** Minimum PK required to drop */
	public static int KARMA_PK_LIMIT;
	
	/** List of pet items that cannot be dropped (seperated by ",") when PVP */
	public static String KARMA_NONDROPPABLE_PET_ITEMS;
	/** List of items that cannot be dropped (seperated by ",") when PVP */
	public static String KARMA_NONDROPPABLE_ITEMS;
	/** List of pet items that cannot be dropped when PVP */
	public static List<Integer> KARMA_LIST_NONDROPPABLE_PET_ITEMS;
	/** List of items that cannot be dropped when PVP */
	public static List<Integer> KARMA_LIST_NONDROPPABLE_ITEMS;
	
	/** List of items that cannot be dropped (seperated by ",") */
	public static String NONDROPPABLE_ITEMS;
	/** List of items that cannot be dropped */
	public static List<Integer> LIST_NONDROPPABLE_ITEMS;
	
	/** List of NPCs that rent pets (seperated by ",") */
	public static String PET_RENT_NPC;
	/** List of NPCs that rent pets */
	public static List<Integer> LIST_PET_RENT_NPC;
	
	/** Duration (in ms) while a player stay in PVP mode after hitting an innocent */
	public static int PVP_NORMAL_TIME;
	/** Duration (in ms) while a player stay in PVP mode after hitting a purple player */
	public static int PVP_PVP_TIME;
	
	// Karma Punishment
	/** Allow player with karma to be killed in peace zone ? */
	public static boolean ALT_GAME_KARMA_PLAYER_CAN_BE_KILLED_IN_PEACEZONE;
	/** Allow player with karma to shop ? */
	public static boolean ALT_GAME_KARMA_PLAYER_CAN_SHOP;
	/** Allow player with karma to use gatekeepers ? */
	public static boolean ALT_GAME_KARMA_PLAYER_CAN_USE_GK;
	/** Allow flagged player to use gatekeepers ? */
	public static boolean ALT_GAME_FLAGGED_PLAYER_CAN_USE_GK;
	/** Allow player with karma to use SOE or Return skill ? */
	public static boolean ALT_GAME_KARMA_PLAYER_CAN_TELEPORT;
	/** Allow player with karma to trade ? */
	public static boolean ALT_GAME_KARMA_PLAYER_CAN_TRADE;
	/** Allow player with karma to use warehouse ? */
	public static boolean ALT_GAME_KARMA_PLAYER_CAN_USE_WAREHOUSE;
	
	/** Enumeration for type of ID Factory */
	public static enum IdFactoryType
	{
		Compaction,
		BitSet,
		Stack
	}
	
	/** ID Factory type */
	public static IdFactoryType IDFACTORY_TYPE;
	/** Check for bad ID ? */
	public static boolean BAD_ID_CHECKING;
	
	/**
	 * Allow lesser effects to be cancelled if stronger effects are used when effects of the same stack group are used.<br>
	 * New effects that are added will be cancelled if they are of lesser priority to the old one.
	 */
	public static boolean EFFECT_CANCELLING;
	
	/** Auto-delete invalid quest data ? */
	public static boolean AUTODELETE_INVALID_QUEST_DATA;
	
	/** Chance that an item will successfully be enchanted */
	public static int ENCHANT_CHANCE_WEAPON;
	public static int ENCHANT_CHANCE_ARMOR;
	public static int ENCHANT_CHANCE_JEWELRY;
	public static int BLESSED_ENCHANT_CHANCE_WEAPON;
	public static int BLESSED_ENCHANT_CHANCE_ARMOR;
	public static int BLESSED_ENCHANT_CHANCE_JEWELRY;
	/** Maximum level of enchantment */
	public static int ENCHANT_MAX_WEAPON;
	public static int ENCHANT_MAX_ARMOR;
	public static int ENCHANT_MAX_JEWELRY;
	/** maximum level of safe enchantment for normal items */
	public static int ENCHANT_SAFE_MAX;
	/** maximum level of safe enchantment for full body armor */
	public static int ENCHANT_SAFE_MAX_FULL;
	
	// Character multipliers
	/** Multiplier for character HP regeneration */
	public static double HP_REGEN_MULTIPLIER;
	/** Mutilplier for character MP regeneration */
	public static double MP_REGEN_MULTIPLIER;
	/** Multiplier for character CP regeneration */
	public static double CP_REGEN_MULTIPLIER;
	
	// Raid Boss multipliers
	/** Multiplier for Raid boss attack multiplier */
	public static float RAID_ATK_MULTIPLIER;
	/** Multiplier for Raid boss defense multiplier */
	public static float RAID_DEF_MULTIPLIER;
	/** Multiplier for Raid boss HP regeneration */
	public static float RAID_HP_REGEN_MULTIPLIER;
	/** Mulitplier for Raid boss MP regeneration */
	public static float RAID_MP_REGEN_MULTIPLIER;
	/** Raid Boss Minion Respawn Time */
	public static int RAID_MINION_RESPAWN_TIME;
	/** Mulitplier for Raid boss minimum time respawn */
	public static float RAID_MIN_RESPAWN_MULTIPLIER;
	/** Mulitplier for Raid boss maximum time respawn */
	public static float RAID_MAX_RESPAWN_MULTIPLIER;
	/** Amount of adenas when starting a new character */
	public static int STARTING_ADENA;
	/** Starting level of a new character */
	public static byte STARTING_LEVEL;
	/** Starting level of a new subclass */
	public static byte STARTING_SUB_LEVEL;
	
	/** Maximum character running speed */
	public static int MAX_RUN_SPEED;
	/** Maximum character Physical Critical Rate */
	public static int MAX_PCRIT_RATE;
	/** Maximum character Magic Critical Rate */
	public static int MAX_MCRIT_RATE;
	/** Maximum character Physical Attack Speed */
	public static int MAX_PATK_SPEED;
	/** Maximum character Magic Attack Speed */
	public static int MAX_MATK_SPEED;
	
	/**
     * This is the first UNREACHABLE level.<BR>
     * example: If you want a max at 78 & 100%, you have to set it to 79.<BR><BR>
     */
	public static byte MAX_PLAYER_LEVEL;
	
	/** Deep Blue Mobs' Drop Rules Enabled */
	public static boolean DEEPBLUE_DROP_RULES;
	public static int UNSTUCK_INTERVAL;
	
	/** Is telnet enabled ? */
	public static boolean IS_TELNET_ENABLED;
	/** Telnet status port */
	public static int TELNET_PORT;
	
	/** Player Protection control */
	public static int PLAYER_SPAWN_PROTECTION;
	public static int PLAYER_FAKEDEATH_UP_PROTECTION;
	/** Player Movement Block Time */
	public static int PLAYER_MOVEMENT_BLOCK_TIME;
	
	/** Define Party XP cutoff point method - Possible values: level and percentage */
	public static String PARTY_XP_CUTOFF_METHOD;
	/** Define the cutoff point value for the "level" method */
	public static int PARTY_XP_CUTOFF_LEVEL;
	/** Define the cutoff point value for the "percentage" method */
	public static double PARTY_XP_CUTOFF_PERCENT;
	
	/** Multiplier of HP that is restored on respawn */
	public static double RESPAWN_RESTORE_HP_MULTIPLIER;
	
	/** Allow randomizing of the respawn point in towns. */
	public static boolean RESPAWN_RANDOM_ENABLED;
	/** The maximum offset from the base respawn point to allow. */
	public static int RESPAWN_RANDOM_MAX_OFFSET;
	
	public static int MAX_PRIVATE_STORE_BUY_LIMIT;
	public static int MAX_PRIVATE_STORE_SELL_LIMIT;
	public static int MAX_PRIVATE_STORE_BUY_LIMIT_DWARF;
	public static int MAX_PRIVATE_STORE_SELL_LIMIT_DWARF;
	
	/** Store skills cooltime on char exit/relogin */
	public static boolean STORE_SKILL_COOLTIME;
	/** Store skills cooltime on char subclass change */
	public static boolean SUBCLASS_STORE_SKILL_COOLTIME;
	/** Show licence or not just after login (if false, will directly go to the Server List */
	public static boolean SHOW_LICENCE;
	
	/** Default punishment for illegal actions */
	public static int DEFAULT_PUNISH;
	/** Parameter for default punishment */
	public static int DEFAULT_PUNISH_PARAM;
	
	/** Accept new game server ? */
	public static boolean ACCEPT_NEW_GAMESERVER;
	/** ID of the game server */
	public static int SERVER_ID;
	/** Hexadecimal ID of the game server */
	public static byte[] HEX_ID;
	/** Accept alternate ID for server ? */
	public static boolean ACCEPT_ALTERNATE_ID;
	/** ID for request to the server */
	public static int REQUEST_ID;
	public static boolean RESERVE_HOST_ON_LOGIN = false;
	
	public static boolean ANNOUNCE_MAMMON_SPAWN;
	public static boolean LAZY_CACHE;
	public static boolean CACHE_CHAR_NAMES;
	
	/** Enable colored name for GM ? */
	public static boolean GM_NAME_COLOR_ENABLED;
	/** Color of GM name */
	public static int GM_NAME_COLOR;
	/** Color of admin name */
	public static int ADMIN_NAME_COLOR;
	/** Place an aura around the GM ? */
	public static boolean GM_HERO_AURA;
	/** Set the GM invulnerable at startup ? */
	public static boolean GM_STARTUP_INVULNERABLE;
	/** Set the GM invisible at startup ? */
	public static boolean GM_STARTUP_INVISIBLE;
	/** Set silence to GM at startup ? */
	public static boolean GM_STARTUP_SILENCE;
	/** Add GM in the GM list at startup ? */
	public static boolean GM_STARTUP_AUTO_LIST;
	
	/** Allow petition ? */
	public static boolean PETITIONING_ALLOWED;
	/** Maximum number of petitions per player */
	public static int MAX_PETITIONS_PER_PLAYER;
	/** Maximum number of petitions pending */
	public static int MAX_PETITIONS_PENDING;
	
	/** Bypass exploit protection ? */
	public static boolean BYPASS_VALIDATION;
	
	/** Only GM buy items for free **/
	public static boolean ONLY_GM_ITEMS_FREE;
	
	/** GM Audit ? */
	public static boolean GMAUDIT;
	
	/** Allow auto-create account ? */
	public static boolean AUTO_CREATE_ACCOUNTS;
	
	public static boolean FLOOD_PROTECTION;
	public static int FAST_CONNECTION_LIMIT;
	public static int NORMAL_CONNECTION_TIME;
	public static int FAST_CONNECTION_TIME;
	public static int MAX_CONNECTION_PER_IP;
	
	public static boolean LOG_LOGIN_ATTEMPTS;
	
	/** Enforce gameguard query on character login ? */
	public static boolean GAMEGUARD_ENFORCE;
	/** Accept chaotic throne clients ? */
	public static boolean ACCEPT_CHAOTIC_THRONE_CLIENTS;
	
	/** Recipebook limits */
	public static int DWARF_RECIPE_LIMIT;
	public static int COMMON_RECIPE_LIMIT;
	
	/** Grid Options */
	public static boolean GRIDS_ALWAYS_ON;
	public static int GRID_NEIGHBOR_TURNON_TIME;
	public static int GRID_NEIGHBOR_TURNOFF_TIME;
	
	/** Clan Hall function related configs */
	public static long CH_TELE_FEE_RATIO;
	public static int CH_TELE1_FEE;
	public static int CH_TELE2_FEE;
	public static long CH_ITEM_FEE_RATIO;
	public static int CH_ITEM1_FEE;
	public static int CH_ITEM2_FEE;
	public static int CH_ITEM3_FEE;
	public static long CH_MPREG_FEE_RATIO;
	public static int CH_MPREG1_FEE;
	public static int CH_MPREG2_FEE;
	public static int CH_MPREG3_FEE;
	public static int CH_MPREG4_FEE;
	public static int CH_MPREG5_FEE;
	public static long CH_HPREG_FEE_RATIO;
	public static int CH_HPREG1_FEE;
	public static int CH_HPREG2_FEE;
	public static int CH_HPREG3_FEE;
	public static int CH_HPREG4_FEE;
	public static int CH_HPREG5_FEE;
	public static int CH_HPREG6_FEE;
	public static int CH_HPREG7_FEE;
	public static int CH_HPREG8_FEE;
	public static int CH_HPREG9_FEE;
	public static int CH_HPREG10_FEE;
	public static int CH_HPREG11_FEE;
	public static int CH_HPREG12_FEE;
	public static int CH_HPREG13_FEE;
	public static long CH_EXPREG_FEE_RATIO;
	public static int CH_EXPREG1_FEE;
	public static int CH_EXPREG2_FEE;
	public static int CH_EXPREG3_FEE;
	public static int CH_EXPREG4_FEE;
	public static int CH_EXPREG5_FEE;
	public static int CH_EXPREG6_FEE;
	public static int CH_EXPREG7_FEE;
	public static long CH_SUPPORT_FEE_RATIO;
	public static int CH_SUPPORT1_FEE;
	public static int CH_SUPPORT2_FEE;
	public static int CH_SUPPORT3_FEE;
	public static int CH_SUPPORT4_FEE;
	public static int CH_SUPPORT5_FEE;
	public static int CH_SUPPORT6_FEE;
	public static int CH_SUPPORT7_FEE;
	public static int CH_SUPPORT8_FEE;
	public static long CH_CURTAIN_FEE_RATIO;
	public static int CH_CURTAIN1_FEE;
	public static int CH_CURTAIN2_FEE;
	public static long CH_FRONT_FEE_RATIO;
	public static int CH_FRONT1_FEE;
	public static int CH_FRONT2_FEE;
	
	/** Castle function related configs */
	public static long CS_TELE_FEE_RATIO;
	public static int CS_TELE1_FEE;
	public static int CS_TELE2_FEE;
	public static long CS_MPREG_FEE_RATIO;
	public static int CS_MPREG1_FEE;
	public static int CS_MPREG2_FEE;
	public static int CS_MPREG3_FEE;
	public static int CS_MPREG4_FEE;
	public static long CS_HPREG_FEE_RATIO;
	public static int CS_HPREG1_FEE;
	public static int CS_HPREG2_FEE;
	public static int CS_HPREG3_FEE;
	public static int CS_HPREG4_FEE;
	public static int CS_HPREG5_FEE;
	public static long CS_EXPREG_FEE_RATIO;
	public static int CS_EXPREG1_FEE;
	public static int CS_EXPREG2_FEE;
	public static int CS_EXPREG3_FEE;
	public static int CS_EXPREG4_FEE;
	public static long CS_SUPPORT_FEE_RATIO;
	public static int CS_SUPPORT1_FEE;
	public static int CS_SUPPORT2_FEE;
	public static int CS_SUPPORT3_FEE;
	public static int CS_SUPPORT4_FEE;
	
	/** Max number of buffs */
	public static byte BUFFS_MAX_AMOUNT;
	/** Max number of debuffs */
	public static byte DEBUFFS_MAX_AMOUNT;
	/** Number of buff slots per row */
	public static byte BUFF_SLOTS_PER_ROW;
	
	/** Alt Settings for devs */
	public static boolean ALT_DEV_NO_QUESTS;
	public static boolean ALT_DEV_NO_SPAWNS;
	
	/** GeoData Settings */
	public static int PATHFINDING;
	public static File PATHNODE_DIR;
	public static String PATHFIND_BUFFERS;
	public static float LOW_WEIGHT;
	public static float MEDIUM_WEIGHT;
	public static float HIGH_WEIGHT;
	public static boolean ADVANCED_DIAGONAL_STRATEGY;
	public static float DIAGONAL_WEIGHT;
	public static int MAX_POSTFILTER_PASSES;
	public static boolean DEBUG_PATH;
	public static boolean FORCE_GEODATA;
	public static Path GEODATA_PATH;
	public static boolean TRY_LOAD_UNSPECIFIED_REGIONS;
	public static Map<String, Boolean> GEODATA_REGIONS;
	
	/** Grand Boss Settings */
	public static int ANTHARAS_WAIT_TIME;
	public static int VALAKAS_WAIT_TIME;
	public static int ANTHARAS_SPAWN_INTERVAL;
	public static int ANTHARAS_SPAWN_RANDOM_INTERVAL;
	public static int VALAKAS_SPAWN_INTERVAL;
	public static int VALAKAS_SPAWN_RANDOM_INTERVAL;
	public static int BAIUM_SPAWN_INTERVAL;
	public static int BAIUM_SPAWN_RANDOM_INTERVAL;
	public static int CORE_SPAWN_INTERVAL;
	public static int CORE_SPAWN_RANDOM_INTERVAL;
	public static int ORFEN_SPAWN_INTERVAL;
	public static int ORFEN_SPAWN_RANDOM_INTERVAL;
	public static int QUEEN_ANT_SPAWN_INTERVAL;
	public static int QUEEN_ANT_SPAWN_RANDOM_INTERVAL;
	public static int ZAKEN_SPAWN_INTERVAL;
	public static int ZAKEN_SPAWN_RANDOM_INTERVAL;
	
	/**
	 * This class initializes all global variables for configuration.<br>
	 * If key doesn't appear in properties file, a default value is setting on by this class.
	 */
	public static void load()
	{
		if (Server.SERVER_MODE == Server.MODE_GAMESERVER)
		{
			FLOOD_PROTECTOR_USE_ITEM = new FloodProtectorConfig("UseItemFloodProtector");
			FLOOD_PROTECTOR_ROLL_DICE = new FloodProtectorConfig("RollDiceFloodProtector");
			FLOOD_PROTECTOR_FIREWORK = new FloodProtectorConfig("FireworkFloodProtector");
			FLOOD_PROTECTOR_ITEM_PET_SUMMON = new FloodProtectorConfig("ItemPetSummonFloodProtector");
			FLOOD_PROTECTOR_HERO_VOICE = new FloodProtectorConfig("HeroVoiceFloodProtector");
			FLOOD_PROTECTOR_GLOBAL_CHAT = new FloodProtectorConfig("GlobalChatFloodProtector");
			FLOOD_PROTECTOR_SUBCLASS = new FloodProtectorConfig("SubclassFloodProtector");
			FLOOD_PROTECTOR_DROP_ITEM = new FloodProtectorConfig("DropItemFloodProtector");
			FLOOD_PROTECTOR_SERVER_BYPASS = new FloodProtectorConfig("ServerBypassFloodProtector");
			FLOOD_PROTECTOR_MULTISELL = new FloodProtectorConfig("MultiSellFloodProtector");
			FLOOD_PROTECTOR_TRANSACTION = new FloodProtectorConfig("TransactionFloodProtector");
			FLOOD_PROTECTOR_MANUFACTURE = new FloodProtectorConfig("ManufactureFloodProtector");
			FLOOD_PROTECTOR_MANOR = new FloodProtectorConfig("ManorFloodProtector");
			FLOOD_PROTECTOR_CHARACTER_SELECT = new FloodProtectorConfig("CharacterSelectFloodProtector");
			
			_log.info("Loading Gameserver Configuration Files.");
			
			Properties serverSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(GAME_SERVER_FILE)))
			{
				serverSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + GAME_SERVER_FILE + " File.");
			}
			
			ENABLE_UPNP = Boolean.parseBoolean(serverSettings.getProperty("EnableUPnP", "False"));
			
			GAMESERVER_HOSTNAME = serverSettings.getProperty("GameserverHostname");
			PORT_GAME = Integer.parseInt(serverSettings.getProperty("GameserverPort", "7777"));

			INTERNAL_HOSTNAME = serverSettings.getProperty("InternalHostname", "*");
			EXTERNAL_HOSTNAME = serverSettings.getProperty("ExternalHostname", "*");
			
			GAME_SERVER_LOGIN_PORT = Integer.parseInt(serverSettings.getProperty("LoginPort", "9014"));
			GAME_SERVER_LOGIN_HOST = serverSettings.getProperty("LoginHost", "127.0.0.1");
			
			REQUEST_ID = Integer.parseInt(serverSettings.getProperty("RequestServerID", "1"));
			ACCEPT_ALTERNATE_ID = Boolean.parseBoolean(serverSettings.getProperty("AcceptAlternateID", "true"));
			
			DATABASE_DRIVER = serverSettings.getProperty("Driver", "org.mariadb.jdbc.Driver");
			DATABASE_URL = serverSettings.getProperty("URL", "jdbc:mariadb://localhost/l2jdb?useSSL=false");
			DATABASE_LOGIN = serverSettings.getProperty("Login", "root");
			DATABASE_PASSWORD = serverSettings.getProperty("Password", "");
			DATABASE_MAX_CONNECTIONS = Integer.parseInt(serverSettings.getProperty("MaximumDbConnections", "45"));
			DATABASE_MAX_IDLE_TIME = Integer.parseInt(serverSettings.getProperty("MaximumDbIdleTime", "0"));
			
			try
			{
				DATAPACK_ROOT = new File(serverSettings.getProperty("DatapackRoot", ".").replaceAll("\\\\", "/")).getCanonicalFile();
			}
			catch (Exception e)
			{
				_log.warning("Error setting datapack root!");
				DATAPACK_ROOT = new File(".");
			}
			
			CHAR_NAME_TEMPLATE = serverSettings.getProperty("CharNameTemplate", ".*");
			PET_NAME_TEMPLATE = serverSettings.getProperty("PetNameTemplate", ".*");
			CLAN_ALLY_NAME_TEMPLATE = serverSettings.getProperty("ClanAllyNameTemplate", ".*");
			
			MAX_CHARACTERS_NUMBER_PER_ACCOUNT = Integer.parseInt(serverSettings.getProperty("CharMaxNumber", "7"));
			MAXIMUM_ONLINE_USERS = Integer.parseInt(serverSettings.getProperty("MaximumOnlineUsers", "100"));
			
			GAMEGUARD_ENFORCE = Boolean.parseBoolean(serverSettings.getProperty("GameGuardEnforce", "false"));
			
			MIN_PROTOCOL_REVISION = Integer.parseInt(serverSettings.getProperty("MinProtocolRevision", "656"));
			MAX_PROTOCOL_REVISION = Integer.parseInt(serverSettings.getProperty("MaxProtocolRevision", "665"));
			
			if (MIN_PROTOCOL_REVISION > MAX_PROTOCOL_REVISION)
			{
				throw new Error("MinProtocolRevision is bigger than MaxProtocolRevision in server configuration file.");
			}
			
			LOG_GAME_DAMAGE = Boolean.parseBoolean(serverSettings.getProperty("LogGameDamage", "False"));
			
			Properties optionsSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(OPTIONS_FILE)))
			{
				optionsSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + OPTIONS_FILE + " File.");
			}
			
			EVERYBODY_HAS_ADMIN_RIGHTS = Boolean.parseBoolean(optionsSettings.getProperty("EverybodyHasAdminRights", "false"));
			
			DISPLAY_SERVER_VERSION = Boolean.parseBoolean(optionsSettings.getProperty("DisplayServerVersion", "false"));
			
			DEBUG = Boolean.parseBoolean(optionsSettings.getProperty("Debug", "false"));
			ASSERT = Boolean.parseBoolean(optionsSettings.getProperty("Assert", "false"));
			DEVELOPER = Boolean.parseBoolean(optionsSettings.getProperty("Developer", "false"));
			ACCEPT_GEOEDITOR_CONN = Boolean.parseBoolean(optionsSettings.getProperty("AcceptGeoeditorConn", "False"));
			TEST_SERVER = Boolean.parseBoolean(optionsSettings.getProperty("TestServer", "false"));
			SERVER_LIST_TESTSERVER = Boolean.parseBoolean(optionsSettings.getProperty("TestServer", "false"));
			
			SERVER_LIST_BRACKET = Boolean.valueOf(optionsSettings.getProperty("ServerListBrackets", "false"));
			SERVER_LIST_CLOCK = Boolean.valueOf(optionsSettings.getProperty("ServerListClock", "false"));
			SERVER_GMONLY = Boolean.valueOf(optionsSettings.getProperty("ServerGMOnly", "false"));
			
			SKILL_CHECK_ENABLE = Boolean.valueOf(optionsSettings.getProperty("SkillCheckEnable", "False"));
			
			AUTODESTROY_ITEM_AFTER = Integer.parseInt(optionsSettings.getProperty("AutoDestroyDroppedItemAfter", "0"));
			PROTECTED_ITEMS = optionsSettings.getProperty("ListOfProtectedItems");
			
			LIST_PROTECTED_ITEMS = new ArrayList<>();
			if (!PROTECTED_ITEMS.isEmpty())
			{
				for (String id : PROTECTED_ITEMS.split(","))
				{
					LIST_PROTECTED_ITEMS.add(Integer.parseInt(id));
				}
			}
			
			UPDATE_ITEMS_ON_CHAR_STORE = Boolean.parseBoolean(optionsSettings.getProperty("UpdateItemsOnCharStore", "false"));
			LAZY_ITEMS_UPDATE = Boolean.parseBoolean(optionsSettings.getProperty("LazyItemsUpdate", "false"));
			DESTROY_DROPPED_PLAYER_ITEM = Boolean.valueOf(optionsSettings.getProperty("DestroyPlayerDroppedItem", "false"));
			DESTROY_EQUIPABLE_PLAYER_ITEM = Boolean.valueOf(optionsSettings.getProperty("DestroyEquipableItem", "false"));
			SAVE_DROPPED_ITEM = Boolean.valueOf(optionsSettings.getProperty("SaveDroppedItem", "false"));
			EMPTY_DROPPED_ITEM_TABLE_AFTER_LOAD = Boolean.valueOf(optionsSettings.getProperty("EmptyDroppedItemTableAfterLoad", "false"));
			SAVE_DROPPED_ITEM_INTERVAL = Integer.parseInt(optionsSettings.getProperty("SaveDroppedItemInterval", "0")) * 60000;
			CLEAR_DROPPED_ITEM_TABLE = Boolean.valueOf(optionsSettings.getProperty("ClearDroppedItemTable", "false"));
			
			PRECISE_DROP_CALCULATION = Boolean.valueOf(optionsSettings.getProperty("PreciseDropCalculation", "True"));
			MULTIPLE_ITEM_DROP = Boolean.valueOf(optionsSettings.getProperty("MultipleItemDrop", "True"));
			
			ALLOW_WAREHOUSE = Boolean.valueOf(optionsSettings.getProperty("AllowWarehouse", "True"));
			WAREHOUSE_CACHE = Boolean.valueOf(optionsSettings.getProperty("WarehouseCache", "False"));
			WAREHOUSE_CACHE_TIME = Integer.parseInt(optionsSettings.getProperty("WarehouseCacheTime", "15"));
			ALLOW_FREIGHT = Boolean.valueOf(optionsSettings.getProperty("AllowFreight", "True"));
			ALLOW_WEAR = Boolean.valueOf(optionsSettings.getProperty("AllowWear", "False"));
			WEAR_DELAY = Integer.parseInt(optionsSettings.getProperty("WearDelay", "5"));
			WEAR_PRICE = Integer.parseInt(optionsSettings.getProperty("WearPrice", "10"));
			ALLOW_LOTTERY = Boolean.valueOf(optionsSettings.getProperty("AllowLottery", "False"));
			ALLOW_RACE = Boolean.valueOf(optionsSettings.getProperty("AllowRace", "False"));
			ALLOW_WATER = Boolean.valueOf(optionsSettings.getProperty("AllowWater", "False"));
			ALLOW_RENTPET = Boolean.valueOf(optionsSettings.getProperty("AllowRentPet", "False"));
			ALLOW_DISCARDITEM = Boolean.valueOf(optionsSettings.getProperty("AllowDiscardItem", "True"));
			ALLOW_FISHING = Boolean.valueOf(optionsSettings.getProperty("AllowFishing", "True"));
			ALLOW_BOAT = Boolean.valueOf(optionsSettings.getProperty("AllowBoat", "False"));
			
			ALLOW_L2WALKER = Boolean.valueOf(optionsSettings.getProperty("AllowL2Walker", "False"));
			ALLOW_MANOR = Boolean.valueOf(optionsSettings.getProperty("AllowManor", "True"));
			ALLOW_NPC_WALKERS = Boolean.valueOf(optionsSettings.getProperty("AllowNpcWalkers", "True"));
			ALLOW_PET_WALKERS = Boolean.valueOf(optionsSettings.getProperty("AllowPetWalkers", "True"));
			
			DEFAULT_GLOBAL_CHAT = optionsSettings.getProperty("GlobalChat", "ON");
			DEFAULT_TRADE_CHAT = optionsSettings.getProperty("TradeChat", "ON");
			
			LOG_CHAT = Boolean.valueOf(optionsSettings.getProperty("LogChat", "False"));
			LOG_ITEMS = Boolean.valueOf(optionsSettings.getProperty("LogItems", "false"));
			
			GMAUDIT = Boolean.valueOf(optionsSettings.getProperty("GMAudit", "False"));
			
			COMMUNITY_TYPE = Integer.parseInt(optionsSettings.getProperty("CommunityType", "1"));
			BBS_SHOW_PLAYERLIST = Boolean.valueOf(optionsSettings.getProperty("BBSShowPlayerList", "False"));
			BBS_DEFAULT = optionsSettings.getProperty("BBSDefault", "_bbshome");
			SHOW_LEVEL_COMMUNITYBOARD = Boolean.valueOf(optionsSettings.getProperty("ShowLevelOnCommunityBoard", "False"));
			SHOW_STATUS_COMMUNITYBOARD = Boolean.valueOf(optionsSettings.getProperty("ShowStatusOnCommunityBoard", "True"));
			NAME_PAGE_SIZE_COMMUNITYBOARD = Integer.parseInt(optionsSettings.getProperty("NamePageSizeOnCommunityBoard", "50"));
			NAME_PER_ROW_COMMUNITYBOARD = Integer.parseInt(optionsSettings.getProperty("NamePerRowOnCommunityBoard", "5"));
			
			ZONE_TOWN = Integer.parseInt(optionsSettings.getProperty("ZoneTown", "0"));
			
			MAX_DRIFT_RANGE = Integer.parseInt(optionsSettings.getProperty("MaxDriftRange", "300"));
			
			MIN_NPC_ANIMATION = Integer.parseInt(optionsSettings.getProperty("MinNPCAnimation", "10"));
			MAX_NPC_ANIMATION = Integer.parseInt(optionsSettings.getProperty("MaxNPCAnimation", "20"));
			
			MIN_MONSTER_ANIMATION = Integer.parseInt(optionsSettings.getProperty("MinMonsterAnimation", "5"));
			MAX_MONSTER_ANIMATION = Integer.parseInt(optionsSettings.getProperty("MaxMonsterAnimation", "20"));
			
			MOVE_BASED_KNOWNLIST = Boolean.parseBoolean(optionsSettings.getProperty("MoveBasedKnownlist", "False"));
			KNOWNLIST_UPDATE_INTERVAL = Long.parseLong(optionsSettings.getProperty("KnownListUpdateInterval", "1250"));
			DEBUG_KNOWNLIST = Boolean.valueOf(optionsSettings.getProperty("DebugKnownList", "False"));
			
			SHOW_NPC_LVL = Boolean.valueOf(optionsSettings.getProperty("ShowNpcLevel", "False"));
			
			FORCE_INVENTORY_UPDATE = Boolean.valueOf(optionsSettings.getProperty("ForceInventoryUpdate", "False"));
			
			AUTODELETE_INVALID_QUEST_DATA = Boolean.valueOf(optionsSettings.getProperty("AutoDeleteInvalidQuestData", "False"));
			
			THREAD_P_EFFECTS = Integer.parseInt(optionsSettings.getProperty("ThreadPoolSizeEffects", "6"));
			THREAD_P_GENERAL = Integer.parseInt(optionsSettings.getProperty("ThreadPoolSizeGeneral", "15"));
			GENERAL_PACKET_THREAD_CORE_SIZE = Integer.parseInt(optionsSettings.getProperty("GeneralPacketThreadCoreSize", "4"));
			IO_PACKET_THREAD_CORE_SIZE = Integer.parseInt(optionsSettings.getProperty("UrgentPacketThreadCoreSize", "2"));
			AI_MAX_THREAD = Integer.parseInt(optionsSettings.getProperty("AiMaxThread", "10"));
			GENERAL_THREAD_CORE_SIZE = Integer.parseInt(optionsSettings.getProperty("GeneralThreadCoreSize", "4"));
			
			DELETE_DAYS = Integer.parseInt(optionsSettings.getProperty("DeleteCharAfterDays", "7"));
			
			DEFAULT_PUNISH = Integer.parseInt(optionsSettings.getProperty("DefaultPunish", "2"));
			DEFAULT_PUNISH_PARAM = Integer.parseInt(optionsSettings.getProperty("DefaultPunishParam", "0"));
			
			LAZY_CACHE = Boolean.valueOf(optionsSettings.getProperty("LazyCache", "True"));
			CACHE_CHAR_NAMES = Boolean.valueOf(optionsSettings.getProperty("CacheCharNames", "True"));
			
			PACKET_LIFETIME = Integer.parseInt(optionsSettings.getProperty("PacketLifeTime", "0"));
			
			DEADLOCK_DETECTOR = Boolean.valueOf(optionsSettings.getProperty("DeadLockDetector", "False"));
			DEADLOCK_CHECK_INTERVAL = Integer.parseInt(optionsSettings.getProperty("DeadLockCheckInterval", "20"));
			RESTART_ON_DEADLOCK = Boolean.valueOf(optionsSettings.getProperty("RestartOnDeadlock", "False"));
			
			BYPASS_VALIDATION = Boolean.valueOf(optionsSettings.getProperty("BypassValidation", "True"));
			
			ONLY_GM_ITEMS_FREE = Boolean.valueOf(optionsSettings.getProperty("OnlyGMItemsFree", "True"));
			
			GRIDS_ALWAYS_ON = Boolean.parseBoolean(optionsSettings.getProperty("GridsAlwaysOn", "False"));
			GRID_NEIGHBOR_TURNON_TIME = Integer.parseInt(optionsSettings.getProperty("GridNeighborTurnOnTime", "1"));
			GRID_NEIGHBOR_TURNOFF_TIME = Integer.parseInt(optionsSettings.getProperty("GridNeighborTurnOffTime", "90"));
			
			ENABLE_FALLING_DAMAGE = Boolean.parseBoolean(optionsSettings.getProperty("EnableFallingDamage", "True"));
			
			/*
			 * Load L2J Server Version Properties file (if exists)
			 */
			Properties serverVersion = new Properties();
			try (InputStream is = new FileInputStream(new File(SERVER_VERSION_FILE)))
			{
				serverVersion.load(is);
			}
			catch (Exception e)
			{
			}
			
			PROJECT_TAG = serverVersion.getProperty("tag", "N/A");
			SERVER_BUILD_DATE = serverVersion.getProperty("buildDate", "N/A");
			
			// Τelnet
			Properties telnetSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(TELNET_FILE)))
			{
				telnetSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + TELNET_FILE + " File.");
			}
			
			IS_TELNET_ENABLED = Boolean.valueOf(telnetSettings.getProperty("EnableTelnet", "False"));
			TELNET_PORT = Integer.parseInt(telnetSettings.getProperty("GameStatusPort", "54321"));
			
			// MMO
			Properties mmoSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(MMO_FILE)))
			{
				mmoSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + MMO_FILE + " File.");
			}
			
			MMO_SELECTOR_SLEEP_TIME = Integer.parseInt(mmoSettings.getProperty("SleepTime", "20"));
			MMO_MAX_SEND_PER_PASS = Integer.parseInt(mmoSettings.getProperty("MaxSendPerPass", "12"));
			MMO_MAX_READ_PER_PASS = Integer.parseInt(mmoSettings.getProperty("MaxReadPerPass", "12"));
			MMO_HELPER_BUFFER_COUNT = Integer.parseInt(mmoSettings.getProperty("HelperBufferCount", "20"));
			
			// id factory
			Properties idSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(ID_FACTORY_FILE)))
			{
				idSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + ID_FACTORY_FILE + " File.");
			}
			
			IDFACTORY_TYPE = IdFactoryType.valueOf(idSettings.getProperty("IDFactory", IdFactoryType.BitSet.name()));
			BAD_ID_CHECKING = Boolean.valueOf(idSettings.getProperty("BadIdChecking", "True"));
			
			// Load FloodProtector Properties file
			Properties security = new Properties();
			try (InputStream is = new FileInputStream(new File(FLOOD_PROTECTOR_FILE)))
			{
				security.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + FLOOD_PROTECTOR_FILE + " File.");
			}
			
			loadFloodProtectorConfigs(security);
			
			// other
			Properties otherSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(OTHER_FILE)))
			{
				otherSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + OTHER_FILE + " File.");
			}
			
			DEEPBLUE_DROP_RULES = Boolean.parseBoolean(otherSettings.getProperty("UseDeepBlueDropRules", "True"));
			GUARD_ATTACK_AGGRO_MOB = Boolean.valueOf(otherSettings.getProperty("GuardAttackAggroMob", "False"));
			EFFECT_CANCELLING = Boolean.valueOf(otherSettings.getProperty("CancelLesserEffect", "True"));
			ALLOW_WYVERN_UPGRADER = Boolean.valueOf(otherSettings.getProperty("AllowWyvernUpgrader", "False"));
			
			/* Inventory slots limits */
			INVENTORY_MAXIMUM_NO_DWARF = Integer.parseInt(otherSettings.getProperty("MaximumSlotsForNoDwarf", "80"));
			INVENTORY_MAXIMUM_DWARF = Integer.parseInt(otherSettings.getProperty("MaximumSlotsForDwarf", "100"));
			INVENTORY_MAXIMUM_GM = Integer.parseInt(otherSettings.getProperty("MaximumSlotsForGMPlayer", "250"));
			
			INVENTORY_MAXIMUM_PET = Integer.parseInt(otherSettings.getProperty("MaximumSlotsForPet", "12"));
			MAX_ITEM_IN_PACKET = Math.max(INVENTORY_MAXIMUM_NO_DWARF, Math.max(INVENTORY_MAXIMUM_DWARF, INVENTORY_MAXIMUM_GM));
			
			/* Warehouse slots limits */
			WAREHOUSE_SLOTS_NO_DWARF = Integer.parseInt(otherSettings.getProperty("MaximumWarehouseSlotsForNoDwarf", "100"));
			WAREHOUSE_SLOTS_DWARF = Integer.parseInt(otherSettings.getProperty("MaximumWarehouseSlotsForDwarf", "120"));
			WAREHOUSE_SLOTS_CLAN = Integer.parseInt(otherSettings.getProperty("MaximumWarehouseSlotsForClan", "200"));
			FREIGHT_SLOTS = Integer.parseInt(otherSettings.getProperty("MaximumFreightSlots", "20"));
			
			/* chance to enchant an item over +3 */
			ENCHANT_CHANCE_WEAPON = Integer.parseInt(otherSettings.getProperty("EnchantChanceWeapon", "68"));
			ENCHANT_CHANCE_ARMOR = Integer.parseInt(otherSettings.getProperty("EnchantChanceArmor", "52"));
			ENCHANT_CHANCE_JEWELRY = Integer.parseInt(otherSettings.getProperty("EnchantChanceJewelry", "54"));
			BLESSED_ENCHANT_CHANCE_WEAPON = Integer.parseInt(otherSettings.getProperty("BlessedEnchantChanceWeapon", "68"));
			BLESSED_ENCHANT_CHANCE_ARMOR = Integer.parseInt(otherSettings.getProperty("BlessedEnchantChanceArmor", "52"));
			BLESSED_ENCHANT_CHANCE_JEWELRY = Integer.parseInt(otherSettings.getProperty("BlessedEnchantChanceJewelry", "54"));
			/* limit on enchant */
			ENCHANT_MAX_WEAPON = Integer.parseInt(otherSettings.getProperty("EnchantMaxWeapon", "25"));
			ENCHANT_MAX_ARMOR = Integer.parseInt(otherSettings.getProperty("EnchantMaxArmor", "25"));
			ENCHANT_MAX_JEWELRY = Integer.parseInt(otherSettings.getProperty("EnchantMaxJewelry", "25"));
			/* limit of safe enchant normal */
			ENCHANT_SAFE_MAX = Integer.parseInt(otherSettings.getProperty("EnchantSafeMax", "3"));
			/* limit of safe enchant full */
			ENCHANT_SAFE_MAX_FULL = Integer.parseInt(otherSettings.getProperty("EnchantSafeMaxFull", "4"));
			
			HP_REGEN_MULTIPLIER = Float.parseFloat(otherSettings.getProperty("HpRegenMultiplier", "1.0"));
			MP_REGEN_MULTIPLIER = Float.parseFloat(otherSettings.getProperty("MpRegenMultiplier", "1.0"));
			CP_REGEN_MULTIPLIER = Float.parseFloat(otherSettings.getProperty("CpRegenMultiplier", "1.0"));
			
			RAID_ATK_MULTIPLIER = Float.parseFloat(otherSettings.getProperty("RaidAtkMultiplier", "1.0"));
			RAID_DEF_MULTIPLIER = Float.parseFloat(otherSettings.getProperty("RaidDefMultiplier", "1.0"));
			RAID_HP_REGEN_MULTIPLIER = Float.parseFloat(otherSettings.getProperty("RaidHpRegenMultiplier", "1.0"));
			RAID_MP_REGEN_MULTIPLIER = Float.parseFloat(otherSettings.getProperty("RaidMpRegenMultiplier", "1.0"));
			RAID_MINION_RESPAWN_TIME = Integer.parseInt(otherSettings.getProperty("RaidMinionRespawnTime", "300000"));
			RAID_MIN_RESPAWN_MULTIPLIER = Float.parseFloat(otherSettings.getProperty("RaidMinRespawnMultiplier", "1.0"));
			RAID_MAX_RESPAWN_MULTIPLIER = Float.parseFloat(otherSettings.getProperty("RaidMaxRespawnMultiplier", "1.0"));
			
			STARTING_ADENA = Integer.parseInt(otherSettings.getProperty("StartingAdena", "0"));
			STARTING_LEVEL = Byte.parseByte(otherSettings.getProperty("StartingLevel", "1"));
			STARTING_SUB_LEVEL = Byte.parseByte(otherSettings.getProperty("StartingSubclassLevel", "40"));
			
			MAX_RUN_SPEED = Integer.parseInt(otherSettings.getProperty("MaxRunSpeed", "250"));
			MAX_PCRIT_RATE = Integer.parseInt(otherSettings.getProperty("MaxPCritRate", "500"));
			MAX_MCRIT_RATE = Integer.parseInt(otherSettings.getProperty("MaxMCritRate", "300"));
			MAX_PATK_SPEED = Integer.parseInt(otherSettings.getProperty("MaxPAtkSpeed", "1500"));
			MAX_MATK_SPEED = Integer.parseInt(otherSettings.getProperty("MaxMAtkSpeed", "1999"));
			
			MAX_PLAYER_LEVEL = Byte.parseByte(otherSettings.getProperty("MaxPlayerLevel", "79"));
			if (MAX_PLAYER_LEVEL > Experience.LEVEL.length - 1)
			{
				MAX_PLAYER_LEVEL = (byte) (Experience.LEVEL.length - 1);
				_log.warning("Invalid max player level! Level was set to " + MAX_PLAYER_LEVEL);
			}
			
			UNSTUCK_INTERVAL = Integer.parseInt(otherSettings.getProperty("UnstuckInterval", "300"));
			
			/* Player protection after teleport or login */
			PLAYER_SPAWN_PROTECTION = Integer.parseInt(otherSettings.getProperty("PlayerSpawnProtection", "0"));
			
			/* Player protection after recovering from fake death (works against mobs only) */
			PLAYER_FAKEDEATH_UP_PROTECTION = Integer.parseInt(otherSettings.getProperty("PlayerFakeDeathUpProtection", "0"));
			
			PLAYER_MOVEMENT_BLOCK_TIME = Integer.parseInt(otherSettings.getProperty("NpcTalkBlockingTime", "1500"));
			
			/* Defines some Party XP related values */
			PARTY_XP_CUTOFF_METHOD = otherSettings.getProperty("PartyXpCutoffMethod", "percentage");
			PARTY_XP_CUTOFF_PERCENT = Double.parseDouble(otherSettings.getProperty("PartyXpCutoffPercent", "3."));
			PARTY_XP_CUTOFF_LEVEL = Integer.parseInt(otherSettings.getProperty("PartyXpCutoffLevel", "30"));
			
			/* Amount of HP that is restored */
			RESPAWN_RESTORE_HP_MULTIPLIER = Float.parseFloat(otherSettings.getProperty("RespawnRestoreHPMultiplier", "0.7"));
			
			RESPAWN_RANDOM_ENABLED = Boolean.parseBoolean(otherSettings.getProperty("RespawnRandomOffset", "True"));
			RESPAWN_RANDOM_MAX_OFFSET = Integer.parseInt(otherSettings.getProperty("RespawnRandomMaxOffset", "20"));
			
			/* Maximum number of available slots for private stores */
			MAX_PRIVATE_STORE_BUY_LIMIT = Integer.parseInt(otherSettings.getProperty("MaxPrivateStoreBuyLimit", "4"));
			MAX_PRIVATE_STORE_SELL_LIMIT = Integer.parseInt(otherSettings.getProperty("MaxPrivateStoreSellLimit", "3"));
			MAX_PRIVATE_STORE_BUY_LIMIT_DWARF = Integer.parseInt(otherSettings.getProperty("MaxPrivateStoreBuyLimitDwarf", "5"));
			MAX_PRIVATE_STORE_SELL_LIMIT_DWARF = Integer.parseInt(otherSettings.getProperty("MaxPrivateStoreSellLimitDwarf", "4"));
			
			STORE_SKILL_COOLTIME = Boolean.parseBoolean(otherSettings.getProperty("StoreSkillCooltime", "True"));
			SUBCLASS_STORE_SKILL_COOLTIME = Boolean.parseBoolean(otherSettings.getProperty("SubclassStoreSkillCooltime", "False"));
			
			PET_RENT_NPC = otherSettings.getProperty("ListPetRentNpc", "7827");
			
			LIST_PET_RENT_NPC = new ArrayList<>();
			if (!PET_RENT_NPC.isEmpty())
			{
				for (String id : PET_RENT_NPC.split(","))
				{
					LIST_PET_RENT_NPC.add(Integer.parseInt(id));
				}
			}
			
			NONDROPPABLE_ITEMS = otherSettings.getProperty("ListOfNonDroppableItems", "1147,425,1146,461,10,2368,7,6,2370,2369,5598");
			
			LIST_NONDROPPABLE_ITEMS = new ArrayList<>();
			if (!NONDROPPABLE_ITEMS.isEmpty())
			{
				for (String id : NONDROPPABLE_ITEMS.split(","))
				{
					LIST_NONDROPPABLE_ITEMS.add(Integer.parseInt(id));
				}
			}
			
			ANNOUNCE_MAMMON_SPAWN = Boolean.parseBoolean(otherSettings.getProperty("AnnounceMammonSpawn", "True"));
			
			GM_NAME_COLOR_ENABLED = Boolean.parseBoolean(otherSettings.getProperty("GMNameColorEnabled", "True"));
			GM_NAME_COLOR = Integer.decode("0x" + otherSettings.getProperty("GMNameColor", "00FFFF"));
			ADMIN_NAME_COLOR = Integer.decode("0x" + otherSettings.getProperty("AdminNameColor", "00FF00"));
			GM_HERO_AURA = Boolean.parseBoolean(otherSettings.getProperty("GMHeroAura", "True"));
			GM_STARTUP_INVULNERABLE = Boolean.parseBoolean(otherSettings.getProperty("GMStartupInvulnerable", "False"));
			GM_STARTUP_INVISIBLE = Boolean.parseBoolean(otherSettings.getProperty("GMStartupInvisible", "False"));
			GM_STARTUP_SILENCE = Boolean.parseBoolean(otherSettings.getProperty("GMStartupSilence", "False"));
			GM_STARTUP_AUTO_LIST = Boolean.parseBoolean(otherSettings.getProperty("GMStartupAutoList", "True"));
			
			PETITIONING_ALLOWED = Boolean.parseBoolean(otherSettings.getProperty("PetitioningAllowed", "True"));
			MAX_PETITIONS_PER_PLAYER = Integer.parseInt(otherSettings.getProperty("MaxPetitionsPerPlayer", "5"));
			MAX_PETITIONS_PENDING = Integer.parseInt(otherSettings.getProperty("MaxPetitionsPending", "25"));
			
			JAIL_IS_PVP = Boolean.valueOf(otherSettings.getProperty("JailIsPvp", "True"));
			JAIL_DISABLE_CHAT = Boolean.valueOf(otherSettings.getProperty("JailDisableChat", "True"));
			
			// Rates
			Properties ratesSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(RATES_FILE)))
			{
				ratesSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + RATES_FILE + " File.");
			}
			
			RATE_XP = Float.parseFloat(ratesSettings.getProperty("RateXp", "1"));
			RATE_SP = Float.parseFloat(ratesSettings.getProperty("RateSp", "1"));
			RATE_PARTY_XP = Float.parseFloat(ratesSettings.getProperty("RatePartyXp", "1"));
			RATE_PARTY_SP = Float.parseFloat(ratesSettings.getProperty("RatePartySp", "1"));
			RATE_QUEST_REWARD = Float.parseFloat(ratesSettings.getProperty("RateQuestReward", "1"));
			RATE_QUEST_REWARD_ADENA = Float.parseFloat(ratesSettings.getProperty("RateQuestRewardAdena", "1"));
			RATE_QUEST_REWARD_XP = Float.parseFloat(ratesSettings.getProperty("RateQuestRewardXp", "1"));
			RATE_QUEST_REWARD_SP = Float.parseFloat(ratesSettings.getProperty("RateQuestRewardSp", "1"));
			RATE_DROP_ADENA = Float.parseFloat(ratesSettings.getProperty("RateDropAdena", "1"));
			RATE_CONSUMABLE_COST = Float.parseFloat(ratesSettings.getProperty("RateConsumableCost", "1"));
			RATE_DROP_ITEMS = Float.parseFloat(ratesSettings.getProperty("RateDropItems", "1"));
			RATE_BOSS_DROP_ITEMS = Float.parseFloat(ratesSettings.getProperty("RateBossDropItems", "1"));
			RATE_DROP_SPOIL = Float.parseFloat(ratesSettings.getProperty("RateDropSpoil", "1"));
			RATE_DROP_MANOR = Integer.parseInt(ratesSettings.getProperty("RateDropManor", "1"));
			RATE_EXTRACT_FISH = Float.parseFloat(ratesSettings.getProperty("RateExtractFish", "1"));
			RATE_DROP_QUEST = Float.parseFloat(ratesSettings.getProperty("RateDropQuest", "1"));
			RATE_KARMA_EXP_LOST = Float.parseFloat(ratesSettings.getProperty("RateKarmaExpLost", "1"));
			RATE_SIEGE_GUARDS_PRICE = Float.parseFloat(ratesSettings.getProperty("RateSiegeGuardsPrice", "1"));
			
			PLAYER_DROP_LIMIT = Integer.parseInt(ratesSettings.getProperty("PlayerDropLimit", "3"));
			PLAYER_RATE_DROP = Integer.parseInt(ratesSettings.getProperty("PlayerRateDrop", "5"));
			PLAYER_RATE_DROP_ITEM = Integer.parseInt(ratesSettings.getProperty("PlayerRateDropItem", "70"));
			PLAYER_RATE_DROP_EQUIP = Integer.parseInt(ratesSettings.getProperty("PlayerRateDropEquip", "25"));
			PLAYER_RATE_DROP_EQUIP_WEAPON = Integer.parseInt(ratesSettings.getProperty("PlayerRateDropEquipWeapon", "5"));
			
			PET_XP_RATE = Float.parseFloat(ratesSettings.getProperty("PetXpRate", "1"));
			PET_FOOD_RATE = Integer.parseInt(ratesSettings.getProperty("PetFoodRate", "1"));
			SINEATER_XP_RATE = Float.parseFloat(ratesSettings.getProperty("SinEaterXpRate", "1"));
			
			KARMA_DROP_LIMIT = Integer.parseInt(ratesSettings.getProperty("KarmaDropLimit", "10"));
			KARMA_RATE_DROP = Integer.parseInt(ratesSettings.getProperty("KarmaRateDrop", "70"));
			KARMA_RATE_DROP_ITEM = Integer.parseInt(ratesSettings.getProperty("KarmaRateDropItem", "50"));
			KARMA_RATE_DROP_EQUIP = Integer.parseInt(ratesSettings.getProperty("KarmaRateDropEquip", "40"));
			KARMA_RATE_DROP_EQUIP_WEAPON = Integer.parseInt(ratesSettings.getProperty("KarmaRateDropEquipWeapon", "10"));
			
			// Alternate settings
			Properties altSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(ALT_SETTINGS_FILE)))
			{
				altSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + ALT_SETTINGS_FILE + " File.");
			}
			
			ALT_GAME_TIREDNESS = Boolean.parseBoolean(altSettings.getProperty("AltGameTiredness", "false"));
			ALT_GAME_CREATION = Boolean.parseBoolean(altSettings.getProperty("AltGameCreation", "false"));
			ALT_GAME_CREATION_SPEED = Double.parseDouble(altSettings.getProperty("AltGameCreationSpeed", "1"));
			ALT_GAME_CREATION_XP_RATE = Double.parseDouble(altSettings.getProperty("AltGameCreationRateXp", "1"));
			ALT_GAME_CREATION_SP_RATE = Double.parseDouble(altSettings.getProperty("AltGameCreationRateSp", "1"));
			ALT_BLACKSMITH_USE_RECIPES = Boolean.parseBoolean(altSettings.getProperty("AltBlacksmithUseRecipes", "true"));
			AUTO_LEARN_SKILLS = Boolean.parseBoolean(altSettings.getProperty("AutoLearnSkills", "false"));
			AUTO_LEARN_3RD_SKILLS = Boolean.parseBoolean(altSettings.getProperty("AutoLearn3rdClassSkills", "false"));
			ALT_GAME_CANCEL_BOW = altSettings.getProperty("AltGameCancelByHit", "Cast").equalsIgnoreCase("bow") || altSettings.getProperty("AltGameCancelByHit", "Cast").equalsIgnoreCase("all");
			ALT_GAME_CANCEL_CAST = altSettings.getProperty("AltGameCancelByHit", "Cast").equalsIgnoreCase("cast") || altSettings.getProperty("AltGameCancelByHit", "Cast").equalsIgnoreCase("all");
			ALT_PERFECT_SHLD_BLOCK = Integer.parseInt(altSettings.getProperty("AltPerfectShieldBlockRate", "5"));
			ALT_GAME_DELEVEL = Boolean.parseBoolean(altSettings.getProperty("Delevel", "true"));
			ALT_WEIGHT_LIMIT = Double.parseDouble(altSettings.getProperty("AltWeightLimit", "1"));
			ALT_GAME_MAGICFAILURES = Boolean.parseBoolean(altSettings.getProperty("MagicFailures", "false"));
			ALT_MOB_AGGRO_IN_PEACEZONE = Boolean.parseBoolean(altSettings.getProperty("AltMobAggroInPeaceZone", "true"));
			ALT_GAME_EXPONENT_XP = Float.parseFloat(altSettings.getProperty("AltGameExponentXp", "0."));
			ALT_GAME_EXPONENT_SP = Float.parseFloat(altSettings.getProperty("AltGameExponentSp", "0."));
			ALT_GAME_FREIGHTS = Boolean.parseBoolean(altSettings.getProperty("AltGameFreights", "false"));
			ALT_GAME_FREIGHT_PRICE = Integer.parseInt(altSettings.getProperty("AltGameFreightPrice", "1000"));
			ALT_PARTY_RANGE = Integer.parseInt(altSettings.getProperty("AltPartyRange", "1600"));
			ALT_PARTY_RANGE2 = Integer.parseInt(altSettings.getProperty("AltPartyRange2", "1400"));
			IS_CRAFTING_ENABLED = Boolean.parseBoolean(altSettings.getProperty("CraftingEnabled", "true"));
			SP_BOOK_NEEDED = Boolean.parseBoolean(altSettings.getProperty("SpBookNeeded", "true"));
			ES_SP_BOOK_NEEDED = Boolean.parseBoolean(altSettings.getProperty("EnchantSkillSpBookNeeded", "true"));
			AUTO_LOOT = Boolean.parseBoolean(altSettings.getProperty("AutoLoot", "false"));
			AUTO_LOOT_RAIDS = Boolean.parseBoolean(altSettings.getProperty("AutoLootRaids", "false"));
			LEVEL_UP_SOUL_CRYSTAL_WHEN_HAS_MANY = Boolean.parseBoolean(altSettings.getProperty("LevelUpSoulCrystalWhenHasMany", "false"));
			ALT_GAME_KARMA_PLAYER_CAN_BE_KILLED_IN_PEACEZONE = Boolean.valueOf(altSettings.getProperty("AltKarmaPlayerCanBeKilledInPeaceZone", "false"));
			ALT_GAME_KARMA_PLAYER_CAN_SHOP = Boolean.valueOf(altSettings.getProperty("AltKarmaPlayerCanShop", "true"));
			ALT_GAME_KARMA_PLAYER_CAN_USE_GK = Boolean.valueOf(altSettings.getProperty("AltKarmaPlayerCanUseGK", "false"));
			ALT_GAME_FLAGGED_PLAYER_CAN_USE_GK = Boolean.valueOf(altSettings.getProperty("AltFlaggedPlayerCanUseGK", "true"));
			ALT_GAME_KARMA_PLAYER_CAN_TELEPORT = Boolean.valueOf(altSettings.getProperty("AltKarmaPlayerCanTeleport", "true"));
			ALT_GAME_KARMA_PLAYER_CAN_TRADE = Boolean.valueOf(altSettings.getProperty("AltKarmaPlayerCanTrade", "true"));
			ALT_GAME_KARMA_PLAYER_CAN_USE_WAREHOUSE = Boolean.valueOf(altSettings.getProperty("AltKarmaPlayerCanUseWareHouse", "true"));
			ALT_GAME_FREE_TELEPORT = Boolean.parseBoolean(altSettings.getProperty("AltFreeTeleporting", "False"));
			ALT_RECOMMEND = Boolean.parseBoolean(altSettings.getProperty("AltRecommend", "False"));
			ALT_MAX_SUBCLASS = Integer.parseInt(altSettings.getProperty("AltMaxSubClasses", "3"));
			ALT_GAME_SUBCLASS_WITHOUT_QUESTS = Boolean.parseBoolean(altSettings.getProperty("AltSubClassWithoutQuests", "False"));
			ALT_ENABLE_TUTORIAL = Boolean.parseBoolean(altSettings.getProperty("AltEnableTutorial", "True"));
			ALT_GAME_VIEWNPC = Boolean.parseBoolean(altSettings.getProperty("AltGameViewNpc", "False"));
			ALT_GAME_NEW_CHAR_ALWAYS_IS_NEWBIE = Boolean.parseBoolean(altSettings.getProperty("AltNewCharAlwaysIsNewbie", "False"));
			ALT_MIN_NEWBIE_LEVEL = Byte.parseByte(altSettings.getProperty("AltMinNewbieLevel", "6"));
			ALT_MAX_NEWBIE_LEVEL = Byte.parseByte(altSettings.getProperty("AltMaxNewbieLevel", "25"));
			DWARF_RECIPE_LIMIT = Integer.parseInt(altSettings.getProperty("DwarfRecipeLimit", "50"));
			COMMON_RECIPE_LIMIT = Integer.parseInt(altSettings.getProperty("CommonRecipeLimit", "50"));
			
			ALT_CLAN_MEMBERS_FOR_WAR = Integer.parseInt(altSettings.getProperty("AltClanMembersForWar", "15"));
			ALT_CLAN_JOIN_DAYS = Integer.parseInt(altSettings.getProperty("DaysBeforeJoinAClan", "5"));
			ALT_CLAN_CREATE_DAYS = Integer.parseInt(altSettings.getProperty("DaysBeforeCreateAClan", "10"));
			
			ALT_CLAN_DISSOLVE_DAYS = Integer.parseInt(altSettings.getProperty("DaysToPassToDissolveAClan", "7"));
			ALT_RECOVERY_PENALTY = Integer.parseInt(altSettings.getProperty("DaysToPassToDissolveAgain", "7"));
			
			ALT_ALLY_JOIN_DAYS_WHEN_LEAVED = Integer.parseInt(altSettings.getProperty("DaysBeforeJoinAllyWhenLeaved", "1"));
			ALT_ALLY_JOIN_DAYS_WHEN_DISMISSED = Integer.parseInt(altSettings.getProperty("DaysBeforeJoinAllyWhenDismissed", "1"));
			ALT_ACCEPT_CLAN_DAYS_WHEN_DISMISSED = Integer.parseInt(altSettings.getProperty("DaysBeforeAcceptNewClanWhenDismissed", "1"));
			ALT_CREATE_ALLY_DAYS_WHEN_DISSOLVED = Integer.parseInt(altSettings.getProperty("DaysBeforeCreateNewAllyWhenDissolved", "10"));
			ALT_MAX_NUM_OF_CLANS_IN_ALLY = Integer.parseInt(altSettings.getProperty("AltMaxNumOfClansInAlly", "12"));
			
			ALT_CHANNEL_ACTIVATION_COUNT = Integer.parseInt(altSettings.getProperty("AltChannelActivationCount", "5"));
			// Just in case admins set it to less than 2 parties
			if (ALT_CHANNEL_ACTIVATION_COUNT < 2)
			{
				ALT_CHANNEL_ACTIVATION_COUNT = 2;
			}
			
			ALT_MANOR_REFRESH_TIME = Integer.parseInt(altSettings.getProperty("AltManorRefreshTime", "20"));
			ALT_MANOR_REFRESH_MIN = Integer.parseInt(altSettings.getProperty("AltManorRefreshMin", "00"));
			ALT_MANOR_APPROVE_TIME = Integer.parseInt(altSettings.getProperty("AltManorApproveTime", "6"));
			ALT_MANOR_APPROVE_MIN = Integer.parseInt(altSettings.getProperty("AltManorApproveMin", "00"));
			ALT_MANOR_MAINTENANCE_PERIOD = Integer.parseInt(altSettings.getProperty("AltManorMaintenancePeriod", "360000"));
			ALT_MANOR_SAVE_ALL_ACTIONS = Boolean.parseBoolean(altSettings.getProperty("AltManorSaveAllActions", "True"));
			ALT_MANOR_SAVE_PERIOD_RATE = Integer.parseInt(altSettings.getProperty("AltManorSavePeriodRate", "2"));
			
			ALT_LOTTERY_PRIZE = Integer.parseInt(altSettings.getProperty("AltLotteryPrize", "50000"));
			ALT_LOTTERY_TICKET_PRICE = Integer.parseInt(altSettings.getProperty("AltLotteryTicketPrice", "2000"));
			ALT_LOTTERY_5_NUMBER_RATE = Float.parseFloat(altSettings.getProperty("AltLottery5NumberRate", "0.6"));
			ALT_LOTTERY_4_NUMBER_RATE = Float.parseFloat(altSettings.getProperty("AltLottery4NumberRate", "0.2"));
			ALT_LOTTERY_3_NUMBER_RATE = Float.parseFloat(altSettings.getProperty("AltLottery3NumberRate", "0.2"));
			ALT_LOTTERY_2_AND_1_NUMBER_PRIZE = Integer.parseInt(altSettings.getProperty("AltLottery2and1NumberPrize", "200"));
			BUFFS_MAX_AMOUNT = Byte.parseByte(altSettings.getProperty("MaxBuffAmount", "20"));
			DEBUFFS_MAX_AMOUNT = Byte.parseByte(altSettings.getProperty("MaxDebuffAmount", "10"));
			BUFF_SLOTS_PER_ROW = Byte.parseByte(altSettings.getProperty("BuffSlotsPerRow", "10"));
			ALT_DEV_NO_QUESTS = Boolean.parseBoolean(altSettings.getProperty("AltDevNoQuests", "False"));
			ALT_DEV_NO_SPAWNS = Boolean.parseBoolean(altSettings.getProperty("AltDevNoSpawns", "False"));
			
			// Four Sepulcher Config
			FS_PENDING_TIME = Integer.parseInt(altSettings.getProperty("PendingTime", "50"));
			FS_ENTRY_TIME = Integer.parseInt(altSettings.getProperty("EntryTime", "5"));
			FS_PARTY_MEMBER_COUNT = Integer.parseInt(altSettings.getProperty("NumberOfNecessaryPartyMembers", "4"));
			
			if (FS_PENDING_TIME <= 0)
			{
				FS_PENDING_TIME = 50;
			}
			if (FS_ENTRY_TIME <= 0)
			{
				FS_ENTRY_TIME = 5;
			}
			if (FS_PARTY_MEMBER_COUNT <= 0)
			{
				FS_PARTY_MEMBER_COUNT = 4;
			}
			
			// Dimensional Rift Config
			RIFT_MIN_PARTY_SIZE = Integer.parseInt(altSettings.getProperty("RiftMinPartySize", "2"));
			RIFT_MAX_JUMPS = Integer.parseInt(altSettings.getProperty("MaxRiftJumps", "4"));
			RIFT_SPAWN_DELAY = Integer.parseInt(altSettings.getProperty("RiftSpawnDelay", "10000"));
			RIFT_AUTO_JUMPS_TIME_MIN = Integer.parseInt(altSettings.getProperty("AutoJumpsDelayMin", "480"));
			RIFT_AUTO_JUMPS_TIME_MAX = Integer.parseInt(altSettings.getProperty("AutoJumpsDelayMax", "600"));
			RIFT_ENTER_COST_RECRUIT = Integer.parseInt(altSettings.getProperty("RecruitCost", "18"));
			RIFT_ENTER_COST_SOLDIER = Integer.parseInt(altSettings.getProperty("SoldierCost", "21"));
			RIFT_ENTER_COST_OFFICER = Integer.parseInt(altSettings.getProperty("OfficerCost", "24"));
			RIFT_ENTER_COST_CAPTAIN = Integer.parseInt(altSettings.getProperty("CaptainCost", "27"));
			RIFT_ENTER_COST_COMMANDER = Integer.parseInt(altSettings.getProperty("CommanderCost", "30"));
			RIFT_ENTER_COST_HERO = Integer.parseInt(altSettings.getProperty("HeroCost", "33"));
			RIFT_BOSS_ROOM_TIME_MULTIPLIER = Float.parseFloat(altSettings.getProperty("BossRoomTimeMultiplier", "1.5"));
			
			// Olympiad settings
			Properties olympiadSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(OLYMPIAD_FILE)))
			{
				olympiadSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + OLYMPIAD_FILE + " File.");
			}
			
			ALT_OLY_START_TIME = Integer.parseInt(olympiadSettings.getProperty("AltOlyStartTime", "20"));
			ALT_OLY_MIN = Integer.parseInt(olympiadSettings.getProperty("AltOlyMin", "00"));
			ALT_OLY_CPERIOD = Long.parseLong(olympiadSettings.getProperty("AltOlyCPeriod", "14400000"));
			ALT_OLY_BATTLE = Long.parseLong(olympiadSettings.getProperty("AltOlyBattle", "180000"));
			ALT_OLY_BWAIT = Long.parseLong(olympiadSettings.getProperty("AltOlyBWait", "600000"));
			ALT_OLY_IWAIT = Long.parseLong(olympiadSettings.getProperty("AltOlyIWait", "300000"));
			ALT_OLY_WPERIOD = Long.parseLong(olympiadSettings.getProperty("AltOlyWPeriod", "604800000"));
			ALT_OLY_VPERIOD = Long.parseLong(olympiadSettings.getProperty("AltOlyVPeriod", "86400000"));
			ALT_OLY_CLASSED = Integer.parseInt(olympiadSettings.getProperty("AltOlyClassedParticipants", "5"));
			ALT_OLY_REG_DISPLAY = Integer.parseInt(olympiadSettings.getProperty("AltOlyRegistrationDisplayNumber","10"));
			ALT_OLY_NONCLASSED = Integer.parseInt(olympiadSettings.getProperty("AltOlyNonClassedParticipants", "9"));
			ALT_OLY_BATTLE_REWARD_ITEM = Integer.parseInt(olympiadSettings.getProperty("AltOlyBattleRewItem", "6651"));
			ALT_OLY_CLASSED_RITEM_C = Integer.parseInt(olympiadSettings.getProperty("AltOlyClassedRewItemCount", "50"));
			ALT_OLY_NONCLASSED_RITEM_C = Integer.parseInt(olympiadSettings.getProperty("AltOlyNonClassedRewItemCount", "30"));
			ALT_OLY_COMP_RITEM = Integer.parseInt(olympiadSettings.getProperty("AltOlyCompRewItem", "6651"));
			ALT_OLY_GP_PER_POINT = Integer.parseInt(olympiadSettings.getProperty("AltOlyGPPerPoint", "1000"));
			ALT_OLY_HERO_POINTS = Integer.parseInt(olympiadSettings.getProperty("AltOlyHeroPoints", "300"));
			ALT_OLY_RANK1_POINTS = Integer.parseInt(olympiadSettings.getProperty("AltOlyRank1Points", "100"));
			ALT_OLY_RANK2_POINTS = Integer.parseInt(olympiadSettings.getProperty("AltOlyRank2Points", "75"));
			ALT_OLY_RANK3_POINTS = Integer.parseInt(olympiadSettings.getProperty("AltOlyRank3Points", "55"));
			ALT_OLY_RANK4_POINTS = Integer.parseInt(olympiadSettings.getProperty("AltOlyRank4Points", "40"));
			ALT_OLY_RANK5_POINTS = Integer.parseInt(olympiadSettings.getProperty("AltOlyRank5Points", "30"));
			ALT_OLY_MAX_POINTS = Integer.parseInt(olympiadSettings.getProperty("AltOlyMaxPoints", "10"));
			ALT_OLY_LOSE_POINTS_ON_TIE = Boolean.valueOf(olympiadSettings.getProperty("AltOlyLosePointsOnTie", "true"));
			ALT_OLY_SHOW_MONTHLY_WINNERS = Boolean.valueOf(olympiadSettings.getProperty("AltOlyShowMonthlyWinners", "true"));
			ALT_OLY_ENCHANT_LIMIT = Integer.parseInt(olympiadSettings.getProperty("AltOlyEnchantLimit", "-1"));
			
			// Custom settings
			Properties customSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(CUSTOM_FILE)))
			{
				customSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + CUSTOM_FILE + " File.");
			}
			
			ANTIBUFF_SHIELD_ENABLE = Boolean.valueOf(customSettings.getProperty("AntibuffShieldEnable", "false"));
			SKILL_REUSE_INDEPENDENT = Boolean.valueOf(customSettings.getProperty("SkillReuseIndependent", "false"));
			
			PASSWORD_CHANGE_ENABLE = Boolean.valueOf(customSettings.getProperty("PasswordChangeEnable", "false"));

			CUSTOM_SPAWNLIST_TABLE = Boolean.valueOf(customSettings.getProperty("CustomSpawnlistTable", "false"));
			SAVE_GMSPAWN_ON_CUSTOM = Boolean.valueOf(customSettings.getProperty("SaveGmSpawnOnCustom", "false"));
			CUSTOM_NPC_TABLE = Boolean.valueOf(customSettings.getProperty("CustomNpcTable", "false"));
			CUSTOM_NPC_SKILLS_TABLE = Boolean.valueOf(customSettings.getProperty("CustomNpcSkillsTable", "false"));
			CUSTOM_MINIONS_TABLE = Boolean.valueOf(customSettings.getProperty("CustomMinionsTable", "false"));
			CUSTOM_ITEM_TABLES = Boolean.valueOf(customSettings.getProperty("CustomItemTables", "false"));
			CUSTOM_ARMORSETS_TABLE = Boolean.valueOf(customSettings.getProperty("CustomArmorSetsTable", "false"));
			CUSTOM_TELEPORT_TABLE = Boolean.valueOf(customSettings.getProperty("CustomTeleportTable", "false"));
			CUSTOM_DROPLIST_TABLE = Boolean.valueOf(customSettings.getProperty("CustomDroplistTable", "false"));
			CUSTOM_MERCHANT_TABLES = Boolean.valueOf(customSettings.getProperty("CustomMerchantTables", "false"));
			CUSTOM_MULTISELL_LOAD = Boolean.valueOf(customSettings.getProperty("CustomMultisellLoad", "false"));
			
			CHAMPION_ENABLE = Boolean.valueOf(customSettings.getProperty("ChampionEnable", "false"));
			CHAMPION_PASSIVE = Boolean.valueOf(customSettings.getProperty("ChampionPassive", "false"));
			CHAMPION_FREQUENCY = Integer.parseInt(customSettings.getProperty("ChampionFrequency", "0"));
			CHAMPION_TITLE = customSettings.getProperty("ChampionTitle", "Champion");
			CHAMP_MIN_LVL = Integer.parseInt(customSettings.getProperty("ChampionMinLevel", "20"));
			CHAMP_MAX_LVL = Integer.parseInt(customSettings.getProperty("ChampionMaxLevel", "70"));
			CHAMPION_HP = Integer.parseInt(customSettings.getProperty("ChampionHp", "8"));
			CHAMPION_HP_REGEN = Float.parseFloat(customSettings.getProperty("ChampionHpRegen", "1."));
			CHAMPION_REWARDS = Float.parseFloat(customSettings.getProperty("ChampionRewards", "8."));
			CHAMPION_ADENAS_REWARDS = Float.parseFloat(customSettings.getProperty("ChampionAdenasRewards", "1."));
			CHAMPION_ATK = Float.parseFloat(customSettings.getProperty("ChampionAtk", "1."));
			CHAMPION_SPD_ATK = Float.parseFloat(customSettings.getProperty("ChampionSpdAtk", "1."));
			CHAMPION_REWARD_LOWER_CHANCE = Integer.parseInt(customSettings.getProperty("ChampionRewardLowerLvlItemChance", "0"));
			CHAMPION_REWARD_HIGHER_CHANCE = Integer.parseInt(customSettings.getProperty("ChampionRewardHigherLvlItemChance", "0"));
			CHAMPION_REWARD_ID = Integer.parseInt(customSettings.getProperty("ChampionRewardItemID", "6393"));
			CHAMPION_REWARD_QTY = Integer.parseInt(customSettings.getProperty("ChampionRewardItemQty", "1"));
			
			ALLOW_AUTO_REWARDER = Boolean.valueOf(customSettings.getProperty("AllowAutoRewarder", "False"));
			AUTO_REWARD_DELAY = Integer.parseInt(customSettings.getProperty("AutoRewardDelay", "1200"));
			AUTO_REWARD_ID = Integer.parseInt(customSettings.getProperty("AutoRewardID", "57"));
			AUTO_REWARD_COUNT = Integer.parseInt(customSettings.getProperty("AutoRewardCount", "1000"));
			
			CUSTOM_STARTING_SPAWN = Boolean.parseBoolean(customSettings.getProperty("CustomStartingSpawn", "False"));
			CUSTOM_SPAWN_X = Integer.parseInt(customSettings.getProperty("CustomSpawnX", ""));
			CUSTOM_SPAWN_Y = Integer.parseInt(customSettings.getProperty("CustomSpawnY", ""));
			CUSTOM_SPAWN_Z = Integer.parseInt(customSettings.getProperty("CustomSpawnZ", ""));
			
			Boost_EXP_COMMAND = Boolean.parseBoolean(customSettings.getProperty("SpExpCommand", "False"));
			AUTO_TARGET_NPC = Boolean.parseBoolean(customSettings.getProperty("EnableAutoTargetNPC", "False"));
			SHOW_NPC_CREST = Boolean.parseBoolean(customSettings.getProperty("ShowNpcCrest", "False"));
			CHANGE_SUBCLASS_EVERYWHERE = Boolean.parseBoolean(customSettings.getProperty("ChooseAllSubClassesEveryWhere", "False"));
			AUTO_NOBLE_STATUS = Boolean.parseBoolean(customSettings.getProperty("AutoNoblesseAtLogin", "False"));
			ALLOW_HERO_ENCHANT = Boolean.parseBoolean(customSettings.getProperty("AllowEnchantHeroItems", "False"));
			MAX_CLIENTS_PER_IP = Integer.parseInt(customSettings.getProperty("MaximumClientsPerIP", "0"));
			ENABLE_REAL_TIME = Boolean.parseBoolean(customSettings.getProperty("EnableRealTime", "False"));
			SEVEN_SIGNS_DUNGEON_NPC_ACCESS = Boolean.parseBoolean(customSettings.getProperty("SevenSignsDungeonNPCAccess", "False"));

			AIO_BUFFER_ENABLED = Boolean.valueOf(customSettings.getProperty("AIOBufferEnabled", "False"));
			NPC_BUFFER_ENABLED = Boolean.valueOf(customSettings.getProperty("NPCBufferEnabled", "False"));
			SCHEMES_MAX_AMOUNT = Integer.parseInt(customSettings.getProperty("MaxSchemeAmount", "4"));
			SCHEME_NAME_TEMPLATE = customSettings.getProperty("SchemeNameTemplate", ".*");
			AIO_BUFFER_NAME_PREFIX = customSettings.getProperty("AIOBufferNamePrefix");
			AIO_BUFFER_SET_NAME_COLOR = Boolean.valueOf(customSettings.getProperty("AIOBufferSetNameColor", "False"));
			AIO_BUFFER_NAME_COLOR = Integer.decode("0x" + customSettings.getProperty("AIOBufferNameColor", "FFD700"));
			BUFFER_BASE_MP_MAX = Float.valueOf(customSettings.getProperty("BufferBaseMpMax", "0"));
			
			ENABLE_MODIFY_SKILL_DURATION = Boolean.parseBoolean(customSettings.getProperty("EnableModifySkillDuration", "False"));
			
			
			// Create Map only if enabled
			if (ENABLE_MODIFY_SKILL_DURATION)
			{
				SKILL_DURATION_LIST = new HashMap<>();
				
				final String propertyRaw = customSettings.getProperty("SkillDurationList", "");
				if (!propertyRaw.isEmpty())
				{
					final String[] propertySplit = propertyRaw.split(";");
					for (String skill : propertySplit)
					{
						final String[] skillSplit = skill.split(",");
						if (skillSplit.length != 2)
						{
							_log.warning("[SkillDurationList]: invalid config property -> SkillDurationList \"" + skill + "\"");
						}
						else
						{
							try
							{
								SKILL_DURATION_LIST.put(Integer.valueOf(skillSplit[0]), Integer.valueOf(skillSplit[1]));
							}
							catch (NumberFormatException nfe)
							{
								if (!skill.isEmpty())
								{
									_log.warning("[SkillDurationList]: invalid config property -> SkillList \"" + skillSplit[0] + "\"" + skillSplit[1]);
								}
							}
						}
					}
				}
			}
			
			ALLOW_WEDDING = Boolean.parseBoolean(customSettings.getProperty("AllowWedding", "False"));
			WEDDING_PRICE = Integer.parseInt(customSettings.getProperty("WeddingPrice", "25000000"));
			WEDDING_TELEPORT = Boolean.parseBoolean(customSettings.getProperty("WeddingTeleport", "True"));
			WEDDING_TELEPORT_PRICE = Integer.parseInt(customSettings.getProperty("WeddingTeleportPrice", "50000"));
			WEDDING_TELEPORT_DURATION = Integer.parseInt(customSettings.getProperty("WeddingTeleportDuration", "60"));
			WEDDING_SAME_SEX = Boolean.parseBoolean(customSettings.getProperty("WeddingAllowSameSex", "False"));
			WEDDING_FORMAL_WEAR = Boolean.parseBoolean(customSettings.getProperty("WeddingFormalWear", "True"));
			
			OFFLINE_TRADE_ENABLE = Boolean.valueOf(customSettings.getProperty("OfflineTradeEnable", "False"));
			OFFLINE_CRAFT_ENABLE = Boolean.valueOf(customSettings.getProperty("OfflineCraftEnable", "False"));
			RESTORE_OFFLINERS = Boolean.parseBoolean(customSettings.getProperty("RestoreOffliners", "True"));
			OFFLINE_MAX_DAYS = Integer.parseInt(customSettings.getProperty("OfflineMaxDays", "10"));
			OFFLINE_DISCONNECT_FINISHED = Boolean.parseBoolean(customSettings.getProperty("OfflineDisconnectFinished", "True"));
			OFFLINE_SET_NAME_COLOR = Boolean.valueOf(customSettings.getProperty("OfflineSetNameColor", "False"));
			OFFLINE_NAME_COLOR = Integer.decode("0x" + customSettings.getProperty("OfflineNameColor", "808080"));
			ALLOW_SIEGE_TELEPORT = Boolean.valueOf(customSettings.getProperty("AllowSiegeTeleport", "False"));
			KEEP_SUBCLASS_SKILLS = Boolean.valueOf(customSettings.getProperty("KeepSubClassSkills", "False"));
			ALLOW_CLASS_MASTERS = Boolean.valueOf(customSettings.getProperty("AllowClassMasters", "False"));
			ALLOW_ENTIRE_TREE = Boolean.valueOf(customSettings.getProperty("AllowEntireTree", "False"));
			ALTERNATE_CLASS_MASTER = Boolean.valueOf(customSettings.getProperty("AlternateClassMaster", "False"));
			if (ALLOW_CLASS_MASTERS || ALTERNATE_CLASS_MASTER)
			{
				CLASS_MASTER_SETTINGS = new ClassMasterSettings(String.valueOf(customSettings.getProperty("ConfigClassMaster")));
			}
			
			// Feature settings
			Properties Feature = new Properties();
			try (InputStream is = new FileInputStream(new File(FEATURE_FILE)))
			{
				Feature.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + FEATURE_FILE + " File.");
			}
			
			CS_TELE_FEE_RATIO = Long.parseLong(Feature.getProperty("CastleTeleportFunctionFeeRatio", "604800000"));
			CS_TELE1_FEE = Integer.parseInt(Feature.getProperty("CastleTeleportFunctionFeeLvl1", "7000"));
			CS_TELE2_FEE = Integer.parseInt(Feature.getProperty("CastleTeleportFunctionFeeLvl2", "14000"));
			CS_SUPPORT_FEE_RATIO = Long.parseLong(Feature.getProperty("CastleSupportFunctionFeeRatio", "86400000"));
			CS_SUPPORT1_FEE = Integer.parseInt(Feature.getProperty("CastleSupportFeeLvl1", "7000"));
			CS_SUPPORT2_FEE = Integer.parseInt(Feature.getProperty("CastleSupportFeeLvl2", "21000"));
			CS_SUPPORT3_FEE = Integer.parseInt(Feature.getProperty("CastleSupportFeeLvl3", "37000"));
			CS_SUPPORT4_FEE = Integer.parseInt(Feature.getProperty("CastleSupportFeeLvl4", "52000"));
			CS_MPREG_FEE_RATIO = Long.parseLong(Feature.getProperty("CastleMpRegenerationFunctionFeeRatio", "86400000"));
			CS_MPREG1_FEE = Integer.parseInt(Feature.getProperty("CastleMpRegenerationFeeLvl1", "2000"));
			CS_MPREG2_FEE = Integer.parseInt(Feature.getProperty("CastleMpRegenerationFeeLvl2", "6500"));
			CS_MPREG3_FEE = Integer.parseInt(Feature.getProperty("CastleMpRegenerationFeeLvl3", "13750"));
			CS_MPREG4_FEE = Integer.parseInt(Feature.getProperty("CastleMpRegenerationFeeLvl4", "20000"));
			CS_HPREG_FEE_RATIO = Long.parseLong(Feature.getProperty("CastleHpRegenerationFunctionFeeRatio", "86400000"));
			CS_HPREG1_FEE = Integer.parseInt(Feature.getProperty("CastleHpRegenerationFeeLvl1", "1000"));
			CS_HPREG2_FEE = Integer.parseInt(Feature.getProperty("CastleHpRegenerationFeeLvl2", "1500"));
			CS_HPREG3_FEE = Integer.parseInt(Feature.getProperty("CastleHpRegenerationFeeLvl3", "2250"));
			CS_HPREG4_FEE = Integer.parseInt(Feature.getProperty("CastleHpRegenerationFeeLvl4", "3270"));
			CS_HPREG5_FEE = Integer.parseInt(Feature.getProperty("CastleHpRegenerationFeeLvl5", "5166"));
			CS_EXPREG_FEE_RATIO = Long.parseLong(Feature.getProperty("CastleExpRegenerationFunctionFeeRatio", "86400000"));
			CS_EXPREG1_FEE = Integer.parseInt(Feature.getProperty("CastleExpRegenerationFeeLvl1", "9000"));
			CS_EXPREG2_FEE = Integer.parseInt(Feature.getProperty("CastleExpRegenerationFeeLvl2", "15000"));
			CS_EXPREG3_FEE = Integer.parseInt(Feature.getProperty("CastleExpRegenerationFeeLvl3", "21000"));
			CS_EXPREG4_FEE = Integer.parseInt(Feature.getProperty("CastleExpRegenerationFeeLvl4", "30000"));
			
			CH_TELE_FEE_RATIO = Long.valueOf(Feature.getProperty("ClanHallTeleportFunctionFeeRation", "86400000"));
			CH_TELE1_FEE = Integer.valueOf(Feature.getProperty("ClanHallTeleportFunctionFeeLvl1", "86400000"));
			CH_TELE2_FEE = Integer.valueOf(Feature.getProperty("ClanHallTeleportFunctionFeeLvl2", "86400000"));
			CH_SUPPORT_FEE_RATIO = Long.valueOf(Feature.getProperty("ClanHallSupportFunctionFeeRation", "86400000"));
			CH_SUPPORT1_FEE = Integer.valueOf(Feature.getProperty("ClanHallSupportFeeLvl1", "86400000"));
			CH_SUPPORT2_FEE = Integer.valueOf(Feature.getProperty("ClanHallSupportFeeLvl2", "86400000"));
			CH_SUPPORT3_FEE = Integer.valueOf(Feature.getProperty("ClanHallSupportFeeLvl3", "86400000"));
			CH_SUPPORT4_FEE = Integer.valueOf(Feature.getProperty("ClanHallSupportFeeLvl4", "86400000"));
			CH_SUPPORT5_FEE = Integer.valueOf(Feature.getProperty("ClanHallSupportFeeLvl5", "86400000"));
			CH_SUPPORT6_FEE = Integer.valueOf(Feature.getProperty("ClanHallSupportFeeLvl6", "86400000"));
			CH_SUPPORT7_FEE = Integer.valueOf(Feature.getProperty("ClanHallSupportFeeLvl7", "86400000"));
			CH_SUPPORT8_FEE = Integer.valueOf(Feature.getProperty("ClanHallSupportFeeLvl8", "86400000"));
			CH_MPREG_FEE_RATIO = Long.valueOf(Feature.getProperty("ClanHallMpRegenerationFunctionFeeRation", "86400000"));
			CH_MPREG1_FEE = Integer.valueOf(Feature.getProperty("ClanHallMpRegenerationFeeLvl1", "86400000"));
			CH_MPREG2_FEE = Integer.valueOf(Feature.getProperty("ClanHallMpRegenerationFeeLvl2", "86400000"));
			CH_MPREG3_FEE = Integer.valueOf(Feature.getProperty("ClanHallMpRegenerationFeeLvl3", "86400000"));
			CH_MPREG4_FEE = Integer.valueOf(Feature.getProperty("ClanHallMpRegenerationFeeLvl4", "86400000"));
			CH_MPREG5_FEE = Integer.valueOf(Feature.getProperty("ClanHallMpRegenerationFeeLvl5", "86400000"));
			CH_HPREG_FEE_RATIO = Long.valueOf(Feature.getProperty("ClanHallHpRegenerationFunctionFeeRation", "86400000"));
			CH_HPREG1_FEE = Integer.valueOf(Feature.getProperty("ClanHallHpRegenerationFeeLvl1", "86400000"));
			CH_HPREG2_FEE = Integer.valueOf(Feature.getProperty("ClanHallHpRegenerationFeeLvl2", "86400000"));
			CH_HPREG3_FEE = Integer.valueOf(Feature.getProperty("ClanHallHpRegenerationFeeLvl3", "86400000"));
			CH_HPREG4_FEE = Integer.valueOf(Feature.getProperty("ClanHallHpRegenerationFeeLvl4", "86400000"));
			CH_HPREG5_FEE = Integer.valueOf(Feature.getProperty("ClanHallHpRegenerationFeeLvl5", "86400000"));
			CH_HPREG6_FEE = Integer.valueOf(Feature.getProperty("ClanHallHpRegenerationFeeLvl6", "86400000"));
			CH_HPREG7_FEE = Integer.valueOf(Feature.getProperty("ClanHallHpRegenerationFeeLvl7", "86400000"));
			CH_HPREG8_FEE = Integer.valueOf(Feature.getProperty("ClanHallHpRegenerationFeeLvl8", "86400000"));
			CH_HPREG9_FEE = Integer.valueOf(Feature.getProperty("ClanHallHpRegenerationFeeLvl9", "86400000"));
			CH_HPREG10_FEE = Integer.valueOf(Feature.getProperty("ClanHallHpRegenerationFeeLvl10", "86400000"));
			CH_HPREG11_FEE = Integer.valueOf(Feature.getProperty("ClanHallHpRegenerationFeeLvl11", "86400000"));
			CH_HPREG12_FEE = Integer.valueOf(Feature.getProperty("ClanHallHpRegenerationFeeLvl12", "86400000"));
			CH_HPREG13_FEE = Integer.valueOf(Feature.getProperty("ClanHallHpRegenerationFeeLvl13", "86400000"));
			CH_EXPREG_FEE_RATIO = Long.valueOf(Feature.getProperty("ClanHallExpRegenerationFunctionFeeRation", "86400000"));
			CH_EXPREG1_FEE = Integer.valueOf(Feature.getProperty("ClanHallExpRegenerationFeeLvl1", "86400000"));
			CH_EXPREG2_FEE = Integer.valueOf(Feature.getProperty("ClanHallExpRegenerationFeeLvl2", "86400000"));
			CH_EXPREG3_FEE = Integer.valueOf(Feature.getProperty("ClanHallExpRegenerationFeeLvl3", "86400000"));
			CH_EXPREG4_FEE = Integer.valueOf(Feature.getProperty("ClanHallExpRegenerationFeeLvl4", "86400000"));
			CH_EXPREG5_FEE = Integer.valueOf(Feature.getProperty("ClanHallExpRegenerationFeeLvl5", "86400000"));
			CH_EXPREG6_FEE = Integer.valueOf(Feature.getProperty("ClanHallExpRegenerationFeeLvl6", "86400000"));
			CH_EXPREG7_FEE = Integer.valueOf(Feature.getProperty("ClanHallExpRegenerationFeeLvl7", "86400000"));
			CH_ITEM_FEE_RATIO = Long.valueOf(Feature.getProperty("ClanHallItemCreationFunctionFeeRation", "86400000"));
			CH_ITEM1_FEE = Integer.valueOf(Feature.getProperty("ClanHallItemCreationFunctionFeeLvl1", "86400000"));
			CH_ITEM2_FEE = Integer.valueOf(Feature.getProperty("ClanHallItemCreationFunctionFeeLvl2", "86400000"));
			CH_ITEM3_FEE = Integer.valueOf(Feature.getProperty("ClanHallItemCreationFunctionFeeLvl3", "86400000"));
			CH_CURTAIN_FEE_RATIO = Long.valueOf(Feature.getProperty("ClanHallCurtainFunctionFeeRation", "86400000"));
			CH_CURTAIN1_FEE = Integer.valueOf(Feature.getProperty("ClanHallCurtainFunctionFeeLvl1", "86400000"));
			CH_CURTAIN2_FEE = Integer.valueOf(Feature.getProperty("ClanHallCurtainFunctionFeeLvl2", "86400000"));
			CH_FRONT_FEE_RATIO = Long.valueOf(Feature.getProperty("ClanHallFrontPlatformFunctionFeeRation", "86400000"));
			CH_FRONT1_FEE = Integer.valueOf(Feature.getProperty("ClanHallFrontPlatformFunctionFeeLvl1", "86400000"));
			CH_FRONT2_FEE = Integer.valueOf(Feature.getProperty("ClanHallFrontPlatformFunctionFeeLvl2", "86400000"));
			
			ALT_GAME_REQUIRE_CASTLE_DAWN = Boolean.parseBoolean(Feature.getProperty("AltRequireCastleForDawn", "False"));
			ALT_GAME_REQUIRE_CLAN_CASTLE = Boolean.parseBoolean(Feature.getProperty("AltRequireClanCastle", "False"));
			ALT_FESTIVAL_MIN_PLAYER = Integer.parseInt(Feature.getProperty("AltFestivalMinPlayer", "5"));
			ALT_MAXIMUM_PLAYER_CONTRIB = Integer.parseInt(Feature.getProperty("AltMaxPlayerContrib", "1000000"));
			ALT_FESTIVAL_MANAGER_START = Long.parseLong(Feature.getProperty("AltFestivalManagerStart", "120000"));
			ALT_FESTIVAL_LENGTH = Long.parseLong(Feature.getProperty("AltFestivalLength", "1080000"));
			ALT_FESTIVAL_CYCLE_LENGTH = Long.parseLong(Feature.getProperty("AltFestivalCycleLength", "2280000"));
			ALT_FESTIVAL_FIRST_SPAWN = Long.parseLong(Feature.getProperty("AltFestivalFirstSpawn", "120000"));
			ALT_FESTIVAL_FIRST_SWARM = Long.parseLong(Feature.getProperty("AltFestivalFirstSwarm", "300000"));
			ALT_FESTIVAL_SECOND_SPAWN = Long.parseLong(Feature.getProperty("AltFestivalSecondSpawn", "540000"));
			ALT_FESTIVAL_SECOND_SWARM = Long.parseLong(Feature.getProperty("AltFestivalSecondSwarm", "720000"));
			ALT_FESTIVAL_CHEST_SPAWN = Long.parseLong(Feature.getProperty("AltFestivalChestSpawn", "900000"));
			
			// Pvp config
			Properties pvpSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(PVP_FILE)))
			{
				pvpSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + PVP_FILE + " File.");
			}
			
			/* Karma System */
			KARMA_MIN_KARMA = Integer.parseInt(pvpSettings.getProperty("MinKarma", "240"));
			KARMA_MAX_KARMA = Integer.parseInt(pvpSettings.getProperty("MaxKarma", "10000"));
			KARMA_XP_DIVIDER = Integer.parseInt(pvpSettings.getProperty("XPDivider", "260"));
			KARMA_LOST_BASE = Integer.parseInt(pvpSettings.getProperty("BaseKarmaLost", "0"));
			
			GM_ON_DIE_DROP_ITEM = Boolean.parseBoolean(pvpSettings.getProperty("CanGMDropEquipment", "false"));
			KARMA_AWARD_PK_KILL = Boolean.parseBoolean(pvpSettings.getProperty("AwardPKKillPVPPoint", "true"));
			
			KARMA_PK_LIMIT = Integer.parseInt(pvpSettings.getProperty("MinimumPKRequiredToDrop", "5"));
			
			KARMA_NONDROPPABLE_PET_ITEMS = pvpSettings.getProperty("ListOfPetItems", "2375,3500,3501,3502,4422,4423,4424,4425,6648,6649,6650");
			KARMA_NONDROPPABLE_ITEMS = pvpSettings.getProperty("ListOfNonDroppableItems", "57,1147,425,1146,461,10,2368,7,6,2370,2369,6842,6611,6612,6613,6614,6615,6616,6617,6618,6619,6620,6621");
			
			KARMA_LIST_NONDROPPABLE_PET_ITEMS = new ArrayList<>();
			if (!KARMA_NONDROPPABLE_PET_ITEMS.isEmpty())
			{
				for (String id : KARMA_NONDROPPABLE_PET_ITEMS.split(","))
				{
					KARMA_LIST_NONDROPPABLE_PET_ITEMS.add(Integer.parseInt(id));
				}
			}
			
			KARMA_LIST_NONDROPPABLE_ITEMS = new ArrayList<>();
			if (!KARMA_NONDROPPABLE_ITEMS.isEmpty())
			{
				for (String id : KARMA_NONDROPPABLE_ITEMS.split(","))
				{
					KARMA_LIST_NONDROPPABLE_ITEMS.add(Integer.parseInt(id));
				}
			}
			
			PVP_NORMAL_TIME = Integer.parseInt(pvpSettings.getProperty("PvPVsNormalTime", "15000"));
			PVP_PVP_TIME = Integer.parseInt(pvpSettings.getProperty("PvPVsPvPTime", "30000"));
			
			// Access levels
			Properties accessLevelSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(ACCESS_LEVELS_FILE)))
			{
				accessLevelSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + ACCESS_LEVELS_FILE + " File.");
			}
			
			MASTER_ACCESS_LEVEL = Integer.parseInt(accessLevelSettings.getProperty("MasterAccessLevel", "100"));
			GM_ESCAPE = Integer.parseInt(accessLevelSettings.getProperty("GMFastUnstuck", "100"));
			GM_FIXED = Integer.parseInt(accessLevelSettings.getProperty("GMResurrectFixed", "100"));
			GM_PEACE_ATTACK = Integer.parseInt(accessLevelSettings.getProperty("GMPeaceAttack", "100"));
			GM_TRANSACTION = Integer.parseInt(accessLevelSettings.getProperty("GMTransaction", "100"));
			GM_ITEM_RESTRICTION = Integer.parseInt(accessLevelSettings.getProperty("GMItemRestriction", "1000"));
			GM_SKILL_RESTRICTION = Integer.parseInt(accessLevelSettings.getProperty("GMSkillRestriction", "1000"));
			
			// Geodata
			Properties geoDataSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(GEODATA_FILE)))
			{
				geoDataSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + GEODATA_FILE + " File.");
			}

			PATHFINDING = Integer.parseInt(geoDataSettings.getProperty("PathFinding", "0"));
			
			try
			{
				PATHNODE_DIR = new File(geoDataSettings.getProperty("PathnodeDirectory", "./data/pathnode").replaceAll("\\\\", "/")).getCanonicalFile();
			}
			catch (Exception e)
			{
				_log.warning("Error setting pathnode directory!");
				PATHNODE_DIR = new File("./data/pathnode");
			}
			
			PATHFIND_BUFFERS = geoDataSettings.getProperty("PathFindBuffers", "100x6;128x6;192x6;256x4;320x4;384x4;500x2");
			LOW_WEIGHT = Float.parseFloat(geoDataSettings.getProperty("LowWeight", "0.5"));
			MEDIUM_WEIGHT = Float.parseFloat(geoDataSettings.getProperty("MediumWeight", "2"));
			HIGH_WEIGHT = Float.parseFloat(geoDataSettings.getProperty("HighWeight", "3"));
			ADVANCED_DIAGONAL_STRATEGY = Boolean.parseBoolean(geoDataSettings.getProperty("AdvancedDiagonalStrategy", "True"));
			DIAGONAL_WEIGHT = Float.parseFloat(geoDataSettings.getProperty("DiagonalWeight", "0.707"));
			MAX_POSTFILTER_PASSES = Integer.parseInt(geoDataSettings.getProperty("MaxPostfilterPasses", "3"));
			DEBUG_PATH = Boolean.parseBoolean(geoDataSettings.getProperty("DebugPath", "False"));
			FORCE_GEODATA = Boolean.parseBoolean(geoDataSettings.getProperty("ForceGeoData", "True"));
			COORD_SYNCHRONIZE = Integer.parseInt(geoDataSettings.getProperty("CoordSynchronize", "-1"));
			
			GEODATA_PATH = Paths.get(geoDataSettings.getProperty("GeoDataPath", "./data/geodata"));
			TRY_LOAD_UNSPECIFIED_REGIONS = Boolean.parseBoolean(geoDataSettings.getProperty("TryLoadUnspecifiedRegions", "True"));
			GEODATA_REGIONS = new HashMap<>();
			for (int regionX = L2World.TILE_X_MIN; regionX <= L2World.TILE_X_MAX; regionX++)
			{
				for (int regionY = L2World.TILE_Y_MIN; regionY <= L2World.TILE_Y_MAX; regionY++)
				{
					String key = regionX + "_" + regionY;
					if (geoDataSettings.containsKey(key))
					{
						GEODATA_REGIONS.put(key, Boolean.parseBoolean(geoDataSettings.getProperty(key, "False")));
					}
				}
			}
			
			// Grand bosses
			Properties grandBossSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(GRAND_BOSS_FILE)))
			{
				grandBossSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + GRAND_BOSS_FILE + " File.");
			}
			
			ANTHARAS_WAIT_TIME = Integer.parseInt(grandBossSettings.getProperty("AntharasWaitTime", "30"));
			VALAKAS_WAIT_TIME = Integer.parseInt(grandBossSettings.getProperty("ValakasWaitTime", "30"));
			ANTHARAS_SPAWN_INTERVAL = Integer.parseInt(grandBossSettings.getProperty("AntharasSpawnInterval", "264"));
			ANTHARAS_SPAWN_RANDOM_INTERVAL = Integer.parseInt(grandBossSettings.getProperty("AntharasSpawnRandomInterval", "72"));
			VALAKAS_SPAWN_INTERVAL = Integer.parseInt(grandBossSettings.getProperty("ValakasSpawnInterval", "264"));
			VALAKAS_SPAWN_RANDOM_INTERVAL = Integer.parseInt(grandBossSettings.getProperty("ValakasSpawnRandomInterval", "72"));
			BAIUM_SPAWN_INTERVAL = Integer.parseInt(grandBossSettings.getProperty("BaiumSpawnInterval", "168"));
			BAIUM_SPAWN_RANDOM_INTERVAL = Integer.parseInt(grandBossSettings.getProperty("BaiumSpawnRandomInterval", "48"));
			CORE_SPAWN_INTERVAL = Integer.parseInt(grandBossSettings.getProperty("CoreSpawnInterval", "60"));
			CORE_SPAWN_RANDOM_INTERVAL = Integer.parseInt(grandBossSettings.getProperty("CoreSpawnRandomInterval", "23"));
			ORFEN_SPAWN_INTERVAL = Integer.parseInt(grandBossSettings.getProperty("OrfenSpawnInterval", "48"));
			ORFEN_SPAWN_RANDOM_INTERVAL = Integer.parseInt(grandBossSettings.getProperty("OrfenSpawnRandomInterval", "20"));
			QUEEN_ANT_SPAWN_INTERVAL = Integer.parseInt(grandBossSettings.getProperty("QueenAntSpawnInterval", "36"));
			QUEEN_ANT_SPAWN_RANDOM_INTERVAL = Integer.parseInt(grandBossSettings.getProperty("QueenAntSpawnRandomInterval", "17"));
			ZAKEN_SPAWN_INTERVAL = Integer.parseInt(grandBossSettings.getProperty("ZakenSpawnInterval", "60"));
			ZAKEN_SPAWN_RANDOM_INTERVAL = Integer.parseInt(grandBossSettings.getProperty("ZakenSpawnRandomInterval", "20"));
			
			try (InputStream is = new FileInputStream(HEXID_FILE))
			{
				Properties hexidSettings = new Properties();
				hexidSettings.load(is);
				
				SERVER_ID = Integer.parseInt(hexidSettings.getProperty("ServerID"));
				HEX_ID = new BigInteger(hexidSettings.getProperty("HexID"), 16).toByteArray();
			}
			catch (Exception e)
			{
				_log.warning("Could not load HexID file (" + HEXID_FILE + "). Hopefully login will give us one.");
			}
		}
		else if (Server.SERVER_MODE == Server.MODE_LOGINSERVER)
		{
			_log.info("Loading LoginServer Configuration Files.");
			
			Properties serverSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(LOGIN_SERVER_FILE)))
			{
				serverSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + LOGIN_SERVER_FILE + " File.");
			}
			
			ENABLE_UPNP = Boolean.parseBoolean(serverSettings.getProperty("EnableUPnP", "False"));
			
			GAME_SERVER_LOGIN_HOST = serverSettings.getProperty("LoginHostname", "*");
			GAME_SERVER_LOGIN_PORT = Integer.parseInt(serverSettings.getProperty("LoginPort", "9014"));
			
			LOGIN_BIND_ADDRESS = serverSettings.getProperty("LoginserverHostname", "*");
			PORT_LOGIN = Integer.parseInt(serverSettings.getProperty("LoginserverPort", "2106"));
			
			LOGIN_TRY_BEFORE_BAN = Integer.parseInt(serverSettings.getProperty("LoginTryBeforeBan", "10"));
			LOGIN_BLOCK_AFTER_BAN = Integer.parseInt(serverSettings.getProperty("LoginBlockAfterBan", "600"));
			
			ACCEPT_NEW_GAMESERVER = Boolean.parseBoolean(serverSettings.getProperty("AcceptNewGameServer", "True"));
			SHOW_LICENCE = Boolean.parseBoolean(serverSettings.getProperty("ShowLicence", "true"));
			
			try
			{
				DATAPACK_ROOT = new File(serverSettings.getProperty("DatapackRoot", ".").replaceAll("\\\\", "/")).getCanonicalFile();
			}
			catch (Exception e)
			{
				_log.warning("Error setting datapack root!");
				DATAPACK_ROOT = new File(".");
			}
			
			DATABASE_DRIVER = serverSettings.getProperty("Driver", "org.mariadb.jdbc.Driver");
			DATABASE_URL = serverSettings.getProperty("URL", "jdbc:mariadb://localhost/l2jdb?useSSL=false");
			DATABASE_LOGIN = serverSettings.getProperty("Login", "root");
			DATABASE_PASSWORD = serverSettings.getProperty("Password", "");
			DATABASE_MAX_CONNECTIONS = Integer.parseInt(serverSettings.getProperty("MaximumDbConnections", "5"));
			DATABASE_MAX_IDLE_TIME = Integer.parseInt(serverSettings.getProperty("MaximumDbIdleTime", "0"));
			
			AUTO_CREATE_ACCOUNTS = Boolean.parseBoolean(serverSettings.getProperty("AutoCreateAccounts", "True"));
			GAMEGUARD_ENFORCE = Boolean.parseBoolean(serverSettings.getProperty("GameGuardEnforce", "False"));
			ACCEPT_CHAOTIC_THRONE_CLIENTS = Boolean.parseBoolean(serverSettings.getProperty("AcceptChaoticThroneClients", "False"));
			
			DEBUG = Boolean.parseBoolean(serverSettings.getProperty("Debug", "false"));
			DEVELOPER = Boolean.parseBoolean(serverSettings.getProperty("Developer", "false"));
			ASSERT = Boolean.parseBoolean(serverSettings.getProperty("Assert", "false"));
			
			FLOOD_PROTECTION = Boolean.parseBoolean(serverSettings.getProperty("EnableFloodProtection", "True"));
			FAST_CONNECTION_LIMIT = Integer.parseInt(serverSettings.getProperty("FastConnectionLimit", "15"));
			NORMAL_CONNECTION_TIME = Integer.parseInt(serverSettings.getProperty("NormalConnectionTime", "700"));
			FAST_CONNECTION_TIME = Integer.parseInt(serverSettings.getProperty("FastConnectionTime", "350"));
			MAX_CONNECTION_PER_IP = Integer.parseInt(serverSettings.getProperty("MaxConnectionPerIP", "50"));
			
			LOG_LOGIN_ATTEMPTS = Boolean.parseBoolean(serverSettings.getProperty("LogLoginAttempts", "False"));
			
			// MMO
			Properties mmoSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(MMO_FILE)))
			{
				mmoSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + MMO_FILE + " File.");
			}
			
			MMO_SELECTOR_SLEEP_TIME = Integer.parseInt(mmoSettings.getProperty("SleepTime", "20"));
			MMO_MAX_SEND_PER_PASS = Integer.parseInt(mmoSettings.getProperty("MaxSendPerPass", "12"));
			MMO_MAX_READ_PER_PASS = Integer.parseInt(mmoSettings.getProperty("MaxReadPerPass", "12"));
			MMO_HELPER_BUFFER_COUNT = Integer.parseInt(mmoSettings.getProperty("HelperBufferCount", "20"));
			
			// Telnet
			Properties telnetSettings = new Properties();
			try (InputStream is = new FileInputStream(new File(TELNET_FILE)))
			{
				telnetSettings.load(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + TELNET_FILE + " File.");
			}
			
			IS_TELNET_ENABLED = Boolean.valueOf(telnetSettings.getProperty("EnableTelnet", "False"));
			TELNET_PORT = Integer.parseInt(telnetSettings.getProperty("LoginStatusPort", "12345"));
		}
		else
		{
			_log.severe("Could not Load Config: server mode was not set");
		}
	}
	
	/**
	 * Save hexadecimal ID of the server in the properties file.
	 * 
	 * @param serverId 
	 * @param string (String) : hexadecimal ID of the server to store
	 * @see net.sf.l2j.Config#HEXID_FILE
	 * @see net.sf.l2j.Config#saveHexid(int serverId, String string, String fileName)
	 */
	public static void saveHexid(int serverId, String string)
	{
		saveHexid(serverId, string, HEXID_FILE);
	}
	
	/**
	 * Save hexadecimal ID of the server in the properties file.
	 * 
	 * @param serverId 
	 * @param string (String) : hexadecimal ID of the server to store
	 * @param fileName (String) : name of the properties file
	 */
	public static void saveHexid(int serverId, String string, String fileName)
	{
		try
		{
			Properties hexSettings = new Properties();
			File file = new File(fileName);
			// Create a new empty file only if it doesn't exist
			file.createNewFile();
			
			try (OutputStream out = new FileOutputStream(file))
			{
				hexSettings.setProperty("ServerID", String.valueOf(serverId));
				hexSettings.setProperty("HexID", string);
				hexSettings.store(out, "the hexID to auth into login");
			}
		}
		catch (Exception e)
		{
			_log.warning("Failed to save hex id to " + fileName + " File.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads flood protector configurations.
	 * @param properties
	 */
	private static void loadFloodProtectorConfigs(final Properties properties)
	{
		loadFloodProtectorConfig(properties, FLOOD_PROTECTOR_USE_ITEM, "UseItem", "4");
		loadFloodProtectorConfig(properties, FLOOD_PROTECTOR_ROLL_DICE, "RollDice", "42");
		loadFloodProtectorConfig(properties, FLOOD_PROTECTOR_FIREWORK, "Firework", "42");
		loadFloodProtectorConfig(properties, FLOOD_PROTECTOR_ITEM_PET_SUMMON, "ItemPetSummon", "16");
		loadFloodProtectorConfig(properties, FLOOD_PROTECTOR_HERO_VOICE, "HeroVoice", "100");
		loadFloodProtectorConfig(properties, FLOOD_PROTECTOR_GLOBAL_CHAT, "GlobalChat", "5");
		loadFloodProtectorConfig(properties, FLOOD_PROTECTOR_SUBCLASS, "Subclass", "20");
		loadFloodProtectorConfig(properties, FLOOD_PROTECTOR_DROP_ITEM, "DropItem", "10");
		loadFloodProtectorConfig(properties, FLOOD_PROTECTOR_SERVER_BYPASS, "ServerBypass", "5");
		loadFloodProtectorConfig(properties, FLOOD_PROTECTOR_MULTISELL, "MultiSell", "1");
		loadFloodProtectorConfig(properties, FLOOD_PROTECTOR_TRANSACTION, "Transaction", "10");
		loadFloodProtectorConfig(properties, FLOOD_PROTECTOR_MANUFACTURE, "Manufacture", "3");
		loadFloodProtectorConfig(properties, FLOOD_PROTECTOR_MANOR, "Manor", "30");
		loadFloodProtectorConfig(properties, FLOOD_PROTECTOR_CHARACTER_SELECT, "CharacterSelect", "30");
	}
	
	/**
	 * Loads single flood protector configuration.
	 * @param properties Properties file reader
	 * @param config flood protector configuration instance
	 * @param configString flood protector configuration string that determines for which flood protector configuration should be read
	 * @param defaultInterval default flood protector interval
	 */
	private static void loadFloodProtectorConfig(final Properties properties, final FloodProtectorConfig config, final String configString, final String defaultInterval)
	{
		config.FLOOD_PROTECTION_INTERVAL = Integer.parseInt(properties.getProperty(StringUtil.concat("FloodProtector", configString, "Interval"), defaultInterval));
		config.LOG_FLOODING = Boolean.parseBoolean(properties.getProperty(StringUtil.concat("FloodProtector", configString, "LogFlooding"), "False"));
		config.PUNISHMENT_LIMIT = Integer.parseInt(properties.getProperty(StringUtil.concat("FloodProtector", configString, "PunishmentLimit"), "0"));
		config.PUNISHMENT_TYPE = properties.getProperty(StringUtil.concat("FloodProtector", configString, "PunishmentType"), "none");
		config.PUNISHMENT_TIME = Integer.parseInt(properties.getProperty(StringUtil.concat("FloodProtector", configString, "PunishmentTime"), "0"));
	}
	
	public static class ClassMasterSettings
	{
		private final Map<Integer, Map<Integer, Integer>> _claimItems;
		private final Map<Integer, Map<Integer, Integer>> _rewardItems;
		private final Map<Integer, Boolean> _allowedClassChange;
		
		public ClassMasterSettings(String _configLine)
		{
			_claimItems = new HashMap<>(3);
			_rewardItems = new HashMap<>(3);
			_allowedClassChange = new HashMap<>(3);
			if (_configLine != null)
			{
				parseConfigLine(_configLine.trim());
			}
		}
		
		private void parseConfigLine(String _configLine)
		{
			StringTokenizer st = new StringTokenizer(_configLine, ";");
			
			while (st.hasMoreTokens())
			{
				// get allowed class change
				int job = Integer.parseInt(st.nextToken());
				
				_allowedClassChange.put(job, true);
				
				Map<Integer, Integer> _items = new HashMap<>();
				// parse items needed for class change
				if (st.hasMoreTokens())
				{
					StringTokenizer st2 = new StringTokenizer(st.nextToken(), "[],");
					
					while (st2.hasMoreTokens())
					{
						StringTokenizer st3 = new StringTokenizer(st2.nextToken(), "()");
						int _itemId = Integer.parseInt(st3.nextToken());
						int _quantity = Integer.parseInt(st3.nextToken());
						_items.put(_itemId, _quantity);
					}
				}
				
				_claimItems.put(job, _items);
				
				_items = new HashMap<>();
				// parse gifts after class change
				if (st.hasMoreTokens())
				{
					StringTokenizer st2 = new StringTokenizer(st.nextToken(), "[],");
					
					while (st2.hasMoreTokens())
					{
						StringTokenizer st3 = new StringTokenizer(st2.nextToken(), "()");
						int _itemId = Integer.parseInt(st3.nextToken());
						int _quantity = Integer.parseInt(st3.nextToken());
						_items.put(_itemId, _quantity);
					}
				}
				
				_rewardItems.put(job, _items);
			}
		}
		
		public boolean isAllowed(int job)
		{
			if (_allowedClassChange == null)
			{
				return false;
			}
			
			if (_allowedClassChange.containsKey(job))
			{
				return _allowedClassChange.get(job);
			}
			
			return false;
		}
		
		public Map<Integer, Integer> getRewardItems(int job)
		{
			if (_rewardItems.containsKey(job))
			{
				return _rewardItems.get(job);
			}
			
			return null;
		}
		
		public Map<Integer, Integer> getRequireItems(int job)
		{
			if (_claimItems.containsKey(job))
			{
				return _claimItems.get(job);
			}
			
			return null;
		}
	}
}