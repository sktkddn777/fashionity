package com.infinity.fashionity.posts.repository;

import com.infinity.fashionity.posts.entity.HashtagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<HashtagEntity, Long> {
    Optional<HashtagEntity> findByName(String name);
}
