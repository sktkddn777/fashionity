package com.infinity.fashionity.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Tokens {

    String accessToken;
    String refreshToken;
}
