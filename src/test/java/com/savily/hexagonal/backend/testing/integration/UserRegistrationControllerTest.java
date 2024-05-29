package com.savily.hexagonal.backend.testing.integration;

import static org.junit.jupiter.api.Assertions.*;
import com.savily.hexagonal.backend.testing.application.UserRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRegistrationControllerTest {
    @Autowired
    private TestRestTemplate client;
    @Test
    public void registerNewUserForValidEmailPassword() {
        final String email = "test@test.com";
        final String password = "TestPass123_";
        final UserRegistrationRequest request = new UserRegistrationRequest(email, password);

        ResponseEntity<String> registrationResponse = client.postForEntity("/api/hexagonal/inmemory/register", request, String.class);

        String jsonWithRegistrationResponse = registrationResponse.getBody();
        assertEquals(HttpStatus.CREATED, registrationResponse.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, registrationResponse.getHeaders().getContentType());
        assertNotNull(jsonWithRegistrationResponse);
        assertTrue(jsonWithRegistrationResponse.contains("User registration successfully"));

    }

    @Test
    public void rejectNewUserEmailIsNotProvided() {
        final String email = "";
        final String password = "TestPass123_";
        final UserRegistrationRequest request = new UserRegistrationRequest(email, password);

        ResponseEntity<String> registrationResponse = client.postForEntity("/api/hexagonal/inmemory/register", request, String.class);

        String jsonWithRegistrationResponse = registrationResponse.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, registrationResponse.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, registrationResponse.getHeaders().getContentType());
        assertNotNull(jsonWithRegistrationResponse);
        assertTrue(jsonWithRegistrationResponse.contains("Email and Password are required"));

    }
    @Test
    public void rejectNewUserPasswordIsNotProvided() {
        final String email = "TestPass123@mail.com";
        final String password = "";
        final UserRegistrationRequest request = new UserRegistrationRequest(email, password);

        ResponseEntity<String> registrationResponse = client.postForEntity("/api/hexagonal/inmemory/register", request, String.class);

        String jsonWithRegistrationResponse = registrationResponse.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, registrationResponse.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, registrationResponse.getHeaders().getContentType());
        assertNotNull(jsonWithRegistrationResponse);
        assertTrue(jsonWithRegistrationResponse.contains("Email and Password are required"));

    }
}
