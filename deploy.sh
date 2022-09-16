#!/bin/bash
set -eux
cd `dirname $0`

echo -e '\e[32mcopy built files to ec2...\e[m'
scp -o StrictHostKeyChecking=no -pr ./build $EC2_USER_WP_KIMAGURE@$EC2_HOST_WP_KIMAGURE:/home/ec2-user/birds-eye/birdseyeapi/
rm -rf ./nginx/log
scp -o StrictHostKeyChecking=no -pr ./nginx $EC2_USER_WP_KIMAGURE@$EC2_HOST_WP_KIMAGURE:/home/ec2-user/birds-eye/birdseyeapi/
scp -o StrictHostKeyChecking=no -p ./docker-compose-prod.yml $EC2_USER_WP_KIMAGURE@$EC2_HOST_WP_KIMAGURE:/home/ec2-user/birds-eye/birdseyeapi/docker-compose.yml
scp -o StrictHostKeyChecking=no -p ./entrypoint.sh $EC2_USER_WP_KIMAGURE@$EC2_HOST_WP_KIMAGURE:/home/ec2-user/birds-eye/birdseyeapi
ssh -o StrictHostKeyChecking=no $EC2_USER_WP_KIMAGURE@$EC2_HOST_WP_KIMAGURE chmod 777 /home/ec2-user/birds-eye/birdseyeapi/entrypoint.sh
scp -o StrictHostKeyChecking=no -p ./Dockerfile $EC2_USER_WP_KIMAGURE@$EC2_HOST_WP_KIMAGURE:/home/ec2-user/birds-eye/birdseyeapi
scp -o StrictHostKeyChecking=no -p ./ec2-serve.sh $EC2_USER_WP_KIMAGURE@$EC2_HOST_WP_KIMAGURE:/home/ec2-user/birds-eye/birdseyeapi/ec2-serve.sh
scp -o StrictHostKeyChecking=no -p ./aws-credentials.env $EC2_USER_WP_KIMAGURE@$EC2_HOST_WP_KIMAGURE:/home/ec2-user/birds-eye/birdseyeapi/aws-credentials.env

echo -e '\e[32mserve on EC2...\e[m'
ssh -o StrictHostKeyChecking=no $EC2_USER_WP_KIMAGURE@$EC2_HOST_WP_KIMAGURE chmod 777 /home/ec2-user/birds-eye/birdseyeapi/ec2-serve.sh
ssh -o StrictHostKeyChecking=no $EC2_USER_WP_KIMAGURE@$EC2_HOST_WP_KIMAGURE /home/ec2-user/birds-eye/birdseyeapi/ec2-serve.sh

echo -e '\e[32mremove cloudfront caches...\e[m'
aws cloudfront create-invalidation --distribution-id E1XC5ZNQDKFHT0 --paths "/*"

echo -e '\e[32mcompleted!\e[m'