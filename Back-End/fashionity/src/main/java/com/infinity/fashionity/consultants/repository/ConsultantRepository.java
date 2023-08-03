package com.infinity.fashionity.consultants.repository;



import com.infinity.fashionity.consultants.dto.ConsultantReviewSummary;
import org.springframework.stereotype.Repository;


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
            "where c.nickname = :consultantNickname")
    Float avgGrade(String consultantNickname);

    // 전체 컨설팅 횟수
    @Query("select count(res.seq) " +
            "from ReservationEntity res " +
            "left join res.schedule s " +
            "left join s.consultant c " +
            "where c.nickname = :consultantNickname")
    Integer totalCnt(String consultantNickname);


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
    List<ReviewEntity> findConsultantReviewsById(Long consultantSeq);

    @Query("select s " +
            "from ScheduleEntity s " +
            "left join s.consultant c " +
            "where c.seq = :consultantSeq")
    List<ScheduleEntity> findConsultantSchedules(Long consultantSeq);

    // 컨설턴트 전체 후기, 평점 조회
    @Query("select new com.infinity.fashionity.consultants.dto.ConsultantReviewSummary(res.seq, r.seq, r.createdAt, r.updatedAt, r.grade, r.content, m.nickname) " +
            "from ConsultantEntity c " +
            "left join c.schedules s " +
            "left join s.reservations res " +
            "left join res.review r " +
            "left join r. reservation res2 " +
            "left join res2.member m " +
            "where c.nickname = :consultantNickname and r.seq is not null and r.deletedAt is null " +
            "order by r.createdAt desc " )
    List<ConsultantReviewSummary> findConsultantReviewsByNickname(String consultantNickname);


    // 컨설턴트 삭제되지 않은 전체 후기 개수 조회
    @Query("select coalesce(count(r.seq),0) " +
            "from ConsultantEntity c " +
            "left join c.schedules s " +
            "left join s.reservations res " +
            "left join res.review r " +
            "where c.nickname = :consultantNickname and r.deletedAt is null")
    Integer totalUndeletedReviewCnt(String consultantNickname);

    // 컨설턴트 삭제된 전체 후기 개수 조회
    @Query("select coalesce(count(r.seq),0) " +
            "from ConsultantEntity c " +
            "left join c.schedules s " +
            "left join s.reservations res " +
            "left join res.review r " +
            "where c.nickname = :consultantNickname and r.deletedAt is not null")
    Integer totalDeletedReviewCnt(String consultantNickname);

    // 컨설턴트 현재까지 전체 수익 조회
    @Query("select coalesce(sum(res.price),0) " +
            "from ConsultantEntity c " +
            "left join c.schedules s " +
            "left join s.reservations res " +
            "where c.nickname = :consultantNickname and res.deletedAt is null and res.date >= current_timestamp ")
    Integer totalSalary(String consultantNickname);

}