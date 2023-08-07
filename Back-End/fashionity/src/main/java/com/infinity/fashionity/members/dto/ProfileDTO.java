package com.infinity.fashionity.members.dto;

import lombok.*;

public class ProfileDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private String profileUrl;
        private String nickname;
        private String profileIntro;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PwRequest {
        private String password;
        private String newPassword;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Response {
        private String profileUrl;
        private String nickname;
        private String profileIntro;
        private Integer followerCnt;
        private Integer followingCnt;
        private boolean myProfile;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PwResponse {
        private boolean success;
    }
}
