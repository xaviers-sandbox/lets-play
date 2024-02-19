FROM openjdk:21
VOLUME [ "/tmp" ]
COPY target/*.jar app.jar
EXPOSE 1111
CMD ["java", "-jar", "/app.jar"]