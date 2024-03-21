#!/bin/bash
############################################
## WARNING!  WARNING!  WARNING!  WARNING! ##
##                                        ##
## DON'T USE NOTEPAD TO CHANGE THIS FILE  ##
## INSTEAD USE SOME DECENT TEXT EDITOR.   ##
## NEWLINE CHARACTERS DIFFER BETWEEN DOS/ ##
## WINDOWS AND UNIX.                      ##
##                                        ##
## USING NOTEPAD TO SAVE THIS FILE WILL   ##
## LEAVE IT IN A BROKEN STATE!!!          ##
############################################
## Writen by DrLecter                     ##
## License: GNU GPL                       ##
## Based on Tiago Tagliaferri's script    ##
## E-mail: tiago_tagliaferri@msn.com      ##
## From "L2J-DataPack"                    ##
############################################
trap finish 2

GSPATH="/var/gameserver"
EXEC_SQL="mariadb"              # mariadb       | mysql
EXEC_SQL_DUMP="mariadb-dump"    # mariadub-dump | mysqldump

configure() {
echo "#############################################"
echo "# You entered script configuration area     #"
echo "# No change will be performed in your DB    #"
echo "# I will just ask you some questions about  #"
echo "# your hosts and DB.                        #"
echo "#############################################"

MYSQLDUMPPATH=`which $EXEC_SQL_DUMP 2>/dev/null`
MYSQLPATH=`which $EXEC_SQL 2>/dev/null`

if [ $? -ne 0 ]; then
echo "We were unable to find MySQL binaries on your path"
while :
 do
  echo -ne "\nPlease enter MySQL binaries directory (no trailing slash): "
  read MYSQLBINPATH
    if [ -e "$MYSQLBINPATH" ] && [ -d "$MYSQLBINPATH" ] && [ -e "$MYSQLBINPATH/mysqldump" ] && [ -e "$MYSQLBINPATH/mysql" ]; then
       MYSQLDUMPPATH="$MYSQLBINPATH/mysqldump"
       MYSQLPATH="$MYSQLBINPATH/mysql"
       break
    else
       echo "The data you entered is invalid. Please verify and try again."
       exit 1
    fi
 done
fi
#LS
echo -ne "\nPlease enter MySQL Login Server hostname (default localhost): "
read LSDBHOST
if [ -z "$LSDBHOST" ]; then
  LSDBHOST="localhost"
fi
echo -ne "\nPlease enter MySQL Login Server database name (default l2jdb): "
read LSDB
if [ -z "$LSDB" ]; then
  LSDB="l2jdb"
fi
echo -ne "\nPlease enter MySQL Login Server user (default root): "
read LSUSER
if [ -z "$LSUSER" ]; then
  LSUSER="root"
fi
echo -ne "\nPlease enter MySQL Login Server $LSUSER's password (won't be displayed) :"
stty -echo
read LSPASS
stty echo
echo ""
if [ -z "$LSPASS" ]; then
  echo "Hum.. i'll let it be but don't be stupid and avoid empty passwords"
elif [ "$LSUSER" == "$LSPASS" ]; then
  echo "You're not too brilliant choosing passwords huh?"
fi
#GS
echo -ne "\nPlease enter MySQL Game Server hostname (default $LSDBHOST): "
read GSDBHOST
if [ -z "$GSDBHOST" ]; then
  GSDBHOST="$LSDBHOST"
fi
echo -ne "\nPlease enter MySQL Game Server database name (default $LSDB): "
read GSDB
if [ -z "$GSDB" ]; then
  GSDB="$LSDB"
fi
echo -ne "\nPlease enter MySQL Game Server user (default $LSUSER): "
read GSUSER
if [ -z "$GSUSER" ]; then
  GSUSER="$LSUSER"
fi
echo -ne "\nPlease enter MySQL Game Server $GSUSER's password (won't be displayed): "
stty -echo
read GSPASS
stty echo
echo ""
if [ -z "$GSPASS" ]; then
  echo "Hum.. i'll let it be but don't be stupid and avoid empty passwords"
elif [ "$GSUSER" == "$GSPASS" ]; then
  echo "You're not too brilliant choosing passwords huh?"
fi
save_config $1
}

