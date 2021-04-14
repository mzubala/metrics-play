#!/bin/bash

./gradlew bootJar
docker-compose up --build --remove-orphans