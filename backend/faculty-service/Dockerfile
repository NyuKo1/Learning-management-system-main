FROM maven:3.8-amazoncorretto-17 AS build

COPY shared-library /build/shared-library
RUN cd /build/shared-library && mvn install -q -DskipTests

COPY faculty-service/pom.xml /build/service/
COPY faculty-service/src /build/service/src/

WORKDIR /build/service
RUN mvn package -q -DskipTests -Dspring.profiles.active="prod"

FROM amazoncorretto:17-alpine3.15

WORKDIR /app

COPY --from=build /build/service/target/faculty-service-1.0.jar /app/

ENTRYPOINT ["java", "-jar", "faculty-service-1.0.jar"]
