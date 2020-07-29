# sage-siparcs-metadata-search

The metadata search is a [Spring MVC web framework](https://spring.io/projects/spring-framework) application that queries ISO XML metadata from a Solr index.

## Before Running

### Solr

For more information on how to set up your Solr instance follow this Github [repository](https://github.com/NCAR/sage-solr-vagrant.git).

### Java

You need to have [OpenJDK](https://adoptopenjdk.net/index.html) 8 or later installed.

### Vagrant

For more information on how to set up your vagrant follow this GitHub [repository](https://github.com/NCAR/sage-solr-vagrant.git).

### Docker Swarm
```
docker swarm init
docker stack deploy -c <docker-compose.yaml> <custom-stack-name>
```
### Docker Config 
```
docker config create <application.properties.20200716> <application.properties>
```
### Docker Secret 
```
docker secret create <application.properties.20200717> <application.properties>
```
### Dockerfile

The Dockerfile contains the version of Java,and it should copy the self-executable JAR file to the work directory.

Dockerfile Example:
```
FROM openjdk:8-jre-alpine

COPY <path-to-JAR-file> <path-to-be-copied-in-docker>

WORKDIR /usr/local

CMD ["/usr/bin/java", "-jar", "-Dspring.config.additional-location=/usr/local/properties/application.properties", "<JAR-file>"]
```
### Docker Compose File

The Docker compose file contains the details of the services that will be used to create a stack and docker config or docker secret if used. 

docker-compose.yaml Example:
```
version: '3.8'

services:
  solr:
    image: solr:8.5.2   ##solr-version
    volumes:
      - "<path-to-metadata-config-folder>:/var/solr"
    ports:
      - "8983:8983"     ##solr-ports
    command:
      - solr-precreate
      - metadata.       ##core-name
    
  siparcs_search:
    image: sage-siparcs-search  ##image-name
    depends_on:
      - solr
    ports:
      - "8080:8080"  ##application-port
    configs:
      - source: <docker-config-file-name>
        target: /usr/local/properties/application.properties
configs:
    <docker-config-file-name>:
      external: true
```
### Properties File

The application properties file (src/main/resources/application.properties) contains the spring.data.solr.host,and spring thymeleaf configuration.

Properties File Examples:
```
spring.thymeleaf.cache=false
spring.thymeleaf.enabled=true
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.data.solr.host=
server.error.whitelabel.enabled=false
application.version=@application.version@
```
## To Build and Run Metadata Search
In order to run a self-executable JAR file you will need to:
- add the maven plugin that can create executable archives. You can find the build plugin [here](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/maven-plugin/reference/html/#repackage)
- You need to have maven installed and run
  ```
  clean install spring-boot:repackage
  ```
- Now, you can run the jar file separately from command line in the target folder with:
  ```
  java -Dspring.config.location=file://<path to properties file> -jar <jar file to be executed>
  ```

