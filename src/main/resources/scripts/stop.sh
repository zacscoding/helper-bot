#!/usr/bin/env bash

SCRIPT_PATH=$( cd "$(dirname "$0")" ; pwd -P )
WORKING_DIR=$( cd "${SCRIPT_PATH}/../" ; pwd -P )

VERSION="1.0"
JAR="helper-bot-${VERSION}.jar"
SERVER_PID_FILE="${WORKING_DIR}/server.pid"

if [ -f "${SERVER_PID_FILE}" ]; then
	if [ -s "${SERVER_PID_FILE}" ]; then
		SERVER_PID=$(cat ${SERVER_PID_FILE})
		PID=$(ps -p ${SERVER_PID} | tail -1 | grep -v grep | grep -v vi | grep -v PID | awk '{print $1}')
	fi
else
	PID=$(ps -ef | grep ${JAR} | grep -v grep | grep -v vi | grep -v PID | awk '{print $2}')
fi

if [ ! -z ${PID} ]; then
	$(kill -9 ${PID})
	if [ -f ${SERVER_PID_FILE} ]; then
		$(rm ${SERVER_PID_FILE})
	fi
	echo "Success to stop ${JAR}"
else
	echo "${JAR} is not running"
fi
