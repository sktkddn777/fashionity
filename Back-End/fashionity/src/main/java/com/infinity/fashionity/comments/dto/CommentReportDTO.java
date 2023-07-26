package com.infinity.fashionity.comments.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentReportDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Request{
        //header
        @JsonIgnore
        private Long memberSeq;

        //pathVariable
        @JsonIgnore
        private Long postSeq;
        @JsonIgnore
        private Long commentSeq;

        //body
        @JsonAlias(value="report_category")
        @NotBlank(message="신고유형을 입력해주세요")
        private String reportCategory;

        @JsonAlias(value="report_content")
        private String reportContent;
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
