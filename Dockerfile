FROM openjdk:11

COPY ./src src/
COPY ./pom.xml pom.xml

COPY target/ImmobenFrontEnd-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 81
CMD ["java","-jar","/app.jar"]
