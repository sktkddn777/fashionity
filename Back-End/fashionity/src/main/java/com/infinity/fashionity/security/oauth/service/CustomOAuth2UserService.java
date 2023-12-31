package com.infinity.fashionity.security.oauth.service;

import com.infinity.fashionity.auth.service.AuthService;
import com.infinity.fashionity.security.oauth.dto.AuthUserInfo;
import com.infinity.fashionity.security.oauth.dto.CustomOAuth2User;
import com.infinity.fashionity.security.oauth.dto.OAuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthService authService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        String providerName = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        OAuthUserInfo oAuthUserInfo = OAuthProvider.getOAuthProviderByName(providerName).toUserInfo(oAuth2User);
        AuthUserInfo user = authService.getOrRegisterUser(oAuthUserInfo);

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }
}