#!/bin/bash

docker run --network host -v /tmp:/tmp sdenboer/sushi-client "$@"
