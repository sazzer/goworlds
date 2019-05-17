FROM adoptopenjdk/openjdk12:jdk-12.0.1_12-alpine-slim

# Set up Dockerize
ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz

# Required dependencies
RUN apk add gettext

# Input arguments from the build
ARG JAR_FILE
RUN echo JAR_FILE=${JAR_FILE}

# Required configuration environment variables
ENV PORT=8080
ENV GOWORLDS_LOGGING_LEVEL=INFO

# Populate the container
WORKDIR /opt/goworlds
ADD ${JAR_FILE} ./service.jar
ADD src/main/docker/application.properties.template src/main/docker/start.sh ./

CMD /opt/goworlds/start.sh
