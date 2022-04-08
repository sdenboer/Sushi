#!/bin/bash

LOCAL_DIR=/home/$(whoami)/sushi-files
DOCKER_DIR=/home/files
mkdir -p "$LOCAL_DIR"
docker run --pull always -p 9443:9443 -p 9444:9444 -v "$LOCAL_DIR":"$DOCKER_DIR" sdenboer/sushi-server "$@"
