package com.infinity.fashionity.posts.controller;

import com.infinity.fashionity.posts.dto.*;
import com.infinity.fashionity.posts.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //전체 게시글 조회
    @GetMapping
    public ResponseEntity<PostListDTO.Response> getAllPosts(@RequestBody PostListDTO.Request dto){
        PostListDTO.Response list = postService.getAllPosts(dto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //게시글 상세 조회
    @GetMapping("/{postSeq}")
    public ResponseEntity<PostDetailDTO.Response> getPost(@PathVariable long postSeq){
        PostDetailDTO.Request dto = PostDetailDTO.Request
                .builder()
                .postSeq(postSeq)
                .build();
        PostDetailDTO.Response post = postService.getPost(dto);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    //게시글 등록
    @PostMapping
    public ResponseEntity<PostSaveDTO.Response> savePost(@RequestBody PostSaveDTO.Request dto){
        PostSaveDTO.Response success = postService.savePost(dto);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    //게시글 수정
    @PutMapping
    public ResponseEntity<PostUpdateDTO.Response> updatePost(@RequestBody PostUpdateDTO.Request dto){
        PostUpdateDTO.Response success = postService.updatePost(dto);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    //게시글 삭제
    @DeleteMapping
    public ResponseEntity<PostDeleteDTO.Response> deletePost(@RequestBody PostDeleteDTO.Request dto){
        PostDeleteDTO.Response success = postService.deletePost(dto);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    // 게시글 좋아요
    @PostMapping("/like")
    public ResponseEntity<PostLikeDTO.Response> likePost(@RequestBody PostLikeDTO.Request dto){
        PostLikeDTO.Response success = postService.likePost(dto);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    // 게시글 신고
    @PostMapping("/report")
    public ResponseEntity<PostReportDTO.Response> reportPost(@RequestBody PostReportDTO.Request dto){
        PostReportDTO.Response success = postService.reportPost(dto);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }
}

