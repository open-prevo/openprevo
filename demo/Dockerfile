# https://hub.docker.com/r/adoptopenjdk/openjdk8-openj9/
FROM adoptopenjdk/openjdk8-openj9
RUN mkdir /opt/app
VOLUME /opt/app
WORKDIR /opt/app
ENTRYPOINT ["sh","./start.sh"]