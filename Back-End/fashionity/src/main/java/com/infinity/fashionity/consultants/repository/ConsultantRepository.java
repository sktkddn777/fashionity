package com.infinity.fashionity.consultants.repository;



import com.infinity.fashionity.consultants.dto.ConsultantReviewSummary;
import org.springframework.data.repository.query.Param;


import com.infinity.fashionity.consultants.entity.ConsultantEntity;
import com.infinity.fashionity.consultants.entity.ReviewEntity;
import com.infinity.fashionity.consultants.entity.ScheduleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ConsultantRepository extends JpaRepository<ConsultantEntity, Long> {

    // 전체 컨설턴트 조회
    Page<ConsultantEntity> findAll(Pageable pageable);

    @Query("select c from ConsultantEntity c " +
            "where c.nickname like %:nickname%")
    Page<ConsultantEntity> findAllWithNickname(@Param("nickname") String nickname,Pageable pageable);

    // 평균 별점
    @Query("select coalesce(avg(r.grade),0) " +
            "from ReviewEntity r " +
            "left join r.reservation res " +
            "left join res.schedule s " +
            "left join s.consultant c " +
            "where c.nickname = :consultantNickname")
    Float avgGrade(@Param("consultantNickname") String consultantNickname);

    // 전체 컨설팅 횟수
    @Query("select count(res.seq) " +
            "from ReservationEntity res " +
            "left join res.schedule s " +
            "left join s.consultant c " +
            "where c.nickname = :consultantNickname")
    Integer totalCnt(@Param("consultantNickname") String consultantNickname);


    // 컨설턴트 정보 조회
    @Query("select c " +
            "from ConsultantEntity c " +
            "left join c.member m " +
            "where m.nickname = :consultantNickname")
    List<ConsultantEntity> findConsultantDetail(@Param("consultantNickname") String consultantNickname);

    // 컨설턴트 별 리뷰 모음
    @Query("select r " +
            "from ReviewEntity r " +
            "left join fetch r.reservation res " +
            "left join res.schedule s " +
            "left join s.consultant c " +
            "where c.seq = :consultantSeq")
    List<ReviewEntity> findConsultantReviewsById(@Param("consultantSeq") Long consultantSeq);

    @Query("select s " +
            "from ScheduleEntity s " +
            "left join s.consultant c " +
            "where c.seq = :consultantSeq")
    List<ScheduleEntity> findConsultantSchedules(@Param("consultantSeq")Long consultantSeq);

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
    List<ConsultantReviewSummary> findConsultantReviewsByNickname(@Param("consultantNickname") String consultantNickname);


    // 컨설턴트 삭제되지 않은 전체 후기 개수 조회
    @Query("select coalesce(count(r.seq),0) " +
            "from ConsultantEntity c " +
            "left join c.schedules s " +
            "left join s.reservations res " +
            "left join res.review r " +
            "where c.nickname = :consultantNickname and r.deletedAt is null")
    Integer totalUndeletedReviewCnt(@Param("consultantNickname")String consultantNickname);

    // 컨설턴트 삭제된 전체 후기 개수 조회
    @Query("select coalesce(count(r.seq),0) " +
            "from ConsultantEntity c " +
            "left join c.schedules s " +
            "left join s.reservations res " +
            "left join res.review r " +
            "where c.nickname = :consultantNickname and r.deletedAt is not null")
    Integer totalDeletedReviewCnt(@Param("consultantNickname") String consultantNickname);


    @Query("select m.seq " +
            "from ConsultantEntity  c " +
            "left join c.member m " +
            "where c.nickname = :consultantNickname ")
    Long findConsultantMemberSeq(@Param("consultantNickname") String consultantNickname);

    @Query("select c " +
            "from ConsultantEntity c " +
            "where c.nickname = :consultantNickname")
    Optional<ConsultantEntity> findByNickname(@Param("consultantNickname")String consultantNickname) ;

    Optional<ConsultantEntity> findByMemberSeq(Long MemberSeq);
}