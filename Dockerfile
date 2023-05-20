FROM openjdk:18.0.2.1-jdk

ADD target/livrariacomercio-0.0.1-SNAPSHOT.jar livrariacomercio-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/livrariacomercio-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080








