#!/bin/bash -eux

cd `dirname $0`

JAR_FILE=`find ./dist -name '*.jar' | head -n 1`
java -jar $JAR_FILE
