package com.infinity.fashionity.consultants.controller;

import com.infinity.fashionity.consultants.dto.*;
import com.infinity.fashionity.consultants.service.ConsultantService;
import com.infinity.fashionity.security.dto.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/consultants")
public class ConsultantController {

    private final ConsultantService consultantService;

    // 유저는 이따가 유저쪽으로 넘기기
    // [공통] 전체 컨설턴트 목록 조회
    @GetMapping
    public ResponseEntity<ConsultantListDTO.Response> getAllConsultants(
            @AuthenticationPrincipal JwtAuthentication auth,
            ConsultantListDTO.Request dto){
        ConsultantListDTO.Response consultantListResponse = consultantService.getAllConsultants(auth.getSeq(), dto);
        return new ResponseEntity<>(consultantListResponse, HttpStatus.OK);}

    // [공통] 컨설턴트 상세 정보 조회
    @GetMapping(value="/{consultantNickname}")
    public ResponseEntity<ConsultantInfoDTO.Response> getConsultantDetail(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable("consultantNickname") String consultantNickname) {
        ConsultantInfoDTO.Response consultantInfoResponse = consultantService.getConsultantDetail(auth.getSeq(), consultantNickname);
        return new ResponseEntity<>(consultantInfoResponse, HttpStatus.OK);
    }

    // [공통] 내가 예약한 목록 조회
    @GetMapping(value = "/reservations")
    public ResponseEntity<UserReservationListDTO.Response> getUserReservationsList(
            @AuthenticationPrincipal JwtAuthentication auth
    ){
        UserReservationListDTO.Response userReservationListResponse = consultantService.getUserReservationsList(auth.getSeq());
        return new ResponseEntity<>(userReservationListResponse, HttpStatus.OK);
    }

    // [컨설턴트] 나의 예약 목록 조회
    @GetMapping(value = "/{consultantNickname}/reservations")
    public ResponseEntity<ConsultantReservationListDTO.Response> getConsultantReservationsList(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable("consultantNickname") String consultantNickname) {
//                ConsultantReservationListDTO.Request dto = ConsultantReservationListDTO.Request.builder()
//                        .consultantNickname(consultantNickname)
//                        .build();
        ConsultantReservationListDTO.Response consultantReservationsListResponse = consultantService.getConsultantReservationsList(auth.getSeq(), consultantNickname);
//        UserReservationListDTO.Response userReservationListResponse = consultantService.getUserReservationsList(1l);
        return new ResponseEntity<>(consultantReservationsListResponse, HttpStatus.OK);
    }

    // [컨설턴트] 상세 예약 정보 조회
    @GetMapping(value = "/{consultantNickname}/reservations/{reservationSeq}")
    public ResponseEntity<ConsultantReservationInfoDTO.Response>getConsultantReservationDetail(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable("consultantNickname") String consultantNickname,
            @PathVariable("reservationSeq") Long reservationSeq) {
            ConsultantReservationInfoDTO.Response consultantReservationInfoResponse = consultantService.getConsultantReservationDetail(auth.getSeq(), consultantNickname, reservationSeq);
            return new ResponseEntity<>(consultantReservationInfoResponse, HttpStatus.OK);
    }

    // [컨설턴트] 전체 리뷰 목록 조회
    @GetMapping(value = "/{consultantNickname}/reviews")
    public ResponseEntity<ConsultantReviewListDTO.Response>getConsultantReviewsList(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable("consultantNickname") String consultantNickname){
        ConsultantReviewListDTO.Response consultantReviewsListResponse = consultantService.getConsultantReviewsList(auth.getSeq(), consultantNickname);
        return new ResponseEntity<>(consultantReviewsListResponse, HttpStatus.OK);
    }


    // [컨설턴트] 평점 통계, 수익 조회
    @GetMapping(value = "/{consultantNickname}/statistics")
    public ResponseEntity<ConsultantStatisticsDTO.Response>getConsultantStatistics(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable("consultantNickname") String consultantNickname){
        ConsultantStatisticsDTO.Response consultantStatisticsResponse = consultantService.getConsultantStatistics(auth.getSeq(), consultantNickname);
        return new ResponseEntity<>(consultantStatisticsResponse, HttpStatus.OK);
    }

}
