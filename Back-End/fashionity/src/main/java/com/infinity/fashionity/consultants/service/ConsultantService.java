package com.infinity.fashionity.consultants.service;


import com.infinity.fashionity.consultants.dto.ConsultantDetailDTO;
import com.infinity.fashionity.consultants.dto.ConsultantListDTO;

import java.util.List;

public interface ConsultantService {
    List<ConsultantListDTO.Response> getAllConsultants();
    ConsultantDetailDTO.Response getConsultant();
}

