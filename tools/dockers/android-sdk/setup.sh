#!/bin/sh
androidSdkFolder=$(pwd)
cd ../../..
rootPath=$(pwd)
cd "$androidSdkFolder" || return
echo "Building docker images"
docker build --rm -t android-environment --no-cache --pull .
docker run -d --rm --name android-jenkins --mount type=bind,source="$rootPath",target=/app android-environment
echo "Checking status"
while [ "$(docker inspect android-jenkins --format='{{.State.Status}}')" != "running" ]
do
  echo "Waiting for container to be running"
  sleep 0.5
done
echo "Container running"
