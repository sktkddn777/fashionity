package com.infinity.fashionity.members.dto;

import lombok.*;
public class SaveDTO {//세이브 할 떄 필요한 request, response
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String id;//아이디값 (5~20)
        private String password;//비밀번호값
        private String nickname;
        private String email;
        private Boolean sns = false; // sns 여부


        //입력시 좌우 공백을 제거해주기 위함
        public void setId(String id) {
            if(id != null) {
                this.id = id.trim();
            }
        }
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private String id;
        private String nickname;
    }
}
