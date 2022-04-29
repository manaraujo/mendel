FROM openjdk:17
ADD target/mendel-0.0.1-SNAPSHOT.jar mendel.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar" , "mendel.jar"]