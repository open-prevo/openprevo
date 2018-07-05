FROM openjdk:8
RUN mkdir /opt/app
COPY build/libs/adapter-dummy-0.0.1-SNAPSHOT.jar /opt/app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/app/app.jar"]
EXPOSE 8080