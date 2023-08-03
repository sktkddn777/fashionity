package com.infinity.fashionity.members.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class MemberFollowDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FollowerResponse {

        @Builder.Default
        List<Follower> followers = new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FollowingResponse {

        @Builder.Default
        List<Following> followings = new ArrayList<>();
    }
}
