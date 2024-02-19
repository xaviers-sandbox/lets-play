FROM openjdk:21
VOLUME [ "/tmp" ]
COPY target/*.jar app.jar
EXPOSE 2222
CMD ["java", "-jar", "/app.jar"]