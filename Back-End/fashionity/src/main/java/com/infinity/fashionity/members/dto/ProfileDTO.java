package com.infinity.fashionity.members.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

public class ProfileDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Setter
    @ToString
    public static class Request {
        private MultipartFile profileImage;
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
        private Integer postsCnt;
        private Integer followerCnt;
        private Integer followingCnt;
        private Boolean isFollowed;
        private boolean myProfile;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class MyProfileResponse {
        private String profileUrl;
        private String nickname;
        private String profileIntro;
        private String id;
        private String email;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PwResponse {
        private boolean success;
    }
}
