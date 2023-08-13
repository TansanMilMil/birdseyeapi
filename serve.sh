#!/bin/bash -eux

cd `dirname $0`

./gradlew build && \
./gradlew run
