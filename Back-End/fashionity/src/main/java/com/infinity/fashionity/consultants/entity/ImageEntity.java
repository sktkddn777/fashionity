package com.infinity.fashionity.consultants.entity;

import com.infinity.fashionity.global.entity.CEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name="reservation_images")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageEntity extends CEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_seq")
    private Long seq;

    @Column(name = "image_url", unique = true, nullable = false, columnDefinition = "TEXT")
    private String url;

    @JoinColumn(name = "reservation_seq", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ReservationEntity reservation;

}
