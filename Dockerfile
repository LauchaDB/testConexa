# Etapa de construcción
FROM maven:3.8.4-openjdk-8 AS build
WORKDIR /app
COPY pom.xml .
# Descargar todas las dependencias
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests -Dspring.profiles.active=prod

# Etapa de ejecución
FROM openjdk:8-jre-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","app.jar"] 