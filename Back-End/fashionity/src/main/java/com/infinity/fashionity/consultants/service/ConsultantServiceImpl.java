package com.infinity.fashionity.consultants.service;

import com.infinity.fashionity.consultants.dto.ConsultantDTO;
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
    public List<ConsultantDTO.Response> allConsultants() {
        List<ConsultantDTO.Response> consultants = new ArrayList<>();

        List<ConsultantEntity> all = consultantRepository.findAll();

        for (ConsultantEntity a : all)
            consultants.add(ConsultantDTO.Response.builder()
                    .seq(a.getSeq())
                    .nickname(a.getMember().getNickname())
                    .profileUrl(a.getMember().getProfileUrl())
                    .level(a.getLevel())
                    .build());
        log.info("consultant=" + consultants);
    return consultants ;
    }


}
