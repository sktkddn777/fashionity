package com.infinity.fashionity.consultants.entity;

import com.infinity.fashionity.global.entity.CUDEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultant_schedule")
@SQLDelete(sql="UPDATE consultant_schedule SET deleted_at = now() WHERE consultant_schedule_seq = ?")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleEntity extends CUDEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_seq")
    private Long seq;

    @Id
    @JoinColumn(name="consultant_info_seq", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name="consultant_info_seq", nullable = false)
    private ConsultantEntity consultant;

    @Column(name = "availabe_datetime", unique = false, nullable = false)
    private LocalDateTime availableDatetime;

    @Column(name="is_available", unique = false, nullable = false)
    private Boolean isAvailable;

    @Column(name="cancel_reason", length=200, unique = false, nullable = true)
    private String cancelReason;



}
