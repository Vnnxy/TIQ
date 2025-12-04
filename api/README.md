## Backend and DB setup 


### 1. Database (PostgreSQL)

You will need a container running the PostgreSQL image.

1. Create a Docker container using the official PostgreSQL image.
2. See: [How to use the Postgres Docker Official Image](https://www.docker.com/blog/how-to-use-the-postgres-docker-official-image/).

**Note:** This setup may change when Docker integration (`docker-compose`) is implemented.

---

### 2. Application properties

Please fill the required properties in the **`application.properties`** file that the backend requires to connect to the database. 

* **File:** `src/main/resources/application.properties`

```properties
spring.datasource.url=<database url> 
spring.datasource.username=<username>
spring.datasource.password=<password>
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
