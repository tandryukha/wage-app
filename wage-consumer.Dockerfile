FROM maven:3.8.5-openjdk-17-slim as compile
COPY model model
COPY wage-consumer application
WORKDIR /model
RUN mvn clean install
WORKDIR /application
RUN mvn clean compile

#TESTS
FROM compile as test
RUN mvn test

#package JAR
FROM compile as build
RUN mvn package -DskipTests=true

###Image for run
FROM openjdk:17-jdk-slim as run-image
ARG JAR_FILE=/application/target/wage-consumer-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} app2.jar
ENTRYPOINT ["java","-jar","/app2.jar"]