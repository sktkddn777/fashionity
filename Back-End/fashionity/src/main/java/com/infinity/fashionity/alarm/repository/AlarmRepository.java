package com.infinity.fashionity.alarm.repository;

import com.infinity.fashionity.alarm.entity.AlarmEntity;
import com.infinity.fashionity.comments.entity.CommentEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.posts.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface AlarmRepository extends JpaRepository<AlarmEntity,Long> {
    List<AlarmEntity> findAllByOwnerSeqOrderByCreatedAtDesc(Long ownerSeq);

    @Transactional
    @Modifying
    @Query("delete from AlarmEntity a " +
            "where a.comment = :comment")
    void deleteByComment(@Param("comment") CommentEntity comment);
    @Transactional
    @Modifying
    @Query("delete from AlarmEntity a " +
            "where a.post=:post")
    void deleteByPost(@Param("post") PostEntity post);

    @Transactional
    @Modifying
    @Query("delete from AlarmEntity a " +
            "where a.publisher = :member ")
    void deleteByMember(@Param("member") MemberEntity member);
}
