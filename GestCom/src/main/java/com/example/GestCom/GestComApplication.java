package com.example.GestCom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.gestion.productorder.entity")
@EnableJpaRepositories(basePackages = "com.gestion.productorder.repository")
@ComponentScan(basePackages = {"com.gestion.productorder", "com.example.GestCom"})
public class GestComApplication {
    public static void main(String[] args) {
        SpringApplication.run(GestComApplication.class, args);
    }
}