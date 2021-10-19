#!/bin/bash
username=$1
password=$2
tagName=$3
description=$4
curl \
  -u "$username:$password" \
  -X POST \
  -H "Accept: application/vnd.github.v3+json" \
  https://api.github.com/repos/ruieduardosoares/Mvi-kotlin/releases \
  -d "{\"tag_name\":\"$tagName\" \"name\":\"Release $tagName\", \"body\":\"$description\"}"
