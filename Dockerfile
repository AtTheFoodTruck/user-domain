FROM openjdk:11
ENV APP_HOME=/user/app
WORKDIR $APP_HOME
COPY build/libs/foodtruck-user-0.0.1-SNAPSHOT.jar UserService.jar

CMD ["java", "-jar", "UserService.jar"]
