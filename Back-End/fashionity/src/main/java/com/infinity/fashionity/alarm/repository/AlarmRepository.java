package com.infinity.fashionity.alarm.repository;

import com.infinity.fashionity.alarm.entity.AlarmEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<AlarmEntity,Long> {
    List<AlarmEntity> findAllByOwnerOrderByCreatedAtDesc(MemberEntity owner);
}
