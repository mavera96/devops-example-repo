package com.example.helloworld.service;

import com.example.helloworld.config.AppConfig.EnvironmentInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class GreetingServiceTest {

    @Autowired
    private GreetingService greetingService;

    @Test
    @DisplayName("Should return greeting with provided name")
    void testGetGreetingWithName() {
        String result = greetingService.getGreeting("John");
        
        assertThat(result).isNotNull();
        assertThat(result).contains("John");
        assertThat(result).contains("Hello from Test Environment");
    }

    @Test
    @DisplayName("Should return greeting with Guest when name is null")
    void testGetGreetingWithNullName() {
        String result = greetingService.getGreeting(null);
        
        assertThat(result).isNotNull();
        assertThat(result).contains("Guest");
    }

    @Test
    @DisplayName("Should return greeting with Guest when name is blank")
    void testGetGreetingWithBlankName() {
        String result = greetingService.getGreeting("   ");
        
        assertThat(result).isNotNull();
        assertThat(result).contains("Guest");
    }

    @Test
    @DisplayName("Should return correct environment info from test profile")
    void testGetEnvironmentInfo() {
        EnvironmentInfo envInfo = greetingService.getEnvironmentInfo();
        
        assertThat(envInfo).isNotNull();
        assertThat(envInfo.environment()).isEqualTo("testing");
        assertThat(envInfo.debugEnabled()).isTrue();
    }

    @Test
    @DisplayName("Should return max connections from test profile")
    void testGetMaxConnections() {
        int maxConnections = greetingService.getMaxConnections();
        
        assertThat(maxConnections).isEqualTo(75);
    }

    @Test
    @DisplayName("Greeting should contain timestamp")
    void testGreetingContainsTimestamp() {
        String result = greetingService.getGreeting("Alice");
        
        assertThat(result).contains("Timestamp:");
    }
}
