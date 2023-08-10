package com.infinity.fashionity.consultants.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserReservationDetail {
    private Long reservationSeq;
    private String consultantNickname;
    private LocalDateTime reservationDateTime;
    private String reservationDetail;

    @Builder.Default
    private List<Image> images = new ArrayList<>();


    public UserReservationDetail(Long reservationSeq, String consultantNickname, LocalDateTime reservationDateTime, String reservationDetail){
        this.reservationSeq = reservationSeq;
        this.consultantNickname = consultantNickname;
        this.reservationDateTime = reservationDateTime;
        this.reservationDetail = reservationDetail;
    }
}

