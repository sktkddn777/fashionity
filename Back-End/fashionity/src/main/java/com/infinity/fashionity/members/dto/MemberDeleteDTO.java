package com.infinity.fashionity.members.dto;

import lombok.*;

public class MemberDeleteDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private Boolean success;
    }

}
