package com.savily.hexagonal.backend.testing.domain;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;


public class UserServiceTest {

    UserRepository userRepository;
    UserService userService;
    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }
    @Test
    public void testFindById() {
        final Id id = Id.generateUniqueIdentifier();
        final User expectedUser = createUser(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(expectedUser));
        Optional<User> user = userService.findById(id);
        assertTrue(user.isPresent());
        assertEquals(expectedUser, user.get());
    }
    @Test
    public void testFindByEmail() {
        final Email email = Email.create("test@example.com");
        final User expectedUser = createUserByEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));
        Optional<User> user = userService.findByEmail(email);
        assertTrue(user.isPresent());
        assertEquals(expectedUser, user.get());
    }

    private User createUserByEmail(Email email) {
        final Id id = Id.generateUniqueIdentifier();
        final Password password = Password.createFromPlainText("SafePass123_");
        return new User(id, email, password);
    }

    private User createUser(Id id) {
        final Email email = Email.create("test@example.com");
        final Password password = Password.createFromPlainText("SafePass123_");
        return new User(id, email, password);
    }
}
