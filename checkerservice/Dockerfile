FROM zaranik/maven-docker-ubuntu:1.0
RUN mkdir /app
ARG JAR_FILE=/target/*.jar
COPY ${JAR_FILE} /app/build.jar
WORKDIR /app
CMD ["java", "-jar", "build.jar"]