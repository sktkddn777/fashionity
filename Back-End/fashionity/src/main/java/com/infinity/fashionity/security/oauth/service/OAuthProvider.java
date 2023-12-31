package com.infinity.fashionity.security.oauth.service;

import com.infinity.fashionity.security.oauth.dto.OAuthUserInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
@Slf4j
public enum OAuthProvider {
    GOOGLE("google") {
        @Override
        public OAuthUserInfo toUserInfo(OAuth2User oauth2User) {
            Map<String, Object> attributes = oauth2User.getAttributes();

            return OAuthUserInfo.builder()
                    .provider(GOOGLE.name)
                    .email(String.valueOf(attributes.get("email")))
                    .nickname(String.valueOf(attributes.get("name")))
                    .oauthId(String.valueOf(oauth2User.getName()))
                    .profileImgUrl(String.valueOf(attributes.get("picture")))
                    .build();
        }
    },

    NAVER("naver") {
        @Override
        public OAuthUserInfo toUserInfo(OAuth2User oauth2User) {
            Map<String, Object> attributes = oauth2User.getAttribute("response");
            return OAuthUserInfo.builder()
                    .provider(NAVER.name)
                    .email(String.valueOf(attributes.get("email")))
                    .nickname(String.valueOf(attributes.get("nickname")))
                    .oauthId(String.valueOf(attributes.get("id")))
                    .profileImgUrl(String.valueOf(attributes.get("profile_image")))
                    .build();
        }
    },
    KAKAO("kakao") {
        @Override
        public OAuthUserInfo toUserInfo(OAuth2User oauth2User) {
            Map<String, Object> attributes = oauth2User.getAttributes();
            Map<String, Object> properties = oauth2User.getAttribute("properties");
            Map<String, Object> account = oauth2User.getAttribute("kakao_account");
            log.info(attributes.toString());
            log.info(properties.toString());
            log.info(account.toString());
            return OAuthUserInfo.builder()
                    .provider(KAKAO.name)
                    .email(String.valueOf(account.get("email")))
                    .nickname(String.valueOf(properties.get("nickname")))
                    .oauthId(String.valueOf(attributes.get("id")))
                    .profileImgUrl(String.valueOf(properties.get("profile_image")))
                    .build();
        }
    };

    private static final Map<String, OAuthProvider> PROVIDERS =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(OAuthProvider::getName, Function.identity())));

    private final String name;

    public static OAuthProvider getOAuthProviderByName(String providerName) {
        if (!PROVIDERS.containsKey(providerName)) {
            throw new IllegalArgumentException("지원하지 않는 로그인입니다.");
        }
        return PROVIDERS.get(providerName);
    }

    public abstract OAuthUserInfo toUserInfo(OAuth2User oauth2User);
}

