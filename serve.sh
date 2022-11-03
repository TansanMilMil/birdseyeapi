#!/bin/bash

cd `dirname $0`
docker-compose down && \
docker-compose up -d && \

docker-compose exec -T mysql mysql -u root -proot1603 -e 'CREATE DATABASE IF NOT EXISTS birds_eye'

./gradlew build && \
docker-compose exec -T java java -jar ./build/libs/birdseyeapi-0.0.1-SNAPSHOT.jar