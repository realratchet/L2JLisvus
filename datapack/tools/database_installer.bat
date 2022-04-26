@echo off
REM #############################################################
REM ## L2JLisvus Database Installer - (written by DrLecter) ##
REM #############################################################
REM ## Interactive script setup -  (written by TanelTM)        ##
REM #############################################################
REM This program is free software: you can redistribute it and/or modify it under
REM the terms of the GNU General Public License as published by the Free Software
REM Foundation, either version 3 of the License, or (at your option) any later
REM version.
REM
REM This program is distributed in the hope that it will be useful, but WITHOUT
REM ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
REM FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
REM details.
REM
REM You should have received a copy of the GNU General Public License along with
REM this program. If not, see http://www.gnu.org/licenses/.

set config_file=vars.txt
set config_version=0

set full=0
set stage=0
set logging=0

set upgrade_mode=0
set backup=.
set logdir=.
set safe_mode=1
set cmode=c
set fresh_setup=0

:loadconfig
title L2JLisvus DB Installer - Reading configuration from file...
cls
if not exist %config_file% goto configure
ren %config_file% vars.bat
call vars.bat
ren vars.bat %config_file%
call :colors 17
if /i %config_version% == 1 goto ls_section
set upgrade_mode=2
echo It seems to be the first time you run this version of
echo database_installer but I found a settings file already.
echo I'll hopefully ask this questions just once.
echo.
echo Configuration upgrade options:
echo.
echo (1) Import and continue: I'll read your old settings and
echo    continue execution, but since no new settings will be
echo    saved, you'll see this menu again next time.
echo.
echo (2) Import and configure: This tool has some new options
echo    available, you choose the values that fit your needs
echo    using former settings as a base.
echo.
echo (3) Ignose stored settings: I'll let you configure me
echo    with a fresh set of default values as a base.
echo.
echo (4) View saved settings: See the contents of the config
echo    file.
echo.
echo (5) Quit: Did you come here by mistake?
echo.
set /P upgrade_mode="Type a number, press Enter (default is '%upgrade_mode%'): "
if %upgrade_mode%==1 goto ls_section
if %upgrade_mode%==2 goto configure
if %upgrade_mode%==3 goto configure
if %upgrade_mode%==4 (cls&type %config_file%&pause&goto loadconfig)
if %upgrade_mode%==5 goto :eof
goto loadconfig

:colors
if /i "%cmode%"=="n" (
if not "%1"=="17" (     color F ) else ( color )
) else ( color %1 )
goto :eof

