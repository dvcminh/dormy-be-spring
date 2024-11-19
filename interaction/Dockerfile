FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/interaction-0.0.1-SNAPSHOT.jar .

EXPOSE 8070:8070

ENTRYPOINT ["java", "-jar", "interaction-0.0.1-SNAPSHOT.jar"]