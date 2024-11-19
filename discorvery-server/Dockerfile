FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/discorvery-0.0.1-SNAPSHOT.jar .

EXPOSE 8761:8761

ENTRYPOINT ["java", "-jar", "discorvery-0.0.1-SNAPSHOT.jar"]
