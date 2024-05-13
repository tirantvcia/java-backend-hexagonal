package com.savily.hexagonal.backend.testing.domain.valueObjects;

import com.savily.hexagonal.backend.testing.domain.common.ValidationError;
import com.savily.hexagonal.backend.testing.domain.common.UuidGenerator;

import java.util.Objects;
import java.util.regex.Pattern;

public class Id {
    private final String uuid;

    private Id(String uuid) {
        this.uuid = uuid;
    }

    public static Id generateUniqueIdentifier() {
        return new Id(generateUuid());
    }
    private static String generateUuid() {
        return UuidGenerator.generate();
    }

    public static Id createFrom(String sourceId) {
        ensuresIsValidId(sourceId);
        return new Id(sourceId);
    }

    private static void ensuresIsValidId(String sourceId) {
        final String regexUuid = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
        if(!Pattern.matches(regexUuid, sourceId)) {
            throw new ValidationError("Invalid format Id");
        }
    }

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Id)) return false;
        Id id = (Id) o;
        return Objects.equals(uuid, id.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
