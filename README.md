# Getting started
This is an aggregator project that can build consumer & producer artifact in one go. 
It doesn't contain shared dependencies to reduce coupling for easier microservice extraction.

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.2/maven-plugin/reference/html/#build-image)
* [Liquibase Migration](https://docs.spring.io/spring-boot/docs/2.7.2/reference/htmlsingle/#howto.data-initialization.migration-tool.liquibase)


# Docker
docker-compose down --rmi all  
docker-compose up --force-recreate

# API
### Send wage
`curl --location --request POST 'http://localhost:8081/wage' \
--header 'Content-Type: application/json' \
--data-raw '{
"name":"Bill",
"surname":"Gates",
"wage":1000000.4,
"eventTime":"2022-01-01T00:00:00Z"
}'`

### Receive wages
`curl --location --request GET 'http://localhost:8080/wage'`

# Scaling
## How to scale producer
- Horizontal scaling of the producer instance

## How to scale consumer
- Running multiple consumers concurrently in the same instance(listener concurrency setting)
- Horizontal scaling of the producer instance
