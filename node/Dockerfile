# https://hub.docker.com/r/adoptopenjdk/openjdk8-openj9/
FROM adoptopenjdk/openjdk8-openj9:latest
RUN mkdir /opt/app
RUN mkdir /opt/app/config
VOLUME /opt/app/config
ARG NODE_JAR
ARG ADAPTER_JAR
COPY ${NODE_JAR} /opt/app/app.jar
COPY ${ADAPTER_JAR}  /opt/app/adapter.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dloader.path=/opt/app/adapter.jar","-jar","/opt/app/app.jar"]
EXPOSE 8080