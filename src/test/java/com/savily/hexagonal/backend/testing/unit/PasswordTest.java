package com.savily.hexagonal.backend.testing.unit;

import com.savily.hexagonal.backend.testing.core.Password;
import com.savily.hexagonal.backend.testing.core.ValidationError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordTest {
    @Test
    @DisplayName("creates a password when the given value meets the requirements for a strong password")
    public void createsPasswordSuccessfully() {
        Assertions.assertInstanceOf(Password.class, Password.createFromPlainText("SecurePass123_"));
    }
    @Test
    @DisplayName("fails when the password is too short")
    public void failsWhenTooShort() {
        Exception exception = assertThrows(ValidationError.class, ()-> {
            Password.createFromPlainText("eP1_");
        }) ;
        String actualExemptionMessage = exception.getMessage();
        String expectedExemptionMessage = "Password is too short";
        assertEquals(expectedExemptionMessage, actualExemptionMessage);
    }
    @Test
    @DisplayName("fails when the number is missing")
    public void failsWhenNumberIsMissing() {
        Exception exception = assertThrows(ValidationError.class, ()-> {
            Password.createFromPlainText("SecurePass_");
        }) ;
        String actualExemptionMessage = exception.getMessage();
        String expectedExemptionMessage = "Password must contain a number";
        assertEquals(expectedExemptionMessage, actualExemptionMessage);
    }
    @Test
    @DisplayName("fails when the lower case is missing")
    public void failsWhenLowerCaseIsMissing() {
        Exception exception = assertThrows(ValidationError.class, ()-> {
            Password.createFromPlainText("S12345P_");
        }) ;
        String actualExemptionMessage = exception.getMessage();
        String expectedExemptionMessage = "Password must contain a lowercase letter";
        assertEquals(expectedExemptionMessage, actualExemptionMessage);
    }
    @Test
    @DisplayName("fails when the upper case is missing")
    public void failsWhenUpperCaseIsMissing() {
        Exception exception = assertThrows(ValidationError.class, ()-> {
            Password.createFromPlainText("s12345p_");
        }) ;
        String actualExemptionMessage = exception.getMessage();
        String expectedExemptionMessage = "Password must contain an uppercase letter";
        assertEquals(expectedExemptionMessage, actualExemptionMessage);
    }
    @Test
    @DisplayName("fails when the underscore is missing")
    public void failsWhenUnderscoreIsMissing() {
        Exception exception = assertThrows(ValidationError.class, ()-> {
            Password.createFromPlainText("s12345P");
        }) ;
        String actualExemptionMessage = exception.getMessage();
        String expectedExemptionMessage = "Password must contain an underscore";
        assertEquals(expectedExemptionMessage, actualExemptionMessage);
    }
    @Test
    @DisplayName("fails for several errors")
    public void failsForSeveralErrors() {
        Exception exception = assertThrows(ValidationError.class, ()-> {
            Password.createFromPlainText("abc1");
        }) ;
        String actualExemptionMessage = exception.getMessage();
        String expectedExemptionMessage = "Password is too short, must contain an uppercase letter, must contain an underscore";
        assertEquals(expectedExemptionMessage, actualExemptionMessage);
    }
    @Test
    @DisplayName(("ensures password is hashed"))
    public void ensuresHashingPassword() {
        Password password = Password.createFromPlainText("SecurePass123_");
        String hashedValue = password.toString();
        assertNotEquals("SecurePass123_", hashedValue);
        assertEquals(64, hashedValue.length());
        assertTrue(Pattern.matches("^[a-fA-F0-9]{64}$", hashedValue));
    }
}
