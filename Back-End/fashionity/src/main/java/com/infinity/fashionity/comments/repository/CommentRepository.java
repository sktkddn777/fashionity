package com.infinity.fashionity.comments.repository;

import com.infinity.fashionity.comments.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
}
