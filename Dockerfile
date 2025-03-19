FROM gradle:8.8-jdk21-alpine AS builder
WORKDIR /gradle

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN gradle clean bootJar

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /gradle/build/libs/app.jar app.jar

ENV TZ=Asia/Seoul
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
