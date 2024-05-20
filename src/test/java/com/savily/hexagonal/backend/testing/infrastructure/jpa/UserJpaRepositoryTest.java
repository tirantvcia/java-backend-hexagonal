package com.savily.hexagonal.backend.testing.infrastructure.jpa;

import static org.junit.jupiter.api.Assertions.*;
import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Password;
import com.savily.hexagonal.backend.testing.infrastructure.UserEntity;
import com.savily.hexagonal.backend.testing.infrastructure.mappers.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class UserJpaRepositoryTest {
    @Autowired
    UserJpaRepository userRepository;

    UserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserMapper();
    }

    @Test
    void testFindById() {
        final Email email = Email.create("test@example.com");

        final Id id = Id.generateUniqueIdentifier();
        final Password password = Password.createFromPlainText("testPass123_");
        User user = new User(id, email, password);
        userRepository.save(mapper.toEntity(user));
        Optional<UserEntity> userById = userRepository.findById(id.toString());
        assertTrue(userById.isPresent());
        UserEntity userEntity = userById.get();
        assertEquals(id.toString(), userEntity.getId());
    }
    @Test
    void testNotFindById() {
        final Id id = Id.generateUniqueIdentifier();
        Optional<UserEntity> userById = userRepository.findById(id.toString());
        assertFalse(userById.isPresent());
    }

    @Test
    void testFindByEmail() {
        final Id id = Id.generateUniqueIdentifier();
        final Password password = Password.createFromPlainText("testPass123_");
        final Email email = Email.create("test@example.com");
        final User user = new User(id, email, password);
        userRepository.save(mapper.toEntity(user));

        Optional<UserEntity> userByEmail = userRepository.findByEmail(email.toString());
        assertTrue(userByEmail.isPresent());
        UserEntity userEntity = userByEmail.get();
        assertEquals(id.toString(), userEntity.getId());
    }

    @Test
    void testDoesNotFindByEmail() {
        final Email email = Email.create("test@example.com");
        Optional<UserEntity> userById = userRepository.findByEmail(email.toString());
        assertFalse(userById.isPresent());
    }

    @Test
    void testFindAll() {
        final Id id = Id.generateUniqueIdentifier();
        final Password password = Password.createFromPlainText("testPass123_");
        final Email email = Email.create("test@example.com");
        final User user = new User(id, email, password);
        userRepository.save(mapper.toEntity(user));

        List<UserEntity> users = userRepository.findAll();
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }


}
