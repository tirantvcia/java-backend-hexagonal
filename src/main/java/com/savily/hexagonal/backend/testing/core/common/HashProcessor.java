package com.savily.hexagonal.backend.testing.core.common;


import org.apache.commons.codec.digest.DigestUtils;

public class HashProcessor {
    public static String hash(String plainText) {
        return DigestUtils.sha256Hex(plainText);
    }
}
