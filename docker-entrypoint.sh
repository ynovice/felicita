#!/bin/bash

liquibase --changelog-file=src/main/resources/db/changelog/db.changelog-master.xml \
          --url=jdbc:postgresql://felicita-postgres:5432/felicita \
          --username=felicita \
          --password=felicita \
          --duplicate-file-mode=WARN \
          update

java -jar target/felicita-backend.jar