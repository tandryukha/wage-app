#having this file in parent folder until model package is published to some artifactory to be available without the need to build local model module
FROM maven:3.8.5-openjdk-17-slim as build
COPY model model
COPY wage-producer application
WORKDIR /model
RUN mvn clean install
WORKDIR /application
RUN mvn package -DskipTests=true

###Image for run
FROM openjdk:17-jdk-slim as run-image
ARG JAR_FILE=/application/target/wage-producer-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} app.jar
#
ENTRYPOINT ["java","-jar","/app.jar"]