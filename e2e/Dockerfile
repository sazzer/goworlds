FROM node:12.5.0-alpine

ENV CI=1

# Install Dockerize
ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz

WORKDIR /goworlds/e2e

# Download dependencies. This means that layer caching helps us out
COPY package.json yarn.lock /goworlds/e2e/
RUN yarn install

ENV SERVICE_URI=
ENV WEBAPP_URL=
ENV SELENIUM_REMOTE_URL=

COPY . /goworlds/e2e/

CMD dockerize -timeout 30s -wait $SERVICE_URI/actuator/health yarn cucumber
