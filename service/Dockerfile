# syntax=docker/dockerfile:1
# https://docs.docker.com/language/java/build-images/

# Stage 1: build the service with gradle.
FROM eclipse-temurin:17-jdk-jammy as build-stage
WORKDIR /workspace
COPY gradle/ ./gradle
COPY gradlew build.gradle settings.gradle ./
COPY src ./src
RUN ./gradlew clean build --no-daemon

# Stage 1: Make the compiled jar ready for production.
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build-stage /workspace/build/libs/voteservice.jar ./
CMD java -jar voteservice.jar