#!/usr/bin/env bash

SCRIPTPATH=$( cd "$(dirname "$0")" ; pwd -P )
WORKING_DIR=$( cd "${SCRIPTPATH}/../" ; pwd -P )

VERSION="1.0"
JAR="helper-bot-"${VERSION}".jar"

# check server pid
function check_server_pid() {
	local pid_file="$WORKING_DIR/server.pid"
	if [[ -f "$pid_file" ]]; then
		if [[ -s "$pid_file" ]]; then
			local pid=$(cat ${pid_file})
			PID=$(ps -p ${pid} | tail -1 | grep -v grep | grep -v vi | grep -v PID | awk '{print $1}')
		fi
	else
		PID=$(ps -ef | grep ${JAR} | grep -v grep | grep -v vi | grep -v PID | awk '{print $2}')
	fi
}
check_server_pid

# check current user is root or not
if [[ "$(id -u)" = "0" ]]; then
	echo "It can not be executed by root." 1>&2
	exit 1
fi

# check override properties
CONFIG_LOCATION=""
function get_spring_config_location() {	
	local properties_file=$WORKING_DIR/application.yaml
	if [[ -f "$properties_file" ]]
	then
        	CONFIG_LOCATION="classpath:/application.yaml,$PROPERTIES_FILE"
	else
        	CONFIG_LOCATION="classpath:/application.yaml"
	fi	
}

if [[ -z $PID ]]; then
	get_spring_config_location
	nohup java -Xms512m -Xmx512m -jar ${WORKING_DIR}/${JAR} --spring.config_location=${CONFIG_LOCATION}  1>> $WORKING_DIR/server.log 2>&1 &
	PID=$!
	echo ${PID} > ${WORKING_DIR}/server.pid
	echo "Success to start bot"
	echo "pid : "${PID}
	tail -f ${WORKING_DIR}/server.log
else
	echo "Already server is running"
	exit 0
fi
