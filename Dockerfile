FROM openjdk:21-jdk-slim

WORKDIR /usr/src/app

COPY ./target/backend-api-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]

EXPOSE 8080