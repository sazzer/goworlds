#!/bin/sh

if [ -f application.properties.template ]; then
    envsubst < application.properties.template > application.properties
fi

CMD="dockerize -timeout 30s"

if [ ! -z "$DOCKERIZE_WAIT" ]; then
  CMD="$CMD $DOCKERIZE_WAIT"
fi

CMD="$CMD java -jar service.jar"

echo Starting...
echo cmd: $CMD

$CMD
