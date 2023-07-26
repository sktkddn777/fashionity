package com.infinity.fashionity.consultants.entity;

import com.infinity.fashionity.global.entity.CUDEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Table(name="reviews")
@SQLDelete(sql = "UPDATE Reviews SET deleted_at = now() WHERE review_seq = ?")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity extends CUDEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_seq")
    private Long seq;

    @Column(name = "review_content", unique = false, nullable = true, length = 200)
    private String content;

    @Column(name = "review_grade", unique = false, nullable = true)
    private float grade;

    @JoinColumn(name = "reservation_seq")
    @OneToOne
    private ReservationEntity reservation;


}
