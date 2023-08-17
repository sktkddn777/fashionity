package com.infinity.fashionity.consultants.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Request {
        private String dateTime;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Response {
        @Builder.Default
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private List<ScheduleSummary> unAvailableDateTimes = new ArrayList<>();
    }
}
