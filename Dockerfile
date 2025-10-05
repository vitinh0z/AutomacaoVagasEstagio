FROM maven:3.9.6-eclipse-temurin-21 AS build


WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package



FROM eclipse-temurin:21-jre-jammy

WORKDIR /app
COPY --from=build /app/target/AutomacaoVagasEstagio-1.0-SNAPSHOT-jar-with-dependencies.jar .
CMD ["java", "-jar", "AutomacaoVagasEstagio-1.0-SNAPSHOT-jar-with-dependencies.jar"]