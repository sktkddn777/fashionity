package com.infinity.fashionity.comments.entity;

import com.infinity.fashionity.global.entity.CEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "comment_reports")
@IdClass(value=CommentReportKey.class)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentReportEntity extends CEntity {

    @Id
    @JoinColumn(name = "comment_seq")
    @ManyToOne(fetch = FetchType.LAZY)
    private CommentEntity comment;

    @Id
    @JoinColumn(name = "member_seq")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @Column(name="comment_report_content",nullable = true,unique = false)
    private String content;

    @Column(name="comment_report_category",nullable = false,unique = false)
    private String category;
}
