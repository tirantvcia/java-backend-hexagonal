package com.savily.hexagonal.backend.testing.infrastructure.controllers;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.savily.hexagonal.backend.testing.application.UserPasswordChangeRequest;
import com.savily.hexagonal.backend.testing.application.UserRegistrationRequest;
import com.savily.hexagonal.backend.testing.application.UserRegistrationResponse;
import com.savily.hexagonal.backend.testing.application.UserRegistrationService;
import com.savily.hexagonal.backend.testing.domain.common.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest userRegistrationDto) {
        logger.debug("In UserRegistrationInMemoryController");
        return userRegistrationController.register(userRegistrationDto);

    }

    @PatchMapping("/changePassword/{email}")
    public ResponseEntity<?> changePasword(@PathVariable(name = "email") String emaill, @RequestBody Map<String, String> parameters) {
        logger.debug("In UserRegistrationInMemoryController");
        String oldPassword = parameters.get("oldPassword");
        String newPassword = parameters.get("newPassword");
        UserPasswordChangeRequest userPasswordChangeRequest = new UserPasswordChangeRequest(emaill, oldPassword, newPassword);
        return userRegistrationController.changePassword(userPasswordChangeRequest);
    }

}
