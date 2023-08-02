package com.infinity.fashionity.comments.repository;

import com.infinity.fashionity.comments.entity.CommentLikeEntity;
import com.infinity.fashionity.comments.entity.CommentLikeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity, CommentLikeKey> {
}
