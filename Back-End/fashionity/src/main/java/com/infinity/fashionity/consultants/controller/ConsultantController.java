package com.infinity.fashionity.consultants.controller;

import com.infinity.fashionity.consultants.dto.Consultant;
import com.infinity.fashionity.consultants.dto.ConsultantListDTO;
import com.infinity.fashionity.consultants.service.ConsultantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/consultants")
public class ConsultantController {

    private final ConsultantService consultantService;

    // 유저는 이따가 유저쪽으로 넘기기
    // [유저] 전체 컨설턴트 목록 조회
    @GetMapping("")
    public ResponseEntity<ConsultantListDTO.Response> getAllConsultants(ConsultantListDTO.Request dto) {
        ConsultantListDTO.Response response = consultantService.getAllConsultants(dto);
        log.info("response = {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // [유저] 컨설턴트 상세 조회
//    @GetMapping("/{consultantNickname}")
//    public ResponseEntity<Consultant> getConsultant(@PathVariable long consultantNickname){
//        Consultant consultant = consultantService.getConsultant();
//        return new ResponseEntity<>(consultant, HttpStatus.OK);
//    }

    /*
    // [유저] 전체 예약 조회
    @GetMapping("/reservations")


    // [유저] 예약 등록
    @PostMapping("/reservation")

    // [컨설턴트] 전체 예약 조회
    @GetMapping("/{consultantNickname}/reservations")

    // [컨설턴트] 단일 예약 조회
    @GetMapping("/{consultantNickname}/reservations/{reservationSeq}")

    // [컨설턴트] 전체 후기/평점 조최
    @GetMapping("/{consultantNickname}/reviews")

    // [컨설턴트] 단일 예약 후기/평점 조회
    @GetMapping("/{consultantNickname}/reservations/{reservationSeq}/review")

    //[컨설턴트] 통계 조회
    @GetMapping("/{consultantNickname}/statistics")

    // [컨설턴트] 예약 수정
    @PutMapping("/reservation/{reservationSeq}/edit")

    // [컨설턴트] 예약 삭제
    @DeleteMapping("/reservation/{reservationSeq}")

    // [컨설턴트] 예약 가능 시간대 설정
    @PostMapping("/reservation/schedule")

    // [컨설턴트] 예약 가능 시간대 삭제
    @DeleteMapping("/reservation/schedule/{scheduleSeq}")

    // [컨설턴트] 리뷰 작성
    @PostMapping("/{reservationSeq}/review")
    // [컨설턴트] 리뷰 수정
    @PutMapping("/reviews/{reviewSeq}/edit")
    */


}