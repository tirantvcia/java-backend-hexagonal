package com.savily.hexagonal.backend.testing.domain;

import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;

import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Id id) {
        return this.userRepository.findById(id);
    }
}