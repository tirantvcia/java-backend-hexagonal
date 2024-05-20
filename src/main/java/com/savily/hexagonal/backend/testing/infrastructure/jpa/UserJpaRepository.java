package com.savily.hexagonal.backend.testing.infrastructure.jpa;


import com.savily.hexagonal.backend.testing.infrastructure.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);

}
