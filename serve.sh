#!/bin/bash -eux

cd `dirname $0`
docker compose down && \
docker compose up -d && \

docker compose exec java ./gradlew build && \
# use application-dev.properties
docker compose exec java java -jar -Dspring.profiles.active=dev ./build/libs/birdseyeapi-0.0.1-SNAPSHOT.jar