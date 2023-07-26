package com.infinity.fashionity.posts.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PostLikeKey implements Serializable {

    private Long member;
    private Long post;

}
