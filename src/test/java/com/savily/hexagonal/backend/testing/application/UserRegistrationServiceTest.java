package com.savily.hexagonal.backend.testing.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.savily.hexagonal.backend.testing.domain.UserService;
import com.savily.hexagonal.backend.testing.domain.common.ValidationError;
import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class UserRegistrationServiceTest {
    UserRegistrationService userRegistrationService;
    UserRepository userRepository;
    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        UserService userService =new UserService(userRepository);
        userRegistrationService = new UserRegistrationService(userService);
    }

    @Test
    public void registerUserSuccessfully() {
        final String email = "test@example.com";
        final String password = "SafePass123_";
        when(userRepository.findByEmail(Email.create(email))).thenReturn(Optional.empty());
        User currentUser = userRegistrationService.register(email, password);
        assertEquals(Email.create(email), currentUser.getEmail());
        assertTrue(currentUser.isMatchingPassword(Password.createFromPlainText(password)));
    }



    @Test
    public void registerFailsWhenUserEmailIsAlreadyExist() {
        final String emailAsString = "test@example.com";
        final String password = "SafePass123_";
        final Email email = Email.create(emailAsString);
        final User expectedUser = createUserByEmail(email);
        User currentUser = userRegistrationService.register(emailAsString, password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));
        Exception exception = assertThrows(ValidationError.class, ()-> {
            userRegistrationService.register(emailAsString, password);
        }) ;
        String actualExemptionMessage = exception.getMessage();
        String expectedExemptionMessage = "User registration fails because email already exists";
        assertEquals(expectedExemptionMessage, actualExemptionMessage);
    }

    private User createUserByEmail(Email email) {
        final Id id = Id.generateUniqueIdentifier();
        final Password password = Password.createFromPlainText("SafePass123_");
        return new User(id, email, password);
    }
}
