package com.savily.hexagonal.backend.testing.domain.common;


import org.apache.commons.codec.digest.DigestUtils;

public class HashGenerator {
    public static String hash(String plainText) {
        return DigestUtils.sha256Hex(plainText);
    }
}
