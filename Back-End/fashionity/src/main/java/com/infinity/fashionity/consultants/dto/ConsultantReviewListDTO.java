package com.infinity.fashionity.consultants.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class ConsultantReviewListDTO {
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        @Builder.Default
        private List<ConsultantReviewSummary> consultantReviewSummaries = new ArrayList<>();
    }
}
