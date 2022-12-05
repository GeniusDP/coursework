FROM maven:3.8.5-openjdk-17-slim
RUN mkdir /app
ARG JAR_FILE=/target/*-jar-with-dependencies.jar
COPY ${JAR_FILE} /app/build.jar
WORKDIR /app
CMD ["java", "-jar", "build.jar"]
