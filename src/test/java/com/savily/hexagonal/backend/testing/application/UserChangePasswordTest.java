package com.savily.hexagonal.backend.testing.application;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserChangePasswordTest {
    Logger logger = LoggerFactory.getLogger(UserRegistrationServiceTest.class);
    UserRegistrationService userRegistrationService;
    UserRepository userRepository;
    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        userRegistrationService = new UserRegistrationService(userRepository);
    }

    @Test
    public void changUserPasswordSuccessfully() {
        final String email = "test@example.com";
        final UserRegistrationRequest userRegistrationRequest = createRegistrationRequest();
        UserRegistrationResponse userRegistrationResponse = userRegistrationService.register(userRegistrationRequest);

        Optional<User> userRepositoryByEmail = userRepository.findByEmail(Email.create(email));
        assertTrue(userRepositoryByEmail.isPresent());
        final User userRegistered = userRepositoryByEmail.get();
        final String newPassword = "NewSafePass124_";
        final String oldPassword = "SafePass123_";
        UserPasswordChangeRequest userPasswordChangeRequest = creatingChangeUserPasswordRequest(oldPassword, newPassword);
        UserPasswordChangeResponse currentUserResponse = userRegistrationService.changePassword(userPasswordChangeRequest);
        Optional<User> userSavedWithNewPassword = userRepository.findByEmail(Email.create(email));
        assertTrue(userSavedWithNewPassword.isPresent());
        User currentUserWithNewPassword = userSavedWithNewPassword.get();
        assertTrue(currentUserWithNewPassword.isMatchingEmail(Email.create(email)));
        Password newPassWord = currentUserWithNewPassword.getPassword();
        assertFalse(currentUserWithNewPassword.isMatchingPassword(Password.createFromPlainText(oldPassword)));
    }

    @Test
    public void failsChangUserPasswordWhenNewPasswordAsTheSameOld() {
        final String email = "test@example.com";
        final UserRegistrationRequest userRegistrationRequest = createRegistrationRequest();
        UserRegistrationResponse userRegistrationResponse = userRegistrationService.register(userRegistrationRequest);

        Optional<User> userRepositoryByEmail = userRepository.findByEmail(Email.create(email));
        assertTrue(userRepositoryByEmail.isPresent());
        final User userRegistered = userRepositoryByEmail.get();


        Exception exception = assertThrows(ValidationError.class, ()-> {
            final String newPassword = "SafePass123_";
            final String oldPassword = "SafePass123_";
            UserPasswordChangeRequest userPasswordChangeRequest = creatingChangeUserPasswordRequest(oldPassword, newPassword);
            UserPasswordChangeResponse currentUserResponse = userRegistrationService.changePassword(userPasswordChangeRequest);
        }) ;
        String actualExemptionMessage = exception.getMessage();
        String expectedExemptionMessage = "New Password must be different as current password";
        assertEquals(expectedExemptionMessage, actualExemptionMessage);
    }

    @Test
    public void failsChangUserPasswordWhenUserIsNotValidatedCorrectlyWithWrongPassword() {
        final String email = "test@example.com";
        final UserRegistrationRequest userRegistrationRequest = createRegistrationRequest();
        UserRegistrationResponse userRegistrationResponse = userRegistrationService.register(userRegistrationRequest);

        Optional<User> userRepositoryByEmail = userRepository.findByEmail(Email.create(email));
        assertTrue(userRepositoryByEmail.isPresent());

        Exception exception = assertThrows(ValidationError.class, ()-> {
            final String onePassword = "OtherSafePass123_";
            final String newPassword = "NewSafePass123_";
            UserPasswordChangeRequest userPasswordChangeRequest = creatingChangeUserPasswordRequest(onePassword, newPassword);
            UserPasswordChangeResponse currentUserResponse = userRegistrationService.changePassword(userPasswordChangeRequest);
        }) ;
        String actualExemptionMessage = exception.getMessage();
        String expectedExemptionMessage = "User Change Password fails, Email or Password are not valid";
        assertEquals(expectedExemptionMessage, actualExemptionMessage);
    }

    @Test
    public void failsChangUserPasswordWhenUserIsNotValidatedCorrectlyWithWrongEmail() {
        final String email = "test@example.com";
        final UserRegistrationRequest userRegistrationRequest = createRegistrationRequest();
        UserRegistrationResponse userRegistrationResponse = userRegistrationService.register(userRegistrationRequest);

        Optional<User> userRepositoryByEmail = userRepository.findByEmail(Email.create(email));
        assertTrue(userRepositoryByEmail.isPresent());

        Exception exception = assertThrows(ValidationError.class, ()-> {
            UserPasswordChangeRequest userPasswordChangeRequest = creatingChangeUserPasswordRequestWithWrongEmail();
            UserPasswordChangeResponse currentUserResponse = userRegistrationService.changePassword(userPasswordChangeRequest);
        }) ;
        String actualExemptionMessage = exception.getMessage();
        String expectedExemptionMessage = "User Change Password fails, Email or Password are not valid";
        assertEquals(expectedExemptionMessage, actualExemptionMessage);
    }


    private static UserRegistrationRequest createRegistrationRequest() {
        final String email = "test@example.com";
        final String password = "SafePass123_";

        return new UserRegistrationRequest(email, password);
    }

    private UserPasswordChangeRequest creatingChangeUserPasswordRequestWithWrongEmail() {
        final String oldPassword = "SafePass123_";
        final String newPassword = "NewSafePass123_";
        final String email = "testWWW@example.com";
        return new UserPasswordChangeRequest(email, oldPassword, newPassword);

    }

    private UserPasswordChangeRequest creatingChangeUserPasswordRequest(String oldPassword, String newPassword) {
        final String email = "test@example.com";
        return new UserPasswordChangeRequest(email, oldPassword, newPassword);
    }
}
