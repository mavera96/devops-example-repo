package com.example.helloworld.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProfileConfig {

    private static final String JAVA_PROFILE_KEY = "java.profiles.active";

    private final String activeJavaProfile;

    public ProfileConfig(@Value("${" + JAVA_PROFILE_KEY + ":default-java-profile}") String activeJavaProfile) {
        // The Java Profile is read directly from the System Property defined by the JVM
        // arguments.
        // Spring's @Value is used to inject the system property.
        this.activeJavaProfile = activeJavaProfile;
    }

    public boolean isJavaProfileActive() {
        return !activeJavaProfile.equals("default-java-profile");
    }

    public String getJavaProfileName() {
        return activeJavaProfile;
    }
}