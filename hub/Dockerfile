# https://hub.docker.com/r/adoptopenjdk/openjdk8-openj9/
FROM adoptopenjdk/openjdk8-openj9
RUN mkdir /opt/app
COPY build/libs/hub-0.0.1-SNAPSHOT.jar /opt/app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/app/app.jar"]
EXPOSE 8080