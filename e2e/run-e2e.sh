#!/bin/sh

mkdir -p output
docker-compose -p goworlds-e2e up --abort-on-container-exit --exit-code-from goworlds-e2e
