package com.infinity.fashionity.comments.entity;

import com.infinity.fashionity.comments.dto.CommentUpdateDTO;
import com.infinity.fashionity.global.entity.CUDEntity;
import com.infinity.fashionity.global.exception.ErrorCode;
import com.infinity.fashionity.global.exception.ValidationException;
import com.infinity.fashionity.global.utils.StringUtils;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.posts.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "comments")
@SQLDelete(sql = "UPDATE Comments SET deleted_at = now() WHERE comment_seq = ?")
@Where(clause = "deleted_at is null")
@DynamicUpdate
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity extends CUDEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_seq")
    private Long seq;

    @Column(name = "comment_content", length = 200, unique = false, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_seq", nullable = false, updatable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "post_seq", nullable = false, updatable = false)
    private PostEntity post;

    //custom method
    public void updateContent(String content){
        if(StringUtils.isBlank(content)) throw new ValidationException(ErrorCode.INVALID_INPUT_VALUE);
        this.content = content;
    }
}
