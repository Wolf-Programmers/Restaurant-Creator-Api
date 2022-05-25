#FROM openjdk:15.0.2
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} restaurant_api.jar
#ADD out/artifacts/restaurant_api_jar/restaurant_api.jar restaurant_api.jar
#ENTRYPOINT ["java","-jar","restaurant_api.jar"]
#
#CMD java -cp src com.example.restaurantapi.RestaurantApiApplication
FROM openjdk:15.0.2
VOLUME /tmp
COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
