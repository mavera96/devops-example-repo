package com.example.helloworld.service;

import com.example.helloworld.config.AppConfig.EnvironmentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GreetingService {

    @Value("${app.greeting.message:Hello World}")
    private String greetingMessage;

    @Value("${app.max.connections:100}")
    private int maxConnections;

    private final EnvironmentInfo environmentInfo;

    @Autowired
    public GreetingService(EnvironmentInfo environmentInfo) {
        this.environmentInfo = environmentInfo;
    }

    public String getGreeting(String name) {
        String formattedName = (name != null && !name.isBlank()) ? name : "Guest";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        return String.format("%s, %s! [Environment: %s, Max Connections: %d, Timestamp: %s]",
                greetingMessage, formattedName, environmentInfo.environment(), maxConnections, timestamp);
    }

    public EnvironmentInfo getEnvironmentInfo() {
        return environmentInfo;
    }

    public int getMaxConnections() {
        return maxConnections;
    }
}
