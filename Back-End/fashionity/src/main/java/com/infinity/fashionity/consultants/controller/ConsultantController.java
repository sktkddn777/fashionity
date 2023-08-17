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
            ConsultantListDTO.Request dto) {
        ConsultantListDTO.Response consultantListResponse = consultantService.getAllConsultants(dto);
        return new ResponseEntity<>(consultantListResponse, HttpStatus.OK);
    }

    // [공통] 컨설턴트 상세 정보 조회
    @GetMapping(value = "/{consultantNickname}")
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
    ) {
        UserReservationListDTO.Response userReservationListResponse = consultantService.getUserReservationsList(auth.getSeq());
        return new ResponseEntity<>(userReservationListResponse, HttpStatus.OK);
    }

    // [컨설턴트] 나의 예약 목록 조회
    @GetMapping(value = "/{consultantNickname}/reservations")
    public ResponseEntity<ConsultantReservationListDTO.Response> getConsultantReservationsList(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable("consultantNickname") String consultantNickname,
            ConsultantReservationListDTO.Request dto) {
//                ConsultantReservationListDTO.Request dto = ConsultantReservationListDTO.Request.builder()
//                        .consultantNickname(consultantNickname)
//                        .build();
            dto.setMemberSeq(auth.getSeq());
            dto.setConsultantNickname(consultantNickname);
        ConsultantReservationListDTO.Response consultantReservationsListResponse = consultantService.getConsultantReservationsList(auth.getSeq(), consultantNickname, dto);
//        UserReservationListDTO.Response userReservationListResponse = consultantService.getUserReservationsList(1l);
        return new ResponseEntity<>(consultantReservationsListResponse, HttpStatus.OK);
    }

    // [컨설턴트] 상세 예약 정보 조회
    @GetMapping(value = "/{consultantNickname}/reservations/{reservationSeq}")
    public ResponseEntity<ConsultantReservationInfoDTO.Response> getConsultantReservationDetail(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable("consultantNickname") String consultantNickname,
            @PathVariable("reservationSeq") Long reservationSeq,
            ConsultantReservationInfoDTO.Request dto) {
        dto.setMemberSeq(auth.getSeq());
        dto.setConsultantNickname(consultantNickname);
        dto.setReservationSeq(reservationSeq);
        ConsultantReservationInfoDTO.Response consultantReservationInfoResponse = consultantService.getConsultantReservationDetail(auth.getSeq(), consultantNickname, reservationSeq, dto);
        return new ResponseEntity<>(consultantReservationInfoResponse, HttpStatus.OK);
    }

    // [컨설턴트] 전체 리뷰 목록 조회
    @GetMapping(value = "/{consultantNickname}/reviews")
    public ResponseEntity<ConsultantReviewListDTO.Response> getConsultantReviewsList(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable("consultantNickname") String consultantNickname) {
        ConsultantReviewListDTO.Response consultantReviewsListResponse = consultantService.getConsultantReviewsList(auth.getSeq(), consultantNickname);
        return new ResponseEntity<>(consultantReviewsListResponse, HttpStatus.OK);
    }


    // [컨설턴트] 평점 통계, 수익 조회
