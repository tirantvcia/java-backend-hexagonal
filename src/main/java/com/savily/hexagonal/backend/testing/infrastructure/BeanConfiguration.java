package com.savily.hexagonal.backend.testing.infrastructure;

import com.savily.hexagonal.backend.testing.application.UserRegistrationService;
import com.savily.hexagonal.backend.testing.infrastructure.repositories.UserRepositoryJpaAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Autowired
    UserRepositoryJpaAdapter userRepositoryJpaAdapter;


    @Bean
    public UserRegistrationService getUserRegistrationService() {
        return new UserRegistrationService(userRepositoryJpaAdapter);
    }
}
