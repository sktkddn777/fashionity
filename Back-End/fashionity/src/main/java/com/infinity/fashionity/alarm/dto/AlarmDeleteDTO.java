package com.infinity.fashionity.alarm.dto;

import lombok.*;

public class AlarmDeleteDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Request{
        private Long seq;
    }
}
