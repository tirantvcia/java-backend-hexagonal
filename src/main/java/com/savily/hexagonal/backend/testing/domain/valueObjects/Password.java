package com.savily.hexagonal.backend.testing.domain.valueObjects;

import com.savily.hexagonal.backend.testing.domain.common.ValidationError;
import com.savily.hexagonal.backend.testing.domain.common.HashGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Password {
    private final String securePassword;
    private Password(String securePass) {
        this.securePassword = securePass;
    }
    public static Password createFromPlainText(String securePassword) {
        ensureIsStrongPassword(securePassword);
        return new Password(hashPlainText(securePassword));
    }

    private static String hashPlainText(String securePassword) {
        return HashGenerator.hash(securePassword);
    }

    private static void ensureIsStrongPassword(String securePassword) {
        List<String> accumulatedErrors = new ArrayList<>();
        if(!hasMoreThanSixChars(securePassword)) {
            accumulatedErrors.add("is too short");
        }
        if(!containsNumber(securePassword)) {
            accumulatedErrors.add("must contain a number");
        }
        if(!containsLowerCaseLetters(securePassword)) {
            accumulatedErrors.add("must contain a lowercase letter");
        }
        if(!containsUpperCaseLetters(securePassword)) {
            accumulatedErrors.add("must contain an uppercase letter");
        }
        if(!containsUnderscore(securePassword)) {
            accumulatedErrors.add("must contain an underscore");
        }
        if(!accumulatedErrors.isEmpty()) {
            throw new ValidationError("Password ".concat(String.join(", " ,accumulatedErrors)));
        }
    }

    private static boolean containsUnderscore(String securePassword) {
        String regexUnderscorePattern = "^.*_.*$";
        return Pattern.compile(regexUnderscorePattern).matcher(securePassword).matches();
    }

    private static boolean containsUpperCaseLetters(String securePassword) {
        String regexUpperCasePattern = "^.*[A-Z].*$";
        return Pattern.compile(regexUpperCasePattern).matcher(securePassword).matches();
    }

    private static boolean containsLowerCaseLetters(String securePassword) {
        String regexLowerCasePattern = "^.*[a-z].*$";
        return Pattern.compile(regexLowerCasePattern).matcher(securePassword).matches();
    }

    private static boolean containsNumber(String securePassword) {
        String regexNumberPattern = "^.*\\d.*$";
        return Pattern.compile(regexNumberPattern).matcher(securePassword).matches();
    }

    private static boolean hasMoreThanSixChars(String securePassword) {
        return securePassword.length() > 6;
    }

    @Override
    public String toString() {
        return securePassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password)) return false;
        Password password = (Password) o;
        return Objects.equals(securePassword, password.securePassword);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(securePassword);
    }
}
