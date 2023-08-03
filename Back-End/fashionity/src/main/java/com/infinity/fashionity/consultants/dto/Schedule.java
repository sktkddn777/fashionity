package com.infinity.fashionity.consultants.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    @JsonAlias(value = "schedule_seq")
    private Long scheduleSeq;

    @JsonAlias(value = "available_datetime")
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime availableDateTime;

    @JsonAlias(value = "is_available")
    private Boolean isAvailable;


}
