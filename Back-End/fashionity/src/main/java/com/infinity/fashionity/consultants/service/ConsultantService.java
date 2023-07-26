package com.infinity.fashionity.consultants.service;


import com.infinity.fashionity.consultants.dto.ConsultantDTO;

import java.util.List;

public interface ConsultantService {
    List<ConsultantDTO.Response> allConsultants();
}
