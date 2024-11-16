#######################################
# Build Stage
#######################################
FROM amazoncorretto:21 AS build
RUN mkdir /app-src
COPY . /app-src
WORKDIR /app-src

RUN ./gradlew bootJar

#######################################
# Production Stage
#######################################
FROM alpine:3.20.3

RUN apk update \
    && apk upgrade \
    && apk add openjdk21-jre-headless

RUN addgroup -S appgroup && adduser -S -s /bin/false -G appgroup appuser

RUN mkdir /app && chown -R appuser:appgroup /app
COPY --from=build /app-src/build/libs/portfolio-ms-0.0.1-SNAPSHOT.jar /app/portfolio-ms-application.jar
WORKDIR /app

USER appuser

EXPOSE 8080

HEALTHCHECK CMD wget -qO- http://localhost:8080/actuator/health | grep "UP" || exit 1

CMD ["java", "-jar", "portfolio-ms-application.jar"]
