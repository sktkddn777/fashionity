package com.infinity.fashionity.members.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class FollowKey implements Serializable {

    private Long member;
    private Long followedMember;
}
