package com.infinity.fashionity.posts.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class PostListDTO {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Request{
        @JsonIgnore
        private Long memberSeq;
        @Builder.Default
        private int page = 0;
        @Builder.Default
        private int size = 12;
        //sorting기준, popular or latest
        private String s;
        //hashtag 입력값
        private String h;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Response{
        @Builder.Default
        private List<Post> posts = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Post{
        @JsonProperty(value = "post_seq")
        private Long postSeq;
        private List<String> images;
        private String content;
        private String name;
        @JsonProperty(value = "profile_img")
        private String profileImg;
        private boolean liked;
        @JsonProperty(value = "like_count")
        private int likeCount;
        @JsonProperty(value = "comment_count")
        private int commentCount;
    }
}
