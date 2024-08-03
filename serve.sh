#!/bin/bash -eux

cd `dirname $0`

./gradlew build --stacktrace && \
./gradlew run --stacktrace
