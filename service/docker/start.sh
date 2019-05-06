#!/bin/sh

CMD="dockerize -timeout 30s"

echo Starting...
echo cmd: $CMD

$CMD /app/goworlds

