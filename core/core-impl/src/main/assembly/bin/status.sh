#author jason.wu@ikang.com
#!/bin/sh

cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf
TEMP_DIR=$DEPLOY_DIR/temp

SPRING_PROFILE="dev"

if [ -n "$2" ]; then
	SPRING_PROFILE="$2"
fi

PIDS=""
#if [ -n "$SERVER_PORT" ]; then
#	PIDS=`netstat -tlnp | grep $SERVER_PORT|awk '{printf $7}'|cut -d/ -f1`
#fi

if [ -z "$PIDS" ]; then
	PIDS=`ps -ef | grep java | grep "$DEPLOY_DIR" |awk '{print $2}'`
fi

if [ -n "$PIDS" ]; then
	echo "INFO: The $DEPLOY_DIR already started!"
	echo "INFO: PID: $PIDS"
	exit 1
fi

echo "ERROR: The $DEPLOY_DIR does not started!"
exit 1