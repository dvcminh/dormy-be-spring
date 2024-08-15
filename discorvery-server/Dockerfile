FROM openjdk:17-oracle
VOLUME /tmp
COPY target/discovery-server-0.0.1-SNAPSHOT.jar discovery.jar
ENTRYPOINT ["java", "-jar", "discovery.jar"]
