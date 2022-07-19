# Recipe Application Java Assignment

## Introduction
This assignment is to 
1) Write a SpringBoot/Java restful backend;


Assignment is to write a Java application with a restful backend, which supports storing recipes and manipulate the recipes.
Each Recipe contain recipe name, recipe type, No of Servings, Ingredients and Instructions
The application is  developed using Java and Spring-Boot

The restful backend should support the following endpoints:

| Method | Path            | Description                                                                                                                                                           |
|--------|-----------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| POST   | /recipes/filter | Returns a paginated list of recipes, if the filter is present then filtered paginated results are provided, If no filters then all recipes displays in the pagination |
| POST   | /recipes        | Creates a new recipes                                                                                                                                                 |
| GET    | /recipes/{id}   | Retrieves the full details of a single recipes                                                                                                                        |
| DELETE | /recipes/{id}   | Deletes a recipes                                                                                                                                                     |


## Swagger Documentation
Swagger documentation is available at http://localhost:8080/swagger-ui.html

## Instructions to start the application
# PreRequisites
Mongo Db is needed to run it locally. Change local profile yml with your local mongodb creds

Use the following to run: `./gradlew bootRun`

To run the tests, use `./gradlew check`

## Improvements can be done
1) Docker image created but mongodb issue needs to be fixed
2) Authentication and Authorization can also be added to restrict the access to the API endpoints
3) Exception Handling while writing the database

