FROM rust:1.34.1-stretch AS build

# create a new empty shell project
RUN USER=root cargo new --bin /goworlds
WORKDIR /goworlds

# copy over your manifests
COPY ./Cargo.toml ./

# this build step will cache your dependencies
RUN cargo build --release
RUN rm src/*.rs

# copy your source tree
COPY ./src ./src

# build for release
RUN rm -rf ./target/release/deps/goworlds*
RUN cargo build --release

# our final base
FROM rust:1.34.1-stretch

ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz

ENV PORT=3000

# Set up where we deploy the app
WORKDIR /goworlds

# copy the build artifact from the build stage
COPY --from=build /goworlds/target/release/goworlds-service .
COPY log4rs.yml .
COPY docker/start.sh .
COPY migrations ./migrations

# set the startup command to run your binary
CMD ["./start.sh"]
