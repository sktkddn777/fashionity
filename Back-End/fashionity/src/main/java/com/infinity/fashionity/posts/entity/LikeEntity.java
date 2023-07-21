package com.infinity.fashionity.posts.entity;

import com.infinity.fashionity.global.entity.CEntity;
import com.infinity.fashionity.members.entity.FollowKey;
import com.infinity.fashionity.members.entity.MemberEntity;

import javax.persistence.*;

@IdClass(LikeKey.class)
@Entity
@Table(name = "post_likes")
public class LikeEntity extends CEntity {

    @Id
    @JoinColumn(name = "post_seq")
    @ManyToOne(fetch = FetchType.LAZY)
    private PostEntity post;

    @Id
    @JoinColumn(name = "member_seq")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;
}
