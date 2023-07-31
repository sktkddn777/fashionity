package com.infinity.fashionity.members.controller;

import com.infinity.fashionity.members.dto.LoginDTO;
import com.infinity.fashionity.members.dto.ProfileDTO;
import com.infinity.fashionity.members.dto.ProfilePostDTO;
import com.infinity.fashionity.members.dto.SaveDTO;
import com.infinity.fashionity.members.service.MemberService;
import com.infinity.fashionity.security.dto.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO.Response> login(
            @RequestBody LoginDTO.Request dto
    ) {
        LoginDTO.Response response = memberService.login(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<SaveDTO.Response> register(
            @RequestBody @Valid SaveDTO.Request dto
    ) {
        SaveDTO.Response response = memberService.register(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/validate/id")
    public ResponseEntity<Boolean> isIdValidate(
            @RequestParam String id
    ) {
        boolean response = memberService.isIdValidate(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/validate/nickname")
    public ResponseEntity<Boolean> isNicknameValidate(
            @RequestParam String nickname
    ) {
        boolean response = memberService.isNicknameValidate(nickname);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/validate/email")
    public ResponseEntity<Boolean> isEmailValidate(
            @RequestParam String email
    ) {
        boolean response = memberService.isEmailValidate(email);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{nickname}")
    public ResponseEntity<ProfileDTO.Response> getProfile(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable String nickname
    ) {
        ProfileDTO.Response response = memberService.getMemberProfile(auth.getSeq(), nickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{nickname}/posts")
    public ResponseEntity<ProfilePostDTO.Response> getMemberProfilePost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable String nickname
    ) {
        ProfilePostDTO.Response response = memberService.getMemberProfilePost(auth.getSeq(), nickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{nickname}/liked")
    public ResponseEntity<ProfilePostDTO.Response> getMemberProfileLikedPost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable String nickname
    ) {
        ProfilePostDTO.Response response = memberService.getMemberProfileLikedPost(auth.getSeq(), nickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{nickname}/hidden")
    public ResponseEntity<ProfilePostDTO.Response> getMemberProfileHiddenPost(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable String nickname
    ) {
        ProfilePostDTO.Response response = memberService.getMemberProfileHiddenPost(auth.getSeq(), nickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/edit")
    public ResponseEntity<ProfileDTO.Response> editMyProfile(
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestBody ProfileDTO.Request profile
    ) {
        ProfileDTO.Response response = memberService.editMemberProfile(auth.getSeq(), profile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/edit/pw")
    public ResponseEntity<ProfileDTO.Response> editMyPassword(
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestBody ProfileDTO.PwRequest data
    ) {
        ProfileDTO.Response response = memberService.editMyPassword(auth.getSeq(), data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
