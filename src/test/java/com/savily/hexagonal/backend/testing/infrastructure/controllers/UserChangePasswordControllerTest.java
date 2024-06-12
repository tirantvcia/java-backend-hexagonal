package com.savily.hexagonal.backend.testing.infrastructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savily.hexagonal.backend.testing.application.*;
import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.repositories.InMemoryUserRepository;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserChangePasswordControllerTest {
    Logger logger = LoggerFactory.getLogger(UserRegistrationServiceTest.class);
    UserRegistrationService userRegistrationService;
    UserRepository userRepository;
    UserRegistrationController userRegistrationController;
    ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        userRegistrationService = new UserRegistrationService(userRepository);
        userRegistrationController = new UserRegistrationController(userRegistrationService);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void changUserPasswordSuccessfully() {
        final String email = "test@example.com";
        final UserRegistrationRequest userRegistrationRequest = createRegistrationRequest();
        userRegistrationController.register(userRegistrationRequest);

        final String newPassword = "NewSafePass124_";
        final String oldPassword = "SafePass123_";
        UserPasswordChangeRequest userPasswordChangeRequest = creatingChangeUserPasswordRequest(oldPassword, newPassword);

        ResponseEntity<Map<String, Object>> changePasswordResponse = userRegistrationController.changePassword(userPasswordChangeRequest);
        Map<String, Object> jsonWithChangePasswordResponse = changePasswordResponse.getBody();

        assert jsonWithChangePasswordResponse != null;
        String email1 = jsonWithChangePasswordResponse.get("email").toString();
        assertNotNull(email1);
        String message = jsonWithChangePasswordResponse.get("message").toString();
        assertNotNull(message);
        assertEquals("User has changed password successfully", message);

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
        userRegistrationController.register(userRegistrationRequest);

        final String newPassword = "SafePass123_";
        final String oldPassword = "SafePass123_";
        UserPasswordChangeRequest userPasswordChangeRequest = creatingChangeUserPasswordRequest(oldPassword, newPassword);

        ResponseEntity<Map<String, Object>> changePasswordResponse = userRegistrationController.changePassword(userPasswordChangeRequest);
        Map<String, Object> jsonWithChangePasswordResponse = changePasswordResponse.getBody();

        assert jsonWithChangePasswordResponse != null;
        String message = jsonWithChangePasswordResponse.get("message").toString();
        assertNotNull(message);
        assertEquals("New Password must be different as current password", message);

        Optional<User> userSavedWithNewPassword = userRepository.findByEmail(Email.create(email));
        assertTrue(userSavedWithNewPassword.isPresent());
        User currentUserWithNewPassword = userSavedWithNewPassword.get();
        assertTrue(currentUserWithNewPassword.isMatchingEmail(Email.create(email)));
        Password newPassWord = currentUserWithNewPassword.getPassword();
        assertTrue(currentUserWithNewPassword.isMatchingPassword(Password.createFromPlainText(oldPassword)));

    }

    @Test
    public void failsChangUserPasswordWhenUserIsNotValidatedCorrectlyWithWrongPassword() {
    }

    @Test
    public void failsChangUserPasswordWhenUserIsNotValidatedCorrectlyWithWrongEmail() {
    }


    private static UserRegistrationRequest createRegistrationRequest() {
        final String email = "test@example.com";
        final String password = "SafePass123_";
        return new UserRegistrationRequest(email, password);
    }

    private UserPasswordChangeRequest creatingChangeUserPasswordRequest(String oldPassword, String newPassword) {
        final String email = "test@example.com";
        return new UserPasswordChangeRequest(email, oldPassword, newPassword);
    }
}
