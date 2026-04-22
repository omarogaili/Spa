package com.Spa.spa;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import com.Spa.spa.Services.OrderServices;
import com.Spa.spa.Services.UserServices;
import com.Spa.spa.models.Order;
import com.Spa.spa.models.Package;
import com.Spa.spa.models.User;

@SpringBootApplication
public class SpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpaApplication.class, args);
    }

    @Bean
    CommandLineRunner seedData(
            MongoOperations mongoOperations,
            OrderServices orderServices,
            UserServices userServices) {
        return args -> {

            if (mongoOperations.count(new Query(), User.class) == 0) {
                userServices.createUser(new User(null, "admin", "admin123", "ADMIN"));
                userServices.createUser(new User(null, "emma", "emma123", "EMPLOYEE"));
                userServices.createUser(new User(null, "leo", "leo123", "EMPLOYEE"));
                userServices.createUser(new User(null, "sara", "sara123", "EMPLOYEE"));
            }

            Package relax = null;
            Package deluxe = null;

            if (mongoOperations.count(new Query(), Package.class) == 0) {
                relax = new Package(null, "Värm", "Sauna, pool and massage", 500);
                relax.setDiscountPercentage(0);

                deluxe = new Package(null, "Kall", "Full spa day with treatment", 499);
                deluxe.setDiscountPercentage(10);

                mongoOperations.save(relax);
                mongoOperations.save(deluxe);
            } else {
                relax = mongoOperations.findAll(Package.class).stream().findFirst().orElse(null);
                deluxe = mongoOperations.findAll(Package.class).stream().skip(1).findFirst().orElse(relax);
            }

            if (mongoOperations.count(new Query(), Order.class) == 0 && relax != null && deluxe != null) {
                Order order1 = new Order(
                        null,
                        "Anna Andersson",
                        2,
                        350,
                        LocalDate.now(),
                        "0701112233",
                        "anna@test.se",
                        null,
                        null
                );

                Order order2 = new Order(
                        null,
                        "Erik Svensson",
                        4,
                        350,
                        LocalDate.now(),
                        "0702223344",
                        "erik@test.se",
                        null,
                        null
                );

                Order order3 = new Order(
                        null,
                        "Lina Karlsson",
                        1,
                        350,
                        LocalDate.now(),
                        "0703334455",
                        "lina@test.se",
                        null,
                        null
                );

                orderServices.addOrder(relax.getId(), order1);
                orderServices.addOrder(deluxe.getId(), order2);
                orderServices.addOrder(relax.getId(), order3);
            }
        };
    }
}