:configure
call :colors 17
title L2JLisvus DB Installer - Setup
cls
set config_version=1
if NOT %upgrade_mode% == 2 (
set fresh_setup=1
set mysqlBinPath=%ProgramFiles%\MySQL\MySQL Server 5.7\bin
set lsuser=root
set lspass=
set lsdb=l2jdb
set lshost=localhost
set gsuser=root
set gspass=
set gsdb=l2jdb
set gshost=localhost

set cmode=c
set backup=.
set logdir=.
)
set mysqlPath=%mysqlBinPath%\mysql.exe
echo New settings will be created for this tool to run in
echo your computer, so I need to ask you few questions.
echo.
echo 1-MySql Binaries
echo --------------------
echo In order to perform my tasks, I need the path for commands
echo such as 'mysql' and 'mysqldump'. Both executables are
echo usually stored in the same place.
echo.
if "%mysqlBinPath%" == "" (
set mysqlBinPath=use path
echo I can't determine if the binaries are available with your
echo default settings.
) else (
echo I can try to find out if the current setting actually works...
echo.
echo %mysqlPath%
)
if not "%mysqlBinPath%" == "use path" call :binaryfind
echo.
echo Searching for MySQL binaries inside PATH...
echo.
WHERE findstr > NUL 2> NUL
if %errorlevel% == 0 echo %path% | findstr /i /c:"mysql" > NUL
if %errorlevel% == 0 (
echo I found MySQL is in your PATH, this will be used by default.
echo If you want to use something different, change 'use path' for
echo something else.
set mysqlBinPath=use path
) else (
echo Look, I can't find "MYSQL" in your PATH environment variable.
echo It would be good if you go and find out where "mysql.exe" and
echo "mysqldump.exe" are.
echo.
echo If you have no idea about the meaning of words such as MYSQL
echo or PATH, you'd better close this window, and consider googling
echo and reading about it. Setup and host an L2J server requires a
echo minimum of technical skills.
)
echo.
echo Write the path to your MySQL binaries (no trailing slash needed):
set /P mysqlBinPath="(default %mysqlBinPath%): "
cls
echo.
echo 2-LoginServer settings
echo --------------------
echo I will connect to the MySQL server you specify, and setup a
echo Loginserver database there, most people use a single MySQL
echo server and database for both Login and Gameserver tables.
echo.
set /P lsuser="MySQL Username (default is '%lsuser%'): "
set /P lspass="Password (will be shown as you type, default '%lspass%'): "
set /P lsdb="Database (default is '%lsdb%'): "
set /P lshost="Host (default is '%lshost%'): "
echo.
echo 3-GameServer settings
echo --------------------
set /P gsuser="User (default is '%gsuser%'): "
set /P gspass="Pass (default is '%gspass%'): "
set /P gsdb="Database (default is '%gsdb%'): "
set /P gshost="Host (default is '%gshost%'): "
echo.
echo 4-Misc. settings
echo --------------------
set /P cmode="Color mode (c)olor or (n)on-color, default %cmode% : "
set /P backup="Path for your backups (default '%backup%'): "
set /P logdir="Path for your logs (default '%logdir%'): "
:safe1
set safemode=y
set /P safemode="Debugging messages and increase verbosity a lil bit (y/n, default '%safemode%'): "
if /i %safemode%==y (set safe_mode=1&goto safe2)
if /i %safemode%==n (set safe_mode=0&goto safe2)
goto safe1
:safe2
echo.
if "%mysqlBinPath%" == "use path" (
set mysqlBinPath=
set mysqldumpPath=mysqldump
set mysqlPath=mysql
) else (
set mysqldumpPath=%mysqlBinPath%\mysqldump.exe
set mysqlPath=%mysqlBinPath%\mysql.exe
)
echo @echo off > %config_file%
echo set config_version=%config_version% >> %config_file%
echo set cmode=%cmode%>> %config_file%
echo set safe_mode=%safe_mode% >> %config_file%
echo set mysqlPath=%mysqlPath%>> %config_file%
echo set mysqlBinPath=%mysqlBinPath%>> %config_file%
echo set mysqldumpPath=%mysqldumpPath%>> %config_file%
echo set lsuser=%lsuser%>> %config_file%
echo set lspass=%lspass%>> %config_file%
echo set lsdb=%lsdb%>> %config_file%
echo set lshost=%lshost% >> %config_file%
echo set gsuser=%gsuser%>> %config_file%
echo set gspass=%gspass%>> %config_file%
echo set gsdb=%gsdb%>> %config_file%
echo set gshost=%gshost%>> %config_file%
echo set logdir=%logdir%>> %config_file%
echo set backup=%backup%>> %config_file%
echo.
echo Script setup complete, your settings were saved in the
echo '%config_file%' file. Remember: your passwords are stored
echo as clear text.
echo.
echo press any key to continue...
pause> nul
goto loadconfig

:ls_section
cls
call :colors 17
set cmdline=
set stage=1
title L2JLisvus DB Installer - Login Server database setup
echo.
echo Trying to make a backup of your loginserver database.
set cmdline="%mysqldumpPath%" --add-drop-table -h %lshost% -u %lsuser% --password=%lspass% %lsdb% ^> "%backup%\loginserver_backup.sql" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto lsdbok
REM if %safe_mode% == 1 goto omfg
:ls_err1
call :colors 47
title L2JLisvus DB Installer - Login Server database setup ERROR!!!
cls
echo.
echo Backup attempt failed! A possible reason for this to 
echo happen, is that your DB doesn't exist yet. I could 
echo try to create %lsdb% for you, or maybe you prefer to
echo proceed with the GameServer part of this tool.
echo.
:ls_ask1
set lsdbprompt=y
echo ATTEMPT TO CREATE LOGINSERVER DATABASE:
echo.
echo (y)es
echo.
echo (n)o
echo.
echo (r)econfigure
echo.
echo (q)uit
echo.
set /p lsdbprompt= Choose (default yes):
if /i %lsdbprompt%==y goto lsdbcreate
if /i %lsdbprompt%==n goto gs_backup
if /i %lsdbprompt%==r goto configure
if /i %lsdbprompt%==q goto end
goto ls_ask1

