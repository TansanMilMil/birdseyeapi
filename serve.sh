#!/bin/bash -eux

cd `dirname $0`

./gradlew build && \
# use application-dev.properties
java -jar ./build/libs/birdseyeapi-0.0.1-SNAPSHOT.jar