save_config() {
if [ -n "$1" ]; then
CONF="$1"
else 
CONF="$GSPATH/tools/database_installer.rc"
fi
echo ""
echo "With these data i can generate a configuration file which can be read"
echo "on future updates. WARNING: this file will contain clear text passwords!"
echo -ne "Shall i generate config file $CONF? (Y/n):"
read SAVE
if [ "$SAVE" == "y" -o "$SAVE" == "Y" -o "$SAVE" == "" ];then 
cat <<EOF>$CONF
#Configuration settings for L2JLisvus database installer script
MYSQLDUMPPATH=$MYSQLDUMPPATH
MYSQLPATH=$MYSQLPATH
LSDBHOST=$LSDBHOST
LSDB=$LSDB
LSUSER=$LSUSER
LSPASS=$LSPASS
GSDBHOST=$GSDBHOST
GSDB=$GSDB
GSUSER=$GSUSER
GSPASS=$GSPASS
EOF
chmod 600 $CONF
echo "Configuration saved as $CONF"
echo "Permissions changed to 600 (rw- --- ---)"
elif [ "$SAVE" != "n" -a "$SAVE" != "N" ]; then
  save_config
fi
}

load_config() {
if [ -n "$1" ]; then
CONF="$1"
else 
CONF="$GSPATH/tools/database_installer.rc"
fi
if [ -e "$CONF" ] && [ -f "$CONF" ]; then
. $CONF
else
echo "Settings file not found: $CONF"
echo "You can specify an alternate settings filename:"
echo $0 config_filename
echo ""
echo "If file doesn't exist it can be created"
echo "If nothing is specified script will try to work with $GSPATH/tools/database_installer.rc"
echo ""
configure $CONF
fi
}

asklogin(){
echo "#############################################"
echo "# WARNING: This section of the script CAN   #"
echo "# destroy your characters and accounts      #"
echo "# information. Read questions carefully     #"
echo "# before you reply.                         #"
echo "#############################################"
echo ""
echo "Choose upgrade (u) if you already have an 'accounts' table but no"
echo "'gameserver' table (ie. your server is a pre LS/GS split version.)"
echo "Choose skip (s) to skip loginserver DB installation and go to"
echo "gameserver DB installation/upgrade."
echo -ne "LOGINSERVER DB install type: (f) full, (u) upgrade or (s) skip or (q) quit? "
read LOGINPROMPT
case "$LOGINPROMPT" in
 "f"|"F") logininstall; loginupgrade; gsbackup; asktype;;
 "u"|"U") loginupgrade; gsbackup; asktype;;
 "s"|"S") gsbackup; asktype;;
 "q"|"Q") finish;;
 *) asklogin;;
esac
}

logininstall(){
echo "Deleting loginserver tables for new content."
$MYL < $GSPATH/tools/login_install.sql &> /dev/null
}

loginupgrade(){
echo "Installling new loginserver content."
$MYL < $GSPATH/sql/accounts.sql &> /dev/null
$MYL < $GSPATH/sql/gameservers.sql &> /dev/null
}

gsbackup(){
while :
  do
   echo ""
   echo -ne "Do you want to make a backup copy of your GSDB? (y/n): "
   read LSB
   if [ "$LSB" == "Y" -o "$LSB" == "y" ]; then
     echo "Making a backup of the original gameserver database."
     mkdir $GSPATH/sql/bak 2> /dev/null
     $MYSQLDUMPPATH --add-drop-table -h $GSDBHOST -u $GSUSER --password=$GSPASS $GSDB > $GSPATH/sql/bak/gameserver_backup.sql
     if [ $? -ne 0 ];then
     echo ""
     echo "There was a problem accesing your GS database, either it wasnt created or authentication data is incorrect."
     exit 1
     fi
     break
   elif [ "$LSB" == "n" -o "$LSB" == "N" ]; then 
     break
   fi
  done 
}

lsbackup(){
while :
  do
   echo ""
   echo -ne "Do you want to make a backup copy of your LSDB? (y/n): "
   read LSB
   if [ "$LSB" == "Y" -o "$LSB" == "y" ]; then
     echo "Making a backup of the original loginserver database."
     mkdir $GSPATH/sql/bak 2> /dev/null
     $MYSQLDUMPPATH --add-drop-table -h $LSDBHOST -u $LSUSER --password=$LSPASS $LSDB > $GSPATH/sql/bak/loginserver_backup.sql
     if [ $? -ne 0 ];then
        echo ""
        echo "There was a problem accesing your LS database, either it wasnt created or authentication data is incorrect."
        exit 1
     fi
     break
   elif [ "$LSB" == "n" -o "$LSB" == "N" ]; then 
     break
   fi
  done 
}

