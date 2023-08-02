package com.infinity.fashionity.comments.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

public class CommentLikeDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Request{
        //head
        @JsonIgnore
        private Long memberSeq;

        //param
        @JsonIgnore
        private Long postSeq;

        @JsonIgnore
        private Long commentSeq;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Response{
        private boolean like;
    }
}
