package com.infinity.fashionity.consultants.repository;

import com.infinity.fashionity.consultants.dto.UserReservationSummary;
import com.infinity.fashionity.consultants.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    @Query("select new com.infinity.fashionity.consultants.dto.UserReservationSummary(res.seq, res.date, m2.profileUrl, m2.nickname) " +
            "from MemberEntity m " +
            "left join m.reservations res " +
            "left join res.schedule s " +
            "left join s.consultant c " +
            "left join c.member m2 " +
            "where m.seq = :memberSeq")
    List<UserReservationSummary> findUserReservations(Long memberSeq);
}
