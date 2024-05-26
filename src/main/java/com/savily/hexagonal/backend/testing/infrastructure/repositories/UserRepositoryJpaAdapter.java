package com.savily.hexagonal.backend.testing.infrastructure.repositories;

import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;
import com.savily.hexagonal.backend.testing.infrastructure.mappers.UserMapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserRepositoryJpaAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper mapper;
    public UserRepositoryJpaAdapter(UserJpaRepository userJpaRepository, UserMapper mapper) {
        this.userJpaRepository = userJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserEntity userSaved = userJpaRepository.save(toEntity(user));
        return mapper.toDomain(userSaved);
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
    public Optional<User> findByEmail(Email email) {
        Optional<UserEntity> userSaved = userJpaRepository.findByEmail(email.toString());
        return userSaved.map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> usersSaved = userJpaRepository.findAll();
        return usersSaved.stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean remove(User user) {
        userJpaRepository.delete(mapper.toEntity(user));
        return false;
    }

}
