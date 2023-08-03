package com.infinity.fashionity.global.utils;

import java.util.Random;

public class StringUtils {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    /**
     * xssFilter링을 하기 위해 < , > , ( , )를 HTML문자 엔티티로 변환해주는 메서드
     * @Param input : xssFiltering을 하기 위한 문자열데이터
     * @Return xssFiltering된 문자열 return
     * */
    public static String xssFilter(String input) {
        if (input != null) {
            input = input.replaceAll("\\<", "&lt;")
                    .replaceAll("\\>", "&gt;")
                    .replaceAll("\\(", "&#40;")
                    .replaceAll("\\)", "&#41;");
        }
        return input;
    }

    /**
     * 입력받은 문자열이 Null인지 확인해주는 메서드
     * @Param input : 확인하고 싶은 String객체
     * @Return Null이면 true, 아니면 false
     * */
    public static boolean isNull(String input){
        return input == null;
    }

    /**
     * 입력받은 문자열이 Blank(Null 혹은 빈 문자열)인지 확인해주는 메서드
     * @Param input : 확인하고 싶은 String객체
     * @Return Blank면 true, 아니면 false
     * */
    public static boolean isBlank(String input){
        return isNull(input) || "".equals(input.trim());
    }

    public static String randomSting(int length){
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i=0;i<length;i++){
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        return sb.toString();
    }

}
