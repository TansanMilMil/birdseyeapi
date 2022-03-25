#/bin/bash

cd `dirname $0`
echo -e '\e[36mEC2: restart docker-compose...\e[m'
docker-compose down && \
docker-compose up -d && \
echo -e '\e[36mEC2: execute jar in docker...\e[m'
docker-compose exec -dT java java -jar ./build/libs/birdseyeapi-0.0.1-SNAPSHOT.jar