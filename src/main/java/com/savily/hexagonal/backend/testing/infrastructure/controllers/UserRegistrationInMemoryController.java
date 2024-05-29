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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/hexagonal/inmemory/")
public class UserRegistrationInMemoryController {

    private final Logger logger = LoggerFactory.getLogger(UserRegistrationInMemoryController.class);
    private final UserRegistrationService service;
    private final UserRegistrationController userRegistrationController;

    public UserRegistrationInMemoryController(@Qualifier("userInMemoryRegistrationService") UserRegistrationService service) {
        this.service = service;
        this.userRegistrationController = new UserRegistrationController(service);
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest userRegistrationDto) {
        logger.debug("In UserRegistrationInMemoryController");
        return userRegistrationController.register(userRegistrationDto);

    }

}
