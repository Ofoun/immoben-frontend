FROM openjdk:11

COPY ./src src/
COPY ./pom.xml pom.xml

ADD target/*.jar immoben-frontend.jar 
EXPOSE 81
CMD ["java","-jar","/*.jar"]
