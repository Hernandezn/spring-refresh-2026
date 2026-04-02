# Spring Refresh (2026)
This is a local Spring Boot RESTful Web API server, for my own experimentation & knowledge review.

It's meant as a coding sandbox for upcoming projects & practice with the technology, not as a deployed Web service.

Works being referenced:
- [Spring Boot official guides](https://spring.io/guides)
- [Client Engagement Portal (from previous work)](https://github.com/revaturelabs/client-engagement-portal-back)

# Features

- **Uptime Captor & Request Captor**
  
  Inspired by IIoT uptime tracking. This captures server uptimes & API requests and persists them in a local database. Uptime windows capture startup & shutdown times, along with whether there was an abnormal shutdown (via a shutdown code & lookup table). Every request entry has the path it targets, the time of the request, the HTTP method used, and a foreign key to its uptime window's database entry.

# High-level Architecture & Dependencies

- **Layered REST API via MVC Components**

  API requests arrive at an MVC controller, then data travels through a Service layer & Repository layer to carry out database communication via Entity models. Responses are DTO models. HTML views are fully servable, and one custom view is already served (through the root path, http://localhost:9012/), with backend model data displayed in the view using JavaScript fetch.

- **Spring Data JPA**

  JPA repositories & annotation-configured Entity mappings carry out database interfacing.

- **Maven, Apache Tomcat, Java 21, application config**

  Baseline dependencies for the project. Dependency management through a Maven pom.xml, run configuration through application.properties (may switch to application.yaml), running an embedded Apache Tomcat server, built using Java 21 (highest LTS Java version with full out-of-the-box support from Lombok at the time of creation).
