package com.infinity.fashionity.comments.controller;

import com.infinity.fashionity.comments.dto.*;
import com.infinity.fashionity.comments.service.CommentService;
import com.infinity.fashionity.global.exception.ErrorCode;
import com.infinity.fashionity.global.exception.ValidationException;
import com.infinity.fashionity.posts.dto.PostDetailDTO;
import com.infinity.fashionity.security.dto.JwtAuthentication;
import com.infinity.fashionity.security.dto.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @GetMapping(value = "/{postSeq}/comments", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentListDTO.Response> getCommentList(
            @PathVariable Long postSeq,
            CommentListDTO.Request dto
    ) {
        dto.setPostSeq(postSeq);
        return new ResponseEntity<>(commentService.getList(dto), HttpStatus.OK);
    }

    @PostMapping(value = "/{postSeq}/comments", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentSaveDTO.Response> saveComment(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable Long postSeq,
            @Validated @RequestBody CommentSaveDTO.Request dto) {
        dto.setMemberSeq(auth.getSeq());
        dto.setPostSeq(postSeq);
        return new ResponseEntity<>(commentService.save(dto), HttpStatus.CREATED);
    }

    @PostMapping(value = "/{postSeq}/comments/{commentSeq}/report", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentReportDTO.Response> reportComment(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable Long postSeq,
            @PathVariable Long commentSeq,
            @RequestBody CommentReportDTO.Request dto) {
        dto.setMemberSeq(auth.getSeq());
        dto.setPostSeq(postSeq);
        dto.setCommentSeq(commentSeq);
        return new ResponseEntity<>(commentService.report(dto), HttpStatus.OK);
    }

    @PostMapping(value = "/{postSeq}/comments/{commentSeq}/like", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentLikeDTO.Response> likeComment(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable Long postSeq,
            @PathVariable Long commentSeq,
            @RequestBody CommentLikeDTO.Request dto) {
        dto.setCommentSeq(commentSeq);
        dto.setPostSeq(postSeq);
        dto.setMemberSeq(auth.getSeq());
        return new ResponseEntity<>(commentService.like(dto), HttpStatus.OK);
    }

    @PutMapping(value = "/{postSeq}/comments/{commentSeq}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentUpdateDTO.Response> updateComment(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable Long postSeq,
            @PathVariable Long commentSeq,
            @Validated @RequestBody CommentUpdateDTO.Request dto) {
        dto.setMemberSeq(auth.getSeq());
        dto.setCommentSeq(commentSeq);
        dto.setPostSeq(postSeq);
        return new ResponseEntity<>(commentService.update(dto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{postSeq}/comments/{commentSeq}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDeleteDTO.Response> deleteComment(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable Long postSeq,
            @PathVariable Long commentSeq) {
        CommentDeleteDTO.Request dto = CommentDeleteDTO.Request.builder()
                .commentSeq(commentSeq)
                .postSeq(postSeq)
                .memberSeq(auth.getSeq())
                .build();

        return new ResponseEntity<>(commentService.delete(dto), HttpStatus.OK);
    }
}
