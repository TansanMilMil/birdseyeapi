#/bin/bash

cd `dirname $0`
echo -e '\e[36mEC2: restart docker-compose...\e[m'
docker-compose down && \
docker-compose up -d