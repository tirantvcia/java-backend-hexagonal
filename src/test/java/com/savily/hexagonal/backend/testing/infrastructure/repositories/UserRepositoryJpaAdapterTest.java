package com.savily.hexagonal.backend.testing.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.*;
import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Password;
import com.savily.hexagonal.backend.testing.infrastructure.UserEntity;
import com.savily.hexagonal.backend.testing.infrastructure.jpa.UserJpaRepository;
import com.savily.hexagonal.backend.testing.infrastructure.mappers.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class UserRepositoryJpaAdapterTest {

    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    UserMapper mapper;

    UserRepositoryJpaAdapter userRepositoryJpaAdapter;
    @BeforeEach
    void setUp() {
        userRepositoryJpaAdapter = new UserRepositoryJpaAdapter(userJpaRepository, mapper);
    }

    @Test
    void save() {
        final Id id = Id.generateUniqueIdentifier();
        final User user = createUser(id);
        Id idUserSaved = userRepositoryJpaAdapter.save(user);
        assertEquals(id, idUserSaved);
        Optional<UserEntity> userJpaRepositoryById = userJpaRepository.findById(id.toString());
        assertTrue(userJpaRepositoryById.isPresent());
        UserEntity userEntity = userJpaRepositoryById.get();
        assertEquals(id.toString(), userEntity.getId());
    }

    private User createUser(Id id) {
        final Email email = Email.create("test@example.com");
        final Password password = Password.createFromPlainText("SafePass123_");
        return new User(id, email, password);
    }

    @Test
    void findById() {
        final Id id = Id.generateUniqueIdentifier();
        final User expectedUser = createUser(id);
        Id idUserSaved = userRepositoryJpaAdapter.save(expectedUser);
        Optional<User> userById = userRepositoryJpaAdapter.findById(id);
        assertTrue(userById.isPresent());
        User actualUser = userById.get();
        assertionsFromUser(expectedUser, actualUser);
    }

    @Test
    void doesNotFindById() {
        final Id id = Id.generateUniqueIdentifier();
        Optional<User> userById = userRepositoryJpaAdapter.findById(id);
        assertFalse(userById.isPresent());
    }

    private static void assertionsFromUser(User expectedUser, User actualUser) {

        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertTrue(expectedUser.isMatchingPassword(actualUser.getPassword()) );
    }

    @Test
    void findByEmail() {
    }

    @Test
    void findAll() {
    }

    @Test
    void remove() {
    }
}