//    @GetMapping(value = "/{consultantNickname}/statistics")
//    public ResponseEntity<ConsultantStatisticsDTO.Response> getConsultantStatistics(
//            @AuthenticationPrincipal JwtAuthentication auth,
//            @PathVariable("consultantNickname") String consultantNickname,
//            ConsultantStatisticsDTO.Request dto) {
//        dto.setMemberSeq(auth.getSeq());
//        dto.setConsultantNickname(consultantNickname);
//        ConsultantStatisticsDTO.Response consultantStatisticsResponse = consultantService.getConsultantStatistics(auth.getSeq(), consultantNickname, dto);
//        return new ResponseEntity<>(consultantStatisticsResponse, HttpStatus.OK);
//    }

    // [공통] 리뷰 작성
    @PostMapping(value = "{reservationSeq}/review")
    public ResponseEntity<ReviewSaveDTO.Response> postReview(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable("reservationSeq") Long reservationSeq,
            ReviewSaveDTO.Request dto) {
        dto.setMemberSeq(auth.getSeq());
        ReviewSaveDTO.Response reviewSaveResponse = consultantService.postReview(auth.getSeq(), reservationSeq, dto);
        return new ResponseEntity<>(reviewSaveResponse, HttpStatus.OK);
    }

    // [공통] 리뷰 수정
    @PutMapping(value = "/reviews/{reviewSeq}/edit")
    public ResponseEntity<ReviewUpdateDTO.Response> updateReview(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable("reviewSeq") Long reviewSeq,
            ReviewUpdateDTO.Request dto) {
        dto.setMemberSeq(auth.getSeq());
        dto.setReviewSeq(reviewSeq);
        ReviewUpdateDTO.Response reviewUpdateResponse = consultantService.updateReview(auth.getSeq(), reviewSeq, dto);
        return new ResponseEntity<>(reviewUpdateResponse, HttpStatus.OK);
    }

    // [공통] 리뷰 삭제
    @DeleteMapping(value = "/reviews/{reviewSeq}")
    public ResponseEntity<ReviewDeleteDTO.Response> deleteReview(
        @AuthenticationPrincipal JwtAuthentication auth,
        @PathVariable("reviewSeq") Long reviewSeq,
        ReviewDeleteDTO.Request dto){
        dto.setMemberSeq(auth.getSeq());
        dto.setReviewSeq(reviewSeq);
        ReviewDeleteDTO.Response reviewDeleteResponse = consultantService.deleteReview(auth.getSeq(), reviewSeq, dto);
        return new ResponseEntity<>(reviewDeleteResponse, HttpStatus.OK);
    }

    // [공통] 에약 상세 저회
    @GetMapping(value = "/reservations/{reservationSeq}")
    public ResponseEntity<UserReservationInfoDTO.Response> getUserReservationDetail(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable("reservationSeq") Long reservationSeq,
            UserReservationInfoDTO.Request dto){
//            dto.setMemberSeq(auth.getSeq());
        dto.setMemberSeq(auth == null ? null : auth.getSeq());
            dto.setReservationSeq(reservationSeq);
        UserReservationInfoDTO.Response userReservatoinInfoResponse = consultantService.getUserReservationDetail(auth.getSeq(), reservationSeq, dto);
        return new ResponseEntity<>(userReservatoinInfoResponse, HttpStatus.OK);

    }


    // 스케쥴 등록
    @PostMapping("reservation/schedule")
    public ResponseEntity<ScheduleDTO.Response> saveSchedule(
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestBody  ScheduleSaveDTO.Request dto){

        log.info("start");
        dto.setMemberSeq(auth == null ? null : auth.getSeq());
        ScheduleDTO.Response scheduleSaveResponse = consultantService.saveSchedule(dto);
        log.info(" = {}",scheduleSaveResponse);
        return new ResponseEntity<>(scheduleSaveResponse, HttpStatus.OK);
    }

    @DeleteMapping("reservation/schedule/{schedule_seq}")
    public ResponseEntity<ScheduleDeleteDTO.Response> deleteSchedule(
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestBody ScheduleDeleteDTO.Request dto,
            @PathVariable("schedule_seq") Long scheduleSeq){

        dto.setMemberSeq(auth == null ? null : auth.getSeq());

        ScheduleDeleteDTO.Response scheduleDeleteResponse = consultantService.deleteSchedule(dto, scheduleSeq);

        return new ResponseEntity<>(scheduleDeleteResponse, HttpStatus.OK);

    }

    /**
     * 유저가 예약을 하는데, 입력한 정보를 바탕으로 유저 정보 추가 업데이트 및 사진 등록
     */
    @PostMapping("reservation")
    public ResponseEntity<ConsultantReservationSaveDTO.Response> saveReservation(
            @AuthenticationPrincipal JwtAuthentication auth,
            ConsultantReservationSaveDTO.Request dto){
        dto.setMemberSeq(auth == null ? null : auth.getSeq());

        ConsultantReservationSaveDTO.Response response = consultantService.saveReservation(dto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("reservation/enter")
    public ResponseEntity<UserReservationInfoDTO.ReservationEnterResponse> getReservationEnterInfo(
          @AuthenticationPrincipal JwtAuthentication auth,
          @RequestParam Long reservationSeq
    ) {
        UserReservationInfoDTO.ReservationEnterResponse response = consultantService.getReservationEnterInfo(auth.getSeq(), reservationSeq);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("reservation")
    public ResponseEntity<ConsultantReservationSaveDTO.Response> saveConsultantImagesInReservation(
            @AuthenticationPrincipal JwtAuthentication auth,
            ConsultantReservationSaveDTO.ConsultantImageSaveRequest dto) {
        dto.setMemberSeq(auth == null ? null : auth.getSeq());

        ConsultantReservationSaveDTO.Response response = consultantService.saveConsultantImages(dto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/reservation/myschedule")
    public ResponseEntity<ScheduleDTO.Response> getConsultantScheduleByDate(
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestParam String dateTime
    ) {
        ScheduleDTO.Response response = consultantService.getSchedule(dateTime, auth.getSeq());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/reservation/myschedule")
    public ResponseEntity<Boolean> deleteConsultantSchedule(
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestParam Long scheduleSeq
            ) {
        consultantService.deleteSchedule(scheduleSeq);
        return ResponseEntity.ok(true);
    }
}

