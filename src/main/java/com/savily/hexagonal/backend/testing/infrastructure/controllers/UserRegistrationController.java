package com.savily.hexagonal.backend.testing.infrastructure.controllers;

import com.savily.hexagonal.backend.testing.application.UserRegistrationRequest;
import com.savily.hexagonal.backend.testing.application.UserRegistrationResponse;
import com.savily.hexagonal.backend.testing.application.UserRegistrationService;
import com.savily.hexagonal.backend.testing.domain.common.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/hexagonal/inmemory/")
public class UserRegistrationController {
    private final Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);
    private final UserRegistrationService service;

    public UserRegistrationController(@Qualifier("userInMemoryRegistrationService") UserRegistrationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest userRegistrationDto) {

        try {
            ensureRequestIsValid(userRegistrationDto);
            UserRegistrationResponse registrationResponse = service.register(userRegistrationDto);
            return handleResponse(registrationResponse);

        } catch (Exception ex) {
            return handleError(ex);

        }

    }

    private static ResponseEntity<Map<String, Object>> handleResponse(UserRegistrationResponse registrationResponse) {
        Map<String, Object> response = new HashMap<>();
        response.put("id",  registrationResponse.getId());
        response.put("email", registrationResponse.getEmail());
        response.put("message", "User registration successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private ResponseEntity<Map<String, Object>> handleError(Exception ex) {
        logger.error(ex.getMessage(), ex);
        Map<String, Object> response = new HashMap<>();
        if(ex instanceof ValidationError) {
            response.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.put("message", "Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private static void ensureRequestIsValid(UserRegistrationRequest userRegistrationDto) {
        if(isBlank(userRegistrationDto.getEmail()) || isBlank(userRegistrationDto.getPassword())) {
            throw new ValidationError("Email and Password are required");
        }
    }


    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }


}
