# --- Stage 1: Build the Java Archive ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies to cache them
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and package the application
COPY src ./src
# Using -DskipTests to speed up the docker build process and avoid testcontainer crashes during image building
RUN mvn clean package -DskipTests

# --- Stage 2: Create a lightweight runtime image ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Expose the standard Spring Boot port
EXPOSE 8080

# We use wildcard because the pom.xml generates something like SpringDataJPADemo-0.0.1-SNAPSHOT.war
COPY --from=build /app/target/*.war app.war

# Run the packed WAR file via the built-in tomcat container inside Spring Boot
ENTRYPOINT ["java", "-jar", "app.war"]
