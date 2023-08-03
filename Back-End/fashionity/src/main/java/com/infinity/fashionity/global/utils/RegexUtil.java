package com.infinity.fashionity.global.utils;

import java.util.regex.Pattern;

public class RegexUtil {

    private static final String ID_REGEX = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{6,20}$";
    private static final String NICKNAME_REGEX = "^[a-zA-Z가-힣0-9]{4,12}$";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PW_REGEX = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$";

    public static boolean checkIdRegex(String id) {
        return Pattern.matches(ID_REGEX, id);
    }

    public static boolean checkNicknameRegex(String nickname) {
        return Pattern.matches(NICKNAME_REGEX, nickname);
    }

    public static boolean checkEmailRegex(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean checkPasswordRegex(String password) {
        return Pattern.matches(PW_REGEX, password);
    }
}
