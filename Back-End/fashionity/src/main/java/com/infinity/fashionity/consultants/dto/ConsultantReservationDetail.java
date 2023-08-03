package com.infinity.fashionity.consultants.dto;

import com.infinity.fashionity.consultants.entity.ImageEntity;
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
public class ConsultantReservationDetail {

    // 예약 아이디
    private Long reservationSeq;

    // 예약자 닉네임
    private String memberNickname;

    // 예약일시
    private LocalDateTime reservationDateTime;

    // 예약 상세
    private String reservationDetail;

    // 예약 등록 시 첨부한 이미지
    @Builder.Default
    private List<Image> images = new ArrayList<>();

    public ConsultantReservationDetail(Long reservationSeq, String memberNickname, LocalDateTime reservationDateTime, String reservationDetail) {
        this.reservationSeq = reservationSeq;
        this.memberNickname = memberNickname;
        this.reservationDateTime = reservationDateTime;
        this.reservationDetail = reservationDetail;
    }
}
