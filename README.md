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

5. Now we are ready to build our image. Run this command in the terminal, you can define your image name/id replacing the parenthesis text: `docker build -t "app-videogames-image" .`


![app image build](https://github.com/CristopherLodbrok117/api-with-docker/blob/ff8319006744467cf270e728306f4b46312ec379/assets/screenshots/13%20-%20build%20docker%20image.png)

6. Run `docker images` or go to docker desktop to see both images

![current images](https://github.com/CristopherLodbrok117/api-with-docker/blob/ff8319006744467cf270e728306f4b46312ec379/assets/screenshots/13%20-%20current%20images.png)

<br>

## Docker Compose

The next step is to configure the docker-compose.yml file. Docker Compose is a tool for defining and running multi-container applications. Compose simplifies the control of our entire application, making it easy to manage services, networks, and volumes in a single, comprehensible YAML configuration file. You can consult [Docker Compose documentation](https://docs.docker.com/compose/) for more information

1. Create a file called docker-compose.yml in the project directory 

```python
# The last version doesn't require to write it in the top

services:

  docker-api:
    image: app-videogames-image
    container_name: app-videogames-container
    build:
      context: .
      dockerfile: Dockerfile
    depends_on:
      mysql_service:
        condition: service_healthy
    ports:
      - "8081:8081"
    networks:
      - spring-network
    environment:
      DATABASE_URL: jdbc:mysql://mysql_database:3306/docker_db?createDatabaseIfNotExists=true&serverTimezone=UTC
      DATABASE_USER: root
      DATABASE_PASSWORD: 1234

  mysql_service:
    image: mysql:latest
    container_name: mysql_database
    environment:
      MYSQL_ROOT_PASSWORD: 1234
      MYSQL_DATABASE: docker_db
      MYSQL_USER: admin
      MYSQL_PASSWORD: 12345
    ports:
      - "3307:3306"
    networks:
      - spring-network
    healthcheck:
      test: [ "CMD", "mysqladmin", "ping", "-h", "localhost" ]
      interval: 10s
      retries: 10

networks:
  spring-network:
    driver: bridge
```

We are defining two services `docker-api` and `mysql-service`

For `docker-api` service
- Use the app image, and define the container name/id with `app-videogames-container`
- We tell in the context path where the Dockerfile is (root)
- We bind the host port to the container port `8081` to `8081`
- We configure a network `spring-network` with the default driver (bridge). Basic but good enough to enable containers communication. And specify it in every service
- In the environment we define environment variables (here you add those ones we used in the application.properties)
- With `depends_on` we tell Docker that this service will start only when mysql-service is running and is healthy (a healthy service is tested automatically)

For `mysql-service` 
- Use the image we downloaded
- Add the container's name
- The root and database environment variables are required (user and password are optional)
- Add the network
- `Healthcheck` allows Docker to monitorize the status of a service, by defining a test to find out if the service is healthy along the whole execution. It runs every 10 seconds (if it fails the test 10 straight times the service is tagged as unhealthy). It uses `mysqladmin` to connect to `MySQL server`, if the server responds means that it's alive

<br>

## Containers up

Now we can build our containers

1. In the terminal run `docker compose up`

![docker compose up](https://github.com/CristopherLodbrok117/api-with-docker/blob/162020656da61ba22feed23ee35506e8be528975/assets/screenshots/14%20-%20docker%20compose%20up%201.png)

Once they're running you will see all the application logs. You can stop them with `ctrl + c`

![services up](https://github.com/CristopherLodbrok117/api-with-docker/blob/162020656da61ba22feed23ee35506e8be528975/assets/screenshots/14%20-%20docker%20compose%20up%202.png)

<br>

## Requests

AMAZING! you've dockerized your application succesfully. Now let's test it using [Insomnia](https://insomnia.rest/) to customize some requests

1. List videogames

![list all](https://github.com/CristopherLodbrok117/api-with-docker/blob/162020656da61ba22feed23ee35506e8be528975/assets/screenshots/20%20-%20request%20get%20list.png)

3. Find by id

![find by id](https://github.com/CristopherLodbrok117/api-with-docker/blob/162020656da61ba22feed23ee35506e8be528975/assets/screenshots/21-%20request%20find%20by%20id.png)

4. Find by name pattern

![find by pattern](https://github.com/CristopherLodbrok117/api-with-docker/blob/162020656da61ba22feed23ee35506e8be528975/assets/screenshots/22%20-%20find%20by%20name%20pattern.png)

5. Save new

![save videogame](https://github.com/CristopherLodbrok117/api-with-docker/blob/162020656da61ba22feed23ee35506e8be528975/assets/screenshots/23%20-%20save%20new%20video%20game.png)

7.  Go to Docker Desktop > containers. Click in your services and you'll see the requests logs

![docker services logs](https://github.com/CristopherLodbrok117/api-with-docker/blob/162020656da61ba22feed23ee35506e8be528975/assets/screenshots/15%20-%20docker%20container%20logs.png)
