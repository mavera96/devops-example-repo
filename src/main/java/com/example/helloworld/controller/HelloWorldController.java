package com.example.helloworld.controller;

import com.example.helloworld.config.AppConfig.EnvironmentInfo;
import com.example.helloworld.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class HelloWorldController {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    private final GreetingService greetingService;
    private final Optional<String> profileMessage;

    @Autowired
    public HelloWorldController(GreetingService greetingService, 
                               @Autowired(required = false) String profileMessage) {
        this.greetingService = greetingService;
        this.profileMessage = Optional.ofNullable(profileMessage);
    }

    @GetMapping("/hello")
    public ResponseEntity<Map<String, Object>> hello(@RequestParam(required = false) String name) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", greetingService.getGreeting(name));
        response.put("profile", activeProfile);
        profileMessage.ifPresent(msg -> response.put("profileMessage", msg));
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/environment")
    public ResponseEntity<Map<String, Object>> getEnvironment() {
        EnvironmentInfo envInfo = greetingService.getEnvironmentInfo();
        
        Map<String, Object> response = new HashMap<>();
        response.put("environment", envInfo.environment());
        response.put("debugEnabled", envInfo.debugEnabled());
        response.put("activeSpringProfile", activeProfile);
        response.put("maxConnections", greetingService.getMaxConnections());
        profileMessage.ifPresent(msg -> response.put("profileSpecificMessage", msg));
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("profile", activeProfile);
        
        return ResponseEntity.ok(response);
    }
}
