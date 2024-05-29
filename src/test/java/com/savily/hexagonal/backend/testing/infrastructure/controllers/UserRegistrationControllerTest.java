package com.savily.hexagonal.backend.testing.infrastructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savily.hexagonal.backend.testing.application.UserRegistrationRequest;
import com.savily.hexagonal.backend.testing.application.UserRegistrationService;
import com.savily.hexagonal.backend.testing.domain.repositories.InMemoryUserRepository;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class UserRegistrationControllerTest {
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
    public void registerNewUserForValidEmailPassword() {
        final String email = "test@test.com";
        final String password = "TestPass123_";
        final UserRegistrationRequest request = new UserRegistrationRequest(email, password);

        ResponseEntity<Map<String, Object>> registrationResponse = userRegistrationController.register(request);

        Map<String, Object> jsonWithRegistrationResponse = registrationResponse.getBody();
        assertEquals(HttpStatus.CREATED, registrationResponse.getStatusCode());


        assert jsonWithRegistrationResponse != null;
        String email1 = jsonWithRegistrationResponse.get("email").toString();
        assertNotNull(email1);
        String message = jsonWithRegistrationResponse.get("message").toString();
        assertNotNull(message);
        assertEquals("User registration successfully", message);
    }

    @Test
    public void rejectNewUserEmailIsNotProvided() {

        final String email = "";
        final String password = "TestPass123_";
        final UserRegistrationRequest request = new UserRegistrationRequest(email, password);

        ResponseEntity<Map<String, Object>> registrationResponse = userRegistrationController.register(request);

        Map<String, Object> jsonWithRegistrationResponse = registrationResponse.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, registrationResponse.getStatusCode());

        assert jsonWithRegistrationResponse != null;
        String message = jsonWithRegistrationResponse.get("message").toString();
        assertNotNull(message);
        assertEquals("Email and Password are required", message);
    }
    @Test
    public void rejectNewUserPasswordIsNotProvided() {
        final String email = "TestPass123@temp.com";
        final String password = "";
        final UserRegistrationRequest request = new UserRegistrationRequest(email, password);

        ResponseEntity<Map<String, Object>> registrationResponse = userRegistrationController.register(request);

        Map<String, Object> jsonWithRegistrationResponse = registrationResponse.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, registrationResponse.getStatusCode());

        assert jsonWithRegistrationResponse != null;
        String message = jsonWithRegistrationResponse.get("message").toString();
        assertNotNull(message);
        assertEquals("Email and Password are required", message);

    }
}
