FROM eclipse-temurin:17-jre-alpine
LABEL authors="igvol"

WORKDIR /app

COPY ./target/forum-service-0.0.1-SNAPSHOT.jar ./forum-serivce.jar

ENV MONGODB_URI=mongodb+srv://igo:QPh2GbpyZdj2UKAy@cohort34.p5p5kfm.mongodb.net/cohort34db?retryWrites'='true&w'='majority&appName'='cohort34

CMD ["java", "-jar", "/app/forum-serivce.jar"]