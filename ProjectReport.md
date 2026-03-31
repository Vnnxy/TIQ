# Project Report

## Technologies:

| Layer | Technology | Key Reason |
| :--- | :--- | :--- |
| **Frontend** | ![Vue.js](https://img.shields.io/badge/Vue.js-4FC08D?style=flat-square&logo=vuedotjs&logoColor=white) + Vuetify | Lightweight SPA creation with robust UI components. |
| **Backend** | ![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=java&logoColor=white) + Spring Boot | Type safety, security, and rapid web service development via Spring Boot. |
| **Database** | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=flat-square&logo=postgresql&logoColor=white) | Relational integrity and seamless integration with Docker/Spring. |
| **Containers** | ![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white) | Multi-container virtualization and simplified deployment. |

## Requirement fulfillment:

### R1: The app is designed to manage transactions and get relevant data through the endpoints.
* **1.1 Individual Transaction:** Access via `/transactions/{id}`. (UI: Transactions Menu -> Search by ID).
* **1.2 Client History:** Retrieve/Delete transactions by `client_id` with Month/Year filters. Access via `/transactions`.
* **1.3 City Analytics:** Average transaction amount (USD) per city/month/year (excluding online). Access via `/transactions/average`.
* **1.4 State Statistics:** Total daily transaction volume for specific US States and months, returned in batches (10, 20, 50, 100). Access via `/transactions/total`.
* **1.5 Top/Bottom amounts:** Retrieve Top/Bottom $N$ amounts for a specific year or range of $M$ years. Access via `/transactions/tb`.


### R2: Resource representation
  All endpoints have their default representation as application/json. However, they also allow the acceptParam parameter and/or the Accept header that defines the desired representation. It is important to note that if both of them are provided. The Accept header will be the priority. If a representation that is not text/csv or application/json is  provided, the endpoint will default back to json. Therefore, the application will not return html. (See more details in Design Considerations)

### R3: Api documentation
The OpenApi documentation describes each endpoint, including the parameters and return types.

### R4-R6: Arquitecture
* **Backend:** Spring Boot / Maven implementation located in `/backend`.
* **Frontend:** Vue.js implementation located in `/frontend`.
* **Containers:** `docker-compose.yml` builds the frontend, backend, and database layers for one-step deployment.

### R7: Short report 
 This document and the code comments fulfill the req.
### R8: The group policy is defined in the Policy section.
### R9: This is fulfilled in AI use section.

## Extra requiremens

### 1: Additional endpoints 

The following endpoints and features where implemented:

* **/merchants:** Full CRUD operations for merchant entities. (Merchants menu)
* **/summaries:** Complex reporting endpoint generating summaries derived from the merchant and transaction data. (Summaries menu)


### Extra 2: 3rd party API 

