package com.savily.hexagonal.backend.testing.infrastructure.mappers;

import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Password;
import com.savily.hexagonal.backend.testing.infrastructure.repositories.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserMapper() {
    }

    public UserEntity toEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(getUserId(user));
        userEntity.setPassword(getUserPassword(user));
        userEntity.setEmail(getUserEmail(user));
        return userEntity;
    }

    private String getUserEmail(User user) {
        Email email = user.getEmail();
        return email.toString();
    }

    private String getUserPassword(User user) {
        Password password = user.getPassword();
        return password.toString();

    }

    private static String getUserId(User user) {
        Id id = user.getId();
        return id.toString();
    }

    public User toDomain(UserEntity userEntity) {
        return new User(Id.createFrom(userEntity.getId()), Email.create(userEntity.getEmail()), Password.createFromHashedValue(userEntity.getPassword()));
    }
}
