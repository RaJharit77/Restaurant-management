package com.restaurant.springBootApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.restaurant.springBootApplication", "com.restaurant"})
public class RestaurantManagementApp {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantManagementApp.class, args);
    }
}