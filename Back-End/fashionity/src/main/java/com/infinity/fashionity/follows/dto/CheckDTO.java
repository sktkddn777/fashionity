package com.infinity.fashionity.follows.dto;

import lombok.*;

public class CheckDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private Boolean isFollowing;
    }
}
