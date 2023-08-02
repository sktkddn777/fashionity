package com.infinity.fashionity.consultants.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class ConsultantReservationListDTO {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

//        private String consultantNickname;

        @Builder.Default
        private List<ConsultantReservationSummary> consultantReservationSummaries = new ArrayList<>();
    }

}
