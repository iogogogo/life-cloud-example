#!/bin/bash

programJar=$1

#上一个进程pid
programId=`ps aux |grep java |grep ${programJar} |awk -F' ' '{print $2}'`

# ps -ef | grep $JARFILE | grep -v grep | awk '{print $2}' | xargs kill -9

if [ "${programId}" != "" ];then
    kill -9 ${programId}
    echo "kill ${programId}"
fi
sleep 1

nohup java -Xbootclasspath/p:$(pwd) -jar -Xmx512m -Xss512k $(pwd)/${programJar} >  /dev/null 2>&1 &

sleep 3

jps