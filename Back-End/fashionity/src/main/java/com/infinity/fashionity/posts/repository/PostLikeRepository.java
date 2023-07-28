package com.infinity.fashionity.posts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository {

    long countBy(long postSeq);
}
