package com.infinity.fashionity.posts.entity;

import com.infinity.fashionity.global.entity.CUDEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@SQLDelete(sql = "UPDATE Posts SET deleted_at = now() WHERE post_seq = ?")
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
    private List<PostImageEntity> postImages;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostHashtagEntity> postHashtags;

}
