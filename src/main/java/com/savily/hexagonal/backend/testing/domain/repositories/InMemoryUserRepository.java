package com.savily.hexagonal.backend.testing.domain.repositories;

import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository{
    private List<User> inMemoryRepoUsers;

    public InMemoryUserRepository(List<User> inMemoryRepoUsers) {
        this.inMemoryRepoUsers = inMemoryRepoUsers;
    }

    public InMemoryUserRepository() {
        this.inMemoryRepoUsers = new ArrayList<>();
    }

    @Override
    public User save(User user) {
        this.inMemoryRepoUsers.add(user);
        return getLastElement();
    }

    private User getLastElement() {
        int lastPosition = this.inMemoryRepoUsers.size() - 1;
        return this.inMemoryRepoUsers.get(lastPosition);
    }


    @Override
    public Optional<User> findById(Id id) {
        return this.inMemoryRepoUsers.stream().filter(user -> user.isMatchingId(id)).findAny();
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return this.inMemoryRepoUsers.stream().filter(user -> user.isMatchingEmail(email)).findAny();
    }

    @Override
    public List<User> findAll() {
        return inMemoryRepoUsers;
    }

    @Override
    public boolean remove(User user) {
        return this.inMemoryRepoUsers.remove(user);
    }
}
