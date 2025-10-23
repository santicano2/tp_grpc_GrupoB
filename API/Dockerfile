FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests -Dos.detected.classifier=linux-x86_64

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /build/target/API-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]