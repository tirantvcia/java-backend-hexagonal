package com.savily.hexagonal.backend.testing.application;

import static org.junit.jupiter.api.Assertions.*;

import com.savily.hexagonal.backend.testing.domain.common.ValidationError;
import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.repositories.InMemoryUserRepository;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


public class UserRegistrationServiceTest {
    Logger logger = LoggerFactory.getLogger(UserRegistrationServiceTest.class);
    UserRegistrationService userRegistrationService;
    UserRepository userRepository;
    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        userRegistrationService = new UserRegistrationService(userRepository);
    }

    @Test
    public void registerUserSuccessfully() {
        final String email = "test@example.com";
        final UserRegistrationRequest userRegistrationRequest = createRegistrationRequest();
        UserRegistrationResponse currentUser = userRegistrationService.register(userRegistrationRequest);

        Optional<User> userRepositoryByEmail = userRepository.findByEmail(Email.create(email));
        assertTrue(userRepositoryByEmail.isPresent());
        User userSaved = userRepositoryByEmail.get();
        assertTrue(userSaved.isMatchingEmail(Email.create(email)));
    }


    @Test
    public void registerFailsWhenUserEmailIsAlreadyExist() {
        final UserRegistrationRequest userRegistrationRequest = createRegistrationRequest();
        UserRegistrationResponse currentUser = userRegistrationService.register(userRegistrationRequest);

        Exception exception = assertThrows(ValidationError.class, ()-> {
            userRegistrationService.register(userRegistrationRequest);
        }) ;
        String actualExemptionMessage = exception.getMessage();
        String expectedExemptionMessage = "User registration fails because email already exists";
        assertEquals(expectedExemptionMessage, actualExemptionMessage);
    }

    private static UserRegistrationRequest createRegistrationRequest() {
        final String email = "test@example.com";
        final String password = "SafePass123_";

        return new UserRegistrationRequest(email, password);
    }




}
