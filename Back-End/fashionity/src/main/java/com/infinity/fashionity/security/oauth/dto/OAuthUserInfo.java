package com.infinity.fashionity.security.oauth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthUserInfo {

    String email;
    String nickname;
    String provider;
    String oauthId;
    String profileImgUrl;

    @Override
    public String toString() {
        return "[AuthUserInfo = email: " + email + "nickname: " + nickname + " provider: " + provider + " oauthId: " + oauthId + " imageUrl: " + profileImgUrl + "]";
    }
}
