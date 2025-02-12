#
# Build stage
#
FROM maven:3.8.5-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM openjdk:17

ENV LANG C.UTF-8
RUN mkdir /app
EXPOSE 8087
WORKDIR /app
COPY --from=build /home/app/target/frame_extractor_api-0.0.1-SNAPSHOT.jar /app/app.jar
CMD java -jar app.jar $APP_OPTIONS