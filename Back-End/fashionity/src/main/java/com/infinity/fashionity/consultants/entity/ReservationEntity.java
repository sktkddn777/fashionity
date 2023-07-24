package com.infinity.fashionity.consultants.entity;

import com.infinity.fashionity.global.entity.CUDEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.posts.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Table(name="reservations")
@SQLDelete(sql = "UPDATE Reservations SET deleted_at = now() WHERE seq = ?")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationEntity extends CUDEntity {

    @Id
    @JoinColumn(name = "schedule_seq")
    @ManyToOne(fetch = FetchType.LAZY)
    private ScheduleEntity schedule;

    @Id
    @JoinColumn(name = "post_seq")
    @ManyToOne(fetch = FetchType.LAZY)
    private ConsultantEntity consultant;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_seq")
    private long seq;


    @Column(name = "reservation_detail")
    private String detail;




//    @Id
//    @JoinColumn(name = "post_seq")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private PostEntity post;
//
//    @Id
//    @JoinColumn(name = "member_seq")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private MemberEntity member;

//
}
