package com.infinity.fashionity.consultants.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.infinity.fashionity.consultants.data.Level;
import com.infinity.fashionity.consultants.entity.ReviewEntity;
import com.infinity.fashionity.consultants.entity.ScheduleEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantDetail {

    // 컨설턴트 아이디
    @JsonAlias(value = "consultant_seq")
    private Long seq;

    // 컨설턴트 닉네임
    @JsonAlias(value = "member_nickname")
    private String nickname;

    // 컨설턴트 프로필 사진 url
    @JsonAlias(value = "member_profile_url")
    private String profileUrl;

    // 컨설턴트 레벨
    @JsonAlias(value = "consultant_level")
    private Level level;

    // 평균 별점
    private Float avgGrade;

    // 전체 컨설팅 횟수
    private Integer totalCnt;

    // 전체 리뷰 + 내용
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    // 예약 가능한 일시
    @Builder.Default
    private List<Schedule> schedules = new ArrayList<>();


}
