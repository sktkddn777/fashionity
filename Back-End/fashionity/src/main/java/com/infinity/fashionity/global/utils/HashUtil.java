package com.infinity.fashionity.global.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class HashUtil {

    public static String makeHashId() {
        String uuid = UUID.randomUUID().toString();
        long l = ByteBuffer.wrap(uuid.getBytes()).getLong();
        return Long.toString(l, 9);
    }
}
