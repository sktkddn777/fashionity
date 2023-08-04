package com.infinity.fashionity.consultants.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class ConsultantReservationListDTO {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @JsonIgnore
        private Long memberSeq;

        @JsonIgnore
        private String consultantNickname;

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private Long memberSeq;
        private String consultantNickname;

        @Builder.Default
        private List<ConsultantReservationSummary> consultantReservationSummaries = new ArrayList<>();
    }

}
