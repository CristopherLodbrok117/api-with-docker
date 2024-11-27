# Base image
FROM eclipse-temurin:17.0.13_11-jdk

# DEFINIR DIRECTORIO RAIZ DE NUESTRO CONTENEDOR
WORKDIR /root

ARG JAR_FILE=target/docker-api-0.0.1-SNAPSHOT.jar
# Copy changing the the name
#COPY ${JAR_FILE} app-videogames.jar
COPY target/docker-api-0.0.1-SNAPSHOT.jar app-videogames.jar

# INFORMAR EL PUERTO DONDE SE EJECUTA EL CONTENEDOR (INFORMATIVO)
EXPOSE 8081

# COPIAR Y PEGAR ARCHIVOS DENTRO DEL CONTENEDOR
COPY ./pom.xml /root
COPY ./.mvn /root/.mvn
COPY ./mvnw /root

# DESCARGAR LAS DEPENDENCIAS
RUN ./mvnw dependency:go-offline

# COPIAR EL CODIGO FUENTE DENTRO DEL CONTENEDOR
COPY ./src /root/src

# CONSTRUIR NUESTRA APLICACION
RUN ./mvnw clean install -DskipTests

# LEVANTAR NUESTRA APLICACION CUANDO EL CONTENEDOR INICIE
ENTRYPOINT ["java","-jar","app-videogames.jar"]
#ENTRYPOINT ["java","-jar","/root/target/SpringDocker-0.0.1-SNAPSHOT.jar"]


#ENTRYPOINT ["java","-jar","app_videogames.jar"]

# .\mvnw clean install -DskipTests