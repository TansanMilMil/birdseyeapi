#!/bin/bash -eu

# setup
cd `dirname $0`

LOG_OUT=./deploy-logs/stdout.log
LOG_ERR=./deploy-logs/stderr.log
exec 1> >(
  while read -r l; do echo "[$(date +"%Y-%m-%d %H:%M:%S")] $l"; done \
    | tee -a $LOG_OUT
)
exec 2> >(
  while read -r l; do echo "[$(date +"%Y-%m-%d %H:%M:%S")] $l"; done \
    | tee -a $LOG_ERR
)

# check diff and pull
git fetch

if git diff main origin/main --exit-code > /dev/null
then
    echo "No changes to deploy"
else 
    echo "Changes detected, deploying"
    git pull
    docker compose down
    docker compose up -d
fi
