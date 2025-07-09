FROM eclipse-temurin:22-jdk

# Set the working directory
WORKDIR /app

# Copy the project JAR file into the container at /app
COPY build/libs/order-service.jar /app/order-service.jar
EXPOSE 8084
# Run the application
ENTRYPOINT ["java", "-jar", "order-service.jar"]