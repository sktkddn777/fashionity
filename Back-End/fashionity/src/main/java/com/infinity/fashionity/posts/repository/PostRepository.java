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
    Integer postsCnt(String nickname);
}
