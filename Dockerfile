FROM openjdk:11-jre-slim-buster
WORKDIR /app

ENV CLASSPATH=/app/*

RUN apt-get update && apt-get install -y vim curl less mlocate netcat telnet iputils-ping
COPY src/main/resources/env.application.properties application.properties
COPY entrypoint.sh .
COPY /target/*.jar .
#COPY env.values .
EXPOSE 9191
RUN chmod +x entrypoint.sh
ENTRYPOINT ["bash", "/app/entrypoint.sh"]