package com.infinity.fashionity.posts.comments.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommentLikeKey {

    private Long member;
    private Long comment;
}
