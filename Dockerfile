#
# Build stage
#
FROM maven:3.8.2-jdk-11-slim AS build
COPY src ./ImmobenWebParent/ImmobenFrontEnd/src
COPY pom.xml ./ImmobenWebParent/ImmobenFrontEnd
RUN mvn -f ./ImmobenWebParent/ImmobenFrontEnd/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build ./ImmobenWebParent/ImmobenFrontEnd/target/immobenfrontend-0.0.1-SNAPSHOT.jar immoben-frontend.jar
EXPOSE 81
ENTRYPOINT ["java","-jar","./immoben-frontend.jar"]