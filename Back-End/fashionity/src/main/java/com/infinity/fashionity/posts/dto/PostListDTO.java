package com.infinity.fashionity.posts.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
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
    public static class Request{
        @Builder.Default
        private int page = 0;
        @Builder.Default
        private int size = 12;
        @Builder.Default
        private String s = "popular";
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        @Builder.Default
        private List<Post> posts = new ArrayList<>();
    }

    public static class Post{
        @JsonAlias(value = "post_seq")
        private long postSeq;
        private List<String> images;
        private String content;
        private String name;
        @JsonAlias(value = "profile_img")
        private String profileImg;
        private boolean liked;
        @JsonAlias(value = "like_count")
        private int likeCount;
        @JsonAlias(value = "comment_count")
        private int commentCount;
    }
}
