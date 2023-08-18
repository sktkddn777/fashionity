package com.infinity.fashionity.consultants.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

public class ReviewDeleteDTO {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{

        @JsonIgnore
        private Long memberSeq;

        @JsonIgnore
        private Long reviewSeq;

        Float reviewGrade;
        String reviewContent;

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private Boolean success;

    }
}
