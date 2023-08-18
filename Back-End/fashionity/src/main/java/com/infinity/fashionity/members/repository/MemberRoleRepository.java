package com.infinity.fashionity.members.repository;

import com.infinity.fashionity.members.entity.MemberRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRoleEntity, Long> {
}
