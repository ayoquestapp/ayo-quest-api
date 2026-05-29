# Stage 1 - Build
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copia primeiro o pom (cache de dependências)
COPY pom.xml .

# Baixa dependências de forma mais robusta
RUN mvn -B dependency:go-offline

# Copia o código
COPY src ./src

# Build da aplicação
RUN mvn -B clean package -DskipTests


# Stage 2 - Runtime (leve e seguro)
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copia jar gerado
COPY --from=build /app/target/*.jar app.jar

# Porta do Fly
EXPOSE 8080

# Start da aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]