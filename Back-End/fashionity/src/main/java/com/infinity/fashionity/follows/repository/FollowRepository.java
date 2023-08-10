package com.infinity.fashionity.follows.repository;

import com.infinity.fashionity.follows.dto.FollowDTO;
import com.infinity.fashionity.follows.entity.FollowEntity;
import com.infinity.fashionity.follows.entity.FollowKey;
import com.infinity.fashionity.members.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<FollowEntity, FollowKey> {

    @Query("select f from FollowEntity f where f.member = :member")
    List<FollowEntity> findByMember(MemberEntity member);

    @Query("select f from FollowEntity f where f.followedMember = :member")
    List<FollowEntity> findByFollowedMember(MemberEntity member);

    Optional<FollowEntity> findById(FollowKey followKey);

}
