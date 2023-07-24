package com.infinity.fashionity.consultants.entity;

import com.infinity.fashionity.global.entity.CUDEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="reservations")
@SQLDelete(sql = "UPDATE Reservations SET deleted_at = now() WHERE reservation_seq = ?")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationEntity extends CUDEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_seq")
    private long seq;

    @JoinColumn(name = "schedule_seq", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private ScheduleEntity schedule;

    @JoinColumn(name = "member_seq", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @Column(name = "reservation_detail")
    private String detail;

    @Column(name = "reservation_date")
    private LocalDateTime date;


}
