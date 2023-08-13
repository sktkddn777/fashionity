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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //전체 게시글 조회
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostListDTO.Response> getAllPosts(
            @AuthenticationPrincipal JwtAuthentication auth,
            PostListDTO.Request dto) {
        dto.setMemberSeq(auth == null ? null : auth.getSeq());
        if(dto.getS() == null || !(dto.getS().equals("popular")||dto.getS().equals("latest")) ){
            dto.setS("popular");
        }
        PostListDTO.Response list = postService.getAllPosts(dto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //게시글 상세 조회
    @GetMapping(value = "/{postSeq}")
    public ResponseEntity<PostDetailDTO.Response> getPost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable long postSeq) {
        PostDetailDTO.Request dto = PostDetailDTO.Request
                .builder()
                .memberSeq(auth == null ? null : auth.getSeq())
                .postSeq(postSeq)
                .build();
        PostDetailDTO.Response post = postService.getPost(dto);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    //게시글 등록
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostSaveDTO.Response> savePost(
            @AuthenticationPrincipal JwtAuthentication auth,
            PostSaveDTO.Request dto) {
        System.out.println("+++++++++++++++++++++++++++"+dto.getImages().size()+"만큼 들어옴");
        dto.setHashtags(dto.getHashtags().stream().map(e->e.replaceAll("\"","")).collect(Collectors.toList()));
        dto.setMemberSeq(auth == null ? null : auth.getSeq());

        return new ResponseEntity<>(postService.savePost(dto), HttpStatus.OK);
    }

    //게시글 수정
    @PutMapping(value = "/{postSeq}", produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostUpdateDTO.Response> updatePost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable Long postSeq,
            PostUpdateDTO.Request dto) {
        dto.setMemberSeq(auth == null ? null : auth.getSeq());
        dto.setPostSeq(postSeq);

        PostUpdateDTO.Response success = postService.updatePost(dto);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    //게시글 삭제
    @DeleteMapping(value = "/{postSeq}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDeleteDTO.Response> deletePost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable Long postSeq) {
        PostDeleteDTO.Request dto = PostDeleteDTO.Request.builder()
                .postSeq(postSeq)
                .memberSeq(auth == null ? null : auth.getSeq())
                .build();
        PostDeleteDTO.Response success = postService.deletePost(dto);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    // 게시글 좋아요
    @PostMapping(value = "/{postSeq}/like", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostLikeDTO.Response> likePost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable Long postSeq,
            PostLikeDTO.Request dto) {
        dto.setMemberSeq(auth == null ? null : auth.getSeq());
        dto.setPostSeq(postSeq);
        PostLikeDTO.Response success = postService.likePost(dto);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    // 게시글 신고
    @PostMapping(value = "/{postSeq}/report", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostReportDTO.Response> reportPost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable Long postSeq,
            @RequestBody PostReportDTO.Request dto) {
        dto.setMemberSeq(auth == null ? null : auth.getSeq());
        dto.setPostSeq(postSeq);
        PostReportDTO.Response success = postService.reportPost(dto);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }
}

