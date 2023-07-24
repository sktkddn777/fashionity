package com.infinity.fashionity.posts.repository;

import com.infinity.fashionity.posts.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity,Long> {
}
