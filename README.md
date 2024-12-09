# Payment Channel Integration Service

## Overview

This is a Spring Boot-based service for managing channel payments and handling callbacks, integrated with Cryptopay. It uses PostgreSQL as the database, Liquibase for database migrations, and provides a REST API documented with Swagger.

## Prerequisites

Before starting, make sure you have the following tools installed:

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)


##  Start PostgreSQL with Docker

First, ensure Docker is running on your machine.

In the project directory, there is a `docker-compose.yml` file that sets up PostgreSQL. To start the PostgreSQL database, use the following command:

```bash 
docker-compose up -d postgres
```

This command pulls the necessary Docker image for PostgreSQL and starts the container. The PostgreSQL instance will be configured using the settings in the docker-compose.yml file.

## Build and Start the Java Application using Docker Compose
Once PostgreSQL is up and running, start the Java application (Spring Boot) within Docker using Docker Compose.

```bash
docker-compose up -d
``` 
This command will build the Docker images (if not built already) and start the services as defined in the docker-compose.yml file. The Java application will be available on port 8081

## Build and Run the Spring Boot Application (Locally)

If you want to build and run the Spring Boot application manually (outside of Docker), follow these steps:


```bash 
./gradlew clean build
```

Run tests:
```bash 
./gradlew test
```

Start the application:
```bash 
./gradlew bootRun
```


## Configure the Application
The application configuration can be found in:
```bash 
src/main/resources/application.yml
```

Key configurations:
```bash 
server port: 8081

crypto:
  api:
    key: x0UpXWdpORzKZ7HV_mtUSg
    secret: S0VMqRZg_HxecPtQAmo-HT8DWFBiGU5T2bD8R-wjd4E
    url: https://business-sandbox.cryptopay.me
  callback:
    secret: _av_bUZ3pqoMh8QZmNxwapm19ZxocUSm2e4eUZVp6H0
  allowed:
    ip: 3.249.128.101
```

Datasource: PostgreSQL with credentials for database payment_db


```bash 

Database URL: jdbc:postgresql://localhost:5432/payment_db
Username: postgres
Password: postgres
```

API Key and Secret for secure communication
Callback IP whitelisting
Ensure that these configurations are correct before proceeding.


## 4. Access the API Documentation (Swagger)
Once the application is running, you can access the Swagger API documentation by navigating to the following URL in your browser:

```bash 
http://localhost:8081/swagger-ui/index.html
```

## 5. Cryptopay Integration Details
The application is integrated with Cryptopay for handling payment callbacks and verifications. Key endpoints include:

Create Channel Payment:

```bash 
Endpoint: POST /api/channels
```

Handle CryptoPay Payment Callback

```bash 
Endpoint: POST /api/channels/callback
Headers: X-Cryptopay-Signature: <signature>
```

Get Channel Payments

```bash 
Endpoint: GET /api/channels/{channel-id}/payments/{channel-payment-id}
```