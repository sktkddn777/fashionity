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
public class UserReservationSummary {

    private Long reservationSeq;
    private LocalDateTime reservationDateTime;
    private String consultantProfileUrl;
    private String consultantNickname;
    private Long reviewSeq;
}
