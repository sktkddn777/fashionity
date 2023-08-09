package com.infinity.fashionity.consultants.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Size;

public class ReviewUpdateDTO {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @JsonIgnore
        Long memberSeq;

        // path Parameter
        @JsonIgnore
        Long reviewSeq;

        Float reviewGrade;

        @Size(max = 200,message = "최대 200자까지 입력할 수 있습니다.")
        String reviewContent;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private Boolean success;
    }
}