* **[QuickChart.io](https://quickchart.io):** Dynamic graph generation for data summaries.
* **[Nominatim (OSM)](https://nominatim.openstreetmap.org):** Geocoding services for city-based input validation in the "Average" section.

### Extra 3: Containers

Separate Dockerfiles are used to build the frontend and backend images, separating and building them.

Deployment is handled through a Docker Compose configuration on WarpGate (docker-compose.yml), which pulls the pre-built images from the GitLab Container Registry and runs them as containers together with a PostgreSQL database. This approach allows the application to be deployed consistently without requiring the source code to be present on the server.

### Extra 4: Implementation principles and patterns 
The application uses frameworks such as Spring Boot and Vue.js that encourage separation of concerns by dividing responsibilities between controllers, services, repositories, and data models in the backend, and component-based views in the frontend.

The backend follows a layered architecture, where controllers handle HTTP requests, services contain business logic, repositories are responsible for data access, config contains configurations and exceptions are managed separately. This improves maintainability, testability, and extensibility.

The frontend uses Vue.js, which is MVVM based. Components encapsulate their own state, logic, and view.

The app uses .env files that are used to customize configuration such as ports, database credentials, and API base URLs. This allows the same application to be deployed in different environments without code changes, improving flexibility and reliability.

### Extra 5: CI/CD pipeline

Automated workflow via `.gitlab-ci.yml`:
1.  **Build:** Automated builds using GitLab Registries upon every push.
2.  **Deploy:** Remote server (WarpGate) pulls the latest images via Docker Compose.
3.  **Initialization:** The backend handles automatic data population on startup.

This 3 steps fulfill the requirement "Build, deploy and run".

## Maturity level:
This is a Level 2 maturity level Restful API. It uses HTTP Verbs, provides Status codes, Resources are identifiable by their URI's. However, it doesnt provide HATEOAS and therefore there are no dynamic links.

By being a level 2 API, the backend does not provide dynamic links, meaning that the frontend relies on defined URI's. So any change in this endpoints would break the application logic, and require to manually change the URI's inside the frontend.



## Distribution:

The work done may include different versions of the same file. As some had to be re-written.

### López Asano Miguel Akira: 

* #### Backend:

    - Configuration files (config folder)
    - Exceptions (exception folder)
    - Db population (startup folder)
    - Controller: ContentTypeNegotiator.java
    - Endpoints (Controllers, services, repositories, models, dto's, formatters.): 
        - /transactions/average (Req 1.3)
        - /transactions/total (Req 1.4)
        - /transactions/tb (Req 1.5)
        - /summaries
        - /merchants
    
* #### Frontend:
    - Main pages(pages folder) excluding 3rd party Api for cities inside TransactionSearch.vue
    - Components( AvgTable, CreateMenu, MainMenu, MerchantMenu, TBTable, TotalTable)
    - router index.js
    - Api callers (api folder)

* #### Containers:
    - Dockerfiles
    - docker-compose.yml
    - .env files and properties
* #### CI/CD:
    - .gitlab-ci.yml
    - Git lab credentials
    - WarpGate setup
* #### WarpGate:
    - Csv clean up
    - Csv upload
    - Setup
* #### Docs:
    - ProjectReport

### Efa Rhys

* #### Backend
    - Endpoints (Controllers, services, repositories, models, dto's, formatters.): 
        - Transactions (CRUD)
        - Transactions/{id}
* #### Frontend:
    - City map 3rd Party Api Implementation (TransactionsSearch.vue)
    - CityMapModal.vue
* #### Docs:
    - OpenApi docs (API DOC)


## AI policy
Generative AI will be used for understanding certain errors by feeding error messages and asking it to make it understandable. AI will be use for fixing frontend and pipeline logic.

Personal use of generative AI by developers will also include using it in a search engine-like manner, to search for features of the pipeline that may be useful during development.

The tools accepted are OpenAI ChatGPT and Claude.


## AI use

- ChatGPT 5.2 Free tier. 
- Claude 3.5 Sonnet

AI was used to identify an error in file CsvDataInit.java.
The prompt was:
"I'm creating a method in Java to populate the db but I get the \<Error Message>. Can you explain what this does? \<My method>"

This resulted in a suggestion of removing the @Transactional annotation i was using and adding Print lines that could help me identify where my program was breaking. It also identified that I was missing .trim() in the csv parsing that was causing some lines to be skipped.

The print lines were kept as they are helpful when initializing the containers.

In this case, the prompt resulted in inmediate result and it was not hard to integrate as the logic behind the code remained the same. 

The rest of the AI use was by using it as an search-engine manner. One query that resulted in a change in the code was:

"What is the difference in Vue.js and Vuetify between a v-table and a v-data-table and what kind of data do they accept." This resulted in switching from a v-table to a v-data-table in summariesPage.vue as this aligned better with my requirements.



## Design considerations
This section contains some design considerations and features that are relevant for the project.

### Exceptions:
The backend uses custom ApiException class to Throw the status codes. This is used to reduce boilerplate code as this and other Spring specific Exceptions that are thrown are interceoted by the ApiExceptionHandler to decide the proper representation of the entity. If the exception is not specifically defined, then a server error is thrown. This is also the reason why try catch is not used in services.

### Interfaces for Services:
Interfaces are used for service definitions instead of abstract classes. This design choice improves flexibility and testability, as it allows multiple implementations of the same service contract. (https://batuhankok.medium.com/interfaces-in-spring-boot-clean-architecture-88d378b1432c)

Using interfaces enables loose coupling between components and allows services to be extended or replaced without affecting dependent classes. Additionally, we are only defining capabilities, as not all services have the same options.

### ContentTypeNegotiator class
This class is handling the representation of the data by taking the req and deciding what content type will be used. This is inside the controller folder as this ...

### Config folder
The ClientWebConfig class is used to change SpringBoot behaviour, and allow the ContentTypeNegotiator to work properly. 

CorsConfig.java: This allows the frontend to communicate with the backend. It is setup to *, which means it will allow all urls. (This is not recommended), however, for teh warpgate url, the addtional headers/params that are withing the backend url cause the CORS to not work properly.

### startup folder
This contains the class responsible for filling the database if it doesnt contain any data. 

### Merchant model
The merchant model contains two id's as one is a surrogate key that represents the original csv id. This id is only used for the initial db population. All behaviour within the backend is used through the generated ids.

## References 
List of resources that were used for the development of the app.

- [Spring-boot Docs](https://docs.spring.io/spring-boot/index.html)
- [Java Docs](https://docs.oracle.com/en/java/)
- [PostgreSQL Docs](https://www.postgresql.org/docs/)
- [JDBC Docs](https://jdbc.postgresql.org/documentation/)
- [GitLab Doc](https://docs.gitlab.com/)
- [GitLab Registry](https://docs.gitlab.com/user/packages/container_registry/)
- [Vue.js Docs](https://vuejs.org/guide/introduction.html)
- [Vuetify Docs](https://vuetifyjs.com/en/getting-started/frequently-asked-questions/#questions)
- [QuickChart.io Docs](https://quickchart.io/documentation/)
- [Baeldung OpenApi](https://www.baeldung.com/spring-rest-openapi-documentation)
- [Baeldung Error handling](https://www.baeldung.com/exception-handling-for-rest-with-spring)
- [Baeldung Transactions](https://www.baeldung.com/transaction-configuration-with-jpa-and-spring)
- [Baeldung Limit Query](https://www.baeldung.com/jpa-limit-query-results)
- [WarpGate](https://warpgate.null.page/docs/)
- [Http Status Codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status)
- [Spring Override Variables](https://medium.com/programmers-journey/how-to-override-spring-properties-with-env-vars-82ee1db2ae78)
- [Axios Vue](https://v2.vuejs.org/v2/cookbook/using-axios-to-consume-apis.html?redirect=true)
- [Axios Dev](https://v2.vuejs.org/v2/cookbook/using-axios-to-consume-apis.html?redirect=true)
- [Out of Memory Java](https://medium.com/@ataberkgrl/overcoming-the-out-of-memory-problem-in-spring-data-jpa-streaming-f64e81ec6c6f)
- [Headers and params ](https://stackoverflow.com/questions/68554176/get-headers-when-valid-body-not-satisfy-and-exceptionhandler-hits)