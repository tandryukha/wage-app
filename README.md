# Getting started
This is an aggregator project that can build consumer & producer artifact in one go. 

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.2/maven-plugin/reference/html/#build-image)
* [Liquibase Migration](https://docs.spring.io/spring-boot/docs/2.7.2/reference/htmlsingle/#howto.data-initialization.migration-tool.liquibase)

# Building
Run `mvn clean verify` to run all the tests for all modules
# Docker
docker-compose down --rmi all  
docker-compose up --force-recreate

# API
### Send wage
`curl --location --request POST 'http://localhost:<producer-app-port>/wage' \
--header 'Content-Type: application/json' \
--data-raw '{
"name":"Bill",
"surname":"Gates",
"wage":1000000.4,
"eventTime":"2022-01-01T00:00:00Z"
}'`

### List wages
`curl --location --request GET 'http://localhost:<consumer-app-port>/wage'`

# Scaling
## How to scale producer
- Horizontal scaling of the producer app instance

## How to scale consumer
- Running multiple consumers concurrently inside the same app instance - just change `messaging.concurrency` in the config file to number of parallel threads
- Horizontal scaling of the producer instance

## Implementation details
Common dependencies, version is not used from parent to reduce coupling between consumer and producer and for smooth microservice extraction later.
