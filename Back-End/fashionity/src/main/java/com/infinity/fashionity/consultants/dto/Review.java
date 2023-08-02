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
public class Review {
    @JsonAlias(value = "review_seq")
    private Long reviewSeq;

    // 어떤 예약의 리뷰인지
    @JsonAlias(value = "reservation_seq")
    private Long reservationSeq;

    @JsonAlias(value = "review_grade")
    private Float reviewGrade;

    // 리뷰 생성 일시
    @JsonAlias(value = "created_at")
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    // 리뷰 수정 일시
    @JsonAlias(value = "updated_at")
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    // 리뷰 삭제 일시
    @JsonAlias(value = "deleted_at")
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime deletedAt;

    // 리뷰 작성자 닉네임
    @JsonAlias(value = "member_nickname")
    private String memberNickname;

    // 리뷰 내용
    @JsonAlias(value = "review_content")
    private String reviewContent;




}
