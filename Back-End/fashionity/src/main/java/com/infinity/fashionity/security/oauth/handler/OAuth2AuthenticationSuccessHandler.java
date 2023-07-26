package com.infinity.fashionity.security.oauth.handler;

import com.infinity.fashionity.global.utils.CookieUtil;
import com.infinity.fashionity.security.dto.Tokens;
import com.infinity.fashionity.security.oauth.dto.CustomOAuth2User;
import com.infinity.fashionity.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();

        Tokens tokens = tokenService.createTokens(oAuth2User.getUserInfo());

        // setRefreshToken in Cookie

        String redirect_uri = "/"; // 프론트 코드 넣어주면 될거 같당

        String targetUrl = UriComponentsBuilder.fromUriString(redirect_uri)
                .queryParam("accessToken", tokens.getAccessToken())
                        .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}