# Этап сборки
FROM eclipse-temurin:21-jdk as builder
WORKDIR /app

ARG MODULE
COPY gradle ./gradle
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .
RUN chmod +x gradlew

COPY common-lib/build.gradle common-lib/
COPY common-lib/src common-lib/src

COPY ${MODULE}/build.gradle ${MODULE}/
COPY ${MODULE}/src ${MODULE}/src

RUN ./gradlew :${MODULE}:bootJar --no-daemon -x test

# Финальный образ
FROM eclipse-temurin:21-jre
ARG MODULE
WORKDIR /app
COPY --from=builder /app/${MODULE}/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
