package com.savily.hexagonal.backend.testing.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.savily.hexagonal.backend.testing.application.UserPasswordChangeRequest;
import com.savily.hexagonal.backend.testing.application.UserRegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRegistrationControllerTest {
    @Autowired
    private TestRestTemplate client;

    private ObjectMapper mapper;


    @BeforeEach
    public void setup() {
        mapper = new ObjectMapper();

    }
    @Test
    public void registerNewUserForValidEmailPassword() throws JsonProcessingException {
        final String email = "test@test.com";
        final String password = "TestPass123_";
        final UserRegistrationRequest request = new UserRegistrationRequest(email, password);

        ResponseEntity<String> registrationResponse = client.postForEntity("/api/hexagonal/inmemory/register", request, String.class);

        String jsonWithRegistrationResponse = registrationResponse.getBody();
        assertEquals(HttpStatus.CREATED, registrationResponse.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, registrationResponse.getHeaders().getContentType());
        assertNotNull(jsonWithRegistrationResponse);
        assertTrue(jsonWithRegistrationResponse.contains("User registration successfully"));

        JsonNode jsonNode = mapper.readTree(jsonWithRegistrationResponse);
        assertEquals("User registration successfully", jsonNode.path("message").asText());
        assertEquals("test@test.com", jsonNode.path("email").asText());
        String regex = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
        assertTrue(Pattern.matches(regex, jsonNode.path("id").asText()));

    }
    @Test
    public void registerNewUserForValidEmailPasswordInJpaData() {
        final String email = "test@test.com";
        final String password = "TestPass123_";
        final UserRegistrationRequest request = new UserRegistrationRequest(email, password);

        ResponseEntity<String> registrationResponse = client.postForEntity("/api/hexagonal/jpa/register", request, String.class);

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


    @Test
    public void changUserPasswordSuccessfully() throws JsonProcessingException {
        final String email = "test@test.com";
        final String password = "TestPass123_";
        final UserRegistrationRequest request = new UserRegistrationRequest(email, password);

        ResponseEntity<String> registrationResponse = client.postForEntity("/api/hexagonal/inmemory/register", request, String.class);

        String jsonWithRegistrationResponse = registrationResponse.getBody();
        JsonNode jsonNode = mapper.readTree(jsonWithRegistrationResponse);
        String userRegisteredId = jsonNode.path("id").asText();

        Map<String, String> parameters= new HashMap<String, String>();
        parameters.put("newPassword", "NewPass123_");
        parameters.put("oldPassword", password);

        HttpEntity entity = new HttpEntity(parameters);

        client.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        ResponseEntity<String> changePasswordResponse = client.exchange("/api/hexagonal/inmemory/changePassword/" + email, HttpMethod.PATCH, entity, String.class);
        String jsonWithChangePasswordResponse = changePasswordResponse.getBody();
        assertEquals(HttpStatus.OK, changePasswordResponse.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, changePasswordResponse.getHeaders().getContentType());
        assertNotNull(jsonWithChangePasswordResponse);
        assertTrue(jsonWithChangePasswordResponse.contains("User has changed password successfully"));

    }



    private UserPasswordChangeRequest creatingChangeUserPasswordRequest(String oldPassword, String newPassword) {
        final String email = "test@example.com";
        return new UserPasswordChangeRequest(email, oldPassword, newPassword);
    }

}
