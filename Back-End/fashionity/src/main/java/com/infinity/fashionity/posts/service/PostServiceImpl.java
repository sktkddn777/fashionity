package com.infinity.fashionity.posts.service;

import com.infinity.fashionity.posts.dto.*;
import com.infinity.fashionity.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    @Override
    public PostListDTO.Response getAllPosts(PostListDTO.Request dto) {

        return null;
    }

    @Override
    public PostDetailDTO.Response getPost(PostDetailDTO.Request dto) {


        return null;
    }

    @Override
    public PostSaveDTO.Response savePost(PostSaveDTO.Request dto) {
        return null;
    }

    @Override
    public PostUpdateDTO.Response updatePost(PostUpdateDTO.Request dto) {
        return null;
    }

    @Override
    public PostDeleteDTO.Response deletePost(PostDeleteDTO.Request dto) {
        return null;
    }

    @Override
    public PostLikeDTO.Response likePost(PostLikeDTO.Request dto) {
        return null;
    }

    @Override
    public PostReportDTO.Response reportPost(PostReportDTO.Request dto) {
        return null;
    }
}
