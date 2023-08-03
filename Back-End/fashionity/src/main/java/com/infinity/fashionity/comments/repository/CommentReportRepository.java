package com.infinity.fashionity.comments.repository;

import com.infinity.fashionity.comments.entity.CommentReportEntity;
import com.infinity.fashionity.comments.entity.CommentReportKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReportEntity, CommentReportKey> {
}
