package com.infinity.fashionity.members.repository;

import com.infinity.fashionity.members.entity.FollowEntity;
import com.infinity.fashionity.members.entity.FollowKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<FollowEntity, FollowKey> {
}
