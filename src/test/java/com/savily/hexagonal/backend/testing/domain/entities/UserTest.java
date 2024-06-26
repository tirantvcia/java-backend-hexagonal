package com.savily.hexagonal.backend.testing.domain.entities;

import com.savily.hexagonal.backend.testing.domain.common.ValidationError;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Password;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    @DisplayName("changes the password when a different one is provided")
    public void changesPasswordTest() {
        final User user = createUser();
        Password newPassword = Password.createFromPlainText("AnotherSafePass123_");
        user.changePassword(newPassword);
        assertTrue(user.isMatchingPassword(newPassword));
    }
    @Test
    @DisplayName("does not allow change the password when a similar one is provided")
    public void doesNotAllowChangePasswordTest() {
        User user = createUser();
        Password newPassword = Password.createFromPlainText("SafePass123_");
        Exception exception = assertThrows(ValidationError.class, ()-> {
            user.changePassword(newPassword);
        }) ;
        String actualExemptionMessage = exception.getMessage();
        String expectedExemptionMessage = "New Password must be different as current password";
        assertEquals(expectedExemptionMessage, actualExemptionMessage);
    }

    private static User createUser() {
        final Id id = Id.generateUniqueIdentifier();
        final Email email = Email.create("test@example.com");
        final Password password = Password.createFromPlainText("SafePass123_");
        final User user = new User(id, email, password);
        return user;
    }
}
