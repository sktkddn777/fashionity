package com.infinity.fashionity.consultants.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    @JsonAlias(value = "image_seq")
    private Long imageSeq;

    @JsonAlias(value = "image_url")
    private String imageUrl;

//    // 예약
//    @JsonAlias(value = "reservation_seq")
//    private Long reservationSeq;


}
