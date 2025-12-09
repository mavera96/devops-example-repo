package com.example.helloworld;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class HelloWorldApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring context loads successfully
        assertThat(true).isTrue();
    }
}
