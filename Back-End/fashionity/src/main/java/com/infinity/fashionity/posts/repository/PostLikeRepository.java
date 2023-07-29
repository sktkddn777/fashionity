package com.infinity.fashionity.posts.repository;

import com.infinity.fashionity.posts.entity.PostLikeEntity;
import com.infinity.fashionity.posts.entity.PostLikeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, PostLikeKey> {

    @Query("SELECT Count(*) FROM PostLikeEntity pl where pl.post.seq = :seq")
    long countBySeq(@Param("seq") Long seq);
}
