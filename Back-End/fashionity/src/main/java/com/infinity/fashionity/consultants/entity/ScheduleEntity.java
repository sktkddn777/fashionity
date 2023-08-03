package com.infinity.fashionity.consultants.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infinity.fashionity.global.entity.CEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "available_datetime", unique = false, nullable = false)
    private LocalDateTime availableDateTime;

    @ColumnDefault("true")
    @Column(name="is_available", unique = false, nullable = false)
    private Boolean isAvailable;

    @Column(name = "canceled_at", unique = false, nullable = true)
    private LocalDateTime canceledAt;

    @Column(name="cancel_reason", length=200, unique = false, nullable = true)
    private String cancelReason;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "schedule")
    @Builder.Default
    private ReservationEntity reservations = new ReservationEntity();

    @JoinColumn(name="consultant_info_seq", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ConsultantEntity consultant;


}
