# Spring Boot with Docker

<br>

In this guide, we're going to follow the steps to  bould a Docker image for running a Spring Boot application. We are not focusing on implementing the API, you can bring your own 
and make only a few changes to achieve the same result.

## Most classical windows installation DOCKER (really classic)

The first step is to google or go to the next [link]( https://www.docker.com/products/docker-desktop). Select your OS and download the Docker Desktop setup program. Double click on it and accept everything, the default configuration is truly reliable (you can always stop and custom some aspects if needed).

![docker download](https://github.com/CristopherLodbrok117/api-with-docker/blob/edb65480f97fc0c16efa9acbb886ccad030150b9/assets/screenshots/00%20-%20install%20docker.png)

Open Docker desktop and explore its different elements. Here wou will se all the images, containers, networks, volumes that we build.

![docker desktop](https://github.com/CristopherLodbrok117/api-with-docker/blob/edb65480f97fc0c16efa9acbb886ccad030150b9/assets/screenshots/01%20-%20docker%20desktop.png)

Once you create your images docker desktop allows gives you control of every detail.

![image detail](https://github.com/CristopherLodbrok117/api-with-docker/blob/edb65480f97fc0c16efa9acbb886ccad030150b9/assets/screenshots/03%20-%20docker%20image%20detail.png)

<br>

## Application properties

These might be the only file you will change on your app. We'll use environment variables insted of hardcoded text, don't worry, we define them later. Also make sure to define ddl-auto with `update` value. Show-sql is totally optional but actually helpful, as always. And very important: define a server port and address with `0.0.0.0`
This last configuration due to when a port is bound to `0.0.0.0` it means that it can be accessed via all the IP addresses on all interfaces on the machine, including 127.0. 0.1

```java
spring.application.name=docker-api

spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=${DATABASE_URL} #environment variables
spring.datasource.username=${DATABASE_USER}
spring.datasource.password=${DATABASE_PASSWORD}

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql: true

server.port=8081
server.address=0.0.0.0
```

<br>

## Mysql Image

The next step is to get our first image. Our API uses mysql database and so we're creating a container for this service. Go to [Docker Hub] and search mysql as follows, copy the command on the right side:

![dockerhub mysql](https://github.com/CristopherLodbrok117/api-with-docker/blob/5326087cdc396eed731c2c3a2190d21767695ad1/assets/screenshots/06%20-%20docker%20hub%20mysql%20image.png)

For the next steps keep open Docker Desktop. In your IDE terminal execute the command you copied and see the result (`docker pull mysql`). If we don't define any version docker downloads the last one by default. Execute  `docker images` in the terminal or go to Docker Desktop to confirm that the image downloaded well.

![mysql donwload](https://github.com/CristopherLodbrok117/api-with-docker/blob/5326087cdc396eed731c2c3a2190d21767695ad1/assets/screenshots/11%20-%20docker%20download%20mysql%20image.png)

<br>

## Jar file (optional)

Before building our app image, we can optionally create our app jar file. The way we configure our dockerfile in this guide will automate this task (for that we only need to know the jar file name), but it's ok to know different ways to achieve the same result

1. In your IDE (I'm using Intellij Idea) open the terminal and write the next command `.\mvnw clean`. Clean Plugin is used when you want to remove files generated at build-time in a project's directory

![maven clean](https://github.com/CristopherLodbrok117/api-with-docker/blob/5326087cdc396eed731c2c3a2190d21767695ad1/assets/screenshots/12%20-%20generate%20jar%201.png)

2. To create the jar file execute `.\mvnw install -DskipTests` with this parameter we skip running the tests

![maven install](https://github.com/CristopherLodbrok117/api-with-docker/blob/5326087cdc396eed731c2c3a2190d21767695ad1/assets/screenshots/12%20-%20generate%20jar%202.png)

3. The build is completed

![jar file](https://github.com/CristopherLodbrok117/api-with-docker/blob/5326087cdc396eed731c2c3a2190d21767695ad1/assets/screenshots/12%20-%20generate%20jar%203.png)

<br>

## App image (Dockerfile)

This will require a few more steps. Please, follow the same order to avoid some common mistakes. In docker we can build images using another image as a base, the reason is that those images already have all the dependencies that the service needs to run succesfully (OS for example, JDK, etc). Thus let's download a base image

1. In dockerhub search [eclipse-temurin](https://hub.docker.com/_/eclipse-temurin)

![](https://github.com/CristopherLodbrok117/api-with-docker/blob/main/assets/screenshots/07%20-%20docker%20hub%20eclipse.png)

2. Select it and go to the tags section. Use the bar to search your java version

![java version](https://github.com/CristopherLodbrok117/api-with-docker/blob/5326087cdc396eed731c2c3a2190d21767695ad1/assets/screenshots/08%20-%20eclipse%20tags.png)

3. Scroll down untill you find an image with the suffix "jdk". Don't copy the entire command, we only need the image name and version

![java 17](https://github.com/CristopherLodbrok117/api-with-docker/blob/5326087cdc396eed731c2c3a2190d21767695ad1/assets/screenshots/09%20-%20eclipse%20for%20java%2017.png)

4. Create a file without extension named "Dockerfile". Please write the next lines (# means comment line)

<br>

```java
# Here you paste the base image name and version
FROM eclipse-temurin:17.0.13_11-jdk

# This tells docker that /root is the work directory
WORKDIR /root

# Inform docker that this container listens for traffic on the specified port 8081
EXPOSE 8081

# Copy the jar file from our PC to the container (and rename it)
COPY target/docker-api-0.0.1-SNAPSHOT.jar app-videogames.jar

# Copy our pom.xml and maven to docker (spring boot includes an embedded maven)
COPY ./pom.xml /root
COPY ./.mvn /root/.mvn
COPY ./mvnw /root

# docker will use the maven we copied to download dependencies
RUN ./mvnw dependency:go-offline

# copy our project code
COPY ./src /root/src

# Remember that formerly we did this manually? well, docker will make it for you with this line
RUN ./mvnw clean install -DskipTests

# Start the application once the container is up
ENTRYPOINT ["java","-jar","app-videogames.jar"]

```

<br>

2. 

![]()

2. 

![]()

2. 

![]()

2. 

![]()


## File.jar

