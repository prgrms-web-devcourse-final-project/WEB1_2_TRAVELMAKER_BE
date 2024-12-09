FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 5000

CMD ["java", "-jar", "app.jar"]
