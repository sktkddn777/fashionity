package com.infinity.fashionity.follows.dto;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

public class CheckDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private Boolean isFollowing;
    }
}
