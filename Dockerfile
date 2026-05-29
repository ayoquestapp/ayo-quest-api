# Stage 1 - Build
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copia primeiro o pom (cache de dependências)
COPY pom.xml .

# Baixa dependências de forma mais robusta
RUN mvn -B dependency:go-offline

# Copia o código
COPY src ./src

# Build da aplicação
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]