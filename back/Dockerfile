FROM openjdk:21
EXPOSE 8081
ARG JAR_FILE=target/mdd-api-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} springboot-mdd-api.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev-docker", "-jar", "/springboot-mdd-api.jar"]
