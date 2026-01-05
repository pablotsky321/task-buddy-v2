# ---------- Etapa 1: Build ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos primero los archivos de configuración para aprovechar la caché
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el resto del código
COPY src ./src

# Construimos el jar
RUN mvn clean package -DskipTests


# ---------- Etapa 2: Runtime ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiamos el jar generado desde la etapa de build
COPY --from=build /app/target/*.jar server.jar

# Exponemos el puerto típico de Spring Boot
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "server.jar"]
