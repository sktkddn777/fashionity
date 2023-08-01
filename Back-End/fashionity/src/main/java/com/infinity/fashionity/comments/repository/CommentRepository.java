package com.infinity.fashionity.comments.repository;

import com.infinity.fashionity.comments.entity.CommentEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.posts.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.xml.stream.events.Comment;
import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    List<CommentEntity> findAllByPost(PostEntity post);
    Page<CommentEntity> findAllByPost(PostEntity post, Pageable pageable);

    @Query("select c,count(cl),case when count(m) >= 1 then true else false end as liked from CommentEntity c " +
            "left join PostEntity p on c.post = p " +
            "left join CommentLikeEntity cl on cl.comment = c " +
            "left join MemberEntity m on cl.member = m and m = :member " +
            "where p = :post " +
            "group by c")
    Page<Object[]> findAllByPostWithCommentsAndLikeCount(@Param("post") PostEntity post, @Param("member") MemberEntity member, Pageable pageable);

    Long countBySeq(long seq);
}
