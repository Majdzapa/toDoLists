#define base docker image
FROM openjdk:8
LABEL maintainer="Hedfi Majd"
ADD target/toDoLists-0.0.1-SNAPSHOT.jar springboot-docker-demo.jar
ENTRYPOINT ["java","-jar","springboot-docker-demo.jar"]