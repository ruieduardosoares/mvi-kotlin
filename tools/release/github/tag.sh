#!/bin/bash
username=$1
accessToken=$2
version=$3
git tag -a "$version" -m "$version"
# shellcheck disable=SC2181
if [ "$?" -ne "0" ]; then
  echo "Sorry, we had a problem here, could not create tag $version!"
else
  echo "Tag $version created successfully for commit $(git rev-parse --short HEAD~2)"
fi
git push origin https://"$accessToken"@github.com/"$username"/mvi-kotlin.git "$version"
