package com.savily.hexagonal.backend.testing.infrastructure.controllers;

import com.savily.hexagonal.backend.testing.application.UserRegistrationRequest;
import com.savily.hexagonal.backend.testing.application.UserRegistrationResponse;
import com.savily.hexagonal.backend.testing.application.UserRegistrationService;
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
    private final UserRegistrationService service;

    public UserRegistrationController(@Qualifier("userInMemoryRegistrationService") UserRegistrationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest userRegistrationDto) {
        UserRegistrationResponse registrationResponse = service.register(userRegistrationDto);
        Map<String, Object> response = new HashMap<>();
        response.put("id",  registrationResponse.getId());
        response.put("email", registrationResponse.getEmail());
        response.put("status", "CREATED");
        response.put("message", "User registration successfully");
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



}
