package com.savily.hexagonal.backend.testing.infrastructure.jpa;

import com.savily.hexagonal.backend.testing.infrastructure.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
}
