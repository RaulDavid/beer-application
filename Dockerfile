FROM maven:3.6.0-jdk-11-slim
WORKDIR /beer-application
ENV PORT 8080

ADD pom.xml /beer-application/pom.xml
ADD src /beer-application/src
RUN ["mvn", "clean"]
RUN ["mvn", "package"]

RUN cp /beer-application/target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS \
 -Djava.security.egd=file:/dev/./urandom \
 -Dspring.profiles.active=prod \
 -jar app.jar
