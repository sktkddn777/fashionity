package com.infinity.fashionity.posts.repository;

import com.infinity.fashionity.posts.entity.PostEntity;
import com.infinity.fashionity.posts.entity.PostImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostImageRepository extends JpaRepository<PostImageEntity, Long> {

    void deleteByPostSeq(Long postSeq);

    List<PostImageEntity> findAllByPost(PostEntity post);
}
