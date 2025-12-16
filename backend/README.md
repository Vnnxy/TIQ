# Backend

## Initial setup
A .env file is required at the root level of the project. 
The file must contain:

* **File:** `.env`
```
POSTGRES_DB=<PostgresDB>
POSTGRES_USER=<PostgresUser>
POSTGRES_PASSWORD=<Password>
```

## Running using docker container

Make sure you have Docker and docker compose installed.

1. Initialize the docker daemon.

2. To build the container and run the backend use:
```
docker compose up --build
```


## Running manually


### 1. Database (PostgreSQL)

You will need a container running the PostgreSQL image.

1. Create a Docker container using the official PostgreSQL image.
2. See: [How to use the Postgres Docker Official Image](https://www.docker.com/blog/how-to-use-the-postgres-docker-official-image/).

**Note:** This setup may change when Docker integration (`docker-compose`) is implemented.

---

### 2. Application properties

Make sure that **`application.properties`** uses the env. variables that are defined inside the .env file. 

* **File:** `src/main/resources/application.properties`

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
```
---
### 3. Buidling and running the project

Java 21 and Maven 3.8.6 was used for this project.

To install the necessary dependencies use:
```
mvn install
```

And to run the application:
```
mvn spring-boot:run
```
