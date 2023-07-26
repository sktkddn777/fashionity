package com.infinity.fashionity.posts.service;

import com.infinity.fashionity.posts.dto.*;
import org.springframework.stereotype.Service;

public interface PostService {

    //전체 게시글 조회
    PostListDTO.Response getAllPosts(PostListDTO.Request dto);
    // 게시글 상세 조회
    PostDetailDTO.Response getPost(PostDetailDTO.Request dto);
    // 게시글 등록
    PostSaveDTO.Response savePost(PostSaveDTO.Request dto);
    // 게시글 수정
    PostUpdateDTO.Response updatePost(PostUpdateDTO.Request dto);
    // 게시글 삭제
    PostDeleteDTO.Response deletePost(PostDeleteDTO.Request dto);
    // 게시글 좋아요
    PostLikeDTO.Response likePost(PostLikeDTO.Request dto);
    // 게시글 신고
    PostReportDTO.Response reportPost(PostReportDTO.Request dto);

}
