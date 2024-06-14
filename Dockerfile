# Use Maven to build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use a smaller JRE image for the runtime
FROM openjdk:17-jdk-alpine
WORKDIR /app
RUN ls
COPY --from=build /app/target/*.jar ./einsbym-api-gateway.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "einsbym-api-gateway.jar"]
