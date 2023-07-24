package com.infinity.fashionity.members.entity;

import com.infinity.fashionity.members.data.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "member_types")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_type_seq")
    private Long seq;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type_role", unique = true, nullable = false, length = 20)
    private MemberRole memberRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq", nullable = false)
    private MemberEntity member;
}
