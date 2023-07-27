package com.infinity.fashionity.consultants.repository;

import com.infinity.fashionity.consultants.entity.ConsultantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConsultantRepository extends JpaRepository<ConsultantEntity, Long> {

    // 전체 컨설턴트 조회
    @Query("select c from ConsultantEntity c join fetch c.member" )
    List<ConsultantEntity> findAll();

    // 평균 별점
    @Query(value = "SELECT "
                        + "IFNULL(AVG(REVIEW_GRADE),0) "
                        + "FROM CONSULTANT_INFOS "
                        + "LEFT JOIN CONSULTANT_SCHEDULE ON CONSULTANT_INFOS.CONSULTANT_INFO_SEQ = CONSULTANT_SCHEDULE.CONSULTANT_INFO_SEQ "
                        + "LEFT JOIN RESERVATIONS ON CONSULTANT_SCHEDULE.SCHEDULE_SEQ = RESERVATIONS.SCHEDULE_SEQ "
                        + "LEFT JOIN REVIEWS ON RESERVATIONS.RESERVATION_SEQ = REVIEWS.RESERVATION_SEQ "
                        + "WHERE CONSULTANT_INFOS.CONSULTANT_INFO_SEQ = :consultantSeq", nativeQuery = true)
    Float avgRating(Long consultantSeq);

    // 전체 컨설팅 횟수
    @Query(value = "SELECT "
            + "COUNT(RESERVATION_SEQ)"
            + "FROM CONSULTANT_INFOS "
            + "LEFT JOIN CONSULTANT_SCHEDULE ON CONSULTANT_INFOS.CONSULTANT_INFO_SEQ = CONSULTANT_SCHEDULE.CONSULTANT_INFO_SEQ "
            + "LEFT JOIN RESERVATIONS ON CONSULTANT_SCHEDULE.SCHEDULE_SEQ = RESERVATIONS.SCHEDULE_SEQ "
            + "WHERE CONSULTANT_INFOS.CONSULTANT_INFO_SEQ = :consultantSeq", nativeQuery = true)

    Integer totalCnt(Long consultantSeq);


    // 개별 컨설턴트 조회
    @Query("select c from ConsultantEntity c join fetch c.member where c.seq = :consultantSeq " )
    List<ConsultantEntity> findConsultant(Long consultantSeq);
}