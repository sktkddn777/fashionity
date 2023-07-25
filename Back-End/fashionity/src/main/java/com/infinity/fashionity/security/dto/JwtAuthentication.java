package com.infinity.fashionity.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class JwtAuthentication {

    private Long seq;

    @Override
    public String toString() {
        return "member ID : " + seq;
    }
}

