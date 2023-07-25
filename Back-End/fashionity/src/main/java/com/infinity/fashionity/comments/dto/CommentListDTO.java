package com.infinity.fashionity.comments.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentListDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Request {
        private Long postSeq;//pathVariable
        @Builder.Default
        private int page = 0;//param
        @Builder.Default
        private int size = 50;//param
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private boolean prev;//이전장이 존재하는지
        private boolean next;//다음장이 존재하는지
        private int page;//현재 페이지가 몇페이지인지
        @Builder.Default
        private List<Comment> comments = new ArrayList<>();//댓글들

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        static class Comment {
            private Long id;

            @JsonAlias(value = "profile_img")
            private String profileImg;

            private String name;

            @JsonAlias(value = "created_at")
            @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
            private LocalDateTime createdAt;

            @JsonAlias(value = "updated_at")
            @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
            private LocalDateTime updatedAt;
            private String content;
            private boolean liked;
        }
    }
}
