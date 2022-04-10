#!/bin/bash

DOCKER_DIR=/home/files

docker run --network host -v "$PWD":"$PWD" -w "$PWD" sdenboer/sushi-client "$@"