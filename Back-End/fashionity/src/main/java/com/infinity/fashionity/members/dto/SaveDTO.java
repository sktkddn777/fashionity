package com.infinity.fashionity.members.dto;

import lombok.*;

public class SaveDTO {//세이브 할 떄 필요한 request, response
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        private String id; //아이디값 (6~20)

        private String password; //비밀번호값

        private String nickname;

        private String email;

        @Builder.Default
        private Boolean sns = false; // sns 여부
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private String id;
        private String email;
    }
}
