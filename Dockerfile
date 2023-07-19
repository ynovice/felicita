FROM amazoncorretto:17-alpine-jdk
VOLUME /tmp

# install bash (needed to run Liquibase)
RUN apk update && apk add bash

# install Liquibase
WORKDIR /liquibase
RUN wget https://github.com/liquibase/liquibase/releases/download/v4.23.0/liquibase-4.23.0.zip
RUN unzip liquibase-4.23.0.zip
ENV PATH="$PATH:/liquibase"

WORKDIR /app
COPY . .
RUN apk add --no-cache maven && mvn clean package -DskipTests
#CMD liquibase --changelog-file=src/main/resources/db/changelog/db.changelog-master.xml --url=jdbc:postgresql://felicita-postgres:5432/felicita --username=felicita --password=felicita --duplicate-file-mode=WARN update
#CMD ["liquibase", "--changelog-file=src/main/resources/db/changelog/db.changelog-master.xml", "--url=jdbc:postgresql://felicita-postgres:5432/felicita", "--username=felicita", "--password=felicita", "--duplicate-file-mode=WARN", "update"]
#ENTRYPOINT ["liquibase", "--changelog-file=src/main/resources/db/changelog/db.changelog-master.xml", "--url=jdbc:postgresql://felicita-postgres:5432/felicita", "--username=felicita", "--password=felicita", "--duplicate-file-mode=WARN", "update", "&&", "java", "-jar", "target/felicita-backend.jar"]
#RUN ["chmod", "+x", "docker-entrypoint.sh"]
ENTRYPOINT ["./docker-entrypoint.sh"]