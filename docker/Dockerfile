FROM openjdk:11-jdk-slim as builder

ARG JAR_FILE=build/libs/recipe-app-1.0.0.jar
EXPOSE 8081
ADD ${JAR_FILE} recipe-app.jar

ENTRYPOINT ["java","-jar","/recipe-app.jar"]