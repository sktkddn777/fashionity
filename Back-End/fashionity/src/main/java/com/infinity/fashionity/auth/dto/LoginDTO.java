package com.infinity.fashionity.auth.dto;


import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.entity.MemberRoleEntity;
import lombok.*;

import java.util.List;

public class LoginDTO {
    @Builder
    @NoArgsConstructor
    @Getter
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
    public static class Response{

        private Long memberSeq;
        private String profileUri;
        private String nickname;
        private List<MemberRole> memberRole;

        private String accessToken;
        private String refreshToken;

    }
}
