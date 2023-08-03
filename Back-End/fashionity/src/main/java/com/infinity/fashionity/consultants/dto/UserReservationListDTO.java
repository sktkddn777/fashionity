package com.infinity.fashionity.consultants.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller 에서 컨설턴트 목로 조회시 받고 줄거
 */
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