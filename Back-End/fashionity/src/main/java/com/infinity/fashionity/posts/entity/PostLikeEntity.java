package com.infinity.fashionity.posts.entity;

import com.infinity.fashionity.global.entity.CEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@IdClass(PostLikeKey.class)
@Entity
@Table(name = "post_likes")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeEntity extends CEntity {


    @Id
    @JoinColumn(name = "post_seq")
    @ManyToOne(fetch = FetchType.LAZY)
    private PostEntity post;

    @Id
    @JoinColumn(name = "member_seq")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;
}
