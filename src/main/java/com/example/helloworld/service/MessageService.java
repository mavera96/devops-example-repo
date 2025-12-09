package com.example.helloworld.service;

import com.example.helloworld.config.ProfileConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Value("${app.greeting}")
    private String greeting;

    private final ProfileConfig profileConfig;

    public MessageService(ProfileConfig profileConfig) {
        this.profileConfig = profileConfig;
    }

    public String getMessage() {
        // Combines Spring Profile greeting with Java Profile specific logic
        String javaProfileInfo = profileConfig.isJavaProfileActive()
                ? " (Java Profile Active: " + profileConfig.getJavaProfileName() + ")"
                : " (Java Profile Inactive)";

        return greeting + javaProfileInfo;
    }
}