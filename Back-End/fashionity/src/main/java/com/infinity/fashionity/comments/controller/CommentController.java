package com.infinity.fashionity.comments.controller;

import com.infinity.fashionity.comments.dto.CommentListDTO;
import com.infinity.fashionity.comments.dto.CommentSaveDTO;
import com.infinity.fashionity.comments.service.CommentService;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;
    @GetMapping("/{postSeq}/comments")
    public ResponseEntity<CommentListDTO.Response> getCommentList(
            @AuthenticationPrincipal JwtAuthenticationToken auth,
            @PathVariable Long postSeq, CommentListDTO.Request dto
    ){
        dto.setPostSeq(postSeq);
        System.out.println(dto);

        CommentListDTO.Response response = CommentListDTO.Response.builder()
                .prev(false)
                .next(false)
                .page(dto.getPage())
                .comments(new ArrayList<>())
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/{postSeq}/comments")
    public ResponseEntity<CommentSaveDTO.Response> saveComment(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable Long postSeq,
            @RequestBody CommentSaveDTO.Request dto){
        dto.setMemberSeq(auth.getSeq());
        dto.setPostSeq(postSeq);
        return new ResponseEntity<>(commentService.save(dto),HttpStatus.CREATED);
    }
}
