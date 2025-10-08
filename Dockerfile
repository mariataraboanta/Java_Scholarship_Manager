FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
# Descarcă toate dependențele în această etapă pentru a beneficia de caching
RUN mvn dependency:go-offline

# Copiază codul sursă și construiește aplicația
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
# Copiază doar JAR-ul generat din etapa de build
COPY --from=build /app/target/*.jar app.jar
# Expunerea portului - doar informativ, Render va seta propriul port
EXPOSE 8081
# Comandă de pornire optimizată pentru Spring Boot
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
