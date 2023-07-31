package com.infinity.fashionity.members.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ProfileDTO {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String profileUrl;
        private String nickname;
        private String profileIntro;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PwRequest {
        private String password;
        private String newPassword;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String profileUrl;
        private String nickname;
        private String profileIntro;
        private Integer followerCnt;
        private Integer followingCnt;
        private boolean myProfile;
    }
}
