package com.savily.hexagonal.backend.testing.application;

import com.savily.hexagonal.backend.testing.domain.UserService;
import com.savily.hexagonal.backend.testing.domain.common.ValidationError;
import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Password;

public class UserRegistrationService {
    private final UserService userService;

    public UserRegistrationService(UserService userService) {
        this.userService = userService;
    }

    public User register(String email, String password) {
        if(isEmailAlreadyExist(email)) {
            throw new ValidationError("User registration fails because email already exists");
        }
        User user = createUser(email, password);
        Id id = userService.save(user);
        return user;
    }

    private boolean isEmailAlreadyExist(String email) {
        return userService.findByEmail(Email.create(email)).isPresent();
    }

    private User createUser(String email, String password) {
        final Id id = Id.generateUniqueIdentifier();
        return new User(id, Email.create(email), Password.createFromPlainText(password));
    }
}
