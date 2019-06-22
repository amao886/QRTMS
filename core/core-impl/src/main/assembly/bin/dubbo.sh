#author jason.wu@ikang.com
#!/bin/sh

source ~/.bash_profile

cd `dirname $0`
chmod a+x *.sh

SPRING_PROFILE=$2

start() {
	./start.sh $SPRING_PROFILE
}

stop() {
	./stop.sh $SPRING_PROFILE
}

restart() {
	./restart.sh $SPRING_PROFILE
}

status() {
	./status.sh $SPRING_PROFILE
}

case $1 in
	start)		start;;
	stop)		stop;;
	restart)	restart;;
	status)		status;;
	*) echo "ERROR: Please input argument $0 {start|stop|restart|status}";;
esac

exit 1