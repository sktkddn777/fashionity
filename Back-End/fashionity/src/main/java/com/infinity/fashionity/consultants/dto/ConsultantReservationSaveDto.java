package com.infinity.fashionity.consultants.dto;

import com.infinity.fashionity.members.data.Gender;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


public class ConsultantReservationSaveDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @JsonIgnore
        private Long memberSeq;

        private String reservationDetail;

        @Builder.Default
        private List<MultipartFile> images = new ArrayList<>();
        private Gender memberGender;
        private Long scheduleSeq;
        private Float memberHeight;
        private Float memberWeight;
        private Integer memberAge;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private Long reservationSeq;
        private boolean success;

    }
}
