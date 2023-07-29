package com.infinity.fashionity.comments.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.infinity.fashionity.comments.entity.CommentEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @JsonAlias(value = "comment_seq")
    private Long commentSeq;

    @JsonAlias(value = "member_seq")
    private Long memberSeq;

    @JsonAlias(value = "profile_img")
    private String profileImg;

    private String nickname;

    @JsonAlias(value = "created_at")
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonAlias(value = "updated_at")
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    private String content;

    @JsonAlias(value = "like_cnt")
    private int likeCnt;

    private boolean liked;
}