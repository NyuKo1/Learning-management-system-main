FROM maven:3.8-amazoncorretto-17 AS build

COPY shared-library /build/shared-library
RUN cd /build/shared-library && mvn install -q -DskipTests

COPY crm-service/pom.xml /build/service/
COPY crm-service/src /build/service/src/

WORKDIR /build/service
RUN mvn package -q -DskipTests -Dspring.profiles.active="prod"

FROM amazoncorretto:17-alpine3.15

WORKDIR /app

COPY --from=build /build/service/target/crm-service-1.0.jar /app/

ENTRYPOINT ["java", "-jar", "crm-service-1.0.jar"]
