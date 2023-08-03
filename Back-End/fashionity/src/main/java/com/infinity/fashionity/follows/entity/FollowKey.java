package com.infinity.fashionity.follows.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class FollowKey implements Serializable {

    private Long member; // 팔로우 한 멤버
    private Long followedMember; // 팔로우 당한 멤버
}
