package com.infinity.fashionity.members.data;

import lombok.Getter;

@Getter
public enum MemberRole {

    USER("USER"),
    ADMIN("ADMIN"),
    CONSULTANT("CONSULTANT");

    private String role;

    MemberRole(String role) {
        this.role=role;
    }

}
