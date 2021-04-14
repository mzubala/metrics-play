FROM amazoncorretto:11-alpine-jdk
COPY build/libs/metrics-play-1.0-SNAPSHOT.jar metrics-play-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/metrics-play-1.0-SNAPSHOT.jar"]