package com.infinity.fashionity.consultants.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ScheduleSummary {

    private Long scheduleSeq;
    private LocalDateTime unAvailableDateTime;
}
