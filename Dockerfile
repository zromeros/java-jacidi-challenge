FROM openjdk:20
VOLUME /temp
EXPOSE 8080
ADD ./target/java-zadkiel-jacidi-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]