package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/// @SpringBootApplication activeaza multe functionalitati spring automat
@SpringBootApplication
public class WorldDatabaseApp {
    ///porneste aplicatia Spring Boot
    /// incarca toate componentele (@RestController, @Service, etc).
    public static void main(String[] args) {
        SpringApplication.run(WorldDatabaseApp.class, args);
    }
}
