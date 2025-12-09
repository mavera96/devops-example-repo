package com.example.helloworld.controller;

import com.example.helloworld.service.GreetingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HelloWorldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GreetingService greetingService;

    @Test
    @DisplayName("GET /api/hello should return greeting with default name")
    void testHelloEndpointWithoutName() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message", containsString("Guest")))
                .andExpect(jsonPath("$.profile").value("test"))
                .andExpect(jsonPath("$.profileMessage").value("Test Environment - Integration Testing"));
    }

    @Test
    @DisplayName("GET /api/hello?name=John should return greeting with provided name")
    void testHelloEndpointWithName() throws Exception {
        mockMvc.perform(get("/api/hello").param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message", containsString("John")))
                .andExpect(jsonPath("$.profile").value("test"));
    }

    @Test
    @DisplayName("GET /api/environment should return environment information")
    void testEnvironmentEndpoint() throws Exception {
        mockMvc.perform(get("/api/environment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.environment").value("testing"))
                .andExpect(jsonPath("$.debugEnabled").value(true))
                .andExpect(jsonPath("$.activeSpringProfile").value("test"))
                .andExpect(jsonPath("$.maxConnections").value(75))
                .andExpect(jsonPath("$.profileSpecificMessage").value("Test Environment - Integration Testing"));
    }

    @Test
    @DisplayName("GET /api/health should return UP status")
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.profile").value("test"));
    }

    @Test
    @DisplayName("GET /api/hello with empty name should return Guest")
    void testHelloEndpointWithEmptyName() throws Exception {
        mockMvc.perform(get("/api/hello").param("name", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Guest")));
    }
}
