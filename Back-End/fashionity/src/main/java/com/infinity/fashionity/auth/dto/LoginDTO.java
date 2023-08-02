package com.infinity.fashionity.auth.dto;


import lombok.*;

public class LoginDTO {
    @Builder
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Request{
        private String id;
        private String password;

        public Request(String id,String password){
            setId(id);
            this.password=password;
        }

        public void setId(String id) {
            if(id!=null) {
                this.id = id.trim();
            }
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response{
        private String accessToken;
    }
}
