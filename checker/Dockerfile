########build stage########
#FROM maven:3.8.5-openjdk-17-slim@sha256:5ba3fae0f77cbe08deac4984dfa4f5397345d5ba8221871285a96e2ef8f16808 as maven_build
#WORKDIR /app
#
#COPY pom.xml .
## To resolve dependencies in a safe way (no re-download when the source code changes)
#RUN mvn clean package -Dmaven.main.skip -Dmaven.test.skip && rm -r target

FROM zaranik/checker-base:1.0
ARG JAR_FILE=/target/*-jar-with-dependencies.jar
COPY ${JAR_FILE} /app/build.jar
WORKDIR /app
CMD ["java", "-jar", "build.jar"]
