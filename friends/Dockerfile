FROM openjdk:17-oracle
VOLUME /tmp
COPY target/friend-0.0.1-SNAPSHOT.jar friend.jar
ENTRYPOINT ["java", "-jar", "friend.jar"]