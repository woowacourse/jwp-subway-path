FROM gradle:7.4-jdk11 as builder
LABEL authors="hyunseo"

COPY build.gradle .
COPY src ./src

RUN gradle build

ARG JAR_FILE="./build/libs/*path.jar"

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
