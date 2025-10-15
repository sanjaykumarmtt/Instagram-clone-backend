FROM eclipse-temurin:21-jdk as builder

WORKDIR /build
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests
RUN ls -al /build/target/
FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=builder /build/target/Instagram_clone-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT [ "java", "-jar", "app.jar" ]