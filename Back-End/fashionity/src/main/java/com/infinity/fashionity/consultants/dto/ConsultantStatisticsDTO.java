package com.infinity.fashionity.consultants.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


public class ConsultantStatisticsDTO {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @JsonIgnore
        Long memberSeq;

        @JsonIgnore
        String consultantNickname ;
    }


    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private Float avgGrade;
        private Integer totalConsultingCnt;
        private Integer totalUndeletedReviewCnt;
        private Integer totalDeletedReviewCnt;
        private Integer totalSalary;
    }
}
