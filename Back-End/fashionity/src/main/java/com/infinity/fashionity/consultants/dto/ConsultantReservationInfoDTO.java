package com.infinity.fashionity.consultants.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class ConsultantReservationInfoDTO {
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        private String consultantNickname;
        private Long reservationSeq;

    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private String consultantNickname;
        private Long reservationSeq;

        @Builder.Default
        private List<ConsultantReservationDetail> consultantReservationDetails = new ArrayList<>();
    }
}
