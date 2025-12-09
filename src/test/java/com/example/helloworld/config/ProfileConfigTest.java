package com.example.helloworld.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileConfigTest {

    private static final String JAVA_PROFILE_KEY = "java.profiles.active";
    private String originalSystemProperty = null;

    @BeforeEach
    void setUp() {
        // Store original value if it exists
        originalSystemProperty = System.getProperty(JAVA_PROFILE_KEY);
    }

    @AfterEach
    void tearDown() {
        // Restore original system property
        if (originalSystemProperty != null) {
            System.setProperty(JAVA_PROFILE_KEY, originalSystemProperty);
        } else {
            System.clearProperty(JAVA_PROFILE_KEY);
        }
    }

    @Test
    void profileConfig_whenSystemPropertySet_isJavaProfileActive() {
        // Arrange: Set a custom Java Profile System Property
        String customProfile = "CI-Build-Profile";
        System.setProperty(JAVA_PROFILE_KEY, customProfile);

        // Act
        ProfileConfig config = new ProfileConfig(System.getProperty(JAVA_PROFILE_KEY));

        // Assert
        assertTrue(config.isJavaProfileActive());
        assertEquals(customProfile, config.getJavaProfileName());
    }

    @Test
    void profileConfig_whenSystemPropertyNotSet_isJavaProfileInactive() {
        // Arrange: Clear the System Property
        System.clearProperty(JAVA_PROFILE_KEY);

        // Act: The constructor will use the default value defined in @Value (which is
        // 'default-java-profile')
        ProfileConfig config = new ProfileConfig("default-java-profile");

        // Assert
        assertFalse(config.isJavaProfileActive());
        assertEquals("default-java-profile", config.getJavaProfileName());
    }
}