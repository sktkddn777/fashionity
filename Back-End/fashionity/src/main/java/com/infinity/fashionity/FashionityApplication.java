package com.infinity.fashionity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // JPA가 Entity 변경 감지를 허용함
@SpringBootApplication
public class FashionityApplication {

    public static void main(String[] args) {
        SpringApplication.run(FashionityApplication.class, args);
    }

}
