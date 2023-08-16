package com.infinity.fashionity.consultants.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infinity.fashionity.members.data.Gender;
import com.infinity.fashionity.members.data.PersonalColor;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ConsultantReservationSaveDTO {
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        @JsonIgnore
        private Long memberSeq;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime availableDateTime;

        private String consultantNickname;

        private PersonalColor personalColor;

        private Gender gender;

        @Builder.Default
        private List<MultipartFile> images = new ArrayList<>();

        private Float height;

        private Float width;

        private Integer age;

        private String detail;

    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private Boolean success;
        private Long reservationSeq;

    }
}
