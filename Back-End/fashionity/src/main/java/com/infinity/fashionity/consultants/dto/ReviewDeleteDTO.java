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

        // Param 으로 가져올 것
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
