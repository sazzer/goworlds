FROM nginx:1.15.8

COPY build /goworlds/html/
COPY docker/run.sh /goworlds/
COPY docker/nginx.config /etc/nginx/conf.d/default.conf

CMD /goworlds/run.sh
