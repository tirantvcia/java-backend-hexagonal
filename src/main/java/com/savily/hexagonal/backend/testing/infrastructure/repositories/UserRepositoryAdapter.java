package com.savily.hexagonal.backend.testing.infrastructure.repositories;

import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;
import com.savily.hexagonal.backend.testing.infrastructure.UserEntity;
import com.savily.hexagonal.backend.testing.infrastructure.jpa.UserJpaRepository;
import com.savily.hexagonal.backend.testing.infrastructure.mappers.UserEntityMapper;
import org.springframework.stereotype.Component;


import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper mapper;
    public UserRepositoryAdapter(UserJpaRepository userJpaRepository, UserEntityMapper mapper) {
        this.userJpaRepository = userJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Id save(User user) {
        UserEntity userSaved = userJpaRepository.save(toEntity(user));
        return Id.createFrom(userSaved.getId());
    }

    private UserEntity toEntity(User user) {
        return mapper.toEntity(user);
    }

    @Override
    public Optional<User> findById(Id id) {
        Optional<UserEntity> userSaved = userJpaRepository.findById(id.toString());
        return userSaved.map(mapper::toDomain);
    }

    @Override
    public User findByEmail(Email email) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return Collections.emptyList();
    }

    @Override
    public void remove(User user) {

    }
}
