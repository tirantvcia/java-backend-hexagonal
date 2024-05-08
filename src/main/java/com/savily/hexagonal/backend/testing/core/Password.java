package com.savily.hexagonal.backend.testing.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Password {
    private String securePassword;
    private Password(String securePass) {
        this.securePassword = securePass;
    }
    public static Password createFromPlainText(String securePassword) {
        ensureIsStringPassword(securePassword);
        return new Password(securePassword);
    }

    private static void ensureIsStringPassword(String securePassword) {
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
}
