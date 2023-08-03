package com.infinity.fashionity.global.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class HashUtil {

    /**
     * 임시 아이디 발급 (소셜로그인 전용)
     */
    public static String makeHashId() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        uuid = uuid.substring(0, 9);
        return uuid;
    }

    /**
     * 임시 비밀번호 발급
     * 영어, 숫자, 특수문자 혼용
     * 8 ~ 20자 (12자로 고정)
     */
    public static String makeHashPassword() {
        char[] special = { '!','@','#','$','%','^','&','*','(',')'};

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        uuid = uuid.substring(0, 11);

        int specialIdx = (int)(Math.random() * special.length);
        uuid.concat(Character.toString(special[specialIdx]));
        return uuid;
    }
}
