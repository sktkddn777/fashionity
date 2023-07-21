package com.infinity.fashionity.posts.entity;

import com.infinity.fashionity.global.entity.CEntity;
import com.infinity.fashionity.members.entity.MemberEntity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@IdClass(ReportKey.class)
@Entity
@Table(name = "post_reports")
public class ReportEntity extends CEntity {

    @Id
    @JoinColumn(name = "member_seq", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @Id
    @JoinColumn(name = "post_seq", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PostEntity post;

    @Column(name = "post_report_content", unique = false, nullable = true, columnDefinition = "TEXT")
    private String content;

    @Column(name = "post_report_category",length = 20, unique = false, nullable = true)
    private String category;
}
