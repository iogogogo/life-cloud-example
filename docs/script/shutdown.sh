#!/bin/sh

programJar=$1

jps |grep ${programJar} |awk '{print $1}' | xargs kill -9

sleep 1

jps