#!/bin/sh

if [ -f application.properties.template ]; then
    envsubst < application.properties.template > application.properties
fi

java -jar service.jar
