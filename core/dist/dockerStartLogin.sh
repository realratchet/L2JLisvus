#!/bin/sh
[ -f log/java0.log.0 ] && mv log/java0.log.0 "log/`date +%Y-%m-%d_%H-%M-%S`_java.log"
[ -f log/stdout.log ] && mv log/stdout.log "log/`date +%Y-%m-%d_%H-%M-%S`_stdout.log"
java -Xms${LOGIN_MEMORY_INITIAL} -Xmx${LOGIN_MEMORY_MAXIMUM} -cp ./../libs/*:L2JLisvus.jar net.sf.l2j.loginserver.L2LoginServer 2>&1 | tee log/stdout.log