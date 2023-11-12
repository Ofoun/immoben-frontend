FROM openjdk:11

COPY ./src src/
COPY ./pom.xml pom.xml

COPY target/immoben-frontend.jar app.jar
EXPOSE 81
CMD ["java","-jar","/app.jar"]
