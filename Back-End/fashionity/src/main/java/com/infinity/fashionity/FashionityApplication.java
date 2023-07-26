package com.infinity.fashionity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class FashionityApplication {

    public static void main(String[] args) {
        SpringApplication.run(FashionityApplication.class, args);
    }

}
