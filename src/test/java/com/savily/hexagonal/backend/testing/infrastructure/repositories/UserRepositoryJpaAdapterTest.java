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

import java.util.List;
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
        clearUsersInDatabase();
    }

    private void clearUsersInDatabase() {
        userRepositoryJpaAdapter.findAll().forEach(userRepositoryJpaAdapter::remove);
    }

    @Test
    void save() {
        final Id id = Id.generateUniqueIdentifier();
        final User user = createUserById(id);
        User userSaved = userRepositoryJpaAdapter.save(user);
        assertTrue(userSaved.isMatchingId(id));
        Optional<UserEntity> userJpaRepositoryById = userJpaRepository.findById(id.toString());
        assertTrue(userJpaRepositoryById.isPresent());
        UserEntity userEntity = userJpaRepositoryById.get();
        assertEquals(id.toString(), userEntity.getId());
    }



    @Test
    void findById() {
        final Id id = Id.generateUniqueIdentifier();
        final User expectedUser = createUserById(id);
        User userSaved = userRepositoryJpaAdapter.save(expectedUser);
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
    @Test
    void findByEmail() {
        final Email email = Email.create("test@example.com");
        final User expectedUser = createUserByEmail(email);
        User userSaved = userRepositoryJpaAdapter.save(expectedUser);
        Optional<User> userByEmail = userRepositoryJpaAdapter.findByEmail(email);
        assertTrue(userByEmail.isPresent());
        User actualUser = userByEmail.get();
        assertionsFromUser(expectedUser, actualUser);
    }

    private static void assertionsFromUser(User expectedUser, User actualUser) {

        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertTrue(expectedUser.isMatchingPassword(actualUser.getPassword()) );
    }
    private User createUserById(Id id) {
        final Email email = Email.create("test@example.com");
        final Password password = Password.createFromPlainText("SafePass123_");
        return new User(id, email, password);
    }
    private User createUserByEmail(Email email) {
        final Id id = Id.generateUniqueIdentifier();
        final Password password = Password.createFromPlainText("SafePass123_");
        return new User(id, email, password);
    }



    @Test
    void findAll() {
        final Id id = Id.generateUniqueIdentifier();
        final User expectedUser = createUserById(id);
        User userSaved = userRepositoryJpaAdapter.save(expectedUser);
        List<User> usersFound = userRepositoryJpaAdapter.findAll();
        assertFalse(usersFound.isEmpty());
        assertEquals(1, usersFound.size());
        User actualUser = usersFound.get(0);
        assertionsFromUser(expectedUser, actualUser);
    }
    @Test
    void doesNotFindAny() {
        List<User> usersFound = userRepositoryJpaAdapter.findAll();
        assertTrue(usersFound.isEmpty());
    }

    @Test
    void remove() {
        final Id id = Id.generateUniqueIdentifier();
        final User user = createUserById(id);
        User userSaved = userRepositoryJpaAdapter.save(user);
        userRepositoryJpaAdapter.remove(user);
        Optional<User> userById = userRepositoryJpaAdapter.findById(id);
        assertFalse(userById.isPresent());
    }
}