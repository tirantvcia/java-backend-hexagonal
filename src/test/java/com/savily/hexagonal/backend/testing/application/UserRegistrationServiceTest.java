package com.savily.hexagonal.backend.testing.application;

import static org.junit.jupiter.api.Assertions.*;

import com.savily.hexagonal.backend.testing.domain.common.ValidationError;
import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.repositories.InMemoryUserRepository;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Password;
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

    @Test
    public void changUserPasswordSuccessfully() {
        final String email = "test@example.com";
        final UserRegistrationRequest userRegistrationRequest = createRegistrationRequest();
        UserRegistrationResponse userRegistrationResponse = userRegistrationService.register(userRegistrationRequest);

        Optional<User> userRepositoryByEmail = userRepository.findByEmail(Email.create(email));
        assertTrue(userRepositoryByEmail.isPresent());
        final User userRegistered = userRepositoryByEmail.get();
        final Password oldPassword = userRegistered.getPassword();
        logger.debug(String.format("Old Password : %s for a user id %s", oldPassword, userRegistered.getId()));

        final String newPassword = "NewSafePass124_";
        UserPasswordChangeRequest userPasswordChangeRequest = creatingChangeUserPasswordRequest(newPassword);
        logger.debug(String.format("New Password : %s for a user email %s", Password.createFromPlainText(userPasswordChangeRequest.getNewPassword()), userPasswordChangeRequest.getEmail()));
        UserPasswordChangeResponse currentUserResponse = userRegistrationService.changePassword(userPasswordChangeRequest);
        Optional<User> userSavedWithNewPassword = userRepository.findByEmail(Email.create(email));
        assertTrue(userSavedWithNewPassword.isPresent());
        User currentUserWithNewPassword = userSavedWithNewPassword.get();
        assertTrue(currentUserWithNewPassword.isMatchingEmail(Email.create(email)));
        logger.debug(String.format("New Password : %s for a user email %s", currentUserWithNewPassword.getPassword(), currentUserWithNewPassword.getEmail()));
        Password newPassWord = currentUserWithNewPassword.getPassword();
        logger.debug(String.format("Old Password : %s versus New : %s, are equal %b", oldPassword, newPassWord, oldPassword.equals(newPassWord)));
        assertFalse(currentUserWithNewPassword.isMatchingPassword(oldPassword));
    }



    private UserPasswordChangeRequest creatingChangeUserPasswordRequest(String newPassword) {
        final String email = "test@example.com";
        final String oldPassword = "SafePass123_";
        return new UserPasswordChangeRequest(email, oldPassword, newPassword);
    }


}
