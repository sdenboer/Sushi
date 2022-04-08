#!/bin/bash

DOCKER_DIR=/home/files

docker run --pull always --network host -v "$PWD":"$DOCKER_DIR" sdenboer/sushi-client "$@"
