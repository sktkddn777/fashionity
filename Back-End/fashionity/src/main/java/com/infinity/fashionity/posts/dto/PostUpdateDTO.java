package com.infinity.fashionity.posts.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class PostUpdateDTO {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{

        @JsonIgnore
        private long memberSeq;

        @JsonIgnore
        private long postSeq;

        @Size(max = 500, message = "500자까지만 입력 가능합니다.")
        private String content;

        @NotBlank
        @Builder.Default
        private List<MultipartFile> images = new ArrayList<>();

        @Builder.Default
        private List<String> hashtag = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private boolean success;
    }
}
