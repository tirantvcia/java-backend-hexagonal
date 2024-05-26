package com.savily.hexagonal.backend.testing.application;

import com.savily.hexagonal.backend.testing.domain.entities.User;

public class Mapper {
    public static UserRegistrationResponse toResponseDto(User user) {
        return new UserRegistrationResponse(user.getEmail().toString(), user.getId().toString());
    }
}
