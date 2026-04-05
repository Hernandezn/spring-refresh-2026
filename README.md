# Spring Refresh (2026)
This is a local Spring Boot RESTful Web API server, for my own experimentation & knowledge review.

It's meant as a coding sandbox for upcoming projects & practice with the technology, not as a deployed Web service.

Resources being referenced:
- [Spring Boot official guides](https://spring.io/guides)
- [Selenium Webdriver documentation](https://www.selenium.dev/documentation/webdriver/)
- [Maven Repository](https://mvnrepository.com/)
- [Client Engagement Portal (from previous work)](https://github.com/revaturelabs/client-engagement-portal-back)

# Features

- **Website Screenshotter API**

  The GET API endpoint at "/screenshot?url=\[URL]" will create and serve a live screenshot of the URL provided. This API is powered by Selenium Webdriver, and it's provided with thread safety using Java's ExecutorService, to provide an internal task queue for screenshots.

- **Uptime Captor & Request Captor**

  Inspired by IIoT uptime tracking. This captures server uptimes & API requests and persists them in a local database.
  - Uptime database entries capture startup & shutdown times, along with whether there was an abnormal shutdown (captured via a shutdown code & lookup table).
  - Every request entry has the path it targets, the time of the request, the HTTP method used, and a foreign key to its uptime window's entry.
  - I've also set up normal to-file logging using Log4J, but this is extraneous to / in addition to these database-captured custom traceability features.

# High-level Architecture & Dependencies

- **Layered REST API via MVC Components**

  API requests arrive at an MVC controller, then data travels through a Service layer & Repository layer to carry out database communication via Entity models. Response JSONs are DTO models. HTML views are fully servable. One custom view is served (on the root path, http://localhost:9012/) for checking API endpoints at a glance, with backend data displayed in this view using JavaScript fetch & DOM manipulation.

- **Selenium Webdriver**

  Runs an internal Web browser to carry out automation tasks that require rendering Web pages.

- **Spring Data JPA**

  JPA repositories & annotation-configured Entity mappings carry out database interfacing.

- **Maven, Apache Tomcat, Java 21, application config**

  Baseline dependencies for the project. Dependency management through a Maven pom.xml, run configuration through application.properties (may switch to application.yaml), running an embedded Apache Tomcat server, built using Java 21 (highest LTS Java version with full out-of-the-box support from Lombok at the time of creation).

# How to Run Locally via Terminal

Once this project is in your local system,

1. [Download Apache Maven (Binary Zip Archive)](https://maven.apache.org/download.cgi)
   - [Installation instructions](https://maven.apache.org/install.html)
2. In a command line, open the folder containing this project, and run the following command:
```
   mvn spring-boot:run
```

The application will have a simple static UI that opens at [localhost:9012](http://localhost:9012/) in a Web browser.

A local database UI will be at [localhost:9012/h2-console](http://localhost:9012/h2-console). In the fields provided, "Driver Class" will be `org.h2.Driver`, "JDBC URL" will be `jdbc:h2:./data/localdb`, "User Name" will be `sa`, and the "Password" field will be blank.
