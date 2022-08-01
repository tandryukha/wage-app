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

