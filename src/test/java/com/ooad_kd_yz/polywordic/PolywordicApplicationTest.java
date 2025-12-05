package com.ooad_kd_yz.polywordic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PolywordicApplicationTest {

    @Test
    void contextLoads() {
        // Verifies that the Spring application context starts successfully
        assertTrue(true);
    }

    @Test
    void testMainMethod() {
        // Calls the main() method directly to ensure full line coverage
        assertDoesNotThrow(() -> PolywordicApplication.main(new String[]{}));
    }

    @Test
    void testSpringApplicationInitialization() {
        // Ensures that SpringApplication can be instantiated with this class
        SpringApplication app = new SpringApplication(PolywordicApplication.class);
        assertNotNull(app);
    }
}