:omfg
cls
call :colors 57
title L2JLisvus DB Installer - potential PICNIC detected at stage %stage%
echo.
echo There was some problem while executing:
echo.
echo "%cmdline%"
echo.
echo I'd suggest you to look for correct values and try this
echo script again later. But maybe you'd prefer to go on now.
echo.
if %stage% == 1 set label=ls_err1
if %stage% == 2 set label=ls_err2
if %stage% == 3 set label=gs_backup
if %stage% == 4 set label=gs_err1
if %stage% == 5 set label=gs_err2
if %stage% == 6 set label=horrible_end
if %stage% == 7 set label=horrible_end
:omfgask1
set omfgprompt=q
echo (c)ontinue running the script
echo.
echo (r)econfigure
echo.
echo (q)uit now
echo.
set /p omfgprompt= Choose (default quit):
if  /i %omfgprompt%==c goto %label%
if  /i %omfgprompt%==r goto configure
if  /i %omfgprompt%==q goto horrible_end
goto omfgask1

:lsdbcreate
call :colors 17
set cmdline=
set stage=2
title L2JLisvus DB Installer - Login Server database setup - DB Creation
echo.
echo Trying to create a Login Server database...
set cmdline="%mysqlPath%" -h %lshost% -u %lsuser% --password=%lspass% -e "CREATE DATABASE %lsdb%" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto logininstall
if %safe_mode% == 1 goto omfg
:ls_err2
call :colors 47
title L2JLisvus DB Installer - Login Server database setup - DB Creation error
cls
echo An error occured while trying to create a database for 
echo your login server.
echo.
echo Possible reasons:
echo 1-You provided innacurate info , check user, password, etc.
echo 2-User %lsuser% don't have enough privileges for 
echo database creation. Check your MySQL privileges.
echo 3-Database exists already...?
echo.
echo Unless you're sure that the pending actions of this tool 
echo could work, i'd suggest you to look for correct values
echo and try this script again later.
echo.
:ls_ask2
set omfgprompt=q
echo (c)ontinue running
echo.
echo (r)econfigure
echo.
echo (q)uit now
echo.
set /p omfgprompt= Choose (default quit):
if /i %omfgprompt%==c goto gs_backup
if /i %omfgprompt%==q goto horrible_end
if /i %omfgprompt%==r goto configure
goto ls_ask2

:lsdbok
call :colors 17
title L2JLisvus DB Installer - Login Server database setup - WARNING!!!
echo.
:asklogin
if %fresh_setup%==0 (
set loginprompt=s
set msg=default skip
) else (
set loginprompt=x
set msg=no default for fresh install
)
echo LOGINSERVER DATABASE install type:
echo.
echo (f)ull: I will destroy data in your `accounts` and 
echo    and `gameserver` tables.
echo.
echo (s)kip: I'll take you to the gameserver database
echo    installation and upgrade options.
echo.
echo (r)econfigure: You'll be able to redefine MySQL path,
echo    user and database information and start over with
echo    those fresh values.
echo.
echo (q)uit
echo.
set /p loginprompt= Choose (%msg%) :
if /i %loginprompt%==f goto logininstall
if /i %loginprompt%==s goto gs_backup
if /i %loginprompt%==r goto configure
if /i %loginprompt%==q goto end
goto asklogin

:logininstall
set stage=3
call :colors 17
set cmdline=
title L2JLisvus DB Installer - Login Server database setup - Full install
echo Deleting loginserver tables for new content.
set cmdline="%mysqlPath%" -h %lshost% -u %lsuser% --password=%lspass% -D %lsdb% ^< login_install.sql 2^> NUL
%cmdline%
if not %ERRORLEVEL% == 0 goto omfg
set full=1
goto gs_backup

:gs_backup
call :colors 17
set cmdline=
if %full% == 1 goto fullinstall
set stage=4
title L2JLisvus DB Installer - Game server database setup
cls
echo.
echo Making a backup of the original gameserver database.
set cmdline="%mysqldumpPath%" --add-drop-table -h %gshost% -u %gsuser% --password=%gspass% %gsdb% ^> "%backup%\gameserver_backup.sql" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto gsdbok
if %safe_mode% == 1 goto omfg
:gs_err1
call :colors 47
title L2JLisvus DB Installer - Game Server database setup - Backup failed!
cls
echo.
echo Backup attempt failed! A possible reason for this to happen,
echo is that your DB doesn't exist yet. I could try to create 
echo %gsdb% for you, but maybe you prefer me to continue with 
echo last part of the script.
echo.
:askgsdb
set gsdbprompt=y
echo ATTEMPT TO CREATE GAMESERVER DATABASE?
echo.
echo (y)es
echo.
echo (n)o
echo.
echo (r)econfigure
echo.
echo (q)uit
echo.
set /p gsdbprompt= Choose (default yes):
if /i %gsdbprompt%==y goto gsdbcreate
if /i %gsdbprompt%==n goto horrible_end
if /i %gsdbprompt%==r goto configure
if /i %gsdbprompt%==q goto end
goto askgsdb

