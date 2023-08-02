package com.infinity.fashionity.consultants.repository;

import com.infinity.fashionity.consultants.dto.Review;
import com.infinity.fashionity.consultants.entity.ConsultantEntity;
import com.infinity.fashionity.consultants.entity.ReviewEntity;
import com.infinity.fashionity.consultants.entity.ScheduleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;

import javax.persistence.Id;
import java.util.List;

public interface ConsultantRepository extends JpaRepository<ConsultantEntity, Long> {

    // 전체 컨설턴트 조회
    Page<ConsultantEntity> findAll(Pageable pageable);

    // 평균 별점
    @Query("select coalesce(avg(r.grade),0) " +
            "from ReviewEntity r " +
            "left join r.reservation res " +
            "left join res.schedule s " +
            "left join s.consultant c " +
            "where c.seq = :consultantSeq")
    Float avgGrade(Long consultantSeq);

    // 전체 컨설팅 횟수
    @Query("select count(res.seq) " +
            "from ReservationEntity res " +
            "left join res.schedule s " +
            "left join s.consultant c " +
            "where c.seq = :consultantSeq")
    Integer totalCnt(Long consultantSeq);


    // 컨설턴트 정보 조회
    @Query("select c " +
            "from ConsultantEntity c " +
            "left join c.member m " +
            "where m.nickname = :consultantNickname")
    List<ConsultantEntity> findConsultantDetail(String consultantNickname);

    // 컨설턴트 별 리뷰 모음
    @Query("select r " +
            "from ReviewEntity r " +
            "left join fetch r.reservation res " +
            "left join res.schedule s " +
            "left join s.consultant c " +
            "where c.seq = :consultantSeq")
    List<ReviewEntity> findConsultantReviews(Long consultantSeq);

    @Query("select s " +
            "from ScheduleEntity s " +
            "left join s.consultant c " +
            "where c.seq = :consultantSeq")
    List<ScheduleEntity> findConsultantSchedules(Long consultantSeq);
}