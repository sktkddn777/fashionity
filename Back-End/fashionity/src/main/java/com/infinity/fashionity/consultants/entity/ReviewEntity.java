package com.infinity.fashionity.consultants.entity;

import com.infinity.fashionity.global.entity.CUDEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Table(name="reservation_images")
@SQLDelete(sql = "UPDATE Reviews SET deleted_at = now() WHERE seq = ?")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity extends CUDEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_seq")
    private long seq;

    @Column(name = "review_content")
    private String content;

    @Column(name = "review_grade")
    private float grade;

    @JoinColumn(name = "reservation_seq")
    @OneToOne
    private ReservationEntity reservation;


}
