# Distributed Event Driven Microservices Architecture Design Sandbox
The ecosystem consists of loosely coupled non-blocking Springboot 3 applications that interact with each other, databases, and queues written in Java 21.

Currently, there are two applications that operate at the database level and interact with NoSQL MongoDB databases: item-inventory-service and item-review-service. These two applications allow the standard CRUD operations. item-inventory-services using the traditional controller while item-review-services uses functional endpoints. They are both heavily written using functional programming.

A third application, online-order-service, operates at the HTTP level and interfaces with the two previously mentioned applications to expose data through CRUD endpoints. It is also heavily written using functional programming and implements functional endpoints.

Lastly, there are two applications that provide shared models and common utilities to the ecosystem: sandbox-domain-model and sandbox-utilities

### Technologies, Frameworks, and Libraries Used:
- Springboot 3
- Java 21
- MongoDB
- MongoDB Reactive
- Reactor
- WebFlux
- WebClient
- Hibernate
- Lombok
- Jackson

### Testing
- Unit Testing : Junit5, Mockito, Datafaker
- Integration Testing : WebClientTest, Reactor Test, Embedded MongoDB, Wiremock, Datafaker

### Robust Software Implementations
- Abstraction, Inheritance, Polymorphism 
- Validation - Bean, Entity, RequestDTO, and ResponseDTO
- Logging
- Caching
- Exception Handling, Retries
