package com.infinity.fashionity.posts.entity;

import com.infinity.fashionity.global.entity.CEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "post_hashtags")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostHashtagEntity extends CEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_seq", nullable = false)
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "hashtag_seq", nullable = false)
    private HashtagEntity hashtag;
}
