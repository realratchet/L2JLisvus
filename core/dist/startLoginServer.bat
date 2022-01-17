@echo off
title Lisvus LoginServer Console
:start
echo Starting L2JLisvus Login Server.
echo.
java -Xms128m -Xmx256m -cp ./../libs/*;L2JLisvus.jar net.sf.l2j.loginserver.L2LoginServer
if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end
:restart
echo.
echo Admin Restart ...
echo.
goto start
:error
echo.
echo Server terminated abnormally
echo.
:end
echo.
echo server terminated
echo.
pause
