package com.infinity.fashionity.posts.entity;

import com.infinity.fashionity.global.entity.CEntity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "hashtags")
public class HashtagEntity extends CEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @Column(name = "hashtag_name", length = 50, unique = false, nullable = false)
    private String name;

}
