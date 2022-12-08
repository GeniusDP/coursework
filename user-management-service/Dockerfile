FROM openjdk:17-alpine@sha256:a996cdcc040704ec6badaf5fecf1e144c096e00231a29188596c784bcf858d05
RUN mkdir /app
ARG JAR_FILE=/target/*.jar
COPY ${JAR_FILE} /app/build.jar
WORKDIR /app
CMD ["java", "-jar", "build.jar"]