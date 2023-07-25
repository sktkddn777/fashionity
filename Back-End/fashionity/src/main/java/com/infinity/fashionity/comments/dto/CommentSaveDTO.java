package com.infinity.fashionity.comments.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CommentSaveDTO {
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
        private Long postSeq;//param으로 받아올것임

        //body
        @NotBlank(message="content를 입력해주세요")
        @Size(max = 200,message = "최대 200자까지 입력할 수 있습니다.")
        private String content;
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
