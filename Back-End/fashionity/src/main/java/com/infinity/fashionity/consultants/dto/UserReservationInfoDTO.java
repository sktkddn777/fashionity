package com.infinity.fashionity.consultants.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class UserReservationInfoDTO {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        @JsonIgnore
        private Long memberSeq;
        @JsonIgnore
        private Long reservationSeq;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private Long memberSeq;
        private Long reservationSeq;

        @Builder.Default
        private List<UserReservationDetail> userReservationDetails = new ArrayList<>();
    }
}
