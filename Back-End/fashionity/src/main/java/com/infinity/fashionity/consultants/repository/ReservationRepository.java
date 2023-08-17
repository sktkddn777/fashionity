package com.infinity.fashionity.consultants.repository;

import com.infinity.fashionity.consultants.dto.*;
import com.infinity.fashionity.consultants.entity.MemberImageEntity;
import com.infinity.fashionity.consultants.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@SuppressWarnings("ALL")
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    @Query("select new com.infinity.fashionity.consultants.dto.UserReservationSummary(res.seq, res.date, m2.profileUrl, c.nickname) " +
            "from MemberEntity m " +
            "left join m.reservations res " +
            "left join res.schedule s " +
            "left join s.consultant c " +
            "left join c.member m2 " +
            "where m.seq = :memberSeq")
    List<UserReservationSummary> findUserReservations(Long memberSeq);

    @Query("select new com.infinity.fashionity.consultants.dto.ConsultantReservationSummary(c.nickname, res.seq, res.date, m.nickname) " +
            "from ConsultantEntity c " +
            "left join c.schedules s " +
            "left join s.reservations res " +
            "left join res.member m " +
            "where c.nickname = :consultantNickname and res.deletedAt is null and res.seq is not null")
    List<ConsultantReservationSummary> findConsultantReservations(String consultantNickname);

    @Query("select new com.infinity.fashionity.consultants.dto.ConsultantReservationDetail(res.seq, res.member.nickname, res.date, res.detail ) " +
            "from ReservationEntity res " +
            "left join res.schedule s " +
            "left join s.consultant c " +
            "where c.nickname = :consultantNickname and res.seq = :reservationSeq")
    List<ConsultantReservationDetail> findConsultantReservation(String consultantNickname, Long reservationSeq);

    @Query("select new com.infinity.fashionity.consultants.dto.UserReservationDetail(res.seq, c.nickname, res.date, res.detail) " +
            "from ReservationEntity res " +
            "left join res.schedule s " +
            "left join res.member m " +
            "left join s.consultant c " +
            "where m.seq = :memberSeq and res.seq = :reservationSeq")
    List<UserReservationDetail> findUserReservation(Long memberSeq, Long reservationSeq);

    @Query("select i " +
            "from ReservationEntity res " +
            "join MemberImageEntity i on i.reservation = res " +
            "where res.seq = :reservationSeq")
    List<MemberImageEntity> findReservationImages(Long reservationSeq);




}