:gsdbcreate
call :colors 17
set stage=5
set cmdline=
title L2JLisvus DB Installer - Game Server database setup - DB Creation
cls
echo Trying to create Game Server database...
set cmdline="%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -e "CREATE DATABASE %gsdb%" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto fullinstall
if %safe_mode% == 1 goto omfg
:gs_err2
call :colors 47
title L2JLisvus DB Installer - Game Server database setup - DB Creation failed!
cls
echo.
echo An error occured while trying to create a database for 
echo your game server.
echo.
echo Possible reasons:
echo 1-You provided innacurate info, check username, pass, etc.
echo 2-User %gsuser% don't have enough privileges for 
echo database creation.
echo 3-Database exists already...?
echo.
echo I'd suggest you to look for correct values and try this
echo script again later. But you can try to reconfigure it now.
echo.
:askgsdbcreate
set omfgprompt=q
echo (r)estart script with fresh configuration values
echo.
echo (q)uit now
echo.
set /p omfgprompt=  Choose (default quit):
if /i %omfgprompt%==r goto configure
if /i %omfgprompt%==q goto horrible_end
goto askgsdbcreate

:gsdbok
call :colors 17
title L2JLisvus DB Installer - Game Server database setup - WARNING!!!
cls
echo.
:asktype
set installtype=u
echo GAMESERVER DATABASE install:
echo.
echo (f)ull: WARNING! I'll destroy ALL of your existing character
echo    data (i really mean it: items, pets.. ALL)
echo.
echo (u)pgrade: I'll do my best to preserve all of your character
echo    data.
echo.
echo (s)kip: We'll get into the last set of questions (cummulative
echo    updates, custom stuff...)
echo.
echo (q)uit
echo.
set /p installtype= Choose (default upgrade):
if /i %installtype%==f goto fullinstall
if /i %installtype%==u goto upgradeinstall
if /i %installtype%==s goto custom
if /i %installtype%==q goto end
goto asktype

:fullinstall
call :colors 17
set stage=6
set cmdline=
title L2JLisvus DB Installer - Game Server database setup - Full install
echo Deleting all gameserver tables for new content...
set cmdline="%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^< full_install.sql 2^> NUL
%cmdline%
if not %ERRORLEVEL% == 0 goto omfg
set full=1
echo.
echo Game Server tables were deleted.
goto upgradeinstall

:upgradeinstall
set stage=6
set cmdline=
if %full% == 1 (
title L2JLisvus DB Installer - Game Server database setup - Installing...
echo Installing new gameserver content.
) else (
title L2JLisvus DB Installer - Game Server database setup - Upgrading...
echo Upgrading gameserver content.
)
if %logging% == 0 set output=NUL
set dest=ls
for %%i in (
accounts.sql
gameservers.sql
) do call :dump %%i
set dest=gs
for %%i in (
aio_buffer.sql
armor.sql
armorsets.sql
auction.sql
auction_bid.sql
auction_watch.sql
auto_announcements.sql
auto_chat.sql
auto_chat_text.sql
castle.sql
castle_doorupgrade.sql
castle_functions.sql
castle_manor_procure.sql
castle_manor_production.sql
castle_siege_guards.sql
character_buff_schemes.sql
character_friends.sql
character_hennas.sql
character_macroses.sql
character_offline_trade.sql
character_offline_trade_items.sql
character_quests.sql
character_recipebook.sql
character_recommends.sql
character_shortcuts.sql
character_skills.sql
character_skills_save.sql
character_subclasses.sql
characters.sql
clan_data.sql
clan_wars.sql
clanhall.sql
clanhall_functions.sql
dimensional_rift.sql
droplist.sql
enchant_skill_trees.sql
etcitem.sql
fish.sql
fishing_skill_trees.sql
forums.sql
four_sepulchers_spawnlist.sql
games.sql
global_tasks.sql
grandboss_data.sql
grandboss_list.sql
helper_buff_list.sql
henna.sql
henna_trees.sql
heroes.sql
items.sql
itemsonground.sql
locations.sql
mapregion.sql
merchant_areas_list.sql
merchant_buylists.sql
merchant_lease.sql
merchant_shopids.sql
merchants.sql
minions.sql
npc.sql
npc_buffer.sql
npcskills.sql
olympiad_data.sql
olympiad_nobles.sql
olympiad_nobles_eom.sql
pets.sql
pets_stats.sql
posts.sql
quest_global_data.sql
raidboss_spawnlist.sql
random_spawn.sql
random_spawn_loc.sql
seven_signs.sql
seven_signs_festival.sql
seven_signs_status.sql
siege_clans.sql
skill_learn.sql
skill_spellbooks.sql
skill_trees.sql
spawnlist.sql
teleport.sql
topic.sql
walker_routes.sql
weapon.sql
weddings.sql
) do call :dump %%i
echo done...
echo.
goto custom

