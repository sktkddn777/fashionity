package com.infinity.fashionity.consultants.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantReservationSummary {

    private String consultantNickname;
    private Long reservationSeq;
    private LocalDateTime reservationDateTime;
    private String memberNickname;

}
