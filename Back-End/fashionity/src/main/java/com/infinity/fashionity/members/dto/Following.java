package com.infinity.fashionity.members.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Following {

    private String profileUrl;
    private String nickname;
    private boolean isFollowing;
}
