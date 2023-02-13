# syntax=docker/dockerfile:1
# https://docs.docker.com/language/java/build-images/

# Build and run locally.
# docker build --tag voteapp
# docker run -dp 8080:8080 voteap
# docker ps
# docker stop [name_from_ps]
# docker ps -a # see terminated processes
# docker restart [name_from_ps]
# docker remove [name_from_ps]

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY gradle/ ./gradle
COPY gradlew ./
COPY build.gradle ./
COPY settings.gradle ./
COPY src ./src

RUN ./gradlew build

CMD ["./gradlew", "bootRun"]