:dump
set cmdline=
if /i %full% == 1 (set action=Installing) else (set action=Upgrading)
echo %action% %1>>"%output%"
echo %action% %~nx1
if "%dest%"=="ls" set cmdline="%mysqlPath%" -h %lshost% -u %lsuser% --password=%lspass% -D %lsdb% ^< ..\sql\%1 2^>^>"%output%"
if "%dest%"=="gs" set cmdline="%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^< ..\sql\%1 2^>^>"%output%"
%cmdline%
if %logging%==0 if NOT %ERRORLEVEL%==0 call :omfg2 %1
goto :eof

:custom
echo.
set cstprompt=n
set /p cstprompt=Install custom gameserver DB tables: (Y) yes or (N) no?
if /i %cstprompt%==y goto cstinstall
if /i %cstprompt%==n goto end

:cstinstall
echo Installing custom content.
cd ..\sql\custom\
echo @echo off> temp.bat
if exist errors.txt del errors.txt
for %%i in (*.sql) do echo "%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^< %%i 2^>^> custom_errors.txt >> temp.bat
call temp.bat> nul
del temp.bat
move custom_errors.txt %workdir%
goto end

:omfg2
cls
call :colors 47
title L2JLisvus DB Installer - potential database issue at stage %stage%
echo.
echo Something caused an error while executing instruction:
echo %mysqlPath% -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb%
echo.
echo with file %~nx1
echo.
echo What should we do now?
echo.
:askomfg2
set ntpebcak=c
echo (l)og it: I will create a log for this file, then continue
echo    with the rest of the list in non-logging mode.
echo.
echo (c)ontinue: Let's pretend that nothing happened and continue with
echo    the rest of the list.
echo.
echo (r)econfigure: Perhaps these errors were caused by a typo.
echo    you can restart from scratch and redefine paths, databases
echo    and user info again.
echo.
echo (q)uit now
echo.
set /p ntpebcak= Choose (default continue):
if  /i %ntpebcak%==c (call :colors 17 & goto :eof)
if  /i %ntpebcak%==l (call :logginon %1 & goto :eof)
if  /i %ntpebcak%==r (call :configure & exit)
if  /i %ntpebcak%==q (call :horrible_end & exit)
goto askomfg2

:logginon
cls
call :colors 17
title L2JLisvus DB Installer - Game Server database setup - Logging options turned on
set logging=1
if %full% == 1 (
  set output=%logdir%\install-%~nx1.log
) else (
  set output=%logdir%\upgrade-%~nx1.log
)
echo.
echo Depending on your request, i'll create a log file for your reading pleasure.
echo.
echo I'll call it %output%
echo.
echo If you already have such a file and would like to keep a copy.
echo go now and read it or back it up, because it's not going to be rotated
echo or anything, instead i'll just overwrite it.
echo.
echo When you're done or if you don't mind, press any key to start.
pause>NUL
set cmdline="%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^<..\sql\%1 2^>^>"%output%"
date /t >"%output%"
time /t >>"%output%"
%cmdline%
echo Log file created, resuming normal operations...
call :colors 17
set logging=0
set output=NUL
goto :eof

:binaryfind
if EXIST "%mysqlBinPath%" (echo Found) else (echo Not Found)
goto :eof

:horrible_end
call :colors 47
title L2JLisvus DB Installer - Oops!
cls
echo This wasn't a clean run, but don't worry.
echo You can get help and support at L2JLisvus Forum.
echo.
echo I'll try to gather some version-related information that you
echo may find useful when asking for support:
echo.
set cf="..\gameserver\config\l2j-commit.properties"
echo Datapack commit reported by properties file:
if NOT EXIST %cf% (
echo   Your %cf% file is missing!
echo   Use eclipse/ant to build one from your repository copy.
echo   With it, we'll be able to help you better.
) else (
type %cf% | find "version" 2> NUL
if not %ERRORLEVEL% == 0 (
echo   An error occured while trying to read
echo   your %cf% file!
echo   Make sure you keep it up to date
echo   and in the correct place.
echo %ERRORLEVEL%
))
echo.
rem del %config_file%
pause
goto end

:end
call :colors 17
title L2JLisvus DB Installer - Script execution finished
cls
echo.
echo Thank you for using L2JLisvus, a fork of L2JServer Project.
echo.
pause
color