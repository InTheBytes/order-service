FROM openjdk:17
 ADD target/orderservice-0.0.1-SNAPSHOT.jar OrderService.jar
 EXPOSE 8080
ENTRYPOINT ["java","-jar","OrderService.jar"]