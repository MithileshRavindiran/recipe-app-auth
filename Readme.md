# Recipe Application Java Assignment

## Introduction
This assignment is to 
1) Write a SpringBoot/Java restful backend;


Assignment is to write a Java application with a restful backend, which supports storing recipes and manipulate the recipes.
Each Recipe contain recipe name, recipe type, No of Servings, Ingredients and Instructions
The application is  developed using Java and Spring-Boot

The restful backend should support the following endpoints:

| Method | Path            | Description                                                                                                                                                           | Access                                       |
|--------|-----------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------|
| POST   | /login          | Authentication token creator                                                                                                                                          | public                                       |
| POST   | /recipes/filter | Returns a paginated list of recipes, if the filter is present then filtered paginated results are provided, If no filters then all recipes displays in the pagination | public                                       |
| POST   | /recipes        | Creates a new recipes                                                                                                                                                 | private(ROLE_USER, ROLE_ADMIN, ROLE_MANAGER) |
| GET    | /recipes/{id}   | Retrieves the full details of a single recipes                                                                                                                        | public                                       |
| DELETE | /recipes/{id}   | Deletes a recipes                                                                                                                                                     | private(ROLE_USER, ROLE_ADMIN, ROLE_MANAGER) |
|        |                 |                                                                                                                                                                       |                                              |
| POST   | /admin/user     | Add new user                                                                                                                                                          | private(ROLE_ADMIN, ROLE_MANAGER)            |



## Swagger Documentation
Swagger documentation is available at http://localhost:8080/swagger-ui/index.html

## Instructions to start the application
# PreRequisites
Mongo Db is needed to run it locally. Change local profile yml with your local mongodb creds

Use the following to run: `./gradlew bootRun`

To run the tests, use `./gradlew check`

## Docker Documentation

## Instructions to start the application from docker
# PreRequisites
From root directory run

 docker-compose up -d


## Improvements can be done
1) User management can be improved by defining more clear roles/permissions and more endpoints for managing roles
2) Scaling of the application can be improved with the orchestration kubernetes
3) design as separate microservices component to increase scalability (eg: auth, api)
4) Clearly define  and use either one of the approach contract first or code first. current code uses code first approach for contract and documentation
5) CI/CD Pipeline integration
6) Automation Testing with CI/CD
7) Load testing to understand the application behaviour and bottlenecks
8) JWT Refresh Token implementation on JWT
9) Utilize JAVA 17 features like Record classes instead of lombok


