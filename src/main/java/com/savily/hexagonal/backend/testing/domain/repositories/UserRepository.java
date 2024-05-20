package com.savily.hexagonal.backend.testing.domain.repositories;

import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Id save(User user);
    Optional<User> findById(Id id);
    Optional<User> findByEmail(Email email);
    List<User> findAll();
    void remove(User user);
}
