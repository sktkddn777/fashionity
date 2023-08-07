package com.infinity.fashionity.consultants.service;


import com.infinity.fashionity.consultants.dto.*;
import org.springframework.transaction.annotation.Transactional;

public interface ConsultantService {

    // [공통] 컨설턴트 목록 조회
    @Transactional(readOnly = true)
    ConsultantListDTO.Response getAllConsultants(Long memberSeq, ConsultantListDTO.Request dto);

    // [공통] 컨설턴트 상세 정보 조회
    @Transactional(readOnly = true)
    ConsultantInfoDTO.Response getConsultantDetail(Long memberSeq, String consultantNickname);

    @Transactional(readOnly = true)
    UserReservationListDTO.Response getUserReservationsList(Long memberSeq);

    @Transactional(readOnly = true)
    ConsultantReservationListDTO.Response getConsultantReservationsList(Long memberSeq, String consultantNickname);

    @Transactional(readOnly = true)
    ConsultantReservationInfoDTO.Response getConsultantReservationDetail(Long memberSeq, String consultantNickname, Long reservationSeq);

    @Transactional(readOnly = true)
    ConsultantReviewListDTO.Response getConsultantReviewsList(Long memberSeq, String consultantNickname);

    @Transactional(readOnly = true)
    ConsultantStatisticsDTO.Response getConsultantStatistics(Long memberSeq, String consultantNickname);
}

