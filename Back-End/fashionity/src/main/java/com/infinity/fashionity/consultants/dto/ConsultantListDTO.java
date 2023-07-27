package com.infinity.fashionity.consultants.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;
import org.springframework.data.jpa.repository.Query;

public class ConsultantListDTO {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        // 컨설턴트 아이디
        @JsonAlias(value = "consultant_seq")
        private Long seq;

        @JsonAlias(value = "member_nickname")
        private String nickname;

        // 컨설턴트 프로필 사진 url
        @JsonAlias(value = "member_profile_url")
        private String profileUrl;

        // 컨설턴트 레벨
        @JsonAlias(value = "consultant_level")
        private String level;

        // 평균 별점
        private Float avgRating;

        // 전체 컨설팅 횟수
        private Integer totalCnt;

    }
}