#!/bin/bash

cd `dirname $0`
docker-compose down && \
docker-compose up -d && \
./gradlew build && \
docker-compose exec -T java java -jar ./build/libs/birdseyeapi-0.0.1-SNAPSHOT.jar