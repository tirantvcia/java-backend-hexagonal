package com.savily.hexagonal.backend.testing.infrastructure.jpa;

import com.savily.hexagonal.backend.testing.infrastructure.UserDataBase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserDataBase, String> {
}
