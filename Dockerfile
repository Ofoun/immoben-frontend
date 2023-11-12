FROM maven:3.8.2-jdk-11 AS build
COPY ./src src/
COPY ./pom.xml pom.xml
RUN mvn clean package -DskipTests
FROM openjdk:11
COPY --from=build /target/immobenfrontend-0.0.1-SNAPSHOT.jar immoben-frontend.jar
EXPOSE 8080
ENTRYPOINT [“java”,“-jar”,“immoben-frontend.jar”]
