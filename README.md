# Getting started
This is the parent(aggregator) project that includes wage producer and consumer applications. 

# Building
Run `mvn clean verify` to run all the tests for all modules

# Running
## Pre-requisites
1. Install Docker desktop
## Run/restart steps
1. Stop and remove existing containers `docker-compose down --rmi all`
2. Start and infrastructure locally in docker compose `docker-compose up --force-recreate`

# Scaling
## How to scale producer
- Horizontal scaling of the producer app instance (see below)
## How to scale consumer
- Running multiple consumers concurrently inside the same app instance - just change `messaging.concurrency` in application.yaml
in the config file to number of parallel listeners
- Horizontal scaling of the producer instance

## Horizontal scaling

To increase amount of service instances, `services.{producer|consumer}.deploy.replicas` to desired number of instances.
Also avoid mapping multiple instances to the same specific port to allow dynamic port allocation for every instance and avoid port collision

    ports:
      - "8080"
    expose:
      - "8080"
    deploy:
      replicas: 2

# How to use custom config files


# Implementation details
Common dependencies, version is not used from parent to reduce coupling between 
consumer and producer and for smooth microservice extraction later.

# Open API
Run docker-compose and navigate to producer and consumer OpenAPI documentation pages
- Consumer: http://localhost:8081/swagger-ui/#/
- Producer: http://localhost:8082/swagger-ui/#/

# API overview
## Send wage
`curl --location --request POST 'http://localhost:8082/wage' \
--header 'Content-Type: application/json' \
--data-raw '{
"name":"Bill",
"surname":"Gates",
"wage":1000000.4,
"eventTime":"2022-01-01T00:00:00Z"
}'`

## List stored wages
`curl --location --request GET 'http://localhost:8081/wage'`

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.2/maven-plugin/reference/html/#build-image)
* [Liquibase Migration](https://docs.spring.io/spring-boot/docs/2.7.2/reference/htmlsingle/#howto.data-initialization.migration-tool.liquibase)
