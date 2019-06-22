#author jason.wu@ikang.com
#!/bin/sh

cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf
SPRING_PROFILE="dev"

if [ -n "$2" ]; then
	SPRING_PROFILE="$2"
fi

PIDS=""
if [ -z "$PIDS" ]; then
	PIDS=`ps -ef | grep java | grep "$DEPLOY_DIR" |awk '{print $2}'`
fi

echo -e "INFO: Stopping the $DEPLOY_DIR ...\c"
if [ -z "$PIDS" ]; then
    echo -e "\nERROR: The $DEPLOY_DIR does not started!"
    exit 1
fi

for PID in $PIDS ; do
    kill -9 $PID > /dev/null 2>&1
done

COUNT=0
NUM_LOCAL=0
TOTAL=20
while [ $COUNT -lt 1 ]; do    
    echo -e ".\c"
    sleep 1
    COUNT=1
    for PID in $PIDS ; do
        PID_EXIST=`ps -f -p $PID | grep java`
        if [ -n "$PID_EXIST" ]; then
            COUNT=0
            break
        fi
    done
    let NUM_LOCAL=NUM_LOCAL+1
	if [ $NUM_LOCAL -gt $TOTAL ]; then
		echo -e "\nERROR: The $DEPLOY_DIR stop failure, Please view the log."
		COUNT=-1
		break
	fi
done

if [ $COUNT -eq 1 ]; then
	echo "OK!";	
fi

echo "INFO: PID: $PIDS"
