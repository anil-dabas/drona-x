FROM eclipse-temurin:22-jdk

# Set the working directory
WORKDIR /app

# Copy the project JAR file into the container at /app
COPY build/libs/driver.jar /app/driver.jar
EXPOSE 8083
# Run the application
ENTRYPOINT ["java", "-jar", "driver.jar"]