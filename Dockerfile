FROM gradle:9.3-jdk25-alpine AS builder
WORKDIR /gradle

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN gradle clean bootJar

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

COPY --from=builder /gradle/build/libs/app.jar app.jar

ENV TZ=Asia/Seoul
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
