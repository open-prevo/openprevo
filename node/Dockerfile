FROM openjdk:8
RUN mkdir /opt/app
ARG NODE_JAR
ARG ADAPTER_JAR
COPY ${NODE_JAR} /opt/app/app.jar
COPY ${ADAPTER_JAR}  /opt/app/adapter.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dloader.path=/opt/app/adapter.jar","-jar","/opt/app/app.jar"]
EXPOSE 8080