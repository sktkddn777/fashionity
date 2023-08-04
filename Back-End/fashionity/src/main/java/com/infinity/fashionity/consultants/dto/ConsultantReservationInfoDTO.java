package com.infinity.fashionity.consultants.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

        @JsonIgnore
        private Long memberSeq;

        @JsonIgnore // path variable
        private String consultantNickname;

        @JsonIgnore // path variable
        private Long reservationSeq;

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private Long memberSeq;
        private String consultantNickname;
        private Long reservationSeq;

        @Builder.Default
        private List<ConsultantReservationDetail> consultantReservationDetails = new ArrayList<>();
    }
}
