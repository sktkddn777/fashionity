package com.infinity.fashionity.posts.entity;

import com.infinity.fashionity.global.entity.CEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "images")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostImageEntity extends CEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @Column(name="image_name",unique=false,nullable = false)
    private String name;

    @Column(name = "image_url", unique = true, nullable = false, columnDefinition = "TEXT")
    private String url;

    @JoinColumn(name = "post_seq",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PostEntity post;
}
