#!/bin/bash

cd `dirname $0`
echo -e '\e[32mstart build...\e[m'
docker-compose down && \
docker-compose up -d && \
docker-compose exec java ./gradlew build && \

echo -e '\e[32mcopy files to ec2...\e[m'
scp -pr ./build wp-kimagure:/home/ec2-user/birds-eye/birdseyeapi/ && \
rm -rf ./nginx/log && \
scp -pr ./nginx wp-kimagure:/home/ec2-user/birds-eye/birdseyeapi/ && \
scp -p ./docker-compose-prod.yml wp-kimagure:/home/ec2-user/birds-eye/birdseyeapi/docker-compose.yml && \
scp -p ./entrypoint.sh wp-kimagure:/home/ec2-user/birds-eye/birdseyeapi && \
ssh wp-kimagure chmod 777 /home/ec2-user/birds-eye/birdseyeapi/entrypoint.sh && \
scp -p ./Dockerfile wp-kimagure:/home/ec2-user/birds-eye/birdseyeapi && \
scp -p ./ec2-serve.sh wp-kimagure:/home/ec2-user/birds-eye/birdseyeapi/ec2-serve.sh && \
scp -p ./aws-credentials.env wp-kimagure:/home/ec2-user/birds-eye/birdseyeapi/aws-credentials.env && \

ssh wp-kimagure chmod 777 /home/ec2-user/birds-eye/birdseyeapi/ec2-serve.sh && \
ssh wp-kimagure /home/ec2-user/birds-eye/birdseyeapi/ec2-serve.sh && \

echo -e '\e[32mremove cloudfront caches...\e[m'
aws cloudfront create-invalidation --distribution-id E1XC5ZNQDKFHT0 --paths "/*" && \

echo -e '\e[32mcompleted!\e[m'