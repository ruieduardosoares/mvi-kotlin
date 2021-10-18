#!/bin/bash
version=$1
git tag -a "$version" -m "$version"
# shellcheck disable=SC2181
if [ "$?" -ne "0" ]; then
  echo "Sorry, we had a problem here, could not create tag $version!"
else
  echo "Tag $version created successfully for commit $(git rev-parse --short HEAD~2)"
fi
git push origin "$version"
echo "Tag $version pushed successfully"
