package com.infinity.fashionity.security.service;

import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.entity.MemberRoleEntity;
import com.infinity.fashionity.security.exception.ExpiredTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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
    private final long accessTokenExpire;

    public JwtProvider(
            @Value("${issuer}") String issuer,
            @Value("${secret-key}") String secretKey,
            @Value("${access-token-expire}") long accessTokenExpire
    ) {
        this.issuer=issuer;
        this.secretKey=secretKey;
        this.accessTokenExpire=accessTokenExpire;
    }

    public String createAccessToken(Long userId, List<MemberRoleEntity> memberRoles) {
        log.info("createAccessToken start");
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + accessTokenExpire * MILLI_SECOND);
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);

        // roleEntity -> role String list
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

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(UTF_8)))
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("토큰 기간 만료ㅠㅠ");
        } catch (JwtException | IllegalArgumentException e) {
            log.error("jwt 에러 났어요");
        }
    }
}
