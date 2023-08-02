package com.infinity.fashionity.consultants.controller;

import com.infinity.fashionity.consultants.dto.ConsultantInfoDTO;
import com.infinity.fashionity.consultants.dto.ConsultantListDTO;
import com.infinity.fashionity.consultants.dto.UserReservationListDTO;
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
    // [유저] 전체 컨설턴트 목록 조회
    @GetMapping
    public ResponseEntity<ConsultantListDTO.Response> getAllConsultants(ConsultantListDTO.Request dto) {
        ConsultantListDTO.Response consultantListResponse = consultantService.getAllConsultants(dto);
        return new ResponseEntity<>(consultantListResponse, HttpStatus.OK);}

    @GetMapping(value="/{consultantNickname}")
    public ResponseEntity<ConsultantInfoDTO.Response> getConsultantDetail(ConsultantInfoDTO.Request dto){
        ConsultantInfoDTO.Response consultantInfoResponse = consultantService.getConsultantDetail(dto);
        return new ResponseEntity<>(consultantInfoResponse, HttpStatus.OK);
    }
    @GetMapping(value = "/reservations")
    public ResponseEntity<UserReservationListDTO.Response> getUserReservationsList(
            @AuthenticationPrincipal JwtAuthentication auth
    ){
        UserReservationListDTO.Response userReservationListResponse = consultantService.getUserReservationsList(auth.getSeq());
//        UserReservationListDTO.Response userReservationListResponse = consultantService.getUserReservationsList(1l);
        return new ResponseEntity<>(userReservationListResponse, HttpStatus.OK);
    }
    }

