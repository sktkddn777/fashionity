package com.infinity.fashionity.members.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class FollowKey implements Serializable {

    private Long member;
    private Long followedMember;

    // equals, hashcode
}
