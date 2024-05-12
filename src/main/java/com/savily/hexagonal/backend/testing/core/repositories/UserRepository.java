package com.savily.hexagonal.backend.testing.core.repositories;

import com.savily.hexagonal.backend.testing.core.entities.User;
import com.savily.hexagonal.backend.testing.core.valueObjects.Email;
import com.savily.hexagonal.backend.testing.core.valueObjects.Id;

import java.util.List;

public interface UserRepository {
    public void save(User user);
    public User findById(Id id);
    public User findByEmail(Email email);
    public List<User> findAll();
    public void remove(User user);
}
