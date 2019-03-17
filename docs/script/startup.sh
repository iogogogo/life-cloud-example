#!/bin/sh

programJar=$1

param=$2

nohup java -jar -Xmx512m -Xss512m $(pwd)/${programJar} ${param} > /dev/null 2>&1 &

sleep 1

jps