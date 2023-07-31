package com.infinity.fashionity.follows.entity;

import com.infinity.fashionity.global.entity.CEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@IdClass(FollowKey.class)
@Entity
@Table(name = "follows")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
