#!/bin/bash

DOCKER_DIR=/home/files

docker run --network host -v "$PWD":"$DOCKER_DIR" sdenboer/sushi-client "$@"
