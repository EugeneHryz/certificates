package com.epam.esm.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.epam.esm.repository.config",
        "com.epam.esm.web.config", "com.epam.esm.service"})
public class CertificatesApp {

    public static void main(String[] args) {
        SpringApplication.run(CertificatesApp.class, args);
    }
}
