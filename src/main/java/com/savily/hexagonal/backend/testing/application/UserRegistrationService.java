package com.savily.hexagonal.backend.testing.application;

import com.savily.hexagonal.backend.testing.domain.common.ValidationError;
import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Password;

import java.util.Optional;

public class UserRegistrationService {
    private final UserRepository repository;

    public UserRegistrationService(UserRepository repository) {
        this.repository = repository;
    }

    public UserRegistrationResponse register(UserRegistrationRequest userRegistrationRequest) {
        final String email = userRegistrationRequest.getEmail();
        ensureIfEmailDoesNotExist(email);
        final String password = userRegistrationRequest.getPassword();
        User user = createUser(email, password);
        repository.save(user);
        return Mapper.toResponseDto(user);
    }

    private void ensureIfEmailDoesNotExist(String email) {
        if(isEmailAlreadyExist(email)) {
            throw new ValidationError("User registration fails because email already exists");
        }
    }

    private boolean isEmailAlreadyExist(String email) {
        return repository.findByEmail(Email.create(email)).isPresent();
    }

    private User createUser(String email, String password) {
        final Id id = Id.generateUniqueIdentifier();
        return new User(id, Email.create(email), Password.createFromPlainText(password));
    }


    public UserPasswordChangeResponse changePassword(UserPasswordChangeRequest userRegistrationRequest) {
        String email = userRegistrationRequest.getEmail();
        Optional<User> userSavedByEmail = repository.findByEmail(Email.create(email));
        User currentUser = userSavedByEmail.get();
        currentUser.changePassword(Password.createFromPlainText(userRegistrationRequest.getNewPassword()));
        repository.save(currentUser);
        return new UserPasswordChangeResponse(currentUser.getEmail().toString(), currentUser.getId().toString());
    }
}
