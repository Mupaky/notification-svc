FROM openjdk:17

COPY target/notification-svc-*.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod" , "-jar", "app.jar"]