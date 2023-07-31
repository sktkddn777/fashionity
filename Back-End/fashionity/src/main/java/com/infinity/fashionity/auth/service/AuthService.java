package com.infinity.fashionity.auth.service;

import com.infinity.fashionity.auth.dto.LoginDTO;
import com.infinity.fashionity.auth.dto.SaveDTO;
import com.infinity.fashionity.security.oauth.dto.AuthUserInfo;
import com.infinity.fashionity.security.oauth.dto.OAuthUserInfo;

public interface AuthService {

    LoginDTO.Response login(LoginDTO.Request dto);
    AuthUserInfo getOrRegisterUser(OAuthUserInfo oauthUserInfo);
    SaveDTO.Response register(SaveDTO.Request dto);
    boolean isIdValidate(String id);
    boolean isNicknameValidate(String nickname);
    boolean isEmailValidate(String email);
}
