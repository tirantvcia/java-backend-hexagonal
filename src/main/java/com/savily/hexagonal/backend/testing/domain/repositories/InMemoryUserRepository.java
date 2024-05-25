package com.savily.hexagonal.backend.testing.domain.repositories;

import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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
        int elementPosition = getPositionByEmail(user);
        final int notFoundIndex = -1;
        if(elementPosition != notFoundIndex) {
            return updatePassword(user, elementPosition);
        }
        this.inMemoryRepoUsers.add(user);
        return getLastElement();
    }

    private User updatePassword(User user, int elementPosition) {
        this.inMemoryRepoUsers.set(elementPosition, user);
        return user;
    }

    private int getPositionByEmail(User user) {
       return IntStream.range(0, this.inMemoryRepoUsers.size())
                .filter(i -> this.inMemoryRepoUsers.get(i).isMatchingEmail(user.getEmail()))
                .findFirst()
                .orElse(-1);
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
