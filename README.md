# Distributed Event Driven Microservices Architecture Design Sandbox
This repository holds a basic but technically sound Store Inventory Backend. The ecosystem consists of secure, loosely coupled, and non-blocking Springboot 3 applications that interact with each other, databases, and topics written in Java 21. 

Currently, there are two lightnening fast applications that operate at the database layer and interact with NoSQL MongoDB databases and a redis server: item-inventory-service and item-review-service. These two applications allow the standard CRUD operations. item-inventory-services uses the traditional controller while item-review-services uses functional endpoints. They are both secured by role based JWTs and heavily written using functional programming.

A third application, online-order-service, operates at the HTTP layer and interfaces with the two previously mentioned applications to expose data through CRUD endpoints. It is also secured by JWT and heavily written using functional programming and implements functional endpoints.

The ecosystem also contains a kafka producer app (item-inventory-producer) and a kafka consumer app (item-inventory-consumer) that allow newly validated items to reach the DB tables. 

App management and orchestration is taken care of by the playground-naming-server, playground-config-server, and playground-api-gateway apps. playground-naming-server serves as the app registry. playground-config-server provides centralized app configurations. playground-api-gateway tackles the task of loadbalancing and being a fascade to other apis.

Lastly, there are two applications that provide shared models and common utilities to the ecosystem: sandbox-domain-model and sandbox-utilities.

### Technologies, Frameworks, and Libraries Used:
- Intellij
- Mac OS
- Springboot 3
- Maven
- Java 21
- Docker
- Kafka
- Offset Explorer 2
- MongoDB
- MongoDB Reactive
- MySql
- MySql Workbench
- JPA Hibernate
- Redisson
- Redis
- Keycloak
- Oauth2
- JWT
- Reactor
- Postman
- WebFlux
- WebClient
- OpenFeign
- Eureka
- Api Gateway
- Config Server
- OpenApi
- Lombok
- Jackson

### Testing
- Unit Testing : Junit5, Mockito, Datafaker
- Integration Testing : WebClientTest, Reactor Test, Embedded MongoDB, Embedded Kafka, Wiremock, Datafaker

### Robust Software Implementations
- API Security
- Abstraction, Inheritance, Polymorphism 
- Validation - Bean, Entity, RequestDTO, and ResponseDTO
- Logging
- Caching
- Unit Testing and Integration Testing
- Exception Handling, Retries
- On-demand Cache and Database Management Operations

# Swagger for Item Inventory Service
<img width="1440" alt="Screen Shot 2024-01-25 at 12 13 46 AM" src="https://github.com/xaviers-sandbox/lets-play/assets/155487917/a93e876b-516c-4fc9-9ddb-df1176cf75d1">

# Swagger for Item Review Service
<img width="1440" alt="Screen Shot 2024-01-25 at 12 14 07 AM" src="https://github.com/xaviers-sandbox/lets-play/assets/155487917/c47898e4-6a94-49db-91c6-14a01956dcda">

# Swagger for Online Store Service
<img width="1440" alt="Screen Shot 2024-01-25 at 6 06 25 PM" src="https://github.com/xaviers-sandbox/lets-play/assets/155487917/1f9f5123-17f0-4f74-935f-24d481d61369">

