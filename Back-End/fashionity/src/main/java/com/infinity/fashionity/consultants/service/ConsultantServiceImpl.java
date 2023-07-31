package com.infinity.fashionity.consultants.service;

import com.infinity.fashionity.consultants.dto.Consultant;
import com.infinity.fashionity.consultants.dto.ConsultantListDTO;
import com.infinity.fashionity.consultants.dto.Review;
import com.infinity.fashionity.consultants.entity.ConsultantEntity;
import com.infinity.fashionity.consultants.entity.ReviewEntity;
import com.infinity.fashionity.consultants.repository.ConsultantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultantServiceImpl implements ConsultantService {
    private final ConsultantRepository consultantRepository;
    // 나머지 repository 연결 필요

    @Override
    @Transactional
    // @Transactional(readOnly = true)
    public ConsultantListDTO.Response getAllConsultants(ConsultantListDTO.Request dto) {

        int page = dto.getPage();
        int size = dto.getSize();

        // 내림차순 정렬
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // 최신순으로 컨설턴트 가져오기
        Page<ConsultantEntity> result = consultantRepository.findAll(pageable);
        log.info(result.toString());

        List<Consultant> consultants = result.getContent().stream()
                .map(entity -> {

//                    List<Object[]> reviewEntities = consultantRepository.findConsultantReviews(entity.getSeq());
//                    List<Review> reviews = reviewEntities.stream().map(en->{
//                        Long reviewSeq = ((BigInteger)en[0]).longValue();
//                        Float reviewGrade = (Float)en[1];
//                        String reviewContent = (String)en[2];
//                        LocalDateTime createdAt = (LocalDateTime)en[3];
//                        LocalDateTime updatedAt = (LocalDateTime)en[4];
//                        LocalDateTime deletedAt = (LocalDateTime)en[5];
//                        Long reservationSeq = ((BigInteger)en[6]).longValue();
//
//                        Review review = Review.builder()
//                                .reviewSeq(reviewSeq)
//                                .reviewGrade(reviewGrade)
//                                .reviewContent(reviewContent)
//                                .createdAt(createdAt)
//                                .updatedAt(updatedAt)
//                                .reservationSeq(reservationSeq)
//                                .build();
//
//                        return review;
//                    }).collect(Collectors.toList());


                    /**
                     * entity -> dto 로 바꾸는 과정 필요
                     * */
                    return Consultant.builder()
                            .seq(entity.getSeq())
                            .nickname(entity.getMember().getNickname())
                            .profileUrl(entity.getMember().getProfileUrl())
                            .level(entity.getLevel().toString())
                            .avgRating(consultantRepository.avgRating(entity.getSeq()))
                            .totalCnt((consultantRepository.totalCnt(entity.getSeq())))
                            .reviews(consultantRepository.findConsultantReviews(entity.getSeq()))
                            //.schedules()
                            .build();
                })
                .collect(Collectors.toList());

        return ConsultantListDTO.Response.builder()
                .prev(result.hasPrevious())
                .next(result.hasNext())
                .consultants(consultants)
                .page(result.getNumber())
                .build();
    }

}