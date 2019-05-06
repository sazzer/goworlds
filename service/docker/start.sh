#!/bin/sh

CMD="dockerize -timeout 30s"

if [ ! -z "$DOCKERIZE_WAIT" ]; then
  CMD="$CMD $DOCKERIZE_WAIT"
fi

echo Starting...
echo cmd: $CMD

$CMD /app/goworlds

