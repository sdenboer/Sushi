@ECHO OFF

SET DOCKER_DIR="/home/files"
SET CURRENTDIR="%cd%"

docker run --network host -v "%cd%":%DOCKER_DIR% -w %DOCKER_DIR% sdenboer/sushi-client %*