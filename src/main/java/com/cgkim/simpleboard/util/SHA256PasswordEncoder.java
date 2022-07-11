package com.cgkim.simpleboard.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 비밀번호를 SHA256으로 암호화하는 역할
 */
public class SHA256PasswordEncoder {

    private static final String algorithm = "SHA-256";

    /**
     * 해싱
     *
     * @param message
     * @return String
     * @throws NoSuchAlgorithmException
     */
    public static String getHash(String message) throws NoSuchAlgorithmException {

        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.update(message.getBytes());
        byte[] result = messageDigest.digest();

        return bytesToString(result);
    }

    /**
     * 바이트를 문자열로 변환
     *
     * @param result
     * @return String
     */
    private static String bytesToString(byte[] result) {

        StringBuilder stringBuilder = new StringBuilder();

        for (byte b : result) {
            stringBuilder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return stringBuilder.toString();
    }
}
