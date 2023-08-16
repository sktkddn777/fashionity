package com.infinity.fashionity.follows.repository;

import com.infinity.fashionity.follows.entity.FollowEntity;
import com.infinity.fashionity.follows.entity.FollowKey;
import com.infinity.fashionity.members.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<FollowEntity, FollowKey> {

    @Query("select f from FollowEntity f where f.member = :member")
    List<FollowEntity> findByMember(@Param("member") MemberEntity member);

    @Query("select f from FollowEntity f where f.followedMember = :member")
    List<FollowEntity> findByFollowedMember(@Param("member")MemberEntity member);

    Optional<FollowEntity> findById(FollowKey followKey);

    @Query("select case when count(f) >= 1 then true else false end as check from FollowEntity f where f.followedMember.seq = :checkSeq and f.member.seq = :seq")
    Boolean getIsFollowing(Long seq, Long checkSeq);
}
