FROM openjdk:17-jdk-alpine
ADD . /src
WORKDIR /src
RUN apk add --no-cache maven
RUN mvn clean package -DskipTests=true
EXPOSE 8080
ENTRYPOINT ["java","-jar","target/translator-0.0.1-SNAPSHOT.jar"]