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
public class ConsultantReviewSummary {

    private Long reservationSeq;
    private Long reviewSeq;
    private LocalDateTime reviewCreatedAt;
    private LocalDateTime reviewUpdatedAt;
    private Float reviewGrade;
    private String reviewContent;

    // 리뷰 작성자
    private String memberNickname;
}
