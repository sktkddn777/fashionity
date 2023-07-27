package com.infinity.fashionity.members.data;

public enum MemberMaxLength {

    ID(20),
    NICKNAME(12),
    PASSWORD(20);

    private int length;

    MemberMaxLength(int length) {
        this.length = length;
    }
}
