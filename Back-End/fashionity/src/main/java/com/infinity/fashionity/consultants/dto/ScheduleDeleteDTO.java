package com.infinity.fashionity.consultants.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

public class ScheduleDeleteDTO {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @JsonIgnore
        private Long memberSeq;

        private String cancelReason;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private boolean success;

    }

}
