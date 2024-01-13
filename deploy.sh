#!/bin/bash -eu

cd `dirname $0`
echo "load .env ----------------------------"
source .env

echo "rm old files(for remove files root user created) ----------------------------"
sudo rm -rf ./.gradle
sudo rm -rf ./build

echo "build jar ----------------------------"
docker compose -f ./docker-compose.ci.yml up -d java
docker compose exec java ./gradlew bootJar
tar cvfz ./build.tgz ./build/

echo "docker compose teardown ----------------------------"
docker compose down

echo "stop birdseyeapi ----------------------------"
ssh $VENUS_SSH_HOST docker compose -f $VENUS_HOME/birdseyeapi/docker-compose.yml down

echo "rm old files(for remove files root user created)"
sudo rm -rf ./.gradle
sudo rm -rf ./build

echo "scp files to venus ----------------------------"
scp ./build.tgz $VENUS_SSH_HOST:$VENUS_HOME/birdseyeapi/build.tgz
ssh $VENUS_SSH_HOST tar xvfz $VENUS_HOME/birdseyeapi/build.tgz -C $VENUS_HOME/birdseyeapi

scp ./docker-compose.yml $VENUS_SSH_HOST:$VENUS_HOME/birdseyeapi/docker-compose.yml
scp ./execute-jar.sh $VENUS_SSH_HOST:$VENUS_HOME/birdseyeapi/execute-jar.sh
scp ./java-entrypoint.sh $VENUS_SSH_HOST:$VENUS_HOME/birdseyeapi/java-entrypoint.sh
scp -r ./nginx $VENUS_SSH_HOST:$VENUS_HOME/birdseyeapi/

echo "start birdseyeapi ----------------------------"
ssh $VENUS_SSH_HOST docker compose -f $VENUS_HOME/birdseyeapi/docker-compose.yml up -d

echo "deploy finished!! ----------------------------"
