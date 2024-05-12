package com.savily.hexagonal.backend.testing.infrastructure.repositories.jpa;

import static org.junit.jupiter.api.Assertions.*;
import com.savily.hexagonal.backend.testing.core.entities.User;
import com.savily.hexagonal.backend.testing.core.valueObjects.Email;
import com.savily.hexagonal.backend.testing.core.valueObjects.Id;
import com.savily.hexagonal.backend.testing.core.valueObjects.Password;
import com.savily.hexagonal.backend.testing.infrastructure.UserDataBase;
import com.savily.hexagonal.backend.testing.infrastructure.jpa.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class UserJpaRepositoryTest {
    @Autowired
    UserJpaRepository userRepository;

    @Test
    void testFindById() {
        final Id id = Id.generateUniqueIdentifier();
        final Password password = Password.createFromPlainText("testPass123_");
        final Email email = Email.create("test@example.com");
        final User user = new User(id, email, password);
        userRepository.save(user.toUserEntity());

        Optional<UserDataBase> userById = userRepository.findById(id.toString());
        assertTrue(userById.isPresent());
        assertEquals(id.toString(), userById.get().getId());
    }


}
