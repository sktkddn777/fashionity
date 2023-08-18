package com.infinity.fashionity.security.service;

import com.infinity.fashionity.global.utils.JwtUtil;
import com.infinity.fashionity.security.dto.JwtAuthentication;
import com.infinity.fashionity.security.dto.JwtAuthenticationToken;
import com.infinity.fashionity.security.dto.Tokens;
import com.infinity.fashionity.security.oauth.dto.AuthUserInfo;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {

    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationToken getAuthentication(String accessToken) {
        jwtUtil.validateToken(accessToken);

        Claims claims = jwtProvider.getClaims(accessToken);

        Long userId = claims.get("id", Long.class);
        List<String> roles = claims.get("roles", List.class);

        JwtAuthentication principal = new JwtAuthentication(userId);
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        return new JwtAuthenticationToken(principal,"", authorities);
    }

    public Tokens createTokens(AuthUserInfo authUserInfo) {

        String accessToken = jwtProvider.createAccessToken(authUserInfo.getId(), authUserInfo.getRoles());

        String refreshToken = jwtProvider.createRefreshToken();

        return Tokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
