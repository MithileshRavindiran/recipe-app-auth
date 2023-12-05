FROM openjdk:17-jdk-slim as builder

ARG JAR_FILE=build/libs/recipe-app-auth-1.0.0.jar
EXPOSE 8082
ADD ${JAR_FILE} recipe-app-auth.jar

ENTRYPOINT ["java","-jar","/recipe-app-auth.jar","--spring.profiles.active=docker"]