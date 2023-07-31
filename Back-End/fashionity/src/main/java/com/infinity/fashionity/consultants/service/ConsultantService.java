package com.infinity.fashionity.consultants.service;


import com.infinity.fashionity.consultants.dto.ConsultantListDTO;

public interface ConsultantService {
    ConsultantListDTO.Response getAllConsultants(ConsultantListDTO.Request dto);
//    Consultant getConsultant();
}

