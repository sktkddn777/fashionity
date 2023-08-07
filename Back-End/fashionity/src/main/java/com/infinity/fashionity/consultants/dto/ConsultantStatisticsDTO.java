package com.infinity.fashionity.consultants.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class ConsultantStatisticsDTO {
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
