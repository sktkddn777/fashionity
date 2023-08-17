package com.infinity.fashionity.consultants.repository;

import com.infinity.fashionity.consultants.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    @Query("select s from ScheduleEntity s where s.seq = :scheduleSeq")
    ScheduleEntity findBySeq(@Param("scheduleSeq") Long scheduleSeq);
}