asktype(){
echo ""
echo ""
echo "WARNING: A full install (f) will destroy all existing character data."
echo -ne "GAMESERVER DB install type: (f) full install, (u) upgrade, (s) skip or (q) quit?"
read INSTALLTYPE
case "$INSTALLTYPE" in
 "f"|"F") fullinstall; upgradeinstall I; custom;;
 "u"|"U") upgradeinstall U; custom;;
        "s"|"S") custom;;
 "q"|"Q") finish;;
 *) asktype;;
esac
}

fullinstall(){
echo "Deleting all gameserver tables for new content."
$MYG < $GSPATH/tools/full_install.sql &> /dev/null
}

upgradeinstall(){
if [ "$1" == "I" ]; then 
echo "Installling new gameserver content."
else
echo "Upgrading gameserver content"
fi
$MYG < $GSPATH/sql/aio_buffer.sql &> /dev/null
$MYG < $GSPATH/sql/armor.sql &> /dev/null
$MYG < $GSPATH/sql/armorsets.sql &> /dev/null
$MYG < $GSPATH/sql/auction.sql &> /dev/null
$MYG < $GSPATH/sql/auction_bid.sql &> /dev/null
$MYG < $GSPATH/sql/auction_watch.sql &> /dev/null
$MYG < $GSPATH/sql/auto_announcements.sql &> /dev/null
$MYG < $GSPATH/sql/auto_chat.sql &> /dev/null
$MYG < $GSPATH/sql/auto_chat_text.sql &> /dev/null
$MYG < $GSPATH/sql/castle.sql &> /dev/null
$MYG < $GSPATH/sql/castle_doorupgrade.sql &> /dev/null
$MYG < $GSPATH/sql/castle_functions.sql &> /dev/null
$MYG < $GSPATH/sql/castle_manor_procure.sql &> /dev/null
$MYG < $GSPATH/sql/castle_manor_production.sql &> /dev/null
$MYG < $GSPATH/sql/castle_siege_guards.sql &> /dev/null
$MYG < $GSPATH/sql/character_buff_schemes.sql &> /dev/null
$MYG < $GSPATH/sql/character_friends.sql &> /dev/null
$MYG < $GSPATH/sql/character_hennas.sql &> /dev/null
$MYG < $GSPATH/sql/character_macroses.sql &> /dev/null
$MYG < $GSPATH/sql/character_offline_trade.sql &> /dev/null
$MYG < $GSPATH/sql/character_offline_trade_items.sql &> /dev/null
$MYG < $GSPATH/sql/character_quests.sql &> /dev/null
$MYG < $GSPATH/sql/character_recipebook.sql &> /dev/null
$MYG < $GSPATH/sql/character_recommends.sql &> /dev/null
$MYG < $GSPATH/sql/character_shortcuts.sql &> /dev/null
$MYG < $GSPATH/sql/character_skills.sql &> /dev/null
$MYG < $GSPATH/sql/character_skills_save.sql &> /dev/null
$MYG < $GSPATH/sql/character_subclasses.sql &> /dev/null
$MYG < $GSPATH/sql/characters.sql &> /dev/null
$MYG < $GSPATH/sql/clan_data.sql &> /dev/null
$MYG < $GSPATH/sql/clan_wars.sql &> /dev/null
$MYG < $GSPATH/sql/clanhall.sql &> /dev/null
$MYG < $GSPATH/sql/clanhall_functions.sql &> /dev/null
$MYG < $GSPATH/sql/dimensional_rift.sql &> /dev/null
$MYG < $GSPATH/sql/droplist.sql &> /dev/null
$MYG < $GSPATH/sql/enchant_skill_trees.sql &> /dev/null
$MYG < $GSPATH/sql/etcitem.sql &> /dev/null
$MYG < $GSPATH/sql/fish.sql &> /dev/null
$MYG < $GSPATH/sql/fishing_skill_trees.sql &> /dev/null
$MYG < $GSPATH/sql/forums.sql &> /dev/null
$MYG < $GSPATH/sql/four_sepulchers_spawnlist.sql &> /dev/null
$MYG < $GSPATH/sql/games.sql &> /dev/null
$MYG < $GSPATH/sql/global_tasks.sql &> /dev/null
$MYG < $GSPATH/sql/grandboss_data.sql &> /dev/null
$MYG < $GSPATH/sql/grandboss_list.sql &> /dev/null
$MYG < $GSPATH/sql/helper_buff_list.sql &> /dev/null
$MYG < $GSPATH/sql/henna.sql &> /dev/null
$MYG < $GSPATH/sql/henna_trees.sql &> /dev/null
$MYG < $GSPATH/sql/heroes.sql &> /dev/null
$MYG < $GSPATH/sql/items.sql &> /dev/null
$MYG < $GSPATH/sql/itemsonground.sql &> /dev/null
$MYG < $GSPATH/sql/locations.sql &> /dev/null
$MYG < $GSPATH/sql/mapregion.sql &> /dev/null
$MYG < $GSPATH/sql/merchant_areas_list.sql &> /dev/null
$MYG < $GSPATH/sql/merchant_buylists.sql &> /dev/null
$MYG < $GSPATH/sql/merchant_lease.sql &> /dev/null
$MYG < $GSPATH/sql/merchant_shopids.sql &> /dev/null
$MYG < $GSPATH/sql/merchants.sql &> /dev/null
$MYG < $GSPATH/sql/minions.sql &> /dev/null
$MYG < $GSPATH/sql/npc.sql &> /dev/null
$MYG < $GSPATH/sql/npc_buffer.sql &> /dev/null
$MYG < $GSPATH/sql/npcskills.sql &> /dev/null
$MYG < $GSPATH/sql/olympiad_data.sql &> /dev/null
$MYG < $GSPATH/sql/olympiad_nobles.sql&> /dev/null
$MYG < $GSPATH/sql/olympiad_nobles_eom.sql&> /dev/null
$MYG < $GSPATH/sql/pets.sql &> /dev/null
$MYG < $GSPATH/sql/pets_stats.sql &> /dev/null
$MYG < $GSPATH/sql/posts.sql &> /dev/null
$MYG < $GSPATH/sql/quest_global_data.sql &> /dev/null
$MYG < $GSPATH/sql/raidboss_spawnlist.sql &> /dev/null
$MYG < $GSPATH/sql/random_spawn.sql &> /dev/null
$MYG < $GSPATH/sql/random_spawn_loc.sql &> /dev/null
$MYG < $GSPATH/sql/seven_signs.sql &> /dev/null
$MYG < $GSPATH/sql/seven_signs_festival.sql &> /dev/null
$MYG < $GSPATH/sql/seven_signs_status.sql &> /dev/null
$MYG < $GSPATH/sql/siege_clans.sql &> /dev/null
$MYG < $GSPATH/sql/skill_learn.sql &> /dev/null
$MYG < $GSPATH/sql/skill_spellbooks.sql &> /dev/null
$MYG < $GSPATH/sql/skill_trees.sql &> /dev/null
$MYG < $GSPATH/sql/spawnlist.sql &> /dev/null
$MYG < $GSPATH/sql/teleport.sql &> /dev/null
$MYG < $GSPATH/sql/topic.sql &> /dev/null
$MYG < $GSPATH/sql/walker_routes.sql &> /dev/null
$MYG < $GSPATH/sql/weapon.sql &> /dev/null
$MYG < $GSPATH/sql/weddings.sql &> /dev/null
}

