package com.savily.hexagonal.backend.testing.infrastructure.repositories;

import com.savily.hexagonal.backend.testing.domain.entities.User;
import com.savily.hexagonal.backend.testing.domain.repositories.UserRepository;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Email;
import com.savily.hexagonal.backend.testing.domain.valueObjects.Id;
import com.savily.hexagonal.backend.testing.infrastructure.mappers.UserMapper;
import org.springframework.stereotype.Component;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Component("userJpaRepository")

public class UserJpaRepository implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;


    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private final UserMapper mapper;
    public UserJpaRepository(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserEntity userSaved = toEntity(user);
        entityManager.persist(userSaved);
        return mapper.toDomain(userSaved);
    }

    @Override
    public boolean update(User currentUser) {
        try {
            Id id = currentUser.getId();
            UserEntity userFound = entityManager.find(UserEntity.class, id.toString());
            userFound.setPassword(currentUser.getPassword().toString());
            entityManager.persist(userFound);
            return true;
        } catch (NoResultException nre) {
            return false;
        }
    }

    private UserEntity toEntity(User user) {
        return mapper.toEntity(user);
    }

    @Override
    public Optional<User> findById(Id id) {
        try {
            UserEntity userFound = entityManager.find(UserEntity.class, id.toString());
            return Optional.ofNullable(userFound).map(mapper::toDomain);
        } catch (NoResultException nre) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        Query query = entityManager
                .createQuery("select u from UserEntity u where u.email = :email")
                .setParameter("email", email.toString());
        try {
            UserEntity userFound = (UserEntity) query.getSingleResult();
            return Optional.ofNullable(userFound).map(mapper::toDomain);
        } catch (NoResultException nre) {
           return Optional.empty();
        }

    }

    @Override
    public List<User> findAll() {
        TypedQuery<UserEntity> typedQuery = entityManager
                .createQuery("select u from UserEntity u", UserEntity.class);
        List<UserEntity> resultList = typedQuery.getResultList();

        if(resultList == null || resultList.isEmpty()) {
            return new ArrayList<User>();
        }
        return resultList.stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean remove(User user) {
        entityManager.remove(mapper.toEntity(user));
        return true;
    }

    @Override
    public void deleteAll() {

        entityManager.createQuery("DELETE FROM UserEntity").executeUpdate();

    }



}
