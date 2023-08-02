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
public class Reservation {
    @JsonAlias(value = "reservation_seq")
    private Long reservationSeq;

    @JsonAlias(value = "reservation_date")
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime reservationDate;

    @JsonAlias(value = "reservation_detail")
    private String reservationDetail;

    @JsonAlias(value = "member_seq")
    private Long memberSeq;

    // 어떤 컨설턴트의 스케줄인지
    @JsonAlias(value = "schedule_seq")
    private Long scheduleSeq;
}
