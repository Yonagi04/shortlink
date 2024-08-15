package com.yonagi.shortlink.admin.toolkit;

import java.security.SecureRandom;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 分组ID随机生成
 * @date 2024/08/15 15:58
 */
public final class RandomGenerator {
    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 生成随机分组id
     * @return 分组id
     */
    public static String generateRandomString() {
        return generateRandomString(6);
    }

    /**
     * 生成随机分组id
     * @param length id长度
     * @return 分组id
     */
    public static String generateRandomString(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }
}
