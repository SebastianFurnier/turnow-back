# -------- BUILD STAGE --------
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiamos pom primero para cachear dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el resto del proyecto
COPY src ./src

# Compilamos
RUN mvn clean package -DskipTests

# -------- RUN STAGE --------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiamos el jar generado
COPY --from=build /app/target/*.jar app.jar

# Puerto por defecto de Spring Boot
EXPOSE 8080

# Variables de entorno (se sobreescriben en runtime)
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]
