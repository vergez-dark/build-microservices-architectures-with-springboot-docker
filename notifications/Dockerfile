FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /build

COPY ./pom.xml ./
COPY ./src ./src

RUN mvn clean package -DskipTests=true




FROM eclipse-temurin:21

WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8083

CMD ["java", "-jar", "app.jar"]
