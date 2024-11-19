FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/feed-0.0.1-SNAPSHOT.jar .

EXPOSE 8085:8085

ENTRYPOINT ["java", "-jar", "feed-0.0.1-SNAPSHOT.jar"]