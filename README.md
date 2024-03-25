
# ChâTop

## Objectifs of project

We want to develop an online portal to enable potential tenants to contact the owners of the various properties they wish to rent.

This project was generated with JAVA version 17 and spring boot version 3.2.3.

## Installation
1. Make sure you have Java JDK and Maven installed on your machine.


2. Clone project

   `git clone https://github.com/Meceline/java-spring`


3. Go to the project directory

   `cd java-spring`


4. Compile and build the project with Maven

   `mvn clean install`


5. Run the application

   `mvn spring-boot:run`

You can access the application at http://localhost:8080

## MySQL
SQL script for creating the schema is available `ressources/sql/script.sql`

Add credentials to your environnement variable :
`spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD`

By default, the database uses port 3306

By default, an account is already registered in the database.
The identifiers are :

- login: test@test.com

- password: test!31


## Cloudinary
Go to https://cloudinary.com/ and create an account

Go to your dashboard and copy your credentials.

Return to the `config/CloudinaryConfig` project and update information :
- CLOUD_NAME
- API_KEY
- API_SECRET

## Swagger

When the project is launched, the documentation can be accessed via Swagger from the url http://localhost:3001/swagger-ui/index.html#/



