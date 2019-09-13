FROM openjdk:8

# COPY ARCHIEVES TO RANGER DIRECTORY
COPY . /opt/ranger-app
WORKDIR /opt/ranger-app

RUN tar -xzvf ranger-examples-0.1-ALPHA-webapp.tar.gz

RUN tar -xzvf ranger-examples-0.1-ALPHA-httpservice.tar.gz

CMD ["sh", "./run-webapp.sh"]