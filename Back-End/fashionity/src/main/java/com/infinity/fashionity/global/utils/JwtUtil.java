package com.infinity.fashionity.global.utils;

import com.infinity.fashionity.security.exception.ExpiredTokenException;
import com.infinity.fashionity.security.exception.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.infinity.fashionity.global.exception.ErrorCode.EXPIRED_TOKEN;
import static com.infinity.fashionity.global.exception.ErrorCode.INVALID_TOKEN;
import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@Slf4j
public class JwtUtil {

    private final String secretKey;
    // TODO: REDIS 추가

    public JwtUtil(@Value("${secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(UTF_8)))
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException(EXPIRED_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(INVALID_TOKEN);
        }
    }

    public Long getExpiration(String token) {
        Date tokenExpiration = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes(UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody().getExpiration();

        Long now = new Date().getTime();
        return tokenExpiration.getTime() - now;
    }
}

