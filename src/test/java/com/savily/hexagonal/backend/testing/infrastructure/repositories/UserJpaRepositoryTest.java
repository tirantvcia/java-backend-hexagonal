package com.savily.hexagonal.backend.testing.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.*;
import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Password;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ComponentScan(basePackages = {
        "com.savily.hexagonal.backend.testing.infrastructure"
})
public class UserJpaRepositoryTest {

    @Autowired
    @Qualifier("userJpaRepository")
    UserRepository userRepository;


    @Test
    void testFindById() {
        final Email email = Email.create("test@example.com");

        final Id id = Id.generateUniqueIdentifier();
        final Password password = Password.createFromPlainText("testPass123_");
        User user = new User(id, email, password);
        userRepository.save(user);
        Optional<User> userById = userRepository.findById(id);
        assertTrue(userById.isPresent());
        User userEntity = userById.get();
        assertEquals(user, userEntity);
    }
    @Test
    void testNotFindById() {
        final Id id = Id.generateUniqueIdentifier();
        Optional<User> userById = userRepository.findById(id);
        assertFalse(userById.isPresent());
    }

    @Test
    void testFindByEmail() {
        final Id id = Id.generateUniqueIdentifier();
        final Password password = Password.createFromPlainText("testPass123_");
        final Email email = Email.create("test@example.com");
        final User user = new User(id, email, password);
        userRepository.save(user);

        Optional<User> userByEmail = userRepository.findByEmail(email);
        assertTrue(userByEmail.isPresent());
        User userEntity = userByEmail.get();
        assertEquals(user, userEntity);
    }

    @Test
    void testDoesNotFindByEmail() {
        final Email email = Email.create("test@example.com");
        Optional<User> userById = userRepository.findByEmail(email);
        assertFalse(userById.isPresent());
    }

    @Test
    void testFindAll() {
        final Id id = Id.generateUniqueIdentifier();
        final Password password = Password.createFromPlainText("testPass123_");
        final Email email = Email.create("test@example.com");
        final User user = new User(id, email, password);
        userRepository.save(user);

        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }


}
