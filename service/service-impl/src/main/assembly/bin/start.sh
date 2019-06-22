#!/bin/bash

cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf
TEMP_DIR=$DEPLOY_DIR/temp

LOGS_DIR="$DEPLOY_DIR/logs"
SPRING_PROFILE="dev"

if [ -n "$1" ]; then
	SPRING_PROFILE="$1"
fi
if [ -z "$SPRING_PROFILE" ]; then
	SPRING_PROFILE="dev"
fi


PIDS=`ps -ef | grep java | grep "$DEPLOY_DIR" |awk '{print $2}'`
if [ -n "$PIDS" ]; then
	echo "ERROR: The $DEPLOY_DIR already started!"
	echo "INFO:  PID: $PIDS"
	exit 1
fi

if [ -z "$LOGS_DIR" ]; then
	LOGS_DIR="$DEPLOY_DIR/logs"
fi

if [ ! -d $LOGS_DIR ]; then
	mkdir $LOGS_DIR
	chmod 775 $LOGS_DIR
fi

if [ ! -d $TEMP_DIR ]; then
	mkdir $TEMP_DIR
	chmod 775 $TEMP_DIR
fi

LOG_FILE=$CONF_DIR/logback.xml
STDOUT_FILE=$DEPLOY_DIR/dubbo.out

LIB_DIR=$DEPLOY_DIR/lib

#DUBBO_OPTS="-classpath $CONF_DIR:$LIB_JARS -Dlogback.configurationFile=$LOG_FILE"

SPRING_OPTS="$SPRING_PROFILE"
#if [ -n "$SPRING_PROFILE" ]; then
#	SPRING_OPTS="-Dspring.profiles.active=$SPRING_PROFILE"
#fi

JAVA_OPTS="-Djava.awt.headless=true -Djava.net.preferIPv4Stack=true"

if [ "$SPRING_OPTS"x == "prod"x ]; then
	JAVA_MEM_OPTS="-server -Xms1024m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70"
else
	JAVA_MEM_OPTS="-server -Xms256m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70"
fi

# file is exists
if [ ! -f "$LOG_FILE" ]; then
    LOG_FILE=$CONF_DIR/logback.xml
fi

DUBBO_OPTS="-Djava.ext.dirs=$LIB_DIR:$JAVA_HOME/jre/lib/ext/ -Dlogback.configurationFile=$LOG_FILE"

RUN_JARS=`ls $DEPLOY_DIR|grep .jar$ |grep impl |grep -v javadoc |grep -v sources`

#echo "$JAVA_MEM_OPTS"
echo -e "INFO: Starting the $DEPLOY_DIR ...\c"
nohup java $JAVA_OPTS $JAVA_MEM_OPTS $DUBBO_OPTS -jar $RUN_JARS $SPRING_OPTS > $STDOUT_FILE 2>&1 &

COUNT=0
NUM_LOCAL=0
TOTAL=120
while [ $COUNT -lt 1 ]; do
    echo -e ".\c"
    sleep 1
    COUNT=`ps -ef | grep java | grep "$DEPLOY_DIR" | awk '{print $2}' | wc -l`
    if [ $COUNT -gt 0 ]; then
    	echo "OK!"
		PIDS=`ps -ef | grep java | grep "$DEPLOY_DIR" | awk '{print $2}'`
		echo "INFO: PID: $PIDS"
        break
    fi
    let NUM_LOCAL=NUM_LOCAL+1
    if [ $NUM_LOCAL -gt $TOTAL ]; then
    	echo "ERROR: The $DEPLOY_DIR startup failure, Please view the boot log."
    	break
    fi
done

echo "INFO: The SPRING_PROFILE	: $SPRING_PROFILE"
echo "INFO: STDOUT: $STDOUT_FILE"