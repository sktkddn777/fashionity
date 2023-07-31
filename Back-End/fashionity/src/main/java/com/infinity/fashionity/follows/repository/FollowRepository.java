package com.infinity.fashionity.follows.repository;

import com.infinity.fashionity.follows.entity.FollowEntity;
import com.infinity.fashionity.follows.entity.FollowKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<FollowEntity, FollowKey> {
}
