package com.infinity.fashionity.comments.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class CommentLikeKey implements Serializable {

    private Long member;
    private Long comment;
}
