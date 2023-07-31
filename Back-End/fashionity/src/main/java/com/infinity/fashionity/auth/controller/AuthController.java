package com.infinity.fashionity.auth.controller;

import com.infinity.fashionity.auth.dto.LoginDTO;
import com.infinity.fashionity.auth.dto.SaveDTO;
import com.infinity.fashionity.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO.Response> login(
            @RequestBody LoginDTO.Request dto
    ) {
        LoginDTO.Response response = authService.login(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<SaveDTO.Response> register(
            @RequestBody @Valid SaveDTO.Request dto
    ) {
        SaveDTO.Response response = authService.register(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/check/id")
    public ResponseEntity<Boolean> isIdValidate(
            @RequestParam String id
    ) {
        boolean response = authService.isIdValidate(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/check/nickname")
    public ResponseEntity<Boolean> isNicknameValidate(
            @RequestParam String nickname
    ) {
        boolean response = authService.isNicknameValidate(nickname);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/check/email")
    public ResponseEntity<Boolean> isEmailValidate(
            @RequestParam String email
    ) {
        boolean response = authService.isEmailValidate(email);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
