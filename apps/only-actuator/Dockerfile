FROM openjdk:8u275-jre
COPY build/libs/only-actuator-*.jar /opt/app.jar
WORKDIR /opt
CMD ["java", "-jar", "app.jar"]


