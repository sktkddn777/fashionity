package com.infinity.fashionity.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // JPA가 Entity 변경 감지를 허용함
public class JpaConfig {
}
