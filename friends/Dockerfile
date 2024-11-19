FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/friends-0.0.1-SNAPSHOT.jar .

EXPOSE 8078:8078

ENTRYPOINT ["java", "-jar", "friends-0.0.1-SNAPSHOT.jar"]