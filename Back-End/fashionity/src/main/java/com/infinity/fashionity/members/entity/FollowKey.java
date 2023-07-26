package com.infinity.fashionity.members.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FollowKey implements Serializable {

    private Long member;
    private Long followedMember;
}
