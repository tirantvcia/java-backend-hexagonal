package com.savily.hexagonal.backend.testing.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.savily.hexagonal.backend.testing.application.UserRegistrationRequest;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional

public class UserJpaRegistrationControllerTest {

    @Autowired
    private TestRestTemplate client;

    private ObjectMapper mapper;


    @Autowired
    @Qualifier("userJpaRepository")
    private UserRepository userRepository;

    @BeforeEach

    public void setup() {
        mapper = new ObjectMapper();
        userRepository.deleteAll();


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
    public void changUserPasswordSuccessfully() throws JsonProcessingException {
        final String email = "test1@test.com";
        final String password = "TestPass123_";
        final UserRegistrationRequest request = new UserRegistrationRequest(email, password);

        ResponseEntity<String> registrationResponse = client.postForEntity("/api/hexagonal/jpa/register", request, String.class);

        String jsonWithRegistrationResponse = registrationResponse.getBody();
        JsonNode jsonNode = mapper.readTree(jsonWithRegistrationResponse);
        String userRegisteredId = jsonNode.path("id").asText();

        Map<String, String> parameters= new HashMap<String, String>();
        parameters.put("newPassword", "NewPass123_");
        parameters.put("oldPassword", password);

        HttpEntity entity = new HttpEntity(parameters);

        client.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        ResponseEntity<String> changePasswordResponse = client.exchange("/api/hexagonal/jpa/changePassword/" + email, HttpMethod.PATCH, entity, String.class);
        String jsonWithChangePasswordResponse = changePasswordResponse.getBody();
        assertEquals(HttpStatus.OK, changePasswordResponse.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, changePasswordResponse.getHeaders().getContentType());
        assertNotNull(jsonWithChangePasswordResponse);
        assertTrue(jsonWithChangePasswordResponse.contains("User has changed password successfully"));

    }

}
