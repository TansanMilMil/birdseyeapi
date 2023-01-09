#!/bin/bash
set -eux
cd `dirname $0`

echo -e '\e[32mstart java build...\e[m'
docker compose down
docker compose up -d java
docker cp . birdseyeapi_java:/project
docker compose exec java ls
docker compose exec java chmod +x ./gradlew
docker compose exec java ./gradlew build
sudo docker cp birdseyeapi_java:/project/build .
