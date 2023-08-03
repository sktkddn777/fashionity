package com.infinity.fashionity.consultants.entity;

import com.infinity.fashionity.consultants.dto.Image;
import com.infinity.fashionity.global.entity.CUDEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import javax.persistence.*;
import javax.validation.constraints.Min;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="reservations")
@SQLDelete(sql = "UPDATE Reservations SET deleted_at = now() WHERE reservation_seq = ?")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReservationEntity extends CUDEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_seq")
    private Long seq;

    @JoinColumn(name = "schedule_seq", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private ScheduleEntity schedule;

    @JoinColumn(name = "member_seq", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    // 예약 일시
    @Column(name = "reservation_date", unique = false, nullable = false)
    private LocalDateTime date;

    // 예약 상세
    @Column(name = "reservation_detail", unique = false, nullable = true, length = 200)
    private String detail;

    // 컨설팅 가격
    @Min(value = 0)
    @Column(name = "reservation_price", unique = false, nullable = false)
    private Integer price;

    // 예약 사진
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reservation")
    @Builder.Default
    private List<ImageEntity> images = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "reservation")
    private ReviewEntity review;

}
