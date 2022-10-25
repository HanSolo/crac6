FROM ubuntu:20.04

ENV JAVA_HOME /opt/jdk
ENV PATH $JAVA_HOME/bin:$PATH

RUN apt-get update -y

ADD "https://github.com/CRaC/openjdk-builds/releases/download/17-crac%2B3/openjdk-17-crac+3_linux-x64.tar.gz" $JAVA_HOME/openjdk.tar.gz
RUN tar --extract --file $JAVA_HOME/openjdk.tar.gz --directory "$JAVA_HOME" --strip-components 1; rm $JAVA_HOME/openjdk.tar.gz;

RUN mkdir -p /opt/crac-files

COPY build/libs/crac6-17.0.0.jar /opt/app/crac6-17.0.0.jar

#CMD ["docker exec -it -u root crac6 java -XX:CRaCCheckpointTo=/opt/crac-files -jar /opt/app/crac6-17.0.0.jar"]

#CMD ["/bin/bash"]

#CMD ["docker run -it --privileged --rm --name crac6 crac6"]

#CMD ["docker exec -it -u root crac6 /bin/bash"]

