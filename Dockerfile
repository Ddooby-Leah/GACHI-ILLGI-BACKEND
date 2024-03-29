FROM adoptopenjdk/openjdk11

VOLUME /tmp

ARG JAR_FILE=build/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java",  "-jar", "-Dspring.profiles.active=local", "-Duser.timezone=Asia/Seoul","/app.jar"]