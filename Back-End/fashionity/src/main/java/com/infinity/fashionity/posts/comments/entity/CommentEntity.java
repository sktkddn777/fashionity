package com.infinity.fashionity.posts.comments.entity;

import com.infinity.fashionity.global.entity.CUDEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "comments")
@SQLDelete(sql = "UPDATE Comments SET deleted_at = now() WHERE seq = ?")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq", nullable = false)
    private MemberEntity member;

}
