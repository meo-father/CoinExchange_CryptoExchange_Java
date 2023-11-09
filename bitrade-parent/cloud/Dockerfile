FROM openjdk:11

ENV TZ 'Asia/Shanghai'
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

RUN mkdir -p /data/app
ADD ./target/cloud.jar /data/app/app.jar
EXPOSE 7000
ENTRYPOINT ["java","-jar","/data/app/app.jar"]