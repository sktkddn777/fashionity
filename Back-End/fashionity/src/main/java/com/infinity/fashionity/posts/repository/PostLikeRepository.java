package com.infinity.fashionity.posts.repository;


import com.infinity.fashionity.posts.entity.PostLikeEntity;
import com.infinity.fashionity.posts.entity.PostLikeKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, PostLikeKey> {
    @Query("SELECT p,count(*) FROM PostLikeEntity pl LEFT JOIN pl.post p GROUP BY p ORDER BY COUNT(p) DESC, p.createdAt DESC")
    Page<Object[]> findPostsOrderByLikesDesc(Pageable pageable);

    @Query("SELECT p,count(*) FROM PostLikeEntity pl LEFT JOIN pl.post p GROUP BY p ORDER BY p.createdAt DESC")
    Page<Object[]> findPostsOrderByCreatedAt(Pageable pageable);
}
