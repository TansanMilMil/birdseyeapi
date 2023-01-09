#!/bin/bash -eux
cd `dirname $0`

echo -e '\e[32mcopy built files to ec2...\e[m'
ARCHIVE_DIR=archive
APP_NAME=birdseyeapi
BASE_DIR=$ARCHIVE_DIR/$APP_NAME

rm -rf $ARCHIVE_DIR
#mkdir -p $ARCHIVE_DIR
mkdir -p $BASE_DIR
cp -rp ./build $BASE_DIR
cp -rp ./nginx $BASE_DIR
cp -rp ./docker-compose-prod.yml $BASE_DIR/docker-compose.yml
cp -rp ./entrypoint.sh $BASE_DIR
chmod 777 $BASE_DIR/entrypoint.sh
cp -rp ./Dockerfile $BASE_DIR
cp -rp ./ec2-serve.sh $BASE_DIR
chmod 777 $BASE_DIR/ec2-serve.sh
cp -rp ./aws-credentials.env $BASE_DIR

tar -zcvf $BASE_DIR.tgz -C $ARCHIVE_DIR $APP_NAME

scp -o StrictHostKeyChecking=no -p $BASE_DIR.tgz $SSH_HUMMINGBIRD:/home/ec2-user/$APP_NAME.tgz

ssh -o StrictHostKeyChecking=no $SSH_HUMMINGBIRD tar -xzvf /home/ec2-user/$APP_NAME.tgz

echo -e '\e[32mserve on EC2...\e[m'
ssh -o StrictHostKeyChecking=no $SSH_HUMMINGBIRD /home/ec2-user/$APP_NAME/ec2-serve.sh

echo -e '\e[32mremove cloudfront caches...\e[m'
aws cloudfront create-invalidation --distribution-id E1XC5ZNQDKFHT0 --paths "/*"

echo -e '\e[32mcompleted!\e[m'