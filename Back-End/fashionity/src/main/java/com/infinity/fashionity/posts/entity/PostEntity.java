package com.infinity.fashionity.posts.entity;

import com.infinity.fashionity.global.entity.CUDEntity;
import com.infinity.fashionity.global.exception.ErrorCode;
import com.infinity.fashionity.global.exception.ValidationException;
import com.infinity.fashionity.global.utils.StringUtils;
import com.infinity.fashionity.members.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@SQLDelete(sql = "UPDATE Posts SET deleted_at = now() WHERE post_seq = ?")
@Where(clause = "deleted_at is null")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostEntity extends CUDEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_seq")
    private Long seq;

    @Column(name = "post_content", unique = false, nullable = true, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq", nullable = false)
    private MemberEntity member;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @Builder.Default
    private List<PostImageEntity> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @Builder.Default
    private List<PostHashtagEntity> postHashtags = new ArrayList<>();

    //댓글 개수
    @Formula("(SELECT count(1) FROM comments c WHERE c.post_seq = post_seq)")
    private int commentCount;

    //좋아요 개수
    @Formula("(SELECT count(1) FROM post_likes pl WHERE pl.post_seq = post_seq)")
    private int likeCount;

    //게시글 수정
    public void updateContent(String content){
        if(StringUtils.isBlank(content)) throw new ValidationException(ErrorCode.INVALID_INPUT_VALUE);
        this.content = content;
    }

}
