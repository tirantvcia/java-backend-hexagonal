package com.savily.hexagonal.backend.testing.infrastructure;

import com.savily.hexagonal.backend.testing.application.UserRegistrationService;
import com.savily.hexagonal.backend.testing.domain.repositories.InMemoryUserRepository;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import com.savily.hexagonal.backend.testing.infrastructure.repositories.UserRepositoryJpaAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Autowired
    UserRepositoryJpaAdapter userRepositoryJpaAdapter;


    @Bean(name = "userJpaRegistrationService")
    public UserRegistrationService getJpaUserRegistrationService() {
        return new UserRegistrationService(userRepositoryJpaAdapter);
    }

    @Bean
    public UserRepository getInMemoryRepository() {
        return new InMemoryUserRepository();
    }

    @Bean(name = "userInMemoryRegistrationService")
    public UserRegistrationService getInMemoryUserRegistrationService() {
        return new UserRegistrationService(getInMemoryRepository());
    }
}
