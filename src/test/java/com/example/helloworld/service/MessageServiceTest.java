package com.example.helloworld.service;

import com.example.helloworld.config.ProfileConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private ProfileConfig profileConfig;

    @InjectMocks
    private MessageService messageService;

    @Test
    void getMessage_withJavaProfileActive() {
        // Set the @Value field using reflection for unit testing
        ReflectionTestUtils.setField(messageService, "greeting", "Test Greeting");

        // Mock the ProfileConfig behavior
        when(profileConfig.isJavaProfileActive()).thenReturn(true);
        when(profileConfig.getJavaProfileName()).thenReturn("test-java");

        String result = messageService.getMessage();

        // Assert the Spring greeting and the Java profile info are present
        assertTrue(result.contains("Test Greeting"));
        assertTrue(result.contains("(Java Profile Active: test-java)"));
    }

    @Test
    void getMessage_withJavaProfileInactive() {
        // Set the @Value field using reflection for unit testing
        ReflectionTestUtils.setField(messageService, "greeting", "Another Greeting");

        // Mock the ProfileConfig behavior
        when(profileConfig.isJavaProfileActive()).thenReturn(false);

        String result = messageService.getMessage();

        // Assert the Spring greeting and the inactive Java profile info are present
        assertTrue(result.contains("Another Greeting"));
        assertTrue(result.contains("(Java Profile Inactive)"));
    }
}