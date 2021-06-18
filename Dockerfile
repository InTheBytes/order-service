FROM openjdk:17
 ADD target/orderservice-0.0.1-SNAPSHOT.jar OrderService.jar
 EXPOSE 8080
ENTRYPOINT ["java","-Dspring.datasource.hikari.maximum-pool-size=1","-jar","OrderService.jar"]
