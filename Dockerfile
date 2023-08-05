FROM azul/zulu-openjdk-alpine:11.0.20

WORKDIR /app

COPY ./build/libs/prom-path-gateway-0.0.1-SNAPSHOT.jar .

EXPOSE 8082

ADD ./build/libs/prom-path-gateway-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
