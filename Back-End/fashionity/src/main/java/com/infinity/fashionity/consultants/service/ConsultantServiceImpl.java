package com.infinity.fashionity.consultants.service;

import com.infinity.fashionity.consultants.dto.ConsultantInfoDTO;
import com.infinity.fashionity.consultants.dto.ConsultantDetail;
import com.infinity.fashionity.consultants.dto.ConsultantSummary;
import com.infinity.fashionity.consultants.dto.ConsultantListDTO;
import com.infinity.fashionity.consultants.entity.ConsultantEntity;
import com.infinity.fashionity.consultants.repository.ConsultantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ConsultantServiceImpl implements ConsultantService {
    private final ConsultantRepository consultantRepository;

    @Override
    @Transactional(readOnly = true)
    public ConsultantListDTO.Response getAllConsultants(ConsultantListDTO.Request dto) {

        int page = dto.getPage();
        int size = dto.getSize();
        List<ConsultantSummary> consultantSummaries = new ArrayList<>();

        // 내림차순 정렬
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // 최신순으로 컨설턴트 가져오기
        Page<ConsultantEntity> result = consultantRepository.findAll(pageable);


        result.stream().forEach(entity -> {
            ConsultantSummary consultantSummary = ConsultantSummary.builder()
                    .seq(entity.getSeq())
                    .nickname(entity.getMember().getNickname())
                    .profileUrl(entity.getMember().getProfileUrl())
                    .level(entity.getLevel())
                    .avgGrade(consultantRepository.avgGrade(entity.getSeq()))
                    .totalCnt(consultantRepository.totalCnt(entity.getSeq()))
                    .build();
            consultantSummaries.add(consultantSummary);
        });

        return ConsultantListDTO.Response.builder()
                .prev(result.hasPrevious())
                .next(result.hasNext())
                .page(result.getNumber())
                .consultants(consultantSummaries)
                .build();
    }


//    @Override
    @Transactional(readOnly = true)
    public ConsultantInfoDTO.Response getConsultantDetail(ConsultantInfoDTO.Request dto){

        String consultantNickname = dto.getConsultantNickname();
        List<ConsultantDetail> consultantDetails = new ArrayList<>();

        List<ConsultantEntity> result = consultantRepository.findConsultantDetail(consultantNickname);

        result.forEach(entity -> {
            ConsultantDetail consultantDetail = ConsultantDetail.builder()
                    .seq(entity.getSeq())
                    .nickname(entity.getMember().getNickname())
                    .profileUrl(entity.getMember().getProfileUrl())
                    .level(entity.getLevel())
                    .avgGrade(consultantRepository.avgGrade(entity.getSeq()))
                    .totalCnt(consultantRepository.totalCnt(entity.getSeq()))
                    .reviews(consultantRepository.consultantReviews(entity.getSeq()))
                    .build();
            consultantDetails.add(consultantDetail);
        });
        return ConsultantInfoDTO.Response.builder()
                .consultant(consultantDetails)
                .build();


    }
}





