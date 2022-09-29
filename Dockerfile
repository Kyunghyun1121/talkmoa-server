
FROM openjdk:17-jdk
ENV APP_HOME=/home/app/
WORKDIR $APP_HOME
COPY build/libs/*.jar talkmoa.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","talkmoa.jar"]
