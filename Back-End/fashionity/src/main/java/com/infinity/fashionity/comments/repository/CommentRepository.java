package com.infinity.fashionity.comments.repository;

import com.infinity.fashionity.comments.entity.CommentEntity;
import com.infinity.fashionity.posts.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.xml.stream.events.Comment;
import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    List<CommentEntity> findAllByPost(PostEntity post);
    Page<CommentEntity> findAllByPost(PostEntity post, Pageable pageable);

    Long countBy(long postSeq);
}
