package com.infinity.fashionity.consultants.entity;

import com.infinity.fashionity.global.entity.CEntity;
import jdk.vm.ci.meta.Local;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultant_schedule")
@DynamicInsert
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleEntity extends CEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_seq")
    private Long seq;


    @Column(name = "availabe_datetime", unique = false, nullable = false)
    private LocalDateTime availableDatetime;

    @ColumnDefault("true")
    @Column(name="is_available", unique = false, nullable = false)
    private Boolean isAvailable;

    @Column(name = "canceled_at", unique = false, nullable = true)
    private LocalDateTime canceledAt;

    @Column(name="cancel_reason", length=200, unique = false, nullable = true)
    private String cancelReason;

    @JoinColumn(name="consultant_info_seq", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ConsultantEntity consultant;


}
