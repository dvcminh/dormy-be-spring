#!/bin/bash

mvn clean install

services=("api-gateway" "chat" "discorvery-server" "feed" "friends" "interaction" "media" "notification" "review" "sso")

for service in "${services[@]}"; do
  cd $service

  docker build -t ducminh210503/$service .

  docker tag ducminh210503/$service ducminh210503/$service:latest

  docker push ducminh210503/$service:latest

  cd ..
done
