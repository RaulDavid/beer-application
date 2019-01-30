FROM openjdk:11.0.1-jre-slim
VOLUME /tmp
ADD target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]


