package com.infinity.fashionity.posts.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PostSaveDTO {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @JsonIgnore
        private Long memberSeq;
        @NotEmpty
        @Builder.Default
        private ArrayList<String> images = new ArrayList<>();
        @NotBlank
        private String content;
        @Builder.Default
        private ArrayList<String> hashtag = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private Long postSeq;
        private boolean success;
    }
}
