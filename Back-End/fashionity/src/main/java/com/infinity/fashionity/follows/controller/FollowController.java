package com.infinity.fashionity.follows.controller;

import com.infinity.fashionity.follows.dto.CheckDTO;
import com.infinity.fashionity.follows.dto.FollowDTO;
import com.infinity.fashionity.follows.service.FollowService;
import com.infinity.fashionity.security.dto.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping
    public ResponseEntity<FollowDTO.Response> follow(
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestBody FollowDTO.Request body
            ) {
        FollowDTO.Response response = followService.follow(auth.getSeq(), body.getNickname());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<FollowDTO.Response> unFollow(
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestBody FollowDTO.Request body
    ) {
        FollowDTO.Response response = followService.unFollow(auth.getSeq(), body.getNickname());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{nickname}")
    public ResponseEntity<CheckDTO.Response> checkFollow(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable String nickname) {
        CheckDTO.Response response = followService.checkFollow(auth.getSeq(), nickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
