FROM adoptopenjdk/openjdk12:jdk-12.0.1_12-alpine-slim

# Input arguments from the build
ARG JAR_FILE
RUN echo JAR_FILE=${JAR_FILE}

# Required dependencies
RUN apk add gettext

# Required configuration environment variables
ENV PORT=8080
ENV GOWORLDS_LOGGING_LEVEL=INFO

# Populate the container
WORKDIR /opt/goworlds
ADD ${JAR_FILE} ./service.jar
ADD src/main/docker/application.properties.template src/main/docker/start.sh ./

CMD /opt/goworlds/start.sh
