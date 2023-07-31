package com.infinity.fashionity.consultants.repository;

import com.infinity.fashionity.consultants.dto.Review;
import com.infinity.fashionity.consultants.entity.ConsultantEntity;
import com.infinity.fashionity.consultants.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    // 컨설턴트 당 리뷰 모음
    @Query("select r from ReviewEntity r left join fetch r.reservation res left join fetch res.schedule s left join fetch s.consultant c where c.seq = :consultantSeq ")
//    @Query(value="SELECT r.review_seq as review_seq, r.review_grade as review_grade, r.review_content as review_content, " +
//            "r.created_at as created_at, r.updated_at as updated_at, r.deleted_at as deleted_at, r.reservation_seq as reservation_seq " +
//            "from reviews r " +
//            "left join reservations res on r.reservation_seq = res.reservation_seq " +
//            "left join consultant_schedule cs on cs.schedule_seq = res.schedule_seq " +
//            "left join consultant_infos ci on ci.consultant_info_seq = cs.consultant_info_seq " +
//            "where ci.consultant_info_seq = :consultantSeq",nativeQuery = true)
    List<ReviewEntity> findConsultantReviews(@Param("consultantSeq") Long consultantSeq);

    // 개별 컨설턴트 조회
    @Query("select c from ConsultantEntity c join fetch c.member where c.seq = :consultantSeq " )
    List<ConsultantEntity> findConsultant(Long consultantSeq);
}