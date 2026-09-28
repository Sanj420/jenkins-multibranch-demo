package demo_app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Jenkins Multibranch POC!";
    }

    @GetMapping("/health")
    public String health() {
        return "UP";
    }
}
