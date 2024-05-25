package com.savily.hexagonal.backend.testing.domain.repositories;

import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {
    UserRepository userRepository;
    @BeforeEach
    public void setUp() {
        userRepository = new InMemoryUserRepository();
    }
    @Test
    public void testFindById() {
        final Id id = Id.generateUniqueIdentifier();
        final User expectedUser = createUser(id);
        userRepository.save(expectedUser);
        Optional<User> user = userRepository.findById(id);
        assertTrue(user.isPresent());
        assertEquals(expectedUser, user.get());
    }
    private User createUser(Id id) {
        final Email email = Email.create("test@example.com");
        final Password password = Password.createFromPlainText("SafePass123_");
        return new User(id, email, password);
    }

    @Test
    public void testDoesNotFindUserById() {
        final Id id = Id.generateUniqueIdentifier();
        Optional<User> user = userRepository.findById(id);
        assertFalse(user.isPresent());
    }
    @Test
    public void testFindByEmail() {
        final Email email = Email.create("test@example.com");
        final User expectedUser = createUserByEmail(email);
        userRepository.save(expectedUser);
        Optional<User> user = userRepository.findByEmail(email);
        assertTrue(user.isPresent());
        assertEquals(expectedUser, user.get());
    }
    private User createUserByEmail(Email email) {
        final Id id = Id.generateUniqueIdentifier();
        final Password password = Password.createFromPlainText("SafePass123_");
        return new User(id, email, password);
    }
    @Test
    public void testDoesNotFindUserByEmail() {
        final Email email = Email.create("test@example.com");
        Optional<User> user = userRepository.findByEmail(email);
        assertFalse(user.isPresent());
    }
    @Test
    public void testFindAll() {
        final Id id = Id.generateUniqueIdentifier();
        final User expectedUser = createUser(id);
        userRepository.save(expectedUser);
        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }
    @Test
    public void testDoesNotFindAnyUsersWhenRepoIsEmpty() {
        List<User> users = userRepository.findAll();
        assertTrue(users.isEmpty());
    }
    @Test
    public void testRemoveUser() {
        final Id id = Id.generateUniqueIdentifier();
        final User expectedUser = createUser(id);
        userRepository.save(expectedUser);
        boolean userRemoved = userRepository.remove(expectedUser);
        assertTrue(userRemoved);
        Optional<User> user = userRepository.findById(id);
        assertFalse(user.isPresent());
    }

}
