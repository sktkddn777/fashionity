package com.infinity.fashionity.posts.entity;

import com.infinity.fashionity.global.entity.CEntity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "post_hashtags")
public class PostHashtagEntity extends CEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_seq", nullable = false)
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_seq", nullable = false)
    private HashtagEntity hashtag;
    /**
     * 밍 이거 어케함
     * */


}
