# syntax=docker/dockerfile:1
# https://docs.docker.com/language/java/build-images/

# Build and run locally.
# docker build --tag voteservice .
# docker run -dp 8080:8080 voteservice
# docker ps
# docker stop [name_from_ps]
# docker ps -a # see terminated processes
# docker restart [name_from_ps]
# docker remove [name_from_ps]

# Important: Build this on a amd64 machine if you want to run it on linux.
# I think you can specify --platform linux/amd64 to build it on mac for linux
# but it's _really_ slow.

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY gradle/ ./gradle
COPY gradlew build.gradle settings.gradle ./
COPY src ./src

RUN ./gradlew clean build --no-daemon

# CMD ["./gradlew", "bootRun"]
CMD java -jar build/libs/voteservice.jar
