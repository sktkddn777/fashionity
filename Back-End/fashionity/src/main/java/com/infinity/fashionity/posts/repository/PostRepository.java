package com.infinity.fashionity.posts.repository;

import com.infinity.fashionity.posts.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<PostEntity,Long> {

//    // 댓글 수
//    @Query(value = "SELECT COUNT(*) AS commentCount\n" +
//            "FROM CommentEntity c\n" +
//            "WHERE c.post.seq = :postSeq")
//    int findCommentCount(Long postSeq);
//
//    // 좋아요 수
//    @Query(value = "SELECT COUNT(*) AS likeCount\n" +
//            "FROM PostLikeEntity pl\n" +
//            "WHERE pl.post.seq = :postSeq"
//    )
//    int findLikeCount(Long postSeq);


}
