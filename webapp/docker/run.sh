#!/bin/sh

envsubst < /goworlds/html/config.js.template > /goworlds/html/config.js

nginx -g 'daemon off;'
