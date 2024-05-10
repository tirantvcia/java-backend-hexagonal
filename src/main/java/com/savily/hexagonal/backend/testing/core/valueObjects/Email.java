package com.savily.hexagonal.backend.testing.core.valueObjects;

import com.savily.hexagonal.backend.testing.core.common.ValidationError;

import java.util.Objects;
import java.util.regex.Pattern;

public class Email {
    private final String address;
    private Email(String address)  {
        this.address = address;
    }

    public static Email create (String address) {
        ensureIsValidEmail(address);
        return new Email(address);
    }

    private static void ensureIsValidEmail(String address)  {
        String regexEmailPattern = "^(.+)@(\\S+)$";
        boolean matches = Pattern.compile(regexEmailPattern).matcher(address).matches();
        if(!matches) {
            throw new ValidationError("Invalid format email");
        }
    }

    @Override
    public String toString() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(address, email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(address);
    }
}
