# Estágio 1: Construção (Build)
# Usamos uma imagem oficial do Maven que já tem o Java (JDK) para compilar o projeto.
FROM maven:3.8.5-openjdk-17 AS build


WORKDIR /app


COPY pom.xml .


RUN mvn dependency:go-offline


COPY src ./src


RUN mvn clean package


FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/target/AutomacaoVagasEstagio-1.0-SNAPSHOT-jar-with-dependencies.jar .

CMD ["java", "-jar", "AutomacaoVagasEstagio-1.0-SNAPSHOT-jar-with-dependencies.jar"]