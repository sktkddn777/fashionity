package com.infinity.fashionity.consultants.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConsultantReservationSummary {

    private String consultantNickname;
    private Long reservationSeq;
    private LocalDateTime reservationDateTime;
    private String memberNickname;

}
