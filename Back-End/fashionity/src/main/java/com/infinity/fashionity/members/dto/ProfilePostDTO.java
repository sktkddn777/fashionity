package com.infinity.fashionity.members.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ProfilePostDTO {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {

        @Builder.Default
        List<ProfilePost> profilePosts = new ArrayList<>();
    }
}
