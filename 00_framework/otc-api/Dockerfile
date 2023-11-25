# Docker image for springboot file run
# VERSION 0.0.1
# Author: Milo
# 基础镜像使用java
FROM openjdk:8
# 作者
MAINTAINER Milo

# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
VOLUME /tmp/app
# 将jar包添加到容器中并更名为app.jar
ADD target/otc-api.jar otc-api.jar
# 运行jar包
RUN bash -c 'touch /otc-api.jar'
ENTRYPOINT ["java","-jar","/otc-api.jar"]