FROM maven:3.8-amazoncorretto-17 AS build

# Install local shared-library to /root/.m2 first
COPY shared-library /build/shared-library
RUN cd /build/shared-library && mvn install -q -DskipTests

# Build the service
COPY auth-service/pom.xml /build/service/
COPY auth-service/src /build/service/src/

WORKDIR /build/service
RUN mvn package -q -DskipTests -Dspring.profiles.active="prod"

FROM amazoncorretto:17-alpine3.15

WORKDIR /app

COPY --from=build /build/service/target/auth-service-1.0.jar /app/

ENTRYPOINT ["java", "-jar", "auth-service-1.0.jar"]
