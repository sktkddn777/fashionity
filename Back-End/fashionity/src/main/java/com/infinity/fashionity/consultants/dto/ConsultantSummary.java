package com.infinity.fashionity.consultants.dto;

import com.infinity.fashionity.consultants.data.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 컨설턴트 목록 조회에서 보여지는 간단한 컨설턴트 정보
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantSummary {

    private Long seq;
    private String profileUrl;
    private Float avgGrade;
    private Integer totalCnt;
    private String nickname;
    private Level level;
}
