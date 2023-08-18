package com.infinity.fashionity.security.service;

import com.infinity.fashionity.members.entity.MemberRoleEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@Slf4j
public class JwtProvider {

    private static final long MILLI_SECOND = 1000L;
    private final String issuer;
    private final String secretKey;
    private final int accessTokenExpire;
    private final int refreshTokenExpire;

    public JwtProvider(
            @Value("${issuer}") String issuer,
            @Value("${secret-key}") String secretKey,
            @Value("${access-token-expire}") int accessTokenExpire,
            @Value("${refresh-token-expire}") int refreshTokenExpire
    ) {
        this.issuer=issuer;
        this.secretKey=secretKey;
        this.accessTokenExpire=accessTokenExpire;
        this.refreshTokenExpire=refreshTokenExpire;
    }

    public String createAccessToken(Long userId, List<MemberRoleEntity> memberRoles) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + accessTokenExpire * MILLI_SECOND);
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);

        List<String> roles = memberRoles.stream().map(
                role -> role.getMemberRole().getRole()
        ).collect(Collectors.toList());

        claims.put("roles", roles);
        return Jwts.builder()
                .setIssuer(issuer)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(UTF_8)))
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String createRefreshToken() {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + refreshTokenExpire * MILLI_SECOND);

        return Jwts.builder()
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(UTF_8)))
                .compact();
    }

    public int getRefreshTokenExpire() {
        return refreshTokenExpire;
    }
}
