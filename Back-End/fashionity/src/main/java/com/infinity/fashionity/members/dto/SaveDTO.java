package com.infinity.fashionity.members.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

public class SaveDTO {//세이브 할 떄 필요한 request, response
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @Length(min = 5, max = 20)
        @NotBlank
        private String id; //아이디값 (5~20)

        @Length(max = 60)
        @NotBlank
        private String password; //비밀번호값

        @Length(max = 13)
        @NotBlank
        private String nickname;

        @Email
        @NotBlank
        private String email;

        private Boolean sns = false; // sns 여부
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private String id;
        private String nickname;
    }
}
