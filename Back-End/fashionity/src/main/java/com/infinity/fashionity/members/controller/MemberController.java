package com.infinity.fashionity.members.controller;

import com.infinity.fashionity.members.dto.LoginDTO;
import com.infinity.fashionity.members.dto.SaveDTO;
import com.infinity.fashionity.members.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
