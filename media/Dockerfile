FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/media-0.0.1-SNAPSHOT.jar .

EXPOSE 8016:8016

ENTRYPOINT ["java", "-jar", "media-0.0.1-SNAPSHOT.jar"]