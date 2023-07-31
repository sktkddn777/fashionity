package com.infinity.fashionity.members.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class FollowDTO {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class FollowerResponse {

        @Builder.Default
        List<Follower> followers = new ArrayList<>();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class FollowingResponse {

        @Builder.Default
        List<Following> followings = new ArrayList<>();
    }
}
