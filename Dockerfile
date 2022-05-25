#FROM openjdk:15.0.2
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} restaurant_api.jar
#ADD out/artifacts/restaurant_api_jar/restaurant_api.jar restaurant_api.jar
#ENTRYPOINT ["java","-jar","restaurant_api.jar"]
#
#CMD java -cp src com.example.restaurantapi.RestaurantApiApplication
# FROM openjdk:15.0.2
# VOLUME /tmp
# COPY target/*.jar app.jar

# ENTRYPOINT ["java","-jar","/app.jar"]


# For Java 8, try this
# FROM openjdk:8-jdk-alpine

# For Java 11, try this
FROM openjdk:15.0.2

# Refer to Maven build -> finalName
ARG JAR_FILE=target/restaurant_api-0.0.1-SNAPSHOT.jar


# cp target/spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} app.jar

# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]
