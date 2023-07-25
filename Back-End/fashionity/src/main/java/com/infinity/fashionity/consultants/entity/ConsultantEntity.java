package com.infinity.fashionity.consultants.entity;

import com.infinity.fashionity.global.entity.CUDEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import javax.persistence.*;


@Entity
@Table(name = "consultant_infos")
@SQLDelete(sql = "UPDATE consultant_infos SET deleted_at = now() WHERE consultant_info_seq = ?")
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ConsultantEntity extends CUDEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultant_info_seq")
    private Long seq;

    @Column(name = "consultant_info_level", length = 20, unique = false, nullable = false)
    private String level;

    @JoinColumn(name = "member_seq", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private MemberEntity member;
}

