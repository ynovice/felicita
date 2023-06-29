#FROM eclipse-temurin:17-jdk-alpine
#VOLUME /tmp
#ARG JAR_FILE
#COPY target/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM amazoncorretto:17-alpine-jdk
VOLUME /tmp
WORKDIR /app
COPY . .
RUN apk add --no-cache maven && mvn clean package
ENTRYPOINT ["java", "-jar", "target/felicita-backend.jar"]


#FROM eclipse-temurin:17-jdk-alpine
#VOLUME /tmp
#WORKDIR /app
#COPY . .
#RUN apk add --no-cache maven \
#    && export APP_JAR=$(mvn -q --non-recursive exec:exec -Dexec.executable="echo" -Dexec.args='${project.build.finalName}.jar') \
#    && mvn clean package -DskipTests
#ENV APP_JAR=${APP_JAR}
#ENTRYPOINT ["java","-jar","/app/target/${APP_JAR}"]

#FROM eclipse-temurin:17-jdk-alpine