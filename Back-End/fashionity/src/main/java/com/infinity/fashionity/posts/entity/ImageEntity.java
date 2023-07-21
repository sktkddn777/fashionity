package com.infinity.fashionity.posts.entity;

import com.infinity.fashionity.global.entity.CEntity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "images")
public class ImageEntity extends CEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @Column(name = "image_url", unique = true, nullable = false, columnDefinition = "TEXT")
    private String url;

    @JoinColumn(name = "post_seq")
    @ManyToOne(fetch = FetchType.LAZY)
    private PostEntity post;

}
