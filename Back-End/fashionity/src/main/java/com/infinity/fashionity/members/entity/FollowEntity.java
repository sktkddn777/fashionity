package com.infinity.fashionity.members.entity;

import com.infinity.fashionity.global.entity.CEntity;

import javax.persistence.*;


@IdClass(FollowKey.class)
@Entity
@Table(name = "follows")
public class FollowEntity extends CEntity {

    @Id
    @JoinColumn(name = "followed_member")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity followedMember;

    @Id
    @JoinColumn(name = "member")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;
}
