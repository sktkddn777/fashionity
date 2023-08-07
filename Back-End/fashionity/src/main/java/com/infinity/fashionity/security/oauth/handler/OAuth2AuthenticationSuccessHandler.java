package com.infinity.fashionity.security.oauth.handler;

import com.infinity.fashionity.global.utils.CookieUtil;
import com.infinity.fashionity.security.dto.Tokens;
import com.infinity.fashionity.security.oauth.dto.CustomOAuth2User;
import com.infinity.fashionity.security.service.JwtProvider;
import com.infinity.fashionity.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final JwtProvider provider;
    private final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();

        Tokens tokens = tokenService.createTokens(oAuth2User.getUserInfo());
        setRefreshTokenInCookie(response, tokens.getRefreshToken());

        String redirect_uri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(cookie -> cookie.getValue())
                .map(cookie -> URLDecoder.decode(cookie, UTF_8))
                .orElse(getDefaultTargetUrl());

        String targetUrl = UriComponentsBuilder.fromUriString(redirect_uri)
                .queryParam("accessToken", tokens.getAccessToken())
                        .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void setRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
        CookieUtil.addCookie(response, "refreshToken", refreshToken, provider.getRefreshTokenExpire());
    }
}