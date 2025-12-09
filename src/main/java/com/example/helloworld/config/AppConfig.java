package com.example.helloworld.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AppConfig {

    @Value("${app.environment}")
    private String environment;

    @Value("${app.feature.debug:false}")
    private boolean debugEnabled;

    @Bean
    @Profile("dev")
    public String developmentMessage() {
        return "Development Environment - Debug Mode Enabled";
    }

    @Bean
    @Profile("test")
    public String testMessage() {
        return "Test Environment - Integration Testing";
    }

    @Bean
    @Profile("prod")
    public String productionMessage() {
        return "Production Environment - Optimized for Performance";
    }

    @Bean
    public EnvironmentInfo environmentInfo() {
        return new EnvironmentInfo(environment, debugEnabled);
    }

    public record EnvironmentInfo(String environment, boolean debugEnabled) {}
}
