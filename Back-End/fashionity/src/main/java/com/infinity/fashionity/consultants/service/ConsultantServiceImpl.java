package com.infinity.fashionity.consultants.service;

import com.infinity.fashionity.consultants.dto.ConsultantDetailDTO;
import com.infinity.fashionity.consultants.dto.ConsultantListDTO;
import com.infinity.fashionity.consultants.entity.ConsultantEntity;
import com.infinity.fashionity.consultants.repository.ConsultantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultantServiceImpl implements ConsultantService {
    private final ConsultantRepository consultantRepository;

    @Override
    public List<ConsultantListDTO.Response> getAllConsultants() {
        List<ConsultantListDTO.Response> consultants = new ArrayList<>();

        List<ConsultantEntity> all = consultantRepository.findAll();

//        all -> 모든 컨설턴트 정보 + member가지고 옴

        for (ConsultantEntity a : all)
            consultants.add(ConsultantListDTO.Response.builder()
                    .seq(a.getSeq())
                    .nickname(a.getMember().getNickname())
                    .profileUrl(a.getMember().getProfileUrl())
                    .level(a.getLevel().toString())
                    .avgRating(consultantRepository.avgRating(a.getSeq()))
                    .totalCnt(consultantRepository.totalCnt(a.getSeq()))
                    .build()
            );

        log.info("consultant=" + consultants);
    return consultants ;
    }

    @Override
    public ConsultantDetailDTO.Response getConsultant() {

        return null;
    }


}