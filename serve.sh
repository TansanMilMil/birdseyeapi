#!/bin/bash

cd `dirname $0`
./gradlew build && \
java -jar ./build/libs/birdseyeapi-0.0.1-SNAPSHOT.jar