package com.infinity.fashionity.consultants.repository;

import com.infinity.fashionity.consultants.dto.ConsultantReservationDetail;
import com.infinity.fashionity.consultants.dto.ConsultantReservationSummary;
import com.infinity.fashionity.consultants.dto.UserReservationSummary;
import com.infinity.fashionity.consultants.entity.ImageEntity;
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
            "where c.nickname = :consultantNickname")
    List<ConsultantReservationSummary> findConsultantReservations(String consultantNickname);

    @Query("select res " +
            "from ReservationEntity res " +
            "left join res.schedule s " +
            "left join s.consultant c " +
            "where c.nickname = :consultantNickname and res.seq = :reservationSeq")
    List<ConsultantReservationDetail> findConsultantReservation(String consultantNickname, Long reservationSeq);

    @Query("select i " +
            "from ReservationEntity res " +
            "left join res.images i " +
            "where res.seq = :reservationSeq")
    List<ImageEntity> findReservationImages(Long reservationSeq);
}
