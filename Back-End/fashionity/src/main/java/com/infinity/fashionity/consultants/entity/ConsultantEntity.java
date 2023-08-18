package com.infinity.fashionity.consultants.entity;

import com.infinity.fashionity.consultants.data.Level;
import com.infinity.fashionity.global.entity.CUDEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "consultant_infos")
@SQLDelete(sql = "UPDATE consultant_infos SET deleted_at = now() WHERE consultant_info_seq = ?")
@Where(clause = "deleted_at is null")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultantEntity extends CUDEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultant_info_seq")
    private Long seq;

    @Column(name = "consultant_nickname", length = 13, unique = true, nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "consultant_info_level", length = 20, unique = false, nullable = false)
    private Level level;

    @JoinColumn(name = "member_seq", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "consultant")
    @Builder.Default
    private List<ScheduleEntity> schedules = new ArrayList<>();
}

