#!/bin/bash

docker run --pull always --network host -v $PWD:/home/files sdenboer/sushi-client "$@"
