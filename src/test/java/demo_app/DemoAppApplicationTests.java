package demo_app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DemoAppApplicationTests {

    @Test
    void helloTest() {
        assertEquals(
            "Hello from Jenkins Multibranch POC!",
            "Hello from Jenkins Multibranch POC!"
        );
    }

    @Test
    void healthTest() {
        assertEquals("UP", "UP");
    }
}
