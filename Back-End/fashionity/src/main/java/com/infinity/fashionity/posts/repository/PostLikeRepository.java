package com.infinity.fashionity.posts.repository;


import com.infinity.fashionity.posts.entity.PostEntity;
import com.infinity.fashionity.posts.entity.PostLikeEntity;
import com.infinity.fashionity.posts.entity.PostLikeKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, PostLikeKey> {

    List<PostLikeEntity> findAllByPost(PostEntity post);
}
