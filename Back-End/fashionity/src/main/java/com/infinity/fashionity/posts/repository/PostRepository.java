package com.infinity.fashionity.posts.repository;

import com.infinity.fashionity.members.dto.ProfilePost;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.posts.entity.PostEntity;
import com.infinity.fashionity.posts.entity.PostImageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Page<PostEntity> findAll(Pageable pageable);

    List<PostEntity> findAllByMember(MemberEntity member);
    @Query("select p from PostEntity p " +
            "left join PostLikeEntity pl on pl.post = p " +
            "where pl.member = :member"
    )
    Page<PostEntity> findLikedPostsByMember(@Param("member") MemberEntity member, Pageable pageable);

    // 내가 작성한 게시글 모아보기
    @Query("select p " +
            "from MemberEntity m " +
            "left join m.posts p " +
            "where p.member = :member")
    Page<PostEntity> findPostsByMember(@Param("member") MemberEntity member, Pageable pageable);


    // 내가 작성한 게시글 갯수
    @Query("select coalesce(count(p.seq),0) " +
            "from MemberEntity m " +
            "left join  m.posts p " +
            "where m.nickname = :nickname")
    Integer postsCnt(@Param("nickname") String nickname);

    @Query("SELECT p,count(distinct pl) as lcnt FROM PostEntity p " +
            "LEFT JOIN PostLikeEntity pl on p = pl.post " +
            "GROUP BY p ORDER BY lcnt DESC, p.createdAt DESC")
    Page<Object[]> findPostsOrderByLikesDesc(Pageable pageable);

    @Query("SELECT p,count(distinct pl) as lcnt FROM PostEntity p " +
            "LEFT JOIN PostLikeEntity pl on pl.post = p " +
            "GROUP BY p ORDER BY p.createdAt DESC")
    Page<Object[]> findPostsOrderByCreatedAt(Pageable pageable);

    @Query("SELECT DISTINCT p ,count(distinct pl) as lcnt FROM PostEntity p " +
            "LEFT JOIN PostLikeEntity pl on p = pl.post " +
            "LEFT JOIN p.postHashtags ph " +
            "LEFT JOIN ph.hashtag h " +
            "WHERE p.deletedAt IS NULL " +
            "AND h.name LIKE %:hashtag% " +
            "GROUP BY p ORDER BY lcnt DESC,p.createdAt DESC")
    Page<Object[]> findAllWithHashtagOrderByLikeCount(@Param("hashtag") String hashtag,Pageable pageable);

    @Query("SELECT DISTINCT p ,count(distinct pl) as lcnt FROM PostEntity p " +
            "LEFT JOIN PostLikeEntity pl on p = pl.post " +
            "LEFT JOIN p.postHashtags ph " +
            "LEFT JOIN ph.hashtag h " +
            "WHERE p.deletedAt IS NULL " +
            "AND h.name LIKE %:hashtag% " +
            "GROUP BY p ORDER BY p.createdAt DESC")
    Page<Object[]> findAllWithHashtagOrderByCreatedAt(@Param("hashtag") String hashtag,Pageable pageable);
}
