package com.infinity.fashionity.consultants.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private Boolean prev;
        private Boolean next;
        private Integer page;

        @Builder.Default
        private List<Consultant> consultants = new ArrayList<>();
    }

}