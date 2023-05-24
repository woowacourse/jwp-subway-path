FROM adoptopenjdk/openjdk11

COPY ./build/libs/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "./app.jar" ]
