package com.infinity.fashionity.members.repository;

import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.entity.MemberRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRoleRepository extends JpaRepository<MemberRoleEntity, Long> {
}
