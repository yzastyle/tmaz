FROM amazoncorretto:21.0.5

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test

RUN mkdir -p /tmazapp
COPY build/libs/*.jar /tmazapp/app.jar
COPY ./entrypoint.sh /tmazapp/entrypoint.sh
RUN chmod +x /tmazapp/entrypoint.sh
CMD ["/tmazapp/entrypoint.sh"]