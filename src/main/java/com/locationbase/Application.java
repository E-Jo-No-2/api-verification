package com.locationbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.locationbase.client", "com.locationbase.controller",
        "com.locationbase.service", "com.locationbase.Domain", "com.locationbase.dto"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
