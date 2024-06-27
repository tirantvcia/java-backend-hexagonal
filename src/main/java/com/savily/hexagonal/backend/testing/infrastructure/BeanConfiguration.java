package com.savily.hexagonal.backend.testing.infrastructure;

import com.savily.hexagonal.backend.testing.application.UserRegistrationService;
import com.savily.hexagonal.backend.testing.domain.repositories.InMemoryUserRepository;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.savily.hexagonal.backend.testing.infrastructure.repositories")
public class BeanConfiguration {

/*
    @Bean(name = "userJpaRepository")
    public UserRepository getUserJpaRepository() {
        UserMapper mapper = new UserMapper();
        return new UserJpaRepository(mapper);
    }
*/

    @Bean(name = "userJpaRegistrationService")
    public UserRegistrationService getJpaUserRegistrationService(@Qualifier("userJpaRepository") UserRepository userRepository) {
        return new UserRegistrationService(userRepository);
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
