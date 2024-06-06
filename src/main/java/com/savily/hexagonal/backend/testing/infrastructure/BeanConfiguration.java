package com.savily.hexagonal.backend.testing.infrastructure;

import com.savily.hexagonal.backend.testing.application.UserRegistrationService;
import com.savily.hexagonal.backend.testing.domain.repositories.InMemoryUserRepository;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import com.savily.hexagonal.backend.testing.infrastructure.mappers.UserMapper;
import com.savily.hexagonal.backend.testing.infrastructure.repositories.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.savily.hexagonal.backend.testing.infrastructure.repositories")
public class BeanConfiguration {


    @Bean(name = "userJpaRepository")
    public UserRepository getUserJpaRepository() {
        UserMapper mapper = new UserMapper();
        return new UserJpaRepository(mapper);
    }

    @Bean(name = "userJpaRegistrationService")
    public UserRegistrationService getJpaUserRegistrationService() {
        return new UserRegistrationService(getUserJpaRepository());
    }


    @Bean(name = "userInMemoryRepository")
    public UserRepository getInMemoryRepository() {
        return new InMemoryUserRepository();
    }
   @Bean(name = "userInMemoryRegistrationService")
    public UserRegistrationService getInMemoryUserRegistrationService() {
        return new UserRegistrationService(getInMemoryRepository());
    }
}
