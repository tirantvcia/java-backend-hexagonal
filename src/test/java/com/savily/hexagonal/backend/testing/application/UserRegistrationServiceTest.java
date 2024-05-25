package com.savily.hexagonal.backend.testing.application;

import static org.junit.jupiter.api.Assertions.*;

import com.savily.hexagonal.backend.testing.domain.UserService;
import com.savily.hexagonal.backend.testing.domain.common.ValidationError;
import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.repositories.InMemoryUserRepository;
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
        userRepository = new InMemoryUserRepository();
        UserService userService = new UserService(userRepository);
        userRegistrationService = new UserRegistrationService(userService);
    }

    @Test
    public void registerUserSuccessfully() {
        final String email = "test@example.com";
        final String password = "SafePass123_";
        final UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(email, password);
        final User expectedUser = createUserByEmail(Email.create(email));
        User currentUser = userRegistrationService.register(userRegistrationRequest);

        Optional<User> userRepositoryByEmail = userRepository.findByEmail(Email.create(email));
        assertTrue(userRepositoryByEmail.isPresent());
        User userSaved = userRepositoryByEmail.get();
        assertTrue(userSaved.isMatchingEmail(Email.create(email)));
    }
    private User createUserByEmail(Email email) {
        final Id id = Id.generateUniqueIdentifier();
        final Password password = Password.createFromPlainText("SafePass123_");
        return new User(id, email, password);
    }

/*
    @Test
    public void registerFailsWhenUserEmailIsAlreadyExist() {
        final String emailAsString = "test@example.com";
        final String password = "SafePass123_";
        final Email email = Email.create(emailAsString);
        final User expectedUser = createUserByEmail(email);
        User currentUser = userRegistrationService.register(emailAsString, password);
        Exception exception = assertThrows(ValidationError.class, ()-> {
            userRegistrationService.register(emailAsString, password);
        }) ;
        String actualExemptionMessage = exception.getMessage();
        String expectedExemptionMessage = "User registration fails because email already exists";
        assertEquals(expectedExemptionMessage, actualExemptionMessage);
    }



 */
}
