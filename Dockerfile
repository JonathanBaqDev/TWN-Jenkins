FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY target/java-maven-app-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]