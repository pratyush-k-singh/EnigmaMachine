package com.enigma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.enigma.persistence.entity")
@EnableJpaRepositories("com.enigma.persistence.repository")
public class EnigmaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnigmaApplication.class, args);
    }
}