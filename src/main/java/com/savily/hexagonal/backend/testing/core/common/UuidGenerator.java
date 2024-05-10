package com.savily.hexagonal.backend.testing.core.common;

import java.util.UUID;

public class UuidGenerator {

    public static String generate() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
