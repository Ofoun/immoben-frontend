FROM openjdk:11

COPY ./src src/
COPY ./pom.xml pom.xml

COPY target/*.jar immoben-frontend.jar
EXPOSE 81
ENTRYPOINT ["java","-jar","/immoben-frontend.jar"]
