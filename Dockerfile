#
# Build stage
#
FROM maven:3.8.2-jdk-11-slim 
COPY src ./ImmobenWebParent/ImmobenFrontEnd/src
COPY pom.xml ./ImmobenWebParent/ImmobenFrontEnd
#RUN mvn -f ./ImmobenFrontEnd/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY target/*.jar app.jar
EXPOSE 81
ENTRYPOINT ["java","-jar","/app.jar"]