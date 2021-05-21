FROM openjdk:16
ADD target/pawnshop.jar pawnshop.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "pawnshop.jar"]