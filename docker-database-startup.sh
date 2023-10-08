#!/bin/bash

CONTAINER_NAME="felicita-postgres"
CONTAINER_LOOKUP_OUTPUT=$(docker ps -a | grep "$CONTAINER_NAME")
CONTAINER_EXISTS=${#CONTAINER_LOOKUP_OUTPUT}

if [ "$CONTAINER_EXISTS" -eq 0 ]
then
  docker run -d \
    -p 5432:5432 \
    --name "$CONTAINER_NAME" \
    -e POSTGRES_USER="$FELICITA_POSTGRES_USERNAME" \
    -e POSTGRES_PASSWORD="$FELICITA_POSTGRES_PASSWORD" \
    postgres
   liquibase \
    --changelog-file ./src/main/resources/db/changelog/db.changelog-master.xml \
    --url jdbc:postgresql://localhost:5432/felicita \
    --username "$FELICITA_POSTGRES_USERNAME" \
    --password "$FELICITA_POSTGRES_PASSWORD" \
    update
else
  docker start "$CONTAINER_NAME"
fi