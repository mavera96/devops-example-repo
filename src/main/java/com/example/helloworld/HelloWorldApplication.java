package com.example.helloworld;

import com.example.helloworld.service.MessageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HelloWorldApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, MessageService messageService) {
        return args -> {
            System.out.println("--- Spring Boot Profile & Java Profile Demo ---");
            System.out.println("Application Message: " + messageService.getMessage());
            System.out.println("---------------------------------------------");
        };
    }
}