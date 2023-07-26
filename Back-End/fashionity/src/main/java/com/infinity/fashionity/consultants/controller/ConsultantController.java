package com.infinity.fashionity;

import com.infinity.fashionity.consultants.entity.ConsultantEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/consultants")
public class ConsultantController {

    // 유저는 이따가 유저쪽으로 넘기기
    // [유저] 전체 컨설턴트 목록 조회
    @GetMapping("")
    public List<ConsultantEntity> getAllConsultants(){
        return null;
    }

    // [유저] 컨설턴트 상세 정보 조회
//    @GetMapping

    // [컨설턴트] 전체 예약 조회
//    @GetMapping

    // [컨설턴트] 단일 예약 조회
//    @GetMapping

    // [컨설턴트] 전체 후기/평점 조회
//    @GetMapping

    // [컨설턴트] 예약별 후기/평점 조회
}
