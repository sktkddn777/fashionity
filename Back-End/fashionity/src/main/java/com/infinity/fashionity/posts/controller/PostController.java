package com.infinity.fashionity.posts.controller;

import com.infinity.fashionity.posts.dto.*;
import com.infinity.fashionity.posts.service.PostService;
import com.infinity.fashionity.security.dto.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //전체 게시글 조회
    @GetMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostListDTO.Response> getAllPosts(@RequestBody PostListDTO.Request dto){
        PostListDTO.Response list = postService.getAllPosts(dto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //게시글 상세 조회
    @GetMapping(value = "/{postSeq}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDetailDTO.Response> getPost(@PathVariable long postSeq){
        PostDetailDTO.Request dto = PostDetailDTO.Request
                .builder()
                .postSeq(postSeq)
                .build();
        PostDetailDTO.Response post = postService.getPost(dto);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    //게시글 등록
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostSaveDTO.Response> savePost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @Validated @RequestBody PostSaveDTO.Request dto){
        dto.setMemberSeq(auth.getSeq());
        PostSaveDTO.Response success = postService.savePost(dto);
        return new ResponseEntity<>(success, HttpStatus.CREATED);
    }

    //게시글 수정
    @PutMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostUpdateDTO.Response> updatePost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestBody PostUpdateDTO.Request dto){
        dto.setMemberSeq(auth.getSeq());
        PostUpdateDTO.Response success = postService.updatePost(dto);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    //게시글 삭제
    @DeleteMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDeleteDTO.Response> deletePost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestBody PostDeleteDTO.Request dto){
        dto.setMemberSeq(auth.getSeq());
        PostDeleteDTO.Response success = postService.deletePost(dto);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    // 게시글 좋아요
    @PostMapping(value = "/like", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostLikeDTO.Response> likePost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestBody PostLikeDTO.Request dto){
        dto.setMemberSeq(auth.getSeq());
        PostLikeDTO.Response success = postService.likePost(dto);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    // 게시글 신고
    @PostMapping(value = "/report", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostReportDTO.Response> reportPost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestBody PostReportDTO.Request dto){
        dto.setMemberSeq(auth.getSeq());
        PostReportDTO.Response success = postService.reportPost(dto);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }
}

