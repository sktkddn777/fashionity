package com.infinity.fashionity.security.oauth.dto;

import com.infinity.fashionity.members.entity.MemberRoleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AuthUserInfo {

    private Long id;
    private String email;
    private List<MemberRoleEntity> roles;

    @Override
    public String toString() {
        return "[AuthUserInfo = id: " + id + " email: " + email + " role: " + roles + "]";
    }
}