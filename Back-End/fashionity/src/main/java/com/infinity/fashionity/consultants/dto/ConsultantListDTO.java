package com.infinity.fashionity.consultants.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller 에서 컨설턴트 목로 조회시 받고 줄거
 */
public class ConsultantListDTO {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @Builder.Default
        private int page = 0;

        @Builder.Default
        private int size = 12;

        @Builder.Default
        private String nickname="";
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private Boolean prev;
        private Boolean next;
        private Integer page;

        @Builder.Default
        private List<ConsultantSummary> consultants = new ArrayList<>();
    }

}