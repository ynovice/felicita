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

RUN ["chmod", "+x", "docker-entrypoint.sh"]
ENTRYPOINT ["./docker-entrypoint.sh"]