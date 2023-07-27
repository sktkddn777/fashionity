package com.infinity.fashionity.posts.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class PostReportKey implements Serializable {

    private Long member;
    private Long post;
}
