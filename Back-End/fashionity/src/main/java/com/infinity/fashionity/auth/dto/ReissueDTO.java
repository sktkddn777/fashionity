package com.infinity.fashionity.auth.dto;

import lombok.*;

public class ReissueDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class Request {
        private String accessToken;
        private Long memberSeq;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class Response{

        private String accessToken;
        private String refreshToken;
    }

}