custom(){
echo ""
echo ""
echo -ne "Install custom gameserver DB tables: (y) yes or (n) no or (q) quit?"
read ASKCS
case "$ASKCS" in
        "y"|"Y") cstinstall;;
        "n"|"N") finish;;
        "q"|"Q") finish;;
        *) custom;;
esac
finish
}

cstinstall(){
while :
  do
   echo ""
   echo -ne "Do you want to make another backup of GSDB before applying custom contents? (y/N): "
   read LSB
   if [ "$LSB" == "Y" -o "$LSB" == "y" ]; then
     echo "Making a backup of the default gameserver tables."
     mkdir $GSPATH/sql/bak 2> /dev/null
     $MYSQLDUMPPATH --add-drop-table -h $GSDBHOST -u $GSUSER --password=$GSPASS $GSDB > $GSPATH/sql/bak/custom_backup.sql 2> /dev/null
     if [ $? -ne 0 ];then
     echo ""
     echo "There was a problem accesing your GS database, server down?."
     exit 1
     fi
     break
   elif [ "$LSB" == "n" -o "$LSB" == "N" -o "$LSB" == "" ]; then 
     break
   fi
  done 
echo "Installing custom content."
for custom in $(ls $GSPATH/sql/custom/*.sql);do 
$MYG < $custom &> /dev/null
done
finish
}

finish(){
echo ""
echo "Script execution finished."
echo "Thank you for using L2JLisvus, a fork of L2JServer Project."
exit 0
}

clear
load_config $1
MYL="$MYSQLPATH -h $LSDBHOST -u $LSUSER --password=$LSPASS -D $LSDB"
MYG="$MYSQLPATH -h $GSDBHOST -u $GSUSER --password=$GSPASS -D $GSDB"
lsbackup
asklogin
