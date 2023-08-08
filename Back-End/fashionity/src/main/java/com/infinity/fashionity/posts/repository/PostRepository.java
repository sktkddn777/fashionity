package com.infinity.fashionity.posts.repository;

import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.posts.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Member;
import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Page<PostEntity> findAll(Pageable pageable);

    List<PostEntity> findAllByMember(MemberEntity member);
    @Query("SELECT p,count(distinct pl) as lcnt,count(distinct c) as ccnt FROM PostEntity p " +
            "LEFT JOIN PostLikeEntity pl on p = pl.post " +
            "LEFT JOIN CommentEntity c on c.post = p " +
            "GROUP BY p ORDER BY lcnt DESC, p.createdAt DESC")
    Page<Object[]> findPostsOrderByLikesDesc(Pageable pageable);

    @Query("SELECT p,count(distinct pl) as lcnt FROM PostEntity p " +
            "LEFT JOIN PostLikeEntity pl on pl.post = p " +
            "LEFT JOIN CommentEntity c on c.post = p " +
            "GROUP BY p ORDER BY p.createdAt DESC")
    Page<Object[]> findPostsOrderByCreatedAt(Pageable pageable);
}
