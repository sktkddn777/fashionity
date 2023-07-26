package com.infinity.fashionity.consultants.repository;

import com.infinity.fashionity.consultants.entity.ConsultantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConsultantRepository extends JpaRepository<ConsultantEntity, Long> {


    // ConsultantEntity = c 객체 c.member
    // 컨설턴트를 찾을 기준 = 닉네임
    @Query("select c from ConsultantEntity c join fetch c.member where c.seq = :seq" )
    // 전체 컨설턴트 조회
    List<ConsultantEntity> findAll();
    // 개별 컨설턴트 조회
    Optional<ConsultantEntity> findById(Long seq);

}