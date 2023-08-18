package com.infinity.fashionity.consultants.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Size;

public class ReviewSaveDTO {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{

        @JsonIgnore
        private Long memberSeq;

        @JsonIgnore
        private Long reservationSeq;

        private Float reviewGrade;

        @Size(max = 200,message = "최대 200자까지 입력할 수 있습니다.")
        private String reviewContent;


    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{

        private Boolean success;
        private Long seq;
    }

}
