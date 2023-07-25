package com.infinity.fashionity.comments.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CommentDeleteDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Request{
        //Header로 받아온 인증정보
        @JsonIgnore
        private Long memberSeq;

        //pathParam
        @JsonIgnore
        private Long postSeq;

        @JsonIgnore
        private Long commentSeq;

        //body
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response{
        private boolean success;
    }
}
