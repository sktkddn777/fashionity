package com.infinity.fashionity.consultants.dto;

import com.infinity.fashionity.members.data.Gender;
import com.infinity.fashionity.members.data.PersonalColor;
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

    // 예약 아이디
    private Long reservationSeq;

    // 컨설턴트 닉네임
    private String consultantNickname;

    // 예약자 닉네임
    private String memberNickname;

    // 예약자 퍼스널컬러
    private PersonalColor personalColor;

    // 예약자 성별
    private Gender gender;

    // 예약자 키
    private Float height;

    // 예약자 몸무게
    private Float weight;

    // 예약자 나이
    private Integer age;

    // 예약일시
    private LocalDateTime reservationDateTime;

    // 예약 상세
    private String reservationDetail;

    // 예약 등록 시 유저가 첨부한 이미지
    @Builder.Default
    private List<Image> memberImages = new ArrayList<>();

    // 컨설턴트가 첨부한 이미지
    @Builder.Default
    private List<Image> consultantImages = new ArrayList<>();

    public UserReservationDetail(
            Long reservationSeq,
            String consultantNickname,
            String memberNickname,
            PersonalColor personalColor,
            Gender gender,
            Float height,
            Float weight,
            Integer age,
            LocalDateTime reservationDateTime,
            String reservationDetail
    ) {
        this.reservationSeq = reservationSeq;
        this.consultantNickname = consultantNickname;
        this.memberNickname = memberNickname;
        this.personalColor = personalColor;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.reservationDateTime = reservationDateTime;
        this.reservationDetail = reservationDetail;
    }
}