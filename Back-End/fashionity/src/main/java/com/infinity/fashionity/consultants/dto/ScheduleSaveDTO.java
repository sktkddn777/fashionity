package com.infinity.fashionity.consultants.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleSaveDTO {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Request{
        @JsonIgnore
        private Long memberSeq;

        @Builder.Default
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private List<LocalDateTime> availableDateTimes = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private Boolean success;

    }
}
