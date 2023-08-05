package com.infinity.fashionity.auth.dto;

import lombok.*;

public class LogoutDTO {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @ToString
    public static class Response {
        private boolean success;
    }
}
