package com.infinity.fashionity.posts.entity;

import com.infinity.fashionity.global.entity.CEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@IdClass(PostReportKey.class)
@Entity
@Table(name = "post_reports")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostReportEntity extends CEntity {

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
