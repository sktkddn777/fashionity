package com.infinity.fashionity.posts.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class ReportKey implements Serializable {

    private Long member;
    private Long post;
}
