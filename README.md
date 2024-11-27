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

These might be the only file you will change on your app. We'll use environment variables insted of hardcoded text, don't worry, we define them later. Also make sure to define ddl-auto with `update` value. show-sql is totally optional. And very important: define a server port and address with `0.0.0.0`
This last configuration due to when a port is bound to 0.0. 0.0 it means that it can be accessed via all the IP addresses on all interfaces on the machine, including 127.0. 0.1

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

