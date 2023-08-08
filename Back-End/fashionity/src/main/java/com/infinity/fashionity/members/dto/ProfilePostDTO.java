package com.infinity.fashionity.members.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class ProfilePostDTO {
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{

        private int page = 0;
        private int size = 50;

        @JsonIgnore
        private String nickname;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {

        private boolean prev;//이전장이 존재하는지
        private boolean next;//다음장이 존재하는지
        private int page;

        private String nickname;

        @Builder.Default
        List<ProfilePost> profilePosts = new ArrayList<>();
    }
}
