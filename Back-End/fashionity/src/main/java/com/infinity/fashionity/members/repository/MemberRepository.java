package com.infinity.fashionity.members.repository;

import com.infinity.fashionity.members.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findById(String id);

    Optional<MemberEntity> findByEmail(String email);

    Optional<MemberEntity> findByNickname(String nickname);

    Optional<MemberEntity> findByIdAndEmail(String id, String email);
    @Query("select m from MemberEntity m join fetch m.memberRoles where m.email = :email")
    MemberEntity findByEmailWithRole(String email);
}














