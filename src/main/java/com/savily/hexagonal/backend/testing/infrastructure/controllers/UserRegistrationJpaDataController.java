package com.savily.hexagonal.backend.testing.infrastructure.controllers;

import com.savily.hexagonal.backend.testing.application.UserRegistrationRequest;
import com.savily.hexagonal.backend.testing.application.UserRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hexagonal/jpa/")
public class UserRegistrationJpaDataController {

    private final Logger logger = LoggerFactory.getLogger(UserRegistrationJpaDataController.class);
    private final UserRegistrationService service;
    private final UserRegistrationController userRegistrationController;

    public UserRegistrationJpaDataController(@Qualifier("userJpaRegistrationService") UserRegistrationService service) {
        this.service = service;
        this.userRegistrationController = new UserRegistrationController(service);
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest userRegistrationDto) {
        logger.debug("In UserRegistrationJpaDataController");
        return userRegistrationController.register(userRegistrationDto);

    }

}
