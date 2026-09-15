#!/usr/bin/env bash

echo "Running deploy-cmds.sh on remote server..."
export IMAGE_NAME=$1
docker-compose -f docker-compose.yaml up -d