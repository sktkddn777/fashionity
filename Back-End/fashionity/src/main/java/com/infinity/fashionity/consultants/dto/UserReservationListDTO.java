package com.infinity.fashionity.consultants.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


public class UserReservationListDTO {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {


        @Builder.Default
        private List<UserReservationSummary> userReservationSummaries = new ArrayList<>();
    }

}