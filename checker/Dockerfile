FROM zaranik/maven-zip:1.0
ARG JAR_FILE=/target/*-jar-with-dependencies.jar
COPY ${JAR_FILE} /app/build.jar
WORKDIR /app
CMD ["java", "-jar", "build.jar"]
