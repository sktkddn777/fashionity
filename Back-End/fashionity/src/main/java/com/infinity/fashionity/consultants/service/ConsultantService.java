package com.infinity.fashionity.consultants.service;


import com.infinity.fashionity.consultants.dto.ConsultantInfoDTO;
import com.infinity.fashionity.consultants.dto.ConsultantListDTO;
import com.infinity.fashionity.consultants.dto.ConsultantReservationListDTO;
import com.infinity.fashionity.consultants.dto.UserReservationListDTO;
import com.infinity.fashionity.security.dto.JwtAuthentication;
import org.springframework.transaction.annotation.Transactional;

public interface ConsultantService {
    ConsultantListDTO.Response getAllConsultants(ConsultantListDTO.Request dto);

    @Transactional(readOnly = true)
    ConsultantInfoDTO.Response getConsultantDetail(ConsultantInfoDTO.Request dto);

    @Transactional(readOnly = true)
    UserReservationListDTO.Response getUserReservationsList(Long memberSeq);

    @Transactional(readOnly = true)
    ConsultantReservationListDTO.Response getConsultantReservationsList(Long memberSeq, String consultantNickname);
}

