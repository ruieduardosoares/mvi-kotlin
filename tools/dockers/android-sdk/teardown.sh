#!/bin/sh
echo "Removing android-docker images"
docker kill android-jenkins
docker rmi -f android-environment thyrlian/android-sdk
