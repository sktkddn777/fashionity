package com.infinity.fashionity.posts.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostListDTO {
    public static class Request{
        private int page = 0;
        private int size = 12;
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
        private String[] images;
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
