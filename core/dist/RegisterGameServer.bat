@echo off
title Lisvus GameServer Registration Console
@java -Djava.util.logging.config.file=console.cfg -cp ./../libs/*;L2JLisvus.jar net.sf.l2j.gsregistering.GameServerRegister
@